<template>
  <campus-experience-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </campus-experience-com-vue>
</template>
<script setup>
  import CampusExperienceComVue from '@/components/ModelComs/CampusExperienceCom.vue';
  import ModelTitleVue from './ModelTitle.vue';

  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
