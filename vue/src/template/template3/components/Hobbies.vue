<template>
  <hobbies-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </hobbies-com-vue>
</template>
<script setup>
  import HobbiesComVue from '@/components/ModelComs/HobbiesCom.vue';
  import ModelTitleVue from './ModelTitle.vue';

  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
