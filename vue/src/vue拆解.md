# Vue 前端项目拆解

> 项目路径：`e:\.dev\Make-Your-Resume\vue`

---

## 一、项目概览

### 技术栈

| 技术 | 用途 |
|------|------|
| **Vue 3** (Composition API) | 前端框架 |
| **Vite 6** | 构建工具 |
| **Pinia** | 状态管理（类似 Vuex，但更轻量） |
| **Vue Router 4** | 路由管理 |
| **Element Plus** | UI 组件库 |
| **Axios** | 网络请求 |
| **SASS/SCSS** | 样式预处理 |
| **ECharts** | 图表库 |
| **jsPDF** | PDF 导出 |
| **CodeMirror** | 代码编辑器（JSON 编辑） |
| **marked** | Markdown 渲染 |
| **vuedraggable** | 拖拽排序 |
| **@wangeditor** | 富文本编辑器 |

### 启动方式

```bash
cd e:\.dev\Make-Your-Resume\vue
npm run dev    # 开发模式，默认 http://localhost:5173
npm run build  # 打包构建
```

---

## 二、目录结构

```
vue/
├── index.html                 # HTML 入口
├── vite.config.js             # Vite 构建配置
├── package.json               # 依赖和脚本
├── .env                       # 环境变量
│
└── src/
    ├── main.js                # 【入口】Vue 应用启动
    ├── App.vue                # 【根组件】路由出口 + 全局层
    │
    ├── router/
    │   └── index.js           # 【路由】所有页面的路径定义
    │
    ├── store/
    │   ├── index.js           # Pinia 实例
    │   ├── user.js            # 用户信息状态
    │   ├── resume.js          # 简历数据状态
    │   ├── uuid.js            # UUID 生成器状态
    │   └── loading.js         # 全局加载状态
    │
    ├── http/
    │   ├── api/
    │   │   ├── user.js        # 用户相关 API（登录/注册/密码）
    │   │   └── interview.js   # 面试模拟 API
    │   └── request.js         # Axios 实例 + 拦截器
    │
    ├── utils/
    │   ├── common.js          # 工具函数（时间格式化、UUID、熟练度转换等）
    │   ├── registerCom.js     # 全局组件注册
    │   └── request.js         # (同 http/request.js，旧引用)
    │
    ├── hooks/
    │   ├── useAddStyle.js         # 给简历模块添加默认样式
    │   ├── useModelOptionsComName.js  # 根据模板名获取对应的属性面板组件名
    │   └── ...其他 hooks
    │
    ├── schema/
    │   ├── model.js           # 【核心】简历数据结构模型（默认 JSON）
    │   ├── style.js           # 默认样式定义
    │   └── import.js          # JSON 导入相关
    │
    ├── template/
    │   ├── index.js           # 【核心】模板列表和组件映射
    │   ├── template1/         # 模板1（简约风格）
    │   ├── template2/         # 模板2（双栏风格）
    │   └── template3/         # 模板3（现代风格）
    │
    ├── components/
    │   ├── NavBar/            # 顶部导航栏
    │   ├── FooterCom/         # 页脚
    │   ├── ModelBox/          # 简历模块容器
    │   ├── ModelComs/         # 简历基础组件（标题、基础信息、教育背景等）
    │   ├── CommonOptions/     # 属性设置面板组件
    │   ├── ResumeTheme/       # 主题设置
    │   └── ImportJsonDialog/  # JSON 导入对话框
    │
    ├── views/
    │   ├── index/             # 【首页】项目介绍 + 模板展示
    │   ├── login/             # 登录页
    │   ├── forgetPassword/    # 忘记密码
    │   ├── resetPassword/     # 重置密码
    │   ├── templates/         # 模板选择页
    │   ├── designer/          # 【核心】简历编辑器
    │   ├── ai/                # AI 简历制作
    │   ├── interview/         # 模拟面试（对接 FastAPI）
    │   ├── article/           # 求职攻略
    │   ├── person/            # 个人中心（含嵌套子页面）
    │   ├── admin/             # 后台管理
    │   └── 404/               # 404 页面
    │
    ├── style/
    │   ├── global.css         # 全局 CSS 变量 + 通用样式
    │   └── index.scss         # Element Plus 主题覆盖
    │
    └── assets/
        └── images/            # 静态图片资源
```

---

## 三、核心数据流

### 3.1 整体架构

```
用户操作 → Vue 组件 → Pinia Store → Axios → Spring Boot 后端 → MySQL
                              ↕
                        localStorage（草稿缓存）
```

### 3.2 简历编辑器数据流（最核心）

```
schema/model.js (默认JSON模板)
       ↓
useAddStyle hook (添加默认样式)
       ↓
Pinia resumeStore (resumeJsonStore)
       ↓
设计师页面 → 左侧: ModelBox 组件列表
             中间: Template 渲染
             右侧: CommonOptions 属性面板
       ↓
用户编辑 → 实时更新 Pinia Store
       ↓
导出: jsPDF / localStorage 存草稿 / JSON 导出
```

### 3.3 路由守卫流程

```
访问页面
  → request.js 拦截器自动带 token
  → 后端验证 token
  → 401 → 强制跳转 /login
  → 200 → 正常加载页面
```

---

## 四、路由结构详解

```js
/                     → 首页（项目介绍 + 模板展示）
/login                → 登录
/forgetPassword       → 忘记密码
/resetPassword        → 重置密码
/templates            → 模板选择
/designer             → 简历编辑器（核心功能）
/aiResume             → AI 简历助手
/interview            → 模拟面试（对接 FastAPI Agent）
/interview/report/:id → 面试报告
/article              → 求职攻略列表
/article/articleDetail → 攻略详情
/person               → 个人中心（嵌套路由）
  ├── personDetail    → 个人信息
  ├── personIntegral  → 我的资产
  ├── accountSetting  → 账号设置
  ├── myResume        → 我的简历列表
  ├── myOnlineResume  → 在线简历
  └── interviewRecords → 面试记录
/admin                → 后台管理（嵌套路由）
  ├── home            → 管理主页
  ├── adminList       → 管理员信息
  ├── userList        → 用户管理
  ├── person          → 个人信息
  ├── notice          → 系统公告
  ├── introduction    → 求职攻略管理
  ├── category        → 攻略分类
  └── resumeSort      → 模板分类
/404                  → 404 页面
```

---

## 五、组件层级说明

### 5.1 页面 → 组件结构

```
App.vue
 ├── NavBar.vue         （所有页面共用导航栏）
 ├── router-view        （页面内容区域）
 │    ├── 首页
 │    │    ├── ProjectIntroduce.vue   （Hero 大标题区）
 │    │    ├── TemplateSelect.vue      （模板选择器）
 │    │    └── TemplateCard.vue        （模板卡片）
 │    │
 │    ├── 简历编辑器
 │    │    ├── DesignNav.vue           （顶部操作栏）
 │    │    ├── ModelList.vue           （左侧模块列表）
 │    │    ├── TemplateX.vue           （中间渲染区域，由模板驱动）
 │    │    │    ├── BaseInfo.vue
 │    │    │    ├── WorkExperience.vue
 │    │    │    ├── EduBackground.vue
 │    │    │    └── ...
 │    │    ├── ResumeTheme.vue         （右侧主题设置）
 │    │    ├── CommonOptions/*         （右侧属性面板）
 │    │    ├── ImportJsonDialog.vue    （导入对话框）
 │    │    └── ModelBox.vue            （模块容器）
 │    │
 │    ├── 个人中心
 │    │    ├── LeftCard.vue            （左侧头像卡片）
 │    │    ├── PersonMenu.vue          （左侧菜单）
 │    │    └── RightContent.vue        （右侧内容区 router-view）
 │    │
 │    └── 模拟面试
 │         ├── 左侧面板（会话历史列表）
 │         ├── 中间面板（即时聊天）
 │         └── 右侧面板（潜台词+策略）
 │
 └── FooterCom.vue       （页脚）
```

---

## 六、核心模块详解

### 6.1 简历编辑器 — 这是项目的核心页面

设计师页面 `views/designer/index.vue` 是**最重要的页面**，由三栏构成：

```
┌──────────┬─────────────────────┬─────────────┐
│ 左侧模块列表  │  中间模板渲染           │  右侧属性面板   │
│            │                      │              │
│ ModelBox   │  Template1/2/3.vue   │  CommonOptions│
│ (可拖拽)    │  实时预览简历          │  编辑字体/颜色  │
│            │                      │  添加内容      │
└──────────┴─────────────────────┴─────────────┘
```

工作流程：

```
1. 用户选择模板 → 路由参数传参 (id, name)
2. 加载 schema/model.js 默认 JSON
3. useAddStyle() 给每个模块加默认样式
4. 渲染中间模板 → 用户编辑 → 右侧面板实时响应
5. localStorage 自动存草稿
6. 导出 PDF / JSON / 保存到后端
```

### 6.2 模板系统 `template/`

```
template/index.js          # 导出所有模板配置
template1/Template1.vue    # 简约风格
template2/Template2.vue    # 双栏风格
template3/Template3.vue    # 现代风格
```

每个模板由多个**模块组件**组成（共 13 种）：

| 模块名 | 说明 | 数据源 |
|--------|------|--------|
| RESUME_TITLE | 简历标题 | schema/model.js |
| BASE_INFO | 基本资料（姓名、年龄、电话等） | schema/model.js |
| JOB_INTENTION | 求职意向 | schema/model.js |
| EDU_BACKGROUND | 教育背景（可多条） | schema/model.js |
| SKILL_SPECIALTIES | 技能特长 | schema/model.js |
| CAMPUS_EXPERIENCE | 校园经历 | schema/model.js |
| INTERNSHIP_EXPERIENCE | 实习经验 | schema/model.js |
| WORK_EXPERIENCE | 工作经验 | schema/model.js |
| PROJECT_EXPERIENCE | 项目经验 | schema/model.js |
| AWARDS | 荣誉奖项 | schema/model.js |
| HOBBIES | 兴趣爱好 | schema/model.js |
| SELF_EVALUATION | 自我评价 | schema/model.js |
| WORKS_DISPLAY | 作品展示 | schema/model.js |

### 6.3 状态管理 `store/`

| Store | 职责 | 关键数据 |
|-------|------|---------|
| userDataStore | 用户登录信息 | id, username, avatar, token |
| resumeJsonStore | 简历内容数据 | 整个简历 JSON（LIST、GLOBAL_STYLE） |
| resumeModelStore | 当前选中的模块 | model, optionsName, title, id |
| uuidStore | 组件重新渲染触发 | 每次 setUuid() 触发子组件更新 |
| loadingStore | 全局加载状态 | isLoading |

### 6.4 API 层 `http/`

```
request.js          ← Axios 实例，自动携带 token
  ├── api/user.js   ← 登录、注册、修改密码、更新用户
  └── api/interview.js  ← 面试创建、消息、报告
```

特点：
- 自动从 `localStorage` 取 `user_data` 拼到请求头 `token`
- 401 自动跳登录
- `VITE_API_BASE_URL` 默认 `http://localhost:8080`

---

## 七、路由元信息说明

每个路由都有 `meta` 字段：

```js
meta: {
  title: '页面标题',         // 浏览器标题/面包屑
  keepAlive: true/false,    // 是否缓存页面
  requireAuth: true/false,  // 是否需要登录
  isShowComNav: true/false, // 个人中心左侧是否显示导航
  showTitle: true/false     // 个人中心是否显示标题栏
}
```

---

## 八、面试模拟模块（外挂 FastAPI）

```
Vue 前端（5173）
  ├── REST → Spring Boot（8080）     ← 现有业务
  └── REST/WS → FastAPI（8000）      ← 面试模拟 Agent
```

- 面试页面路径：`/interview`
- WebSocket 协议连接 FastAPI `ws://localhost:8000/ws/chat/{session_id}`
- 数据存储在 FastAPI 的 SQLite 中，不经过 Spring Boot

---

## 九、常见开发任务索引

| 想做什么 | 改哪个文件 |
|---------|-----------|
| 修改首页内容 | `views/index/index.vue` + `components/ProjectIntroduce.vue` |
| 新增一个简历模块 | `schema/model.js` 加数据 → `template/` 加渲染组件 → `components/CommonOptions/` 加属性面板 → `hooks/useModelOptionsComName.js` 加映射 |
| 新增一个简历模板 | `template/` 下新建目录 → `template/index.js` 注册 |
| 修改导航栏菜单 | `components/NavBar/NavBar.vue` |
| 新增页面 | 1. 建 `.vue` 文件 → 2. `router/index.js` 加路由 → 3. 导航栏加菜单项 |
| 修改 API 地址 | `.env` 文件改 `VITE_API_BASE_URL` |
| 修改全局颜色主题 | `style/global.css` 的 CSS 变量 + `style/index.scss` 的 Element Plus 变量 |
| 修改面试 Agent 地址 | `.env` 文件改 `VITE_INTERVIEW_API_URL` |
| 修改简历默认数据 | `schema/model.js` |
| 修改全局样式 | `style/global.css` |

---

## 十、与后端 Spring Boot 的对应关系

| 前端 | 后端接口 | 说明 |
|------|---------|------|
| `api/user.js` loginAsync | `POST /login` | 登录 |
| `api/user.js` registerAsync | `POST /user/register` | 注册 |
| `api/user.js` forgetPasswordAsync | `GET /validate/sendValidationEmail` | 发送验证码 |
| `api/user.js` resetPasswordAsync | `GET /validate/resetPassword` | 重置密码 |
| `api/user.js` updateUser | `PUT /user/update` 或 `PUT /admin/update` | 更新用户 |
| `utils/request.js` | 自动带 token | 所有请求的拦截器 |
| 文件上传 | `POST /files/upload` | 阿里云 OSS |
| wang-editor 上传 | `POST /files/wang/upload` | 富文本图片上传 |