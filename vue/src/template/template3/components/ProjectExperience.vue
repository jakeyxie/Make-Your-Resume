<template>
  <project-experience-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </project-experience-com-vue>
</template>
<script setup>
  import ProjectExperienceComVue from '@/components/ModelComs/ProjectExperienceCom.vue';
  import ModelTitleVue from './ModelTitle.vue';
  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
