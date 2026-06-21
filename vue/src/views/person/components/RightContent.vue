<template>
  <div class="right-content-box">
    <!-- 菜单标题 -->
    <content-title-vue
      v-if="route.meta.showTitle"
      :title="(route.meta.title)"
    ></content-title-vue>
    <div class="router-box">
      <router-view ></router-view>
    </div>
  </div>
</template>
<script setup>
  import ContentTitleVue from './ContentTitle.vue';
  import { useRoute } from 'vue-router';

  const route = useRoute();
</script>
<style lang="scss" scoped>
  .right-content-box {
    flex: 1;
    min-height: 300px;
    margin: 270px 0 0px 30px;
    box-sizing: border-box;
    .router-box {
      padding: 0 0 20px 0;
    }
  }
</style>
