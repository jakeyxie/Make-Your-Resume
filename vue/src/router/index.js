import { createRouter, createWebHistory } from 'vue-router'

const Designer = () => import('@/views/designer/index.vue');
const Index = () => import('@/views/index/index.vue');
const NotFound = () => import('@/views/404/notFound.vue');
const Login = () => import('@/views/login/index.vue')
const Person = () => import('@/views/person/index.vue')
const PersonDetail = () => import('@/views/person/personDetial/index.vue')
const PersonIntegral = () => import('@/views/person/integralDetail/index.vue')
const AccountSetting = () => import('@/views/person/accountSetting/index.vue')
const MyResume = () => import('@/views/person/myResume/index.vue')
const MyOnlineResume = () => import('@/views/person/onlineResume/index.vue')
// const MyComment = () => import('@/views/person/accountSetting/index.vue')
const ForgetPassword = () => import('@/views/forgetPassword/index.vue')
const ResetPassword = () => import('@/views/resetPassword/index.vue')
const Article = () => import('@/views/article/index.vue');
const Templates = () => import('@/views/templates/index.vue')
const ArticleDetail = () => import('@/views/admin/introduction/components/IntroductionDetail.vue');
const Ai = () => import('@/views/ai/index.vue');

//管理员界面
const Admin = () => import('@/views/admin/index.vue')
const Home = () => import('@/views/admin/home/index.vue')
const UserList = () => import('@/views/admin/userList/index.vue')
const AdminList = () => import('@/views/admin/adminList/index.vue')
const AdminPerson = () => import('@/views/admin/adminPerson/index.vue')
const Notice = () => import('@/views/admin/notice/index.vue')
const Introduction = () => import('@/views/admin/introduction/index.vue')
const Category = () => import('@/views/admin/category/index.vue')
const Apply = () => import('@/views/admin/resumeSort/index.vue')

const Test = () => import('@/components/LoginDialog/LoginDialog.vue')

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Index',
      meta: {
        title: '首页',
        keepAlive: true,    //是否缓存该页面
        requireAuth: false  //是否需要认真
      },
      component: Index
    },
    {
      path: '/404',
      name: '404',
      meta: {
        title: '404',
        keepAlive: false,
        requireAuth: false
      },
      component: NotFound
    },
    {
      path: '/designer',
      name: 'Designer',
      meta:{
        title: '设计',
        keepAlive: true,
        requireAuth: true
      },
      component: Designer
    },
    {
      path: '/login',
      name: 'Login',
      meta:{
        title: '登录',
        keepAlive: true,
        requireAuth: false
      },
      component: Login
    },
    {
      path: '/article',
      name: 'Article',
      meta:{
        title: '求职攻略',
        keepAlive: true,
        requireAuth: true
      },
      component: Article
    },
    {
      path: '/article/articleDetail',
      name: 'ArticleDetail',
      meta:{
        title: '求职攻略详情',
        keepAlive: true,
        requireAuth: true
      },
      component: ArticleDetail
    },
    {
      path: '/templates',
      name: 'Templates',
      meta:{
        title: '简历模板',
        keepAlive: true,
        requireAuth: true
      },
      component: Templates
    },
    {
      path: '/aiResume',
      name: 'aiResume',
      meta:{
        title: '智能简历',
        keepAlive: true,
        requireAuth: true
      },
      component: Ai
    },
    {
      path: '/person',
      name: 'Person',
      meta:{
        title: '个人主页',
        keepAlive: true,
        requireAuth: true
      },
      component: Person,
      children: [
        {
          path: 'personDetail',
          name: 'PersonDetail',
          meta: {
            title: '个人信息',
            keepAlive: true,
            isShowComNav: true,
            requireLogin: true,
            showTitle: true
          },
          component: PersonDetail
        },
        {
          path: 'personIntegral',
          name: 'PersonIntegral',
          meta: {
            title: '我的资产',
            keepAlive: true,
            isShowComNav: true,
            requireLogin: true,
            showTitle: false
          },
          component: PersonIntegral
        },
        {
          path: 'accountSetting',
          name: 'AccountSetting',
          meta: {
            title: '账号设置',
            keepAlive: true,
            isShowComNav: true,
            requireLogin: true,
            showTitle: true
          },
          component: AccountSetting
        },
        // {
        //   path: 'myComment',
        //   name: 'MyComment',
        //   meta: {
        //     title: '我的评论',
        //     keepAlive: true,
        //     isShowComNav: true,
        //     requireLogin: true,
        //     showTitle: true
        //   },
        //   component: MyComment
        // },
        {
          path: 'myResume',
          name: 'MyResume',
          meta: {
            title: '我的简历',
            keepAlive: true,
            isShowComNav: true,
            requireLogin: true,
            showTitle: true
          },
          component: MyResume
        },
        {
          path: 'myOnlineResume',
          name: 'MyOnlineResume',
          meta: {
            title: '在线简历',
            keepAlive: true,
            isShowComNav: true,
            requireLogin: true,
            showTitle: true
          },
          component: MyOnlineResume
        }
      ]
    },
    {
      path: '/forgetPassword',
      name: 'ForgetPassword',
      meta:{
        title: '忘记密码',
        keepAlive: true,
        requireAuth: false
      },
      component: ForgetPassword
    },
    {
      path: '/resetPassword',
      name: 'ResetPassword',
      meta:{
        title: '重置密码',
        keepAlive: true,
        requireAuth: false
      },
      component: ResetPassword
    },

    //管理员界面
    {
      path: '/admin',
      name: 'Admin',
      meta: {
        title: '管理员页面',
        keepAlive: true,
        isShowComNav: false,
        requireLogin: true
      },
      component: Admin,
      redirect: '/admin/home',
      children: [
        {
          path: 'home',
          meta: {
            name: '主页',
            keepAlive: true,
            requireAuth: true
          },
          component: Home,
        },
        {
          path: 'adminList',
          meta: {
            name: '管理员信息',
            keepAlive: true,
            requireAuth: true
          },
          component: AdminList
        },
        {
          path: 'userList',
          meta: {
            name: '普通用户信息',
            keepAlive: true,
            requireAuth: true
          },
          component: UserList
        },
        {
          path: 'person',
          meta: {
            name: '个人信息',
            keepAlive: true,
            requireAuth: true
          },
          component: AdminPerson
        },
        { 
          path: 'notice', 
          meta: { 
            name: '系统公告', 
            keepAlive: true,
            requireAuth: true 
          },
          component: Notice
        },
        { 
          path: 'introduction', 
          meta: { 
            name: '求职攻略',
            keepAlive: true,
            requireAuth: true 
          },
          component: Introduction
        },
        { 
          path: 'category', 
          meta: { 
            name: '攻略分类',            
            keepAlive: true,
            requireAuth: true 
          },
          component: Category
        },
        { 
          path: 'resumeSort',
          meta: { name: '模板分类' ,
            keepAlive: true,
            requireAuth: true
          },
          component: Apply
        }
      ]
    },
    {
      path: '/test',
      name: 'Test',
      meta:{
        title: '测试',
        keepAlive: true,
        requireAuth: true
      },
      component: Person
    },
    {
      path: '/:pathMatch(.*)', redirect: '404'
    },
  ]
})

export default router
