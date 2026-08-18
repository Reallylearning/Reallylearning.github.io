---
permalink: /963c64vx.html
title: http基本原理
date: '2026-05-02 16:18:48'
---
### http请求方法

| 序号 | 方法    | 描述                                                         |
| :--- | :------ | :----------------------------------------------------------- |
| 1    | GET     | 从服务器获取资源。用于请求数据而不对数据进行更改。例如，从服务器获取网页、图片等。 |
| 2    | POST    | 向服务器发送数据以创建新资源。常用于提交表单数据或上传文件。发送的数据包含在请求体中。 |
| 3    | PUT     | 向服务器发送数据以更新现有资源。如果资源不存在，则创建新的资源。与 POST 不同，PUT 通常是幂等的，即多次执行相同的 PUT 请求不会产生不同的结果。 |
| 4    | DELETE  | 从服务器删除指定的资源。请求中包含要删除的资源标识符。       |
| 5    | PATCH   | 对资源进行部分修改。与 PUT 类似，但 PATCH 只更改部分数据而不是替换整个资源。 |
| 6    | HEAD    | 类似于 GET，但服务器只返回响应的头部，不返回实际数据。用于检查资源的元数据（例如，检查资源是否存在，查看响应的头部信息）。 |
| 7    | OPTIONS | 返回服务器支持的 HTTP 方法。用于检查服务器支持哪些请求方法，通常用于跨域资源共享（CORS）的预检请求。 |
| 8    | TRACE   | 回显服务器收到的请求，主要用于诊断。客户端可以查看请求在服务器中的处理路径。 |
| 9    | CONNECT | 建立一个到服务器的隧道，通常用于 HTTPS 连接。客户端可以通过该隧道发送加密的数据。 |

### http请求信息

- Accept: 请求报头域，用于指定客户端可接受哪些类型的信息 
- Accept-Language: 指定客户端可接受的语言类型
- Accept-Encoding: 指定客户端可接受的内容编码
- Host: 用于指定请求资源的主机 IP 和端口号，其内容为请求 URL 的原始服务器或网关的位置    从 HTTP 1.1 版本开始，请求必须包含此内容
- Referer: 此内容用来标识这个请求是从哪个页面发过来的，服务器可以拿到这一信息并做相应的处理，如做来源统计、防盗链处理等
- User-Agent: 简称 UA，它是一个特殊的字符串头，可以使服务器识别客户使用的操作系统及版本、浏览器及版本等信息。在做爬虫时加上此信息，可以伪装为浏览器；如果不加，很可能会被识别出为爬虫
- Content-Type: 也叫互联网媒体类型（Internet Media Type）或者 MIME 类型，在 HTTP 协议消息头中，它用来表示具体请求中的媒体类型信息。例如，text/html 代表 HTML 格式，image/gif 代表 GIF 图片，application/json 代表 JSON 类型，更多对应关系可以查看此对照表：http://tool.oschina.net/commons

### HTTP 响应头信息

| 响应头信息（英文）        | 响应头信息（中文） | 描述                                                         |
| :------------------------ | :----------------- | :----------------------------------------------------------- |
| Date                      | 日期               | 响应生成的日期和时间。例如：Wed, 18 Apr 2024 12:00:00 GMT    |
| Server                    | 服务器             | 服务器软件的名称和版本。例如：Apache/2.4.1 (Unix)            |
| Content-Type              | 内容类型           | 响应体的媒体类型（MIME类型），如`text/html; charset=UTF-8`, `application/json`等。 |
| Content-Length            | 内容长度           | 响应体的大小，单位是字节。例如：3145                         |
| Content-Encoding          | 内容编码           | 响应体的压缩编码，如 `gzip`, `deflate`等。                   |
| Content-Language          | 内容语言           | 响应体的语言。例如：zh-CN                                    |
| Content-Location          | 内容位置           | 响应体的 URI。例如：/index.html                              |
| Content-Range             | 内容范围           | 响应体的字节范围，用于分块传输。例如：bytes 0-999/8000       |
| Cache-Control             | 缓存控制           | 控制响应的缓存行为, 如 no-cache 表示必须重新请求。           |
| Connection                | 连接               | 管理连接的选项，如`keep-alive`或`close`，keep-alive 表示连接不会在传输后关闭。。 |
| Set-Cookie                | 设置 Cookie        | 设置客户端的 cookie。例如：sessionId=abc123; Path=/; Secure  |
| Expires                   | 过期时间           | 响应体的过期日期和时间。例如：Thu, 18 Apr 2024 12:00:00 GMT  |
| Last-Modified             | 最后修改时间       | 资源最后被修改的日期和时间。例如：Wed, 18 Apr 2024 11:00:00 GMT |
| ETag                      | 实体标签           | 资源的特定版本的标识符。例如："33a64df551425fcc55e6"         |
| Location                  | 位置               | 用于重定向的 URI。例如：/newresource                         |
| Pragma                    | 实现特定的指令     | 包含实现特定的指令，如 `no-cache`。                          |
| WWW-Authenticate          | 认证信息           | 认证信息，通常用于HTTP认证。例如：Basic realm="Access to the site" |
| Accept-Ranges             | 接受范围           | 指定可接受的请求范围类型。例如：bytes                        |
| Age                       | 经过时间           | 响应生成后经过的秒数，从原始服务器生成到代理服务器。例如：24 |
| Allow                     | 允许方法           | 列出资源允许的 HTTP 方法 。例如：GET, POST，HEAD等           |
| Vary                      | 变化               | 告诉下游代理如何使用响应头信息来确定响应是否可以从缓存中获取。例如：Accept |
| Strict-Transport-Security | 严格传输安全       | 指示浏览器仅通过 HTTPS 与服务器通信。例如：max-age=31536000; includeSubDomains |
| X-Frame-Options           | 框架选项           | 控制页面是否允许在框架中显示，防止点击劫持攻击。例如：SAMEORIGIN |
| X-Content-Type-Options    | 内容类型选项       | 指示浏览器不要尝试猜测资源的 MIME 类型。例如：nosniff        |
| X-XSS-Protection          | XSS保护            | 控制浏览器的 XSS 过滤和阻断。例如：1; mode=block             |
| Public-Key-Pins           | 公钥固定           | HTTP 头信息，用于HTTP公共密钥固定（HPKP），一种安全机制，用于防止中间人攻击。例如：pin-sha256="base64+primarykey"; pin-sha256="base64+backupkey"; max-age=expireTime |

### cookie和session区别

| 对比项       | Cookie                        | Session                            |
| ------------ | ----------------------------- | ---------------------------------- |
| **存储位置** | 浏览器（客户端）              | 服务器（内存 / 数据库）            |
| **存储大小** | 很小，单个约 4KB              | 无限制（看服务器配置）             |
| **存储类型** | 只能存**字符串**              | 可存任意数据类型（对象、数组等）   |
| **安全性**   | 低（明文 / 可篡改，容易被盗） | 高（数据存在服务器，无法直接篡改） |
| **生命周期** | 可长期保存（设置过期时间）    | 默认临时                           |
| **性能压力** | 无（存在用户本地）            | 有（用户越多，服务器占用越高）     |
| **数据隐私** | 完全暴露给用户 / 浏览器       | 用户看不到原始数据                 |