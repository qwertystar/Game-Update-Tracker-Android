# Game Update Tracker-Android

[![gpt-3.5-turbo](https://img.shields.io/badge/LLM-gpt--3.5--turbo-green?logo=openai)](https://chat.openai.com/)
[![文心一言](https://img.shields.io/badge/LLM-%E6%96%87%E5%BF%83%E4%B8%80%E8%A8%80-blue?logo=baidu)](https://yiyan.baidu.com/)

## 介绍

使用Jetpack Compose 创建的用以提醒游戏有新版本的Android应用。

## 软件架构

该应用使用Jetpack Compose编写界面，并通过ViewModel和StateFlow来管理界面状态。
主要入口为MainActivity.kt，并且界面部分包含在ui文件夹下的Screen.kt和ViewModel.kt中。同时，应用直接使用JSoup从特定网站进行数据爬取。

## 安装教程

你可以选择以下两种方法构建这个应用：

1. 克隆这个仓库，并使用Android Studio构建安装包
2. 在远程仓库托管的网站寻找本仓库的发行版，随附有稳定的已构建好的安装包

然后只需安装到你的Android 手机即可使用。

**注意：** 受制于Compose框架对Android版本的限制，这个应用应安装在**Android 8.0**及以上版本的系统中。

## 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request

## 图标来源

图标使用以下模型生成：

[![AI system](https://img.shields.io/badge/AI-DALL·E%202-green?logo=openai)](https://openai.com/dall-e-2)

## 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5. Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. Gitee 封面人物是一档用来展示 Gitee
   会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
