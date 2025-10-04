import { navbar } from "vuepress-theme-hope";
import { iconPlugin } from '@vuepress/plugin-icon';
export default navbar([
  // 主页
  {
    text: "主页",
    icon: "material-symbols:home",
    link: "/",
    // 确保首页在任何情况下都高亮
    activeMatch: "^/$",
  },

  // 计算机基础 (带嵌套)
  {
    text: "计算机基础",
    icon: "code",
    prefix: "/computer-science/", // 假设的前缀，您可以根据实际情况修改
    children: [
      {
        text: "数据结构",
        icon: "mdi:sitemap",
        prefix: "data-structures/",
        children: [
          { text: "树", icon: "tree", link: "tree" },
          { text: "图", icon: "uil:share-alt", link: "graph" },
          { text: "排序", icon: "sort", link: "sorting" },
          { text: "查找", icon: "material-symbols:search-rounded", link: "searching" },
        ],
      },
      { text: "计算机网络", icon: "wifi", link: "computer-network" },
      { text: "操作系统", icon: "server", link: "operating-system" },
      { text: "计算机组成原理", icon: "microchip", link: "computer-architecture" },
    ],
  },

  // 其他页面
  { text: "标签", icon: "tags", link: "/tag/" },
  { text: "分类", icon: "typcn:th-list", link: "/category/" },
  { text: "归档", icon: "hugeicons:archive-01", link: "/article/" },
  { text: "时间线", icon: "mdi:timeline", link: "/timeline/" },
  { text: "关于", icon: "user", link: "/about/" },
]);