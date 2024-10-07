<template>
  <div class="accordion">
    <!-- Accordion Title with GSAP animation on hover -->
    <div class="accordion-title" @click="toggleAccordion" ref="accordionTitle">
      <slot name="title"></slot>
    </div>

    <!-- Accordion Content, toggled by isVisible -->
    <div class="accordion-content" v-show="isVisible">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const isVisible = ref(false)
const accordionTitle = ref(null)

const toggleAccordion = () => {
  isVisible.value = !isVisible.value
}
</script>

<style scoped>
.accordion-title {
  cursor: pointer;
  font-weight: bold;
  margin-bottom: 10px;
  transition: all 0.3s ease;
  width: 100%;
  text-decoration: underline;
  text-decoration-thickness: 0.8px;
}
.accordion-content {
  overflow: hidden;
  transition: max-height 0.5s ease;
}

.accordion-title:hover {
  color: white;
  background-color: #cce0ac48;
  border-radius: 5px;
}
</style>
