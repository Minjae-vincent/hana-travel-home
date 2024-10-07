<!-- ExchangeRate.vue -->

<template>
  <div class="top-intro">
    <div class="intro-serve">간편하게, 스마트하게!</div>
    <div class="intro-main">최적의 환율 타이밍으로 여행지 추천</div>

    <img src="/assets/img/exchange.png" class="recommend-img" />
  </div>
  <div class="container">
    <v-tabs
      v-model="selectedTab"
      background-color="transparent"
      color="#03ac8e"
      align-tabs="center"
      centered
      class="tabs-container"
    >
      <v-tab :value="0">한 눈에 보기</v-tab>
      <v-tab :value="1">자세히 보기</v-tab>
    </v-tabs>

    <v-window
      v-model="selectedTab"
      class="tab-content"
      :reverse-transition="reverseTransition"
      :transition="transition"
    >
      <v-window-item :value="0">
        <ExchangeRateGraph />
      </v-window-item>

      <v-window-item :value="1">
        <ExchangeDetail />
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup>
import ExchangeDetail from '@/components/ExchangeRate/detail/ExchangeDetail.vue'
import { ref, watch, onMounted } from 'vue'
import ExchangeRateGraph from '@/components/ExchangeRate/oneEye/ExchangeRateGraph.vue'
import gsap from 'gsap'

const selectedTab = ref(0)
const previousTab = ref(0)
const transition = ref('slide-x-transition')
const reverseTransition = ref('slide-x-reverse-transition')

watch(selectedTab, (newVal) => {
  if (newVal > previousTab.value) {
    transition.value = 'slide-x-transition'
    reverseTransition.value = 'slide-x-reverse-transition'
  } else {
    transition.value = 'slide-x-reverse-transition'
    reverseTransition.value = 'slide-x-transition'
  }
  previousTab.value = newVal
})

onMounted(() => {
  // GSAP floating animation
  gsap.to('.recommend-img', {
    y: -20,
    duration: 1.5,
    repeat: -1,
    yoyo: true,
    ease: 'power1.inOut'
  })
})
</script>

<style scoped>
.container {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.image-container {
  text-align: center;
  margin-bottom: 20px;
}

.v-tabs {
  width: auto;
  margin: 0 auto;
  display: inline-flex;
  border-bottom: 1px solid #ddd;
}

.v-tab {
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 24px;
  padding: 10px 40px;
  text-transform: none;
  cursor: pointer;
  margin: 0 50px;
}

.v-tab--active {
  border-bottom: 2px solid #03ac8e;
  color: #03ac8e;
}

.my-travel-section {
  padding: 20px;
  text-align: center;
}

.top-intro {
  width: 100%;
  height: 600px;
  background-color: #cce0ac48;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  font-family: 'Hana2-Bold';
}

.recommend-img {
  height: 350px;
  margin: 20px;
  margin-top: 0;
}

.intro-serve {
  font-size: 24px;
  font-weight: 700;
  color: #4e5968;
}

.intro-main {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 20px;
}
</style>
