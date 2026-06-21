<template>
  <edu-background-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </edu-background-com-vue>
</template>
<script setup>
  import EduBackgroundComVue from '@/components/ModelComs/EduBackgroundCom.vue';
  import ModelTitleVue from './ModelTitle.vue';

  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
