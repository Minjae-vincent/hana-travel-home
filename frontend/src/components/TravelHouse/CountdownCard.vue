<template>
  <div
    style="
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin: 0;
    "
  >
    <div class="notice">
      <span id="numOfBloggers" style="font-size: 70px; color: #03ac8e">{{
        numOfBloggers.toLocaleString()
      }}</span>
      <span>&nbsp;명의 트래블로거가 지금</span>
    </div>
    <div class="flip-wrapper">
      <div class="card-wrapper">
        <div :class="{ 'top-flip': isFlipping }" class="card_u">
          <h1 style="display: flex; align-items: center; justify-content: center">
            <v-avatar size="72" class="avatar" :image="`/assets/img/${currentFlag}.gif`"></v-avatar>
            &nbsp;
            {{ currentName }}
          </h1>
        </div>
        <div :class="{ 'bottom-flip': isFlipping }" class="card_d">
          <h1 style="display: flex; align-items: center; justify-content: center">
            <v-avatar size="72" class="avatar" :image="`/assets/img/${currentFlag}.gif`"></v-avatar>
            &nbsp;
            {{ currentName }}
          </h1>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, watch, onMounted, nextTick } from 'vue'

import { CountUp } from 'countup.js'

const props = defineProps({
  name: String,
  flag: String,
  numOfBlogger: Number
})

const numOfBloggers = ref(props.numOfBlogger)

const currentName = ref(props.name)
const nextName = ref('')

const currentFlag = ref(props.flag)

const isFlipping = ref(false)

watch(
  () => props.name,
  (newVal, oldVal) => {
    if (newVal !== oldVal) {
      nextName.value = newVal
      currentName.value = nextName.value
      isFlipping.value = true
      numOfBloggers.value = props.numOfBlogger
      currentFlag.value = props.flag
      animateNumOfTravelers()
      setTimeout(() => {
        nextName.value = ''
        isFlipping.value = false
      }, 500)
    }
  },
  { immediate: false }
)

const animateNumOfTravelers = async () => {
  await nextTick()

  const countUp = new CountUp('numOfBloggers', numOfBloggers.value)
  if (!countUp.error) {
    countUp.start()
  } else {
    console.error(countUp.error)
  }
}

onMounted(() => {
  animateNumOfTravelers()
})
</script>

<style scoped>
@import '/src/assets/styles/styles.css';
.flip-wrapper {
  display: flex;
  width: 100%;
  height: 100%;
}

.card-wrapper {
  width: auto;
  height: auto;
  height: auto;
  text-align: center;
  margin: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
}

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.notice {
  font-family: 'Hana2-Bold';
  font-size: 48px;
  /* font-weight: bold; */
  display: flex;
  align-items: center;
}
</style>
