import { createApp } from 'vue'     //创建vue实例
import App from './App.vue'         //根组件
import store from './store';        //pinia仓库
//引入router
import router from './router'
//注册自定义组件
import component from '@/utils/registerCom';
// 注册所有图标
import SvgIcon from '@/components/SvgIcon/SvgIcon.vue';
import ElementPlus from 'element-plus'
// 图标
import '@/assets/iconfont/iconfont.js';
import 'element-plus/dist/index.css'
// import '@/style/index.scss'
// 颜色选择控件
import ColorPicker from 'colorpicker-v3'; // 注册组件
import 'colorpicker-v3/style.css' // 引入样式文件

import zhCn from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/style/global.css'

const app = createApp(App)
app.use(ElementPlus,{
    locale: zhCn
})//ElementPlus中文语言包
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}//注册Element所有组件
//注册Svg 仓库 路由 颜色选择器 组件
app.component('SvgIcon', SvgIcon);
app.use(store);
app.use(router)
app.use(ColorPicker);
app.use(component);
app.mount('#app')
