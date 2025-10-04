import { defineUserConfig } from "vuepress";

import theme from "./theme.js";

export default defineUserConfig({
  base: "/Blog/",

  lang: "zh-CN",
  title: '计算机学习记录',
  description: '',
  head: [['link', { rel: 'icon', href: 'img/favicon.png' }]],


  theme,

  // 和 PWA 一起启用
  // shouldPrefetch: false,
});
