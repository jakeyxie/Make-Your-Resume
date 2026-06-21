<template>
  <div class="account-setting-box">
    <ul>
      <li>
        <div class="left">
          <span class="label">邮箱: </span>
          <span class="email">{{ form.email }}</span>
        </div>
        <div class="right">
          <el-tag type="success" class="mx-1" effect="dark">
            已验证
          </el-tag>
        </div>
      </li>
      <li>
        <div class="left">
          <span class="label">密码: </span>
          <span class="email">********</span>
        </div>
        <div class="right">
          <el-button link @click="handleModify">修改密码</el-button>
        </div>
      </li>
    </ul>
  </div>

  <!-- 修改密码弹窗 -->
  <modify-pass-dialog-vue :dialog-visible="dialogVisible" @cancle="cancle"></modify-pass-dialog-vue>
</template>
<script setup>
  import ModifyPassDialogVue from './components/ModifyPassDialog.vue';

  import { useUserDataStore } from '@/store/user';
  const { userDataStore } = useUserDataStore();
  const store = useUserDataStore();
  const form = reactive(userDataStore);

  // 修改密码弹窗
  const dialogVisible = ref(false);
  const handleModify = () => {
    dialogVisible.value = true;
  };
  const cancle = () => {
    dialogVisible.value = false;
  };
</script>

<style lang="scss" scoped>
  .account-setting-box {
    ul {
      width: 90%;
      display: flex;
      flex-direction: column;
      padding-left: 10px;
      li {
        list-style: none;
        display: flex;
        justify-content: space-between;
        height: 70px;
        align-items: center;
        span {
          line-height: 70px;

        }
        .left {
          font-size: 16px;
          display: flex;
          .label {
            width: 50px;
          }
        }
        .right {
          .el-button {
            padding: 0;
          }
        }
      }
    }
  }
</style>
