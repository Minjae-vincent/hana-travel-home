<template>
  <div class="top-intro">
    <div class="intro-serve">어디로 떠날까 고민된다면?</div>
    <div class="intro-main">트래블홈 모아보기</div>

    <img src="/assets/img/airplain.png" class="recommend-img" />
  </div>
  <div class="container">
    <!-- 실시간 여행자  -->
    <div v-if="!isLoading">
      <div class="realtime-wrapper" data-aos="fade-up" data-aos-once="true">
        <div class="card-section">
          <div class="card-container">
            <CountdownCard
              v-if="currentCountry"
              :name="currentCountry.codeName"
              :flag="currentCountry.code"
              :numOfBlogger="currentCountry.numOfBloggers"
            />
          </div>
          <div class="notice">를 여행 중 &nbsp;</div>
        </div>
        <div v-if="!isLoading">
          <RealTimePodium :topThreeCountries="topThreeCountries" />
        </div>
        <div v-else>
          <v-skeleton-loader height="350" />
        </div>
      </div>
    </div>
    <div v-else>
      <v-skeleton-loader height="350" />
    </div>

    <!-- 여행지 추천 -->
    <div class="recommend-wrapper" data-aos="fade-up" data-aos-once="true">
      <div v-if="authStore.isAuthenticated">
        <Recommend />
      </div>
      <div v-else>
        <NotLogIn />
      </div>
    </div>

    <!-- 실시간 환율 추천 -->
    <div class="exchange-rate-wrapper" data-aos="fade-up" data-aos-once="true">
      <ExchangeRecommend />
    </div>

    <!-- 여행자 보험 추천 -->
    <div class="insurance-recommend" data-aos="fade-up" data-aos-once="true">
      <div v-if="authStore.isAuthenticated">
        <Insurance />
      </div>
      <div v-else>
        <NotLogIn />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, reactive, computed } from 'vue'
import RealTimePodium from '/src/components/TravelHouse/RealTimePodium.vue'
import CountdownCard from '/src/components/TravelHouse/CountdownCard.vue'
import AOS from 'aos'
import NotLogIn from '@/components/TravelHouse/NotLogIn.vue'
import Recommend from '@/components/TravelHouse/Recommend.vue'
import gsap from 'gsap'

import { useAuthStore } from '@/stores/auth'
import ExchangeRecommend from '@/components/TravelHouse/ExchangeRecommend.vue'
import Insurance from '@/components/TravelHouse/Insurance.vue'
import axios from 'axios'

const isLoading = ref(true)
const authStore = useAuthStore()

const countries = reactive([])

const topThreeCountries = computed(() => countries.slice(0, 3))

const currentCountry = ref(null)
let index = 0

const getRealTimeCountries = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('http://34.47.69.197:8081/api/user/travel/real-time')
    countries.push(...response.data.data)
    currentCountry.value = countries[0]
    isLoading.value = false

    // 데이터 로드 후 interval 설정
    interval = setInterval(updateCountry, 5000)
  } catch (error) {
    console.error(error)
    isLoading.value = false
  }
}

const updateCountry = () => {
  if (countries.length === 0) return
  index = (index + 1) % countries.length
  currentCountry.value = countries[index]
}

let interval

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })

  gsap.to('.recommend-img', {
    y: -20,
    duration: 1.5,
    repeat: -1,
    yoyo: true,
    ease: 'power1.inOut'
  })

  getRealTimeCountries()
})
onUnmounted(() => {
  clearInterval(interval)
})
</script>

<style scoped>
.container {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0;
}

.card-container {
  width: 752px;
  height: 280px;
}

.notice {
  font-family: 'Hana2-Bold';
  font-size: 48px;
  display: flex;
  align-items: center;
  width: auto;
  margin-top: 80px;
  margin-left: -120px;
}

.card-section {
  display: flex;
  width: auto;
}

.recommend-wrapper {
  padding-top: 80px;
}

.exchange-rate-wrapper {
  padding-top: 80px;
  width: auto;
}

.insurance-recommend {
  padding-top: 80px;
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

.recommend-img {
  height: 350px;
  width: 650px;
  margin: 20px;
  margin-top: 0;
}
</style>
