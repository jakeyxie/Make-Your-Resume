import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

export default defineConfig({
  base: './',
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  build: {
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true
      }
    },
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
    AutoImport({
      resolvers: [ElementPlusResolver({importStyle:"sass"})],
      imports: ['vue', 'vue-router']
    }),
    Components({
      resolvers: [
        ElementPlusResolver({importStyle:"sass"}),
        (name) => {
          const customComponents = {
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
            'RouterLink': 'vue-router',
            'RouterView': 'vue-router',
            'SvgIcon': './src/components/SvgIcon/SvgIcon.vue',
          }
          if (customComponents[name]) return { name, from: customComponents[name] }
        }
      ],
      dirs: ['src/**'],
      extensions: ['vue'],
      dts: false
    }),
  ],
})
