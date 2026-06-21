import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

export const loginAsync= (LoginForm) => {
    request.post('/login', LoginForm.form).then(res => {
        if (res.code === '200') {
            // 存储用户信息
            localStorage.setItem("user_data", JSON.stringify(res.data || {}))
            ElMessage.success('登录成功')
            router.push('/')
        } else {
            ElMessage.error(res.msg)
        }
    })
};

export const registerAsync = (RegisterForm) => {
    request.post('/user/register', RegisterForm.form).then(res => {
        if (res.code === '200') {
            ElMessage.success('注册成功')
            router.push('/login')
        } else {
            ElMessage.error(res.msg)
        }
    })
};

export const forgetPasswordAsync = (ruleForm) => {
    return request.get('/validate/sendValidationEmail',{
        params: {
            email: ruleForm.email
        }
    }).then(res => {
        if (res.code === '200') {
            return res;
        } else {
            ElMessage.error(res.msg)
            return false;
        }
    })
};
export const resetPasswordAsync = (ruleForm) => {
    return request.get('/validate/resetPassword',{
        params: {
            token: ruleForm.token,
            password: ruleForm.password,
            confirmPassword: ruleForm.confirmPassword,
        }
    }).then(res => {
        if (res.code === '200') {
            ElMessage({
            message: '重置密码成功',
            type: 'success'
        });
        return res;
        } else {
            ElMessage({
                message: data.data,
                type: 'error'
            });
            return false;
        }
    })
};
export const updateUser = (user) => {
    let url
    if (user.role === 'user') {
        url = '/user/update'
    }
    return request.put(url, user).then(res => {
        if (res.code === '200') {
            ElMessage.success('更新成功')
        }
        else {
            ElMessage.error('更新失败')
        }
    })
};