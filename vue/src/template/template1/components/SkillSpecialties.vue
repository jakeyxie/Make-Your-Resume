<template>
  <skill-specialties-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </skill-specialties-com-vue>
</template>
<script  setup>
  import SkillSpecialtiesComVue from '@/components/ModelComs/SkillSpecialtiesCom.vue';
  import ModelTitleVue from './ModelTitle.vue';
  const props = defineProps({
    modelData: {
      type: Object
    },
  })
</script>
