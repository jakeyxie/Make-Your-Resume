<template>
  <div class="preview-box">
    <slot></slot>
    <!-- 关闭按钮 -->
    <div class="close" @click.stop="close">
      <el-icon color="#fff"><CloseBold /></el-icon>
    </div>
  </div>
</template>

<script setup>
const emit = defineEmits(['close']);
const close = () => {
  emit('close');
};
</script>
<style lang="scss" scoped>
.preview-box {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 90;
  transition: all 0.3s;
  .close {
    position: fixed;
    width: 30px;
    height: 30px;
    background-color: #000;
    top: 12%;
    left: 62%;
    transform: translate(-50%, -50%);
    border-radius: 50%;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
  }
}
</style>
