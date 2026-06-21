import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite' // 自动导入vue中的组件
import Components from 'unplugin-vue-components/vite' //自动导入ui-组件 比如 element-plus等
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers' // 对应组件库引入 ，AntDesignVueResolver

export default defineConfig({
  base: './',
  resolve: {
    // 设置别名
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    // terser作为压缩工具
    minify: 'terser',
    terserOptions: {
      compress: {
        //生产环境时移除console.log()
        drop_console: true,
        //移除debug语句
        drop_debugger: true
      }
    },
    //对于所有来自 node_modules 目录的第三方依赖，按照它们的模块名称将它们拆分成不同的代码块（chunk）。
    // 这种拆分可以帮助你在生产环境中按需加载依赖，提高页面加载速度和性能。
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            return id.toString().split('node_modules/')[1].split('/')[0].toString();
          }
        }
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/style/index.scss" as *;`,
      },
    },
  },
  plugins: [
    vue(),
    //element-plus按需导入
    AutoImport({
      resolvers: [ElementPlusResolver({importStyle:"sass"})],
      imports: ['vue', 'vue-router'] // 自动导入 Vue 和路由相关 API
    }),
    Components({
      resolvers: [
        // 配置elementPlus采用sass样式配置系统
        ElementPlusResolver({importStyle:"sass"}),
        // 自定义组件自动注册
        (name) => {
          const customComponents = {
            //自定义组件
            'AwardsCom': './src/components/ModelComs/AwardsCom.vue',
            'BaseInfoOptions': './src/components/CommonOptions/BaseInfoOptions.vue',
            'CampusExperienceCom': './src/components/ModelComs/CampusExperienceCom.vue',
            'CommonOptions': './src/components/CommonOptions/CommonOptions.vue',
            'CommonTitleOptions': './src/components/CommonOptions/CommonTitleOptions.vue',
            'EduBackgroundCom': './src/components/ModelComs/EduBackgroundCom.vue',
            'FooterCom': './src/components/FooterCom/FooterCom.vue',
            'HobbiesCom': './src/components/ModelComs/HobbiesCom.vue',
            'ImportJsonDialog': './src/components/ImportJsonDialog/ImportJsonDialog.vue',
            'InternshipExperienceCom': './src/components/ModelComs/InternshipExperienceCom.vue',
            'JobIntentionCom': './src/components/ModelComs/JobIntentionCom.vue',
            'LoadingCom': './src/components/Loading/LoadingCom.vue',
            'ModelBox': './src/components/ModelBox/ModelBox.vue',
            'NavBar': './src/components/NavBar/NavBar.vue',
            'PreviewImage': './src/components/PreviewImage/PreviewImage.vue',
            'ProjectExperienceCom': './src/components/ModelComs/ProjectExperienceCom.vue',
            'ResumeTheme': './src/components/ResumeTheme/ResumeTheme.vue',
            'ResumeTitleOptions': './src/components/CommonOptions/ResumeTitleOptions.vue',
            'TipJsonDialog': './src/components/TipJsonDialog/TipJsonDialog.vue',
            'WorkExperienceCom': './src/components/ModelComs/WorkExperienceCom.vue',
            'WorksDisplayCom': './src/components/ModelComs/WorksDisplayCom.vue',
            'SelfEvaluationCom': './src/components/ModelComs/SelfEvaluationCom.vue',
            'SkillSpecialtiesCom': './src/components/ModelComs/SkillSpecialtiesCom.vue',
            //vue 组件
            'RouterLink': 'vue-router',
            'RouterView': 'vue-router',
            'SvgIcon': './src/components/SvgIcon/SvgIcon.vue',
          }
          if (customComponents[name]) return { name, from: customComponents[name] }
        }
      ],
      dirs: ['src/**'], // 扫描目录
      extensions: ['vue'], // 仅处理 vue 文件
      dts: false // 禁用 .d.ts 生成（JS 项目不需要）
    }),
  ],
  define: {
    'import.meta.env.VITE_API_KEY': JSON.stringify('sk-35e16b96353e41179fa076c7342b244a'),
    'import.meta.env.MODE': JSON.stringify(process.env.NODE_ENV || 'development')
  }

})
