# 艺术评价系统后端
### 系统开发人员：黎展鹏
### 算法开发人员：老师和研究生
采用框架：
* 小程序系统：springboot+jpa+mysql
* 后台系统：thymeleaf
* 主模块：yipin-basic
* 工具模块：yipin-common

## 9/26：
* 小程序系统进度：
  * 已完成：首页、发布页面、我的页面基础接口已经开发完毕。
  * 未完成：发现页面和动态页面的基础接口，和微信支付模块（等申请到公司的商户号后才能进行开发）。
* 后台管理系统进度：
  * 已完成：权限管理、字典管理、用户管理、作品管理、专家管理、轮播图管理、艺期话题管理。
  * 未完成：账单管理、运维与数据管理
## 10/1:
* 更新内容：
  * 小程序首页搜索功能、小程序消息盒子功能、发现页面艺期话题模块
## 10/16
* 更新内容：
  * 系统：用户排名模块、小程序活动模块、作品收藏模块、代表作模块
  * 算法：视觉肌理算法、画作构图算法
  * 开始准备整合微信支付
## 11/5
*更新内容：
  * 系统：微信支付，活动订单模块，分享模块,大师模块
  * 修复问题：把上传图片到阿里云云存储改为了上传到我们自己的服务器；使用延迟队列解决订单一直得不到完结的问题；解决了上传文件大小限制的问题。
  * 开始准备购物车模块，后台管理系统上传商品模块
## 11/14
* 更新内容：
  * 系统：每日一句上传功能，每日一句接口的完善
