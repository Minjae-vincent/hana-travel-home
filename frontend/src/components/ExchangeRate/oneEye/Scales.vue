<template>
  <v-container class="seesaw-container">
    <div ref="seesaw" class="seesaw">
      <div ref="leftItem" class="seesaw-item left-item" @click="selectItem('left')"></div>
      <div ref="seesawBoard" class="seesaw-board"></div>
      <div ref="rightItem" class="seesaw-item right-item" @click="selectItem('right')"></div>
    </div>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import gsap from 'gsap'

// Refs for the seesaw and its components
const seesaw = ref(null)
const leftItem = ref(null)
const rightItem = ref(null)
const seesawBoard = ref(null)

// Function to handle item selection and animate the seesaw
const selectItem = (side) => {
  if (side === 'left') {
    animateSeesaw(leftItem.value, 'left')
  } else {
    animateSeesaw(rightItem.value, 'right')
  }
}

// Function to animate the seesaw based on the selected item
const animateSeesaw = (item, side) => {
  const weight = side === 'left' ? 10 : 5 // Example weights
  const tiltAngle = (weight / 15) * 30 // Adjust tilt based on weight

  const tl = gsap.timeline()
  if (side === 'left') {
    tl.to(item, { y: -100, duration: 1 }).to(
      seesawBoard.value,
      { rotation: -tiltAngle, transformOrigin: '50% 50%', duration: 1 },
      '<'
    )
  } else {
    tl.to(item, { y: -100, duration: 1 }).to(
      seesawBoard.value,
      { rotation: tiltAngle, transformOrigin: '50% 50%', duration: 1 },
      '<'
    )
  }
}
</script>

<style scoped>
.seesaw-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.seesaw {
  position: relative;
  width: 400px;
  height: 200px;
}

.seesaw-board {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 10px;
  background-color: #795548;
  transform-origin: center;
  transform: translate(-50%, -50%);
}

.seesaw-item {
  position: absolute;
  width: 50px;
  height: 50px;
  background-color: #3f51b5;
}

.left-item {
  left: 0;
  bottom: 10px;
}

.right-item {
  right: 0;
  bottom: 10px;
}
</style>
