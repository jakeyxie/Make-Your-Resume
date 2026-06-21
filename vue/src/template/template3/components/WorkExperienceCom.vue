<template>
  <work-experience-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </work-experience-com-vue>
</template>
<script setup>
  import WorkExperienceComVue from '@/components/ModelComs/WorkExperienceCom.vue';
  import ModelTitleVue from './ModelTitle.vue';
  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
