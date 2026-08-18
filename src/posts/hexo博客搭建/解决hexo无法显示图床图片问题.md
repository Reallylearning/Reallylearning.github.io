---
title: 解决hexo无法显示图床图片问题
tags:
  - hexo
categories:
  - hexo博客
abbrlink: 24f770ca
date: 2025-09-04 10:39:21
permalink: /mtar0cwq.html
---

hexo引入图片的方式有很多种：

- 从本地文件加载。
- 使用图床，markdown中直接引用图床的链接。

### 1.问题描述

Hexo使用图床的方式加载在blog中加载图片，会在非本人的电脑或者手机端报“html访问图片资源403问题(http referrer)”，导致采用图床方式加载的图片全部无法加载。

### 2.问题原因

http请求体的header中有一个referrer字段，用来表示发起http请求的源地址信息，这个referrer信息是可以省略但是不可修改的，就是说你只能设置是否带上这个referrer信息，不能定制referrer里面的值。

服务器端在拿到这个referrer值后就可以进行相关的处理，比如图片资源，可以通过referrer值判断请求是否来自本站，若不是则返回403或者重定向返回其他信息，从而实现图片的防盗链。上面出现403就是因为，请求的是别人服务器上的资源，但把自己的referrer信息带过去了，被对方服务器拦截返回了403。

在前端可以通过meta来设置referrer policy(来源策略)，具体可以设置哪些值以及对应的结果参考[这里](https://links.jianshu.com/go?to=https%3A%2F%2Fdeveloper.mozilla.org%2Fzh-CN%2Fdocs%2FWeb%2FHTTP%2FHeaders%2FReferrer-Policy)。所以针对上面的403情况的解决方法，就是把referrer设置成`no-referrer`，这样发送请求不会带上referrer信息，对方服务器也就无法拦截了。

浏览器中referrer默认的值是`no-referrer-when-downgrade`，就是除了降级请求的情况以外都会带上referrer信息。降级请求是指https协议的地址去请求http协议，所以上面403的情况还有另一种解决方法就是，请求的图片地址换成http协议，自己的地址使用http协议，这样降级请求也不会带上referrer。

### 3.解决办法

#### 3.1 butterfly主题

在D:\blog\themes\butterfly\layout\includes目录下有一个head.pug文件，修改该文件的meta信息，会使生成的所有页面都带有该head。在head.pug文件中添加如下内容，结果参见图片。

```js
meta(name="referrer" content="no-referrer")
```

![image-20250904104351062](https://blog-1375630728.cos.ap-beijing.myqcloud.com/imgs/image-20250904104351062.png)

butterfly添加头.png

#### 3.2 yilia主题

在G:\blog\themes\yilia\layout_partial目录下有一个head.ejs，同样在head.ejs文件中添加如下meta信息即可

```js
<meta name="referrer" content="no-referrer" />
```

### 4.结果

```undefined
使用hexo g 重新生成一下工程
```

在G:\blog\public\2021目录下就是所有21年生成的blog，找到该目录下的任意一个index.html，用文本编辑器打开就可以看到已经自动生成了referrer标签了。

![img](https://blog-1375630728.cos.ap-beijing.myqcloud.com/imgs/13838098-34cdaee390f1a3dd.png)

生成的html中包含有referrer头.png

然后使用hexo d指令将工程同步到github即可

