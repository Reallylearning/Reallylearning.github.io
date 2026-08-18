# Reallylearning 的个人博客

基于 [VuePress Hope](https://vuepress-theme-hope.github.io/) 搭建的个人技术博客，部署在 GitHub Pages。

🔗 **在线访问**：https://reallylearning.github.io

## 技术栈

- **框架**：VuePress 2 + VuePress Hope 主题
- **构建工具**：Vite
- **包管理器**：pnpm
- **部署**：GitHub Actions 自动部署到 `gh-pages` 分支
- **域名**：GitHub Pages 默认域名

## 本地开发

### 环境要求

- Node.js >= 18
- pnpm >= 10

### 安装依赖

```bash
pnpm install
```

### 启动开发服务器

```bash
pnpm run docs:dev
```

启动后访问 http://localhost:8080

### 构建生产版本

```bash
pnpm run docs:build
```

构建产物输出到 `src/.vuepress/dist/`

## 目录结构

```
.
├── .github/
│   └── workflows/
│       └── deploy-docs.yml    # GitHub Actions 部署配置
├── src/
│   ├── .vuepress/
│   │   ├── config.ts          # VuePress 配置文件
│   │   ├── dist/              # 构建产物（git 忽略）
│   │   └── public/            # 静态资源（原样复制到输出目录）
│   └── ...                    # 博客文章（Markdown）
├── package.json
├── pnpm-lock.yaml
└── .gitignore
```

## 部署方式

推送到 `main` 分支自动触发 GitHub Actions 部署：

1. Actions 拉取源码
2. 安装依赖（pnpm install --frozen-lockfile）
3. 执行 `pnpm run docs:build` 构建静态站点
4. 通过 `JamesIves/github-pages-deploy-action` 将构建产物推送到 `gh-pages` 分支
5. GitHub Pages 读取 `gh-pages` 分支对外提供访问

> 部署配置中开启了 `clean: true`，每次部署会清空 `gh-pages` 分支，避免旧文件残留。

## 分支说明

| 分支       | 用途                                            |
| ---------- | ----------------------------------------------- |
| `main`     | 源码主分支，所有文章和配置修改在此进行          |
| `gh-pages` | 构建产物分支，由 Actions 自动生成，请勿手动修改 |

## 写新文章

在 `src/` 目录下新建 `.md` 文件，按照 VuePress 的目录结构组织即可。文章顶部添加 Frontmatter：

```markdown
---
title: 文章标题
date: 2026-08-18
category: 分类
tag:
  - 标签1
  - 标签2
---

文章内容...
```

## 常用命令

| 命令                  | 说明                   |
| --------------------- | ---------------------- |
| `pnpm run docs:dev`   | 启动开发服务器         |
| `pnpm run docs:build` | 构建生产版本           |
| `git push`            | 推送代码，触发自动部署 |

## License

MIT