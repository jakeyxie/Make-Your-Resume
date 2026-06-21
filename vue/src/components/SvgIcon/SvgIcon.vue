<template>
  <svg :class="svgClass" :style="{ fontSize: size }" aria-hidden="true" >
    <use v-bind:xlink:href="iconClassName" :style="{ fill: color + ' !important' }" />
  </svg>
</template>

<script setup>
import { computed } from 'vue';
const props = defineProps({
  iconName: {
    type: String,
    required: true
  },
  className: {
    type: String,
    default: ''
  },
  color: {
    type: String,
    default: '#409eff'
  },
  size: {
    type: String,
    default: '14px'
  }
});
const iconClassName = computed(()=>{
  return `#${props.iconName}`;
})
const svgClass = computed(() => {
  if (props.className) {
    return `svg-icon ${props.className}`;
  }
  return 'svg-icon';
});
</script>

<style scoped lang="scss">
.svg-icon {
  width: 1em;
  height: 1em;
  position: relative;
  fill: currentColor;
  vertical-align: -2px;
}
</style>