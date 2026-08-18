import { sidebar } from "vuepress-theme-hope";

export default sidebar({
  SidebarSorter: ["readme", "order", "title", "filename"],
  "/": [
    {
      text: "文章",
      icon: "book",
      prefix: "posts/",
      children: "structure",
    },
  ],
});
