import { defineUserConfig } from "vuepress";
import theme from "./theme.js";

import obsidianCallouts from "markdown-it-obsidian-callouts";
import path from 'node:path'
import { autoFrontmatterPlugin, addShortPermalink,addCreateDate,addTitleByFilename } from '@vuepress/plugin-auto-frontmatter'


export default defineUserConfig({
  base: "/",

  lang: "zh-CN",
  title: '计算机学习记录',
  description: '',
  head: [
    ['link', { rel: 'icon', href: 'img/favicon.png' }],
    ['meta', { name: "referrer", content: "no-referrer" }],
  ],

  theme,

  pagePatterns: [
    '**/*.md',                // 1. 默认：所有 md 都要
    '!**/_*.md',              // 2. 所有 _开头 的文件 → 不发布
    '!**/draft/**',           // 3. 所有叫 draft 的整个文件夹 → 不发布
    '!**/note/**',
    '!**/assets/**',
    '!**/english/**',            // 3. 所有叫 draft 的整个文件夹 → 不发布
    '!.vuepress',
    '!node_modules'
  ],

  plugins: [
    autoFrontmatterPlugin({
      // 匹配 `posts` 下的所有文件，但是排除 `posts/model` 目录
      filter: ['posts/**/*.md', '!posts/model'], 
      handle: (data, context) => {
        addTitleByFilename(data, context) 
        addCreateDate(data, context, { format: 'full' }) 
        addShortPermalink(data, { length: 8, prefix: '/', suffix: '.html' }) 
        return data
      },
    }),
  ],
  

  extendsMarkdown: (md) => {
    md.use(obsidianCallouts);
  },

  // 和 PWA 一起启用
  // shouldPrefetch: false,

  
});