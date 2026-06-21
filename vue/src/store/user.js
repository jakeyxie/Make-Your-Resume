import { defineStore } from 'pinia';

const user_data = JSON.parse(localStorage.getItem('user_data') || '{}');

export const useUserDataStore = defineStore('userData',{
    state: () => {
        return {
            userDataStore: reactive(user_data)
        };
    },
    getters: {},
    actions: {
        updateUserData(obj) {
            this.userDataStore = obj;
            // 通过对象替换触发响应式更新
            this.userDataStore = { ...this.userDataStore, ...obj };
        }
    }
});

