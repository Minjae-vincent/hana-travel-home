<template>
  <div class="top-intro">
    <div class="intro-serve">다음 목적지를 찾아보세요!</div>
    <div class="intro-main">지금 떠나기 딱 좋은 여행지 추천</div>

    <img src="/assets/img/recommend_main.png" class="recommend-img" />
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
      <div v-if="isTraveling">
        <v-tab :value="0">행선지 추천</v-tab>
      </div>
      <div v-else>
        <v-tab :value="0">여행지 추천</v-tab>
      </div>

      <div v-if="isAuthenticated">
        <v-tab :value="1">내 여행기 보기</v-tab>
        <!-- <v-tab :value="2">TRAVEL LAB</v-tab> -->
      </div>
      <div v-else>
        <!-- <v-tab :value="2">TRAVEL LAB</v-tab> -->
      </div>
    </v-tabs>

    <v-window
      v-model="selectedTab"
      class="tab-content"
      :reverse-transition="reverseTransition"
      :transition="transition"
    >
      <v-window-item :value="0">
        <div v-if="isTraveling">
          <TravelNextDest />
        </div>
        <div v-else>
          <TravelRecommendationSection />
        </div>
      </v-window-item>

      <v-window-item :value="1">
        <TravelJournal />
      </v-window-item>

      <v-window-item :value="2">
        <TravelLabSection />
      </v-window-item>
    </v-window>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import gsap from 'gsap'
import TravelRecommendationSection from '/src/components/myHouse/TravelRecommend.vue'
import TravelLabSection from '/src/components/myHouse/TravelLab.vue'
import TravelJournal from '/src/components/myHouse/TravelJournal.vue'
import TravelNextDest from '@/components/myHouse/TravelNextDest.vue'

const selectedTab = ref(0)
const previousTab = ref(0)
const transition = ref('slide-x-transition')
const reverseTransition = ref('slide-x-reverse-transition')

const authStore = useAuthStore()
const isAuthenticated = authStore.isAuthenticated
const isTraveling = authStore.isTraveling

// const dialog = ref(false)

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
@font-face {
  font-family: 'Pretendard-Regular';
  src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')
    format('woff');
  font-weight: 400;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Bold';
  src: url('@/assets/fonts/Hana2-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}
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
