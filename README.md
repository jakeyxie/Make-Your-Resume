
<div align="center">
  <h1>📄 简才网 · 智能简历平台</h1>
  <p><strong>JianCai Wang — Make Your Resume</strong></p>
  <p>一个功能完整的在线简历制作、模板选择、AI辅助的智能简历平台</p>
</div>

---

## 📋 项目概述

简才网是一个全栈简历制作平台，包含：

- **用户端**：在线简历设计师（14套模板）、AI智能简历助手、求职攻略
- **管理端**：用户管理、模板管理、文章/公告管理、数据统计看板
- **核心功能**：拖拽式简历编辑、JSON导入/导出、PDF导出、多模板切换

## 🏗️ 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **前端** | Vue 3 + Vite + Pinia + Vue Router | 3.5 / 6.x / 3.x / 4.x |
| **UI** | Element Plus + SCSS + ECharts | 2.9 / 5.6 |
| **后端** | Spring Boot + MyBatis + MySQL | 3.4.4 / 3.0.3 |
| **工具** | Hutool + Lombok + JWT (Auth0) + PageHelper | |
| **AI** | DeepSeek API (智能简历助手) | |

## 🚀 快速开始

### 环境要求

- Node.js >= 18, pnpm/npm
- JDK 21+, Maven 3.8+
- MySQL 8.0+

### 1. 后端启动

```bash
cd springboot

# 创建数据库（MySQL）
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS graduation_2025 DEFAULT CHARSET utf8mb4;"

# 配置环境变量（可选，默认有开发环境值）
export DB_PASSWORD=123456
export JWT_SECRET=your-jwt-secret-key

# 启动（会自动执行 src/main/resources/ 下的SQL初始化脚本，如有）
mvn spring-boot:run
```

后端默认运行在 **http://localhost:8080**

### 2. 前端启动

```bash
cd vue

# 安装依赖
npm install

# 配置环境变量（复制模板并修改）
cp .env.example .env
# 编辑 .env 文件填入正确的 API Key

# 开发模式启动
npm run dev
```

前端默认运行在 **http://localhost:5173**

> 💡 开发时请确保后端先启动，前端 `.env` 中 `VITE_API_BASE_URL` 指向后端地址。

## 📁 项目结构

```
Make-Your-Resume/
├── springboot/                          # 后端 Spring Boot 应用
│   ├── pom.xml                          # Maven 构建配置
│   ├── env.example                      # 环境变量模板
│   └── src/main/
│       ├── java/com/example/
│       │   ├── Application.java         # 应用入口
│       │   ├── common/                  # 通用组件
│       │   │   ├── CorsConfig.java      # CORS 跨域配置
│       │   │   ├── JWTInterceptor.java  # JWT 鉴权拦截器
│       │   │   ├── Result.java          # 统一响应封装
│       │   │   └── WebConfig.java       # Web MVC 配置
│       │   ├── config/
│       │   │   └── JwtConfig.java       # JWT & 安全配置
│       │   ├── controller/              # 控制器层
│       │   ├── entity/                  # 实体类
│       │   ├── exception/               # 异常处理
│       │   ├── mapper/                  # MyBatis Mapper
│       │   ├── service/                 # 服务层
│       │   └── utils/                   # 工具类
│       └── resources/
│           ├── application.yml          # 主配置（支持环境变量）
│           └── mapper/                  # MyBatis XML
│
├── vue/                                 # 前端 Vue 3 应用
│   ├── index.html                       # HTML 入口
│   ├── vite.config.js                   # Vite 构建配置
│   ├── .env.example                     # 环境变量模板
│   └── src/
│       ├── App.vue                      # 根组件
│       ├── main.js                      # 应用入口
│       ├── assets/                      # 静态资源
│       ├── components/                  # 通用组件
│       │   ├── CommonOptions/           # 公共选项面板
│       │   ├── LoginDialog/             # 登录对话框
│       │   ├── ModelBox/               # 拖拽模块容器
│       │   ├── ModelComs/              # 简历模块编辑组件
│       │   ├── NavBar/                 # 导航栏
│       │   └── ResumeTheme/            # 主题设置
│       ├── hooks/                      # 组合式函数
│       ├── http/api/                   # API 请求封装
│       ├── router/                     # 路由配置
│       ├── schema/                     # JSON Schema
│       ├── store/                      # Pinia 状态管理
│       ├── style/                      # 全局样式
│       ├── template/                   # 简历模板（14套）
│       │   ├── template1/
│       │   ├── template2/
│       │   ├── template3/
│       │   └── index.js
│       ├── utils/                      # 工具函数
│       └── views/                      # 页面视图
│           ├── admin/                  # 管理后台
│           ├── ai/                     # AI简历助手
│           ├── article/                # 求职攻略
│           ├── designer/               # 简历设计器（核心）
│           ├── index/                  # 首页
│           ├── login/                  # 登录注册
│           ├── person/                 # 个人中心
│           └── templates/              # 模板市场
│
├── .gitignore
└── README.md
```

## 🧩 核心功能模块

### 简历设计器 (`/designer`)
- 拖拽式模块编排（14种简历模块）
- 实时预览 + 分页线指示
- 全局主题设置（颜色、字体、间距）
- 14套精美简历模板
- PDF 导出（基于 html2canvas + jsPDF）
- JSON 导入/导出（支持草稿保存到本地或云端）

### AI 智能简历 (`/aiResume`)
- 接入 DeepSeek API
- 智能生成简历内容和建议

### 管理后台 (`/admin`)
- ECharts 数据看板
- 用户/管理员管理
- 模板分类管理
- 文章公告管理

## 🔧 配置说明

### 环境变量（后端）

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `DB_PASSWORD` | 数据库密码 | `123456` |
| `SMTP_PASSWORD` | 邮件SMTP授权码 | `nvgqdkbbumgdjiaj` |
| `JWT_SECRET` | JWT签名密钥 | `my-resume-jwt-secret-key-2025` |
| `CORS_ORIGINS` | 允许跨域的来源 | `http://localhost:5173,http://localhost:8080` |
| `FRONTEND_URL` | 前端URL（邮件链接） | `http://localhost:5173` |

### 环境变量（前端 `vue/.env`）

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `VITE_API_KEY` | DeepSeek API Key | 需自行填写 |
| `VITE_API_BASE_URL` | 后端API地址 | `http://localhost:8080` |

## 🛠️ 优化日志

本项目在初始版本基础上进行了以下优化：

### 安全性
- 🔑 API密钥从代码中移除，移至环境变量
- 🔒 密码存储采用 BCrypt 加密
- 🛡️ JWT 使用独立密钥签名，与用户密码解耦
- 🌐 CORS 配置可配置化，支持多环境
- 📧 硬编码URL替换为可配置属性

### 代码质量
- 🧹 移除了生产代码中的测试/练习文件
- 🐛 修复了 `CustomerException` 拼写错误
- 🏷️ 修复了多处文件命名拼写错误（`Diglog`→`Dialog`, `Detial`→`Detail`, `Evalution`→`Evaluation`）
- 🔄 简化为统一 Hutool 依赖，移除重复引用
- 💬 日志替换 `System.out.println`
- 🗑️ 移除死代码（无用路由、未使用 import、重复的组件注册）
- 📝 所有数据写操作添加 `@Transactional` 事务注解

### 结构优化
- 📂 移除了 `test/` 目录中的练习类
- 🔧 配置类统一整理
- 🎯 移除测试路由 `/test`

## 📄 License

MIT
