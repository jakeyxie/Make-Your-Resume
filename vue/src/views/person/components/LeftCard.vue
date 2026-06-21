<template>
  <div class="left-person-card">
    <!-- 头像 -->
    <div class="avatar-box">
      <el-upload
        class="person-avatar-uploader"
        action="http://localhost:8080/files/upload"
        list-type="picture"
        :headers="{ token: data.user.token }"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload">
        <img
          v-if="data.user.avatar"
          :src="data.user.avatar"
          class="avatar"
        />
        <img v-else style="width: 70px; height: 70px; border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">

        <!-- 相机图标 -->
        <div v-if="!data.user.avatar" class="camera-box">
          <svg-icon icon-name="icon-icon-" class="iconfont" color="#fff" size="30px"></svg-icon>
        </div>
      </el-upload>
    </div>
    <!-- 菜单 -->
    <person-menu-vue></person-menu-vue>
  </div>
</template>
<script  setup>
  import { reactive } from "vue";
  import { ElMessage } from 'element-plus';
  import PersonMenuVue from './PersonMenu.vue';
  import request from "@/utils/request.js";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('user_data') || '{}'),
  })


  const update = () => {
    let url
    if (data.user.role === 'admin') {
      url = '/admin/update'
    }
    if (data.user.role === 'user') {
      url = '/user/update'
    }
    request.put(url, data.user).then(res => {
      if (res.code === '200') {
        ElMessage.success('更新成功')
        localStorage.setItem("user_data", JSON.stringify(data.user))
      }
    })
  }
  const handleAvatarSuccess = async (res) => {
    update()
    data.user.avatar = res.data
  };

  const beforeAvatarUpload = (rawFile) => {
    if (rawFile.size / 1024 / 1024 > 5) {
      ElMessage.error('头像大小不能大于5M');
      return false;
    }
    return true;
  };
</script>
<style lang="scss" scoped>
  .left-person-card {
    width: 300px;
    min-height: 400px;
    border-radius: 5px;
    box-shadow: 0 5px 21px 0 rgb(78 78 78 / 25%);
    background-color: rgba(#fff, 0.5);
    z-index: 2;
    margin-top: 90px;
    .avatar-box {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 140px;
      .person-avatar-uploader {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        // overflow: hidden;
        position: relative;
        .camera-box {
          position: absolute;
          right: 4px;
          bottom: 4px;
        }
        :deep(.el-upload) {
          width: 70px;
          height: 70px;
          border: 1px solid #ccc !important;
          border-radius: 50%;
          .avatar {
            width: 70px;
            height: 70px;
            border-radius: 50%;
          }
        }
      }
    }
  }
</style>
