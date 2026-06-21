<template>
  <works-display-com-vue :modelData="modelData">
    <!-- 标题 -->
    <template v-slot:model-title>
      <model-title-vue :title="modelData.title" :iconfont="modelData.iconfont"></model-title-vue>
    </template>
  </works-display-com-vue>
</template>
<script setup>
  import WorksDisplayComVue from '@/components/ModelComs/WorksDisplayCom.vue';
  import ModelTitleVue from './ModelTitle.vue';
  const props = defineProps({
    modelData: {
      type: Object
    }
  })
</script>
