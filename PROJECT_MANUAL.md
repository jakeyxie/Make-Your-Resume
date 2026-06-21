# 简才网 — 智能简历制作平台 · 项目总说明书

> **项目名称：** 简才网 — 每个人的简历制作神器  
> **英文名称：** Smart Resume Platform  
> **作者：** JakeyXie  
> **版本：** v1.0.0 (2025)  
> **技术栈：** Spring Boot 3.4.4 + Vue 3.5 + MyBatis 3.0 + Element Plus 2.9 + MySQL

---

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 技术架构](#2-技术架构)
- [3. 项目结构](#3-项目结构)
- [4. 后端详解 (Spring Boot)](#4-后端详解-spring-boot)
  - [4.1 项目配置](#41-项目配置)
  - [4.2 分层架构](#42-分层架构)
  - [4.3 实体模型](#43-实体模型)
  - [4.4 数据库表结构](#44-数据库表结构)
  - [4.5 API 接口清单](#45-api-接口清单)
  - [4.6 认证与鉴权](#46-认证与鉴权)
  - [4.7 工具与中间件](#47-工具与中间件)
- [5. 前端详解 (Vue 3)](#5-前端详解-vue-3)
  - [5.1 工程配置](#51-工程配置)
  - [5.2 路由结构](#52-路由结构)
  - [5.3 状态管理 (Pinia)](#53-状态管理-pinia)
  - [5.4 组件体系](#54-组件体系)
  - [5.5 核心业务模块](#55-核心业务模块)
- [6. 核心业务流程](#6-核心业务流程)
- [7. 部署说明](#7-部署说明)
- [8. 附录](#8-附录)

---

## 1. 项目概述

**简才网**是一个全栈 Web 应用，提供在线简历制作、模板管理、求职攻略阅读、AI 辅助简历、管理员后台等完整功能。用户可以通过可视化拖拽编辑器，选择多种简历模板，自定义各模块样式与数据，最终导出为 PDF 或在线分享。

### 核心功能

| 功能模块 | 描述 |
|---------|------|
| **在线简历制作** | 可视化设计师，支持 3 套模板、13 个简历模块、实时预览 |
| **简历模板市场** | 14 套模板预览，按风格分类筛选 |
| **JSON 导入/导出** | 支持通过 JSON 格式批量导入导出简历数据 |
| **PDF 导出** | 多页 A4 PDF 导出，自动分页检测 |
| **AI 简历助手** | 接入 DeepSeek API，支持 AI 对话辅助 |
| **求职攻略** | 文章发布/浏览系统，支持分类、富文本编辑、评论 |
| **用户中心** | 个人信息管理、我的简历、在线简历、账号设置 |
| **管理员后台** | 仪表盘（ECharts 图表）、用户管理、管理员管理、公告管理、攻略管理、分类管理 |
| **邮箱验证** | 基于 QQ SMTP 的密码重置流程 |
| **Excel 导入导出** | 管理员/用户数据的 Excel 批量操作 |

---

## 2. 技术架构

```
┌─────────────────────────────────────────────────────┐
│                    浏览器 (Browser)                    │
│  ┌─────────────────────────────────────────────────┐ │
│  │          Vue 3 SPA (Vite 构建)                    │ │
│  │  Element Plus / ECharts / CodeMirror / wangEditor │ │
│  │  Pinia 状态管理 / Vue Router 4 / Axios            │ │
│  └──────────────────┬──────────────────────────────┘ │
└─────────────────────┼────────────────────────────────┘
                      │ HTTP REST API (JSON)
                      │ JWT Token 认证
┌─────────────────────┼────────────────────────────────┐
│               Spring Boot 3.4.4                       │
│  ┌──────────────────┴──────────────────────────────┐ │
│  │  Controller 层 (12 个 REST Controller)           │ │
│  │  Service 层 (9 个 Service)                      │ │
│  │  Mapper 层 (9 个 MyBatis 接口 + 9 个 XML)        │ │
│  │  Interceptor (JWT 拦截器) / CorsConfig           │ │
│  └──────────────────┬──────────────────────────────┘ │
│                     │                                  │
│  ┌──────────────────┴──────────────────────────────┐ │
│  │  MySQL 8.0 (graduation_2025)                     │ │
│  │  9 张数据表                                       │ │
│  └─────────────────────────────────────────────────┘ │
│                                                       │
│  外部服务: QQ SMTP (邮件) / DeepSeek API (AI)         │
└───────────────────────────────────────────────────────┘
```

### 技术栈明细

| 层级 | 技术 | 版本 |
|------|------|------|
| **后端框架** | Spring Boot | 3.4.4 |
| **Java 版本** | JDK | 21 |
| **ORM** | MyBatis + MyBatis Spring Boot Starter | 3.0.3 |
| **分页** | PageHelper | 1.4.6 |
| **数据库** | MySQL (Connector/J) | - |
| **认证** | Auth0 java-jwt (HMAC256) | 4.3.0 |
| **工具库** | Hutool | 5.8.13 |
| **工具库** | Apache Commons Lang3 | 3.12.0 |
| **Office** | Apache POI (OOXML) | 5.2.3 |
| **邮件** | Spring Boot Starter Mail | - |
| **Lombok** | Project Lombok | - |
| **前端框架** | Vue 3 (Composition API) | 3.5.13 |
| **构建工具** | Vite | 6.2.1 |
| **UI 框架** | Element Plus | 2.9.7 |
| **路由** | Vue Router | 4.5.0 |
| **状态管理** | Pinia | 3.0.2 |
| **图表** | ECharts | 5.6.0 |
| **HTTP 客户端** | Axios | 1.8.4 |
| **富文本** | wangEditor | 5.1.23 |
| **代码编辑器** | CodeMirror 6 (vue-codemirror) | 6.1.1 |
| **拖拽排序** | vuedraggable | 4.1.0 |
| **PDF 生成** | jsPDF + html2canvas | 3.0.1 |
| **Markdown 渲染** | marked + DOMPurify | 15.0.11 / 3.2.5 |
| **CSS 预处理** | Sass | 1.86.3 |

---

## 3. 项目结构

```
Graduation_Project/
├── springboot/                          # 【后端】Spring Boot 工程
│   ├── pom.xml                          # Maven 依赖与构建配置
│   └── src/main/
│       ├── java/com/example/
│       │   ├── Application.java         # 应用入口 + @MapperScan
│       │   ├── common/                  # 通用配置
│       │   │   ├── CorsConfig.java      # 跨域配置
│       │   │   ├── WebConfig.java       # WebMVC 配置 (拦截器注册)
│       │   │   ├── JWTInterceptor.java  # JWT 认证拦截器
│       │   │   └── Result.java          # 统一响应封装
│       │   ├── controller/              # 控制器层 (12 个)
│       │   │   ├── WebController.java
│       │   │   ├── AdminController.java
│       │   │   ├── UserController.java
│       │   │   ├── CategoryController.java
│       │   │   ├── IntroductionController.java
│       │   │   ├── NoticeController.java
│       │   │   ├── TemplateController.java
│       │   │   ├── ResumeSortController.java
│       │   │   ├── UserTemplateController.java
│       │   │   ├── FileController.java
│       │   │   ├── ValidateController.java
│       │   │   └── EchartsController.java
│       │   ├── service/                 # 业务逻辑层 (9 个)
│       │   │   ├── AdminService.java
│       │   │   ├── UserService.java
│       │   │   ├── CategoryService.java
│       │   │   ├── IntroductionService.java
│       │   │   ├── NoticeService.java
│       │   │   ├── ResumeSortService.java
│       │   │   ├── TemplateService.java
│       │   │   ├── UserTemplateService.java
│       │   │   └── ValidateService.java
│       │   ├── mapper/                  # MyBatis 数据访问层 (9 个)
│       │   │   ├── AdminMapper.java
│       │   │   ├── UserMapper.java
│       │   │   ├── CategoryMapper.java
│       │   │   ├── IntroductionMapper.java
│       │   │   ├── NoticeMapper.java
│       │   │   ├── ResumeSortMapper.java
│       │   │   ├── TemplateMapper.java
│       │   │   ├── UserTemplateMapper.java
│       │   │   └── ValidateMapper.java
│       │   ├── entity/                  # 实体类 (10 个)
│       │   │   ├── Account.java         # 账户基类
│       │   │   ├── Admin.java           # 管理员 (extends Account)
│       │   │   ├── User.java            # 普通用户 (extends Account)
│       │   │   ├── Category.java
│       │   │   ├── Introduction.java
│       │   │   ├── Notice.java
│       │   │   ├── ResumeSort.java
│       │   │   ├── Template.java
│       │   │   ├── UserTemplate.java
│       │   │   └── Validate.java
│       │   ├── exception/               # 异常处理
│       │   │   ├── CustomerException.java
│       │   │   └── GlobalExceptionHandler.java
│       │   ├── utils/                   # 工具类
│       │   │   ├── TokenUtils.java      # JWT 令牌工具
│       │   │   └── DateUtils.java       # 日期时间工具
│       │   └── test/                    # 练习文件 (非业务代码)
│       └── resources/
│           ├── application.yml          # 应用主配置
│           └── mapper/                  # MyBatis XML (9 个)
│               ├── AdminMapper.xml
│               ├── UserMapper.xml
│               ├── CategoryMapper.xml
│               ├── IntroductionMapper.xml
│               ├── NoticeMapper.xml
│               ├── ResumeSortMapper.xml
│               ├── TemplateMapper.xml
│               ├── UserTemplateMapper.xml
│               └── ValidateMapper.xml
│
├── vue/                                 # 【前端】Vue 3 工程
│   ├── package.json                     # 依赖与脚本
│   ├── vite.config.js                   # Vite 构建配置
│   ├── index.html                       # SPA 入口 HTML
│   ├── auto-imports.d.ts                # 自动导入类型声明
│   └── src/
│       ├── main.js                      # Vue 应用入口
│       ├── App.vue                      # 根组件
│       ├── router/index.js              # 路由配置 (29 条路由)
│       ├── store/                       # Pinia 状态管理
│       │   ├── index.js                 # Pinia 实例创建
│       │   ├── loading.js               # 全局加载状态
│       │   ├── resume.js                # 简历 JSON + 选中模块
│       │   ├── user.js                  # 用户信息
│       │   └── uuid.js                  # 强制刷新 UUID
│       ├── http/api/                    # API 请求封装
│       │   ├── express.js               # DeepSeek AI API
│       │   └── user.js                  # 用户认证 API
│       ├── utils/                       # 工具函数
│       │   ├── request.js               # Axios 实例 + 拦截器
│       │   ├── common.js                # 通用工具
│       │   ├── html2pdf.js              # PDF 导出
│       │   └── registerCom.js           # 全局组件注册
│       ├── hooks/                       # Vue Composables
│       │   ├── useAddStyle.js           # 样式注入
│       │   ├── useCopyModel.js          # 模块复制
│       │   ├── useDeleteModel.js        # 模块删除
│       │   ├── useDegreeList.js         # 学历列表
│       │   ├── useFontSizeList.js       # 字体大小列表
│       │   ├── useModelIndex.js         # 模块索引
│       │   └── useModelOptionsComName.js # 模块选项组件名映射
│       ├── schema/                      # 数据模式
│       │   ├── model.js                 # 默认简历 JSON 结构
│       │   ├── import.js                # 导入示例 JSON
│       │   └── style.js                 # 默认样式配置
│       ├── style/                       # 全局样式
│       │   ├── global.css               # 全局重置
│       │   ├── index.scss               # Element Plus 主题变量
│       │   └── options.scss             # 选项面板样式
│       ├── assets/                      # 静态资源
│       │   ├── images/                  # 图片 (logo, 模板预览, 背景)
│       │   ├── iconfont/iconfont.js     # SVG 图标字体库
│       │   └── font/font.css            # 自定义字体
│       ├── components/                  # 全局组件
│       │   ├── NavBar/                  # 前台导航栏
│       │   ├── AdminNavBar/             # 后台导航栏
│       │   ├── FooterCom/               # 页脚
│       │   ├── Loading/                 # 全局加载动画
│       │   ├── SvgIcon/                 # SVG 图标组件
│       │   ├── LoginDialog/             # 登录弹窗
│       │   ├── ResumeTheme/             # 主题设置面板
│       │   ├── ModelBox/                # 简历模块容器
│       │   ├── ImportJsonDialog/        # JSON 导入对话框
│       │   ├── TipJsonDialog/           # JSON 示例对话框
│       │   ├── PreviewImage/            # 图片预览
│       │   ├── CommonOptions/           # 通用选项面板
│       │   └── ModelComs/               # 简历模块展示组件 (11 个)
│       ├── views/                       # 页面视图
│       │   ├── index/                   # 首页
│       │   ├── designer/                # 简历设计器 (核心)
│       │   ├── templates/               # 模板市场
│       │   ├── ai/                      # AI 助手
│       │   ├── article/                 # 求职攻略
│       │   ├── login/                   # 登录/注册
│       │   ├── forgetPassword/          # 忘记密码
│       │   ├── resetPassword/           # 重置密码
│       │   ├── emailVerify/             # 邮箱验证
│       │   ├── person/                  # 个人中心
│       │   ├── admin/                   # 管理后台
│       │   └── 404/                     # 404 页面
│       └── template/                    # 简历模板
│           ├── index.js                 # 模板清单 (14 套)
│           ├── type.js                  # 模板类型定义
│           ├── template1/               # 模板 1 (单栏)
│           ├── template2/               # 模板 2 (双栏)
│           └── template3/               # 模板 3 (时间轴)
│
├── files/                               # 【上传文件目录】运行时存储
└── META-INF/                            # Maven 元信息
```

---

## 4. 后端详解 (Spring Boot)

### 4.1 项目配置

**主配置文件:** [`springboot/src/main/resources/application.yml`](springboot/src/main/resources/application.yml)

| 配置项 | 值 | 说明 |
|--------|-----|------|
| 服务端口 | `8080` | HTTP 服务端口 |
| 数据库连接 | `jdbc:mysql://localhost:3306/graduation_2025` | MySQL 8.0，UTF-8 编码 |
| 数据库用户 | `root` / `1234` | 开发环境凭证 |
| MyBatis Mapper | `classpath:mapper/*.xml` | XML 映射文件路径 |
| MyBatis 日志 | `StdOutImpl` | 标准输出 SQL 日志 |
| MyBatis 驼峰 | `true` | 自动下划线转驼峰 |
| 邮件服务 | `smtp.qq.com` | QQ SMTP 邮箱 |
| 发件邮箱 | `1293555912@qq.com` | 发送密码重置邮件 |

### 4.2 分层架构

```
┌──────────────────────────────────────────┐
│          Controller (12 个)               │  接收 HTTP 请求，参数校验，调用 Service
│          @RestController                  │
├──────────────────────────────────────────┤
│          Service (9 个)                   │  业务逻辑，组装数据，分页封装
│          @Service                         │
├──────────────────────────────────────────┤
│          Mapper (9 个接口 + 9 个 XML)     │  数据访问，SQL 执行
│          MyBatis Interface                │
├──────────────────────────────────────────┤
│          Entity (10 个实体)               │  数据模型，POJO
│          POJO / Lombok @Data              │
└──────────────────────────────────────────┘
```

**通用组件:**
- **`CorsConfig`** — 允许所有来源的跨域请求
- **`WebConfig`** — 注册 JWT 拦截器，排除公开端点 (`/login`, `/user/register`, `/user/forgetPassword`, `/validate/**`, `/files/download/**`)
- **`JWTInterceptor`** — 从请求头/参数中提取 Token，解码验证，查找用户，校验签名
- **`Result`** — 统一响应格式 `{ code, msg, data }`
- **`GlobalExceptionHandler`** — 全局异常捕获，返回统一错误格式
- **`CustomerException`** — 自定义业务异常 (code + msg)

### 4.3 实体模型

| 实体类 | 数据库表 | 父类 | 说明 |
|--------|---------|------|------|
| `Account` | - | - | 账户基类: id, username, password, phone, email, role, token, avatar, surname |
| `Admin` | `admin` | Account | 管理员，增加 ids/idsArr（批量操作） |
| `User` | `user` | Account | 普通用户，结构同 Admin |
| `Category` | `category` | - | 求职攻略分类: id, title |
| `Introduction` | `introduction` | - | 求职攻略文章: id, img, title, content, time, categoryId, userId |
| `Notice` | `notice` | - | 系统公告: id, title, content, time |
| `ResumeSort` | `resumesort` | - | 简历模板分类: resumeSortId, sort |
| `Template` | `templatelist` | - | 简历模板: templateListId, name, preview, style |
| `UserTemplate` | `usertemplate` | - | 用户保存的简历: id, img, content, time, resumeId, userId |
| `Validate` | `validate` | - | 密码重置验证: id, userId, email, resetToken, type, gmtCreate, gmtModified |

### 4.4 数据库表结构

| 表名 | 主要字段 | 用途 |
|------|---------|------|
| `admin` | id, username, password, phone, email, role, avatar, surname | 管理员账户 |
| `user` | id, username, password, phone, email, role, avatar, surname | 普通用户账户 |
| `category` | id, title | 求职攻略分类 |
| `introduction` | id, img, title, content, time, category_id, user_id | 求职攻略文章 |
| `notice` | id, title, content, time | 系统公告 |
| `resumesort` | resume_sort_id, sort | 简历模板分类 |
| `templatelist` | template_list_id, name, preview, style | 简历模板 |
| `usertemplate` | id, img, content, time, resume_id, user_id | 用户创建的简历 |
| `validate` | id, user_id, email, reset_token, type, gmt_create, gmt_modified | 密码重置令牌 |

### 4.5 API 接口清单

> 所有接口返回统一格式 `Result { code, msg, data }`  
> JWT Token 通过请求头或参数传递（公开端点除外）

#### 认证接口

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/login` | 登录 (role: admin/user) | 否 |
| POST | `/user/register` | 用户注册 | 否 |
| GET/POST | `/validate/sendValidationEmail` | 发送密码重置邮件 | 否 |
| GET/POST | `/validate/resetPassword` | 重置密码 | 否 |

#### 管理员管理 (`/admin`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/admin/selectAll` | 查询所有管理员 |
| GET | `/admin/selectAllByObject` | 按条件筛选管理员 |
| GET | `/admin/selectPage` | 分页查询 |
| POST | `/admin/register` | 注册管理员 |
| PUT | `/admin/update` | 更新管理员 |
| DELETE | `/admin/delete/{id}` | 删除管理员 |
| DELETE | `/admin/deleteBatch` | 批量删除 |
| GET | `/admin/export` | 导出 Excel |
| POST | `/admin/import` | 导入 Excel |

#### 用户管理 (`/user`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/user/selectAll` | 查询所有用户 |
| GET | `/user/selectAllByObject` | 按条件筛选用户 |
| GET | `/user/selectPage` | 分页查询 |
| PUT | `/user/update` | 更新用户信息 |
| DELETE | `/user/delete/{id}` | 删除用户 |
| DELETE | `/user/deleteBatch` | 批量删除 |
| GET | `/user/export` | 导出 Excel |
| POST | `/user/import` | 导入 Excel |

#### 求职攻略 (`/introduction`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/introduction/selectAll` | 查询所有攻略 |
| GET | `/introduction/selectPage` | 分页查询攻略 |
| GET | `/introduction/selectById/{id}` | 查看攻略详情 |
| POST | `/introduction/add` | 发布攻略 |
| PUT | `/introduction/update` | 更新攻略 |
| DELETE | `/introduction/delete/{id}` | 删除攻略 |

#### 公告管理 (`/notice`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/notice/selectAll` | 查询所有公告 |
| GET | `/notice/selectPage` | 分页查询公告 |
| POST | `/notice/add` | 发布公告 (管理员+) |
| PUT | `/notice/update` | 更新公告 (管理员+) |
| DELETE | `/notice/delete/{id}` | 删除公告 (管理员+) |

#### 分类管理 (`/category`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/category/selectAll` | 查询所有分类 |
| GET | `/category/selectPage` | 分页查询分类 |
| POST | `/category/add` | 新增分类 |
| PUT | `/category/update` | 更新分类 |
| DELETE | `/category/delete/{id}` | 删除分类 |

#### 简历模板分类 (`/resumesort`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/resumesort/selectAll` | 查询所有分类 |
| GET | `/resumesort/selectAllSorts` | 查询分类 (无序) |
| GET | `/resumesort/selectPage` | 分页查询分类 |
| POST | `/resumesort/add` | 新增分类 |
| PUT | `/resumesort/update` | 更新分类 |
| DELETE | `/resumesort/delete/{id}` | 删除分类 |

#### 简历模板 (`/template`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/template/selectAll` | 按名称/风格筛选模板 |
| GET | `/template/selectPage` | 分页查询模板 |

#### 用户简历 (`/userTemplate`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/userTemplate/selectAll` | 查询所有用户简历 |
| GET | `/userTemplate/selectPage` | 分页查询用户简历 |
| GET | `/userTemplate/selectById/{id}` | 查看简历详情 |
| POST | `/userTemplate/add` | 保存简历 |
| PUT | `/userTemplate/update` | 更新简历 |
| DELETE | `/userTemplate/delete/{id}` | 删除简历 |

#### 文件管理 (`/files`)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/files/upload` | 上传文件 |
| POST | `/files/wang/upload` | wangEditor 图片上传 |
| GET | `/files/download/{fileName}` | 下载文件 |

#### 图表统计 (`/echarts`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/echarts/pie` | 饼图数据 (各类别攻略数量分布) |
| GET | `/echarts/bar` | 柱状图数据 (Top 5 用户发文量) |
| GET | `/echarts/line` | 折线图数据 (近 7 天每日发文数) |

### 4.6 认证与鉴权

**JWT Token 流程:**

```
登录请求 → 验证邮箱/密码 → createToken(userId-role, password)
                                ↓
                    JWT Audience: "1-admin" 或 "1-user"
                    JWT 签名: HMAC256 (密钥 = 用户密码)
                    过期时间: 1 天
                                ↓
后续请求 → Header: token → JWTInterceptor.preHandle()
                                ↓
             解析 Audience → 分割 userId + role
                                ↓
             根据 role 查 AdminService/UserService
                                ↓
             以用户密码为密钥验证 JWT 签名
                                ↓
             成功 → 放行 / 失败 → 401
```

**角色权限:**
- **admin**: 可访问所有管理功能、所有数据
- **user**: 只能查看/编辑自己的简历和攻略；公告只能查看
- **公开端点**: `/login`, `/register`, `/validate/**`, `/files/download/**`

### 4.7 工具与中间件

| 工具/中间件 | 用途 |
|------------|------|
| **PageHelper** | 所有分页查询自动拦截 SQL 追加 LIMIT |
| **Hutool** | Excel 导入导出 (ExcelWriter/ExcelReader)、日期工具、HTML 标签清理 |
| **Auth0 JWT** | Token 生成和验证 |
| **Spring Mail** | `@Async` 异步发送密码重置邮件 |
| **Apache POI** | Hutool 底层 Excel 操作依赖 |
| **Commons Lang3** | DateUtils 继承的日期处理基类 |

---

## 5. 前端详解 (Vue 3)

### 5.1 工程配置

**构建配置:** [`vue/vite.config.js`](vue/vite.config.js)

| 配置项 | 值 | 说明 |
|--------|-----|------|
| 路径别名 | `@` → `./src` | 模块导入简写 |
| 基础路径 | `./` | 相对路径 (生产部署) |
| 全局 SCSS | `@use "@/style/index.scss" as *` | 注入所有组件 |
| 自动导入 | `unplugin-auto-import` | Vue/VueRouter API 无需 import |
| 按需加载 | `unplugin-vue-components` | Element Plus + 自定义组件自动注册 |
| 代码压缩 | Terser (drop console/debugger) | 仅生产构建 |
| 分包策略 | `manualChunks` | node_modules 按包名独立拆分 |

### 5.2 路由结构

| 路径 | 页面 | 认证 | 说明 |
|------|------|------|------|
| `/` | 首页 | 否 | 模板展示，入口引导 |
| `/login` | 登录/注册 | 否 | 翻牌动画登录页 |
| `/forgetPassword` | 忘记密码 | 否 | 邮箱发送重置链接 |
| `/resetPassword` | 重置密码 | 否 | Token 校验 + 新密码 |
| `/designer` | 简历设计器 | 是 | **核心页面**：三栏布局编辑 |
| `/aiResume` | AI 助手 | 是 | DeepSeek AI 对话 |
| `/templates` | 模板市场 | 是 | 浏览全部模板 |
| `/article` | 求职攻略列表 | 是 | 分类筛选 + 文章卡片 |
| `/article/articleDetail` | 攻略详情 | 是 | 富文本内容 + 评论 |
| `/person` | 个人中心 | 是 | 5 个子页面的父路由 |
| `/person/personDetail` | 个人信息 | 是 | 编辑个人资料 |
| `/person/personIntegral` | 我的资产 | 是 | 积分详情 (待开发) |
| `/person/accountSetting` | 账号设置 | 是 | 邮箱 + 密码修改 |
| `/person/myResume` | 我的简历 | 是 | 已保存简历列表 |
| `/person/myOnlineResume` | 在线简历 | 是 | 同上功能 |
| `/admin` | 管理后台 | 是 | 8 个子页面的父路由 |
| `/admin/home` | 仪表盘 | 是 | ECharts 统计图表 |
| `/admin/userList` | 用户管理 | 是 | CRUD + Excel |
| `/admin/adminList` | 管理员管理 | 是 | CRUD + Excel |
| `/admin/person` | 管理员个人 | 是 | 编辑资料 |
| `/admin/notice` | 公告管理 | 是 | 公告 CRUD |
| `/admin/introduction` | 攻略管理 | 是 | 文章 CRUD + 富文本 |
| `/admin/category` | 分类管理 | 是 | 攻略分类 CRUD |
| `/admin/resumeSort` | 简历分类 | 是 | 模板分类 CRUD |
| `/404` | 404 页面 | 否 | 未匹配路由 |

### 5.3 状态管理 (Pinia)

| Store | 文件 | 核心状态 | 用途 |
|-------|------|---------|------|
| `useLoadingStore` | `store/loading.js` | `isLoading` | 全局加载动画控制 |
| `useResumeJsonStore` | `store/resume.js` | `resumeJsonStore` | 正在编辑的完整简历 JSON |
| `useResumeModelStore` | `store/resume.js` | `model`, `optionsName`, `title`, `id` | 当前选中的模块信息 |
| `useUserDataStore` | `store/user.js` | `userDataStore` | 当前登录用户信息 (持久化 localStorage) |
| `useUuidStore` | `store/uuid.js` | `refreshUuid` | 强制刷新渲染的 UUID key |

### 5.4 组件体系

#### 布局组件
| 组件 | 说明 |
|------|------|
| `NavBar` | 前台顶部导航 (Logo + 菜单 + 用户下拉) |
| `AdminNavBar` | 后台顶部导航 (Logo + 面包屑 + 用户下拉) |
| `FooterCom` | 页脚 (版权信息) |
| `LoadingComVue` | 全局加载遮罩层 (CSS 动画) |
| `SvgIcon` | 通用 SVG 图标组件 |

#### 设计师核心组件
| 组件 | 说明 |
|------|------|
| `DesignNav` | 设计师顶部工具栏 (保存/导出/导入/重置) |
| `ModelList` | 左侧可拖拽模块列表面板 |
| `ModelBox` | 简历模块容器 (选中/复制/删除) |
| `ResumeTheme` | 全局主题设置面板 |
| `Title` | 面板标题 + 折叠切换 |

#### 通用选项面板 (`CommonOptions/`)
| 组件 | 说明 |
|------|------|
| `CommonOptions` | 通用样式配置 (颜色/字号/字重/边距) |
| `CommonTitleOptions` | 标题样式配置 |
| `BaseInfoOptions` | 基本信息配置 (数据 + 样式) |
| `ResumeTitleOptions` | 简历标题配置 (数据 + 样式) |

#### 简历模块展示组件 (`ModelComs/`)
| 组件 | 对应模块 |
|------|---------|
| `AwardsCom` | 奖项荣誉 |
| `CampusExperienceCom` | 校园经历 |
| `EduBackgroundCom` | 教育背景 |
| `HobbiesCom` | 兴趣爱好 |
| `InternshipExperienceCom` | 实习经历 |
| `JobIntentionCom` | 求职意向 |
| `ProjectExperienceCom` | 项目经历 |
| `SelfEvaluationCom` | 自我评价 |
| `SkillSpecialtiesCom` | 技能特长 |
| `WorkExperienceCom` | 工作经历 |
| `WorksDisplayCom` | 作品展示 |

#### 其他组件
| 组件 | 说明 |
|------|------|
| `LoginDialog` | 程序化调用的登录弹窗 |
| `ImportJsonDialog` | CodeMirror JSON 导入 |
| `TipJsonDialog` | JSON 格式示例查看 |
| `TemplateImgDiglog` | 保存模板到个人中心 |
| `PreviewImage` | 全屏图片预览 |

### 5.5 核心业务模块

#### 5.5.1 首页 (`views/index/`)

- **横幅区** (`ProjectIntroduce.vue`): 标题"智能简历助你打造满分简历"，快捷入口按钮
- **模板工厂** (`TemplateSelect.vue`): 按风格分类筛选，搜索框，模板卡片分页网格

#### 5.5.2 简历设计器 (`views/designer/`) — 核心页面

三栏响应式布局：

```
┌──────────────┬────────────────────┬──────────────┐
│  左面板 300px │     中央预览区       │  右面板 350px │
│              │                    │              │
│  Title       │  实时预览 (模板1/2/3) │  Title       │
│  (折叠)       │                    │              │
│              │  ┌──────────────┐  │  动态选项面板  │
│  ModelList   │  │ 简历模块容器   │  │  或          │
│  (可拖拽排序)  │  │ (ModelBox)   │  │  ResumeTheme │
│  - 显示/隐藏  │  │              │  │  (全局主题)   │
│  - 点击选中   │  │  copy/delete │  │              │
│              │  └──────────────┘  │              │
│              │                    │              │
└──────────────┴────────────────────┴──────────────┘
```

**数据流:**
1. 从路由参数 / localStorage草稿 / 默认TEMPLATE_JSON 初始化
2. `useAddStyle()` 为每个模块注入默认样式
3. 中央用动态 `<component :is>` 渲染模板1/2/3
4. 修改通过 `changeResumeJsonData()` 同步 store
5. debounced 自动保存到 localStorage 草稿
6. 导出 PDF: `html2canvas` 截图 → `JsPDF` 生成多页 A4

#### 5.5.3 求职攻略 (`views/article/`)

- 轮播推荐位（前 4 篇）
- 分类筛选 + 标题搜索
- 文章卡片（封面、标题、摘要、作者、分类标签）
- 详情页：富文本渲染 (`v-dompurify-html`)、作者信息、评论区
- 支持 wangEditor 富文本编辑器发布文章

#### 5.5.4 AI 助手 (`views/ai/`)

- 对话式聊天界面
- 用户输入 → DeepSeek API (`deepseek-chat` 模型)
- Markdown 渲染回复 (`marked` + `DOMPurify`)
- 加载动画指示器

#### 5.5.5 个人中心 (`views/person/`)

- 左侧：头像上传 + 菜单导航
- 右侧：子路由内容区
  - 个人信息编辑
  - 我的简历（已保存模板列表，支持继续编辑或删除）
  - 在线简历
  - 账号设置（邮箱 + 修改密码弹窗）
  - 我的资产（待开发）

#### 5.5.6 管理后台 (`views/admin/`)

- **仪表盘**: ECharts 三图联动（饼图/柱状图/折线图）
- **用户/管理员管理**: 完整 CRUD + Excel 批量导入导出
- **公告管理**: 权限分级 (admin 可编辑，user 只读)
- **攻略管理**: 封面图上传 + wangEditor 富文本
- **分类管理**: 攻略分类、模板分类

---

## 6. 核心业务流程

### 6.1 用户注册/登录

```
注册 → 填写用户名/邮箱/密码 → POST /user/register → 写入 user 表
登录 → 选择 role → 输入邮箱/密码 → POST /login → 返回 JWT Token
    → 存储到 localStorage 'user_data' → 跳转首页
```

### 6.2 密码重置

```
忘记密码 → 输入邮箱 → GET /validate/sendValidationEmail
    → QQ SMTP 发送重置链接 (含 UUID token, 5 分钟有效)
    → 用户点击链接 → /resetPassword?token=xxx
    → 输入新密码 → GET /validate/resetPassword → 密码更新
    → 自动跳转登录页
```

### 6.3 简历制作全流程

```
1. 选择模板 → 浏览模板市场 → 点击"立即制作" → 携带模板 id/name 跳转 /designer
2. 编辑简历 → 设计师三栏界面：
   - 左侧：排序/显示/隐藏模块（13 个模块可选）
   - 中央：实时预览，点击模块选中
   - 右侧：编辑选中模块的样式和数据，或全局主题设置
3. 保存/导出：
   - 保存草稿 → localStorage 自动存储
   - 保存到个人中心 → POST /userTemplate/add
   - 导出 JSON → 下载为 .json 文件
   - 导入 JSON → CodeMirror 编辑 → 合并到当前简历
   - 导出 PDF → html2canvas + JsPDF → 多页 A4 下载
4. 继续编辑 → 个人中心 → 我的简历 → "继续制作" → 恢复草稿到设计器
```

### 6.4 管理员数据管理

```
仪表盘 → ECharts 加载 /echarts/pie + /echarts/bar + /echarts/line
用户管理 → 分页查询 /user/selectPage → 增删改查 + Excel 导入导出
攻略管理 → wangEditor 富文本 → 图片上传 /files/wang/upload → 发布 /introduction/add
公告管理 → 仅 admin 角色可编辑，user 仅可浏览公告
```

---

## 7. 部署说明

### 7.1 环境要求

| 软件 | 版本 | 说明 |
|------|------|------|
| JDK | 21+ | 后端运行环境 |
| Maven | 3.6+ | 后端构建 |
| Node.js | 18+ | 前端构建 |
| MySQL | 8.0+ | 数据库，需先创建库 `graduation_2025` |
| npm | 9+ | 前端依赖管理 |

### 7.2 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS graduation_2025
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

项目使用 MyBatis，表结构由 SQL 映射文件定义，需要手动建表（参考 [4.4 节](#44-数据库表结构) 中的字段说明）。

### 7.3 后端启动

```bash
# 1. 进入后端目录
cd springboot

# 2. 修改 application.yml 中的数据库连接和邮箱配置

# 3. Maven 安装依赖并启动
mvn spring-boot:run

# 或者打包运行
mvn clean package -DskipTests
java -jar target/springboot-0.0.1-SNAPSHOT.jar

# 服务启动在 http://localhost:8080
```

### 7.4 前端启动

```bash
# 1. 进入前端目录
cd vue

# 2. 安装依赖
npm install

# 3. 开发模式运行
npm run dev

# 4. 生产构建
npm run build
# 产出在 vue/dist/ 目录

# 5. 预览生产构建
npm run preview
```

### 7.5 生产部署建议

1. 前端 `npm run build` 生成 `dist/` 后部署到 Nginx
2. Nginx 反向代理 `/api` 到 `localhost:8080`
3. 数据库配置文件外置（环境变量或配置中心）
4. 修改 JWT 签名密钥为更安全的方式（当前使用用户密码）
5. 密码存储应改为 BCrypt 哈希（当前为明文）
6. 上传文件目录 (`files/`) 需要持久化存储

---

## 8. 附录

### 8.1 简历模板清单

项目包含 14 套模板预览（`template/` 目录中实际实现了 3 套）：

| 模板 | 布局风格 | 特色 |
|------|---------|------|
| Template 1 | 单栏 | 经典上下布局，适合大多数场景 |
| Template 2 | 双栏 | 左侧暗色个人信息栏 + 右侧内容区 |
| Template 3 | 时间轴 | 左侧竖线时间轴样式，带背景纹理 |

### 8.2 简历模块清单 (13 个)

| 序号 | 模块 | Model 标识 | 说明 |
|------|------|-----------|------|
| 1 | 简历标题 | `RESUME_TITLE` | 简历大标题 |
| 2 | 基本信息 | `BASE_INFO` | 姓名/年龄/地址/邮箱/电话/头像 |
| 3 | 求职意向 | `JOB_INTENTION` | 意向职位/城市/薪资/状态 |
| 4 | 教育背景 | `EDU_BACKGROUND` | 多条教育经历 |
| 5 | 技能特长 | `SKILL_SPECIALTIES` | 技能+熟练度 |
| 6 | 校园经历 | `CAMPUS_EXPERIENCE` | 校园活动/社团 |
| 7 | 实习经历 | `INTERNSHIP_EXPERIENCE` | 实习经历 |
| 8 | 工作经历 | `WORK_EXPERIENCE` | 正式工作经历 |
| 9 | 项目经历 | `PROJECT_EXPERIENCE` | 项目描述 |
| 10 | 奖项荣誉 | `AWARDS` | 获奖列表 |
| 11 | 兴趣爱好 | `HOBBIES` | 文本描述 |
| 12 | 自我评价 | `SELF_EVALUATION` | 文本描述 |
| 13 | 作品展示 | `WORKS_DISPLAY` | 作品链接/描述 |

### 8.3 技术债务与改进建议

| 问题 | 严重程度 | 建议 |
|------|---------|------|
| 密码明文存储 | 🔴 高 | 使用 BCrypt/SHA-256 哈希存储 |
| JWT 密钥为用户密码 | 🔴 高 | 使用固定服务端密钥 |
| 硬编码 API Key | 🟡 中 | DeepSeek Key 应移到后端或环境变量 |
| 数据库配置硬编码 | 🟡 中 | 使用环境变量或配置中心 |
| 无单元测试 | 🟡 中 | 补充后端 Service 层测试 |
| SQL 日志输出到控制台 | 🟢 低 | 生产环境关闭 StdOutImpl |
| `files/` 目录本地存储 | 🟢 低 | 可迁移到 OSS/S3 |
| 部分功能显示"待开发" | 🟢 低 | 积分系统等可后续迭代 |

### 8.4 关键约定

- **API 响应格式:** 所有接口统一使用 `Result { code: String, data: Object, msg: String }`
- **401 错误码:** 前端拦截器识别 `code === '401'` 自动跳转登录页
- **角色标识:** JWT Audience 格式 `{userId}-{role}`，role 取值 `admin` 或 `user`
- **分页参数:** `pageNum` (默认 1), `pageSize` (默认 10) ，通过 PageHelper 分页
- **文件上传路径:** `{user.dir}/files/`，文件名格式 `{timestamp}_{original}`
- **本地存储 Key:** `user_data` (用户信息), `resumeDraft` (草稿), `resumeVersion` (版本号)

---

> **文档生成日期:** 2026年6月21日  
> **最后更新:** 基于项目源码自动分析生成
