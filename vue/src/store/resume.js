import { defineStore } from 'pinia';
import TEMPLATE_JSON from '@/schema/model';


export const useResumeJsonStore = defineStore('resumeJson',{
    state: () => {
        return {
            resumeJsonStore: TEMPLATE_JSON
        };
    },
    getters: {},
    actions: {
        changeResumeJsonData(obj) {
            this.resumeJsonStore = obj;
        }
    }
});

export const useResumeModelStore = defineStore('resumeModel',{
    state: () => {
        return {
            model: '', // 选中的模块名称
            optionsName: '', // 需要使用的属性设置面板组件名
            title: '主题设置',
            id: '' // 选中的id
        };
    },
    getters: {},
    actions: {
        setResumeModel({ model, optionsName, title, id }) {
            // 批量更新
            this.$patch({
                model,
                optionsName,
                title,
                id
            });
        }
    }
});
