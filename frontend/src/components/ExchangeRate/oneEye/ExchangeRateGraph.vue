<template>
  <Podium data-aos="fade-up" />

  <div class="divider"></div>

  <Graph data-aos="fade-up" />
  <!-- <Scales /> -->
</template>

<script setup>
import Podium from '/src/components/ExchangeRate/oneEye/Podium.vue'
import Graph from '/src/components/ExchangeRate/oneEye/Graph.vue'
// import Scales from '/src/components/ExchangeRate/oneEye/Scales.vue'
</script>

<style scoped>
.divider {
  background-color: #e0e0e0;
  margin: 80px;
}
</style>
