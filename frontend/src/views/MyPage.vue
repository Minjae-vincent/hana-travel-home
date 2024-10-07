<template>
  <div class="top-intro">
    <div class="intro-serve">내 계좌, 외화, 소비 통계까지 한눈에 확인!</div>
    <div class="intro-main">더 똑똑하게 관리하고, 간편한 금융 생활을 챙기세요.</div>

    <img src="/assets/img/finance.png" class="recommend-img" />
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
      <v-tab :value="0">내 계좌</v-tab>
      <v-tab :value="1">소비 통계</v-tab>
    </v-tabs>
    <v-window
      v-model="selectedTab"
      class="tab-content"
      :reverse-transition="reverseTransition"
      :transition="transition"
    >
      <v-window-item :value="0">
        <div class="account-wrapper" data-aos="fade-up">
          <Accout />
        </div>
        <div v-if="!isLoding">
          <div class="foreign-currency-wrapper" data-aos="fade-up">
            <h1 style="font-family: 'Hana2-B'">보유외화</h1>
            <div class="foreign-currency-grid">
              <div
                v-for="(place, index) in possessionCurrency"
                :key="index"
                class="foreign-currency"
                data-aos="fade-up"
              >
                <div class="possession-container">
                  <div class="currency-content">
                    <div class="country-wrapper">
                      <div class="country-flag">
                        <v-avatar
                          size="60"
                          class="avatar"
                          :image="`/assets/img/${place.code}.gif`"
                        ></v-avatar>
                      </div>
                      <div class="country-info">
                        <div>{{ place.countryName }} {{ place.code }}</div>
                        <div style="font-family: 'Hana2-B'">
                          {{ place.balance.toLocaleString() }}
                          {{ currencySymbols[place.code] }}
                        </div>
                      </div>
                    </div>
                    <div class="view-detail" @click="openModal(place)">
                      자세히보기
                      <v-icon class="mdi-logo">mdi-arrow-right</v-icon>
                    </div>
                  </div>

                  <div class="target-content">
                    <div style="width: 100%">
                      <template v-if="!place.needGoal">
                        <v-progress-linear
                          :model-value="place.progress"
                          height="30"
                          color="#03AC8E"
                          striped
                          style="border-radius: 20px; border: 1px solid black"
                        >
                          <strong>{{ place.progress }}%</strong>
                        </v-progress-linear>
                      </template>
                      <template v-else>
                        <div class="target-not-configured">목표 금액을 설정해주세요!</div>
                      </template>
                    </div>
                    <div style="font-size: 15px; font-family: 'Hana2-Light'">
                      외화 목표 금액:
                      <span style="font-family: 'Hana2-B'; font-size: 16px">
                        {{ place.targetAmount.toLocaleString() }}
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <v-skeleton-loader height="350" />
        </div>
      </v-window-item>
      <v-window-item :value="1">
        <ConsumptionReport />
      </v-window-item>
    </v-window>
  </div>
  <DetailModal :details="selectedCurrency" :isDialogVisible="isDialogVisible" @close="closeModal" />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import Accout from '@/components/myPage/Accout.vue'
import DetailModal from '@/components/myPage/DetailModal.vue'
import ConsumptionReport from '/src/views/ConsumptionReport.vue'
import gsap from 'gsap'

import AOS from 'aos'

import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

// State to track selected currency for modal
const selectedCurrency = ref(null)
const isDialogVisible = ref(false)
const selectedTab = ref(0)
const previousTab = ref(0)
const transition = ref('slide-x-transition')
const reverseTransition = ref('slide-x-reverse-transition')

const authStore = useAuthStore()
const userId = authStore.userId
const isLoding = ref(false)

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

const openModal = (currency) => {
  selectedCurrency.value = currency
  isDialogVisible.value = true
}

const closeModal = () => {
  isDialogVisible.value = false
}
// 통화 기호 매핑 객체
const currencySymbols = {
  USD: '$', // 미국 달러
  JPY: '¥', // 일본 엔화
  EUR: '€', // 유로
  GBP: '£', // 영국 파운드
  CNY: '¥', // 중국 위안화
  SGD: 'S$', // 싱가포르 달러
  CAD: 'C$', // 캐나다 달러
  AUD: 'A$', // 호주 달러
  HKD: 'HK$', // 홍콩 달러
  THB: '฿', // 태국 바트
  CHF: 'CHF', // 스위스 프랑
  SEK: 'kr', // 스웨덴 크로나
  IDR: 'Rp', // 인도네시아 루피아
  NZD: 'NZ$', // 뉴질랜드 달러
  NOK: 'kr', // 노르웨이 크로네
  DKK: 'kr', // 덴마크 크로네
  MYR: 'RM', // 말레이시아 링깃
  AED: 'د.إ', // 아랍에미리트 디르함
  SAR: '﷼', // 사우디아라비아 리얄
  KWD: 'KD', // 쿠웨이트 디나르
  BHD: 'BD', // 바레인 디나르
  BND: 'B$' // 브루나이 달러
}

const possessionCurrency = ref([])

// possessionCurrency.value.forEach((currency) => {
//   if (currency.targetAmount === 0 || currency.targetAmount === null) {
//     currency.progress = null // 또는 0으로 설정할 수 있습니다.
//     currency.needGoal = true // 목표 설정이 필요함을 표시하는 플래그
//   } else {
//     currency.progress = Math.round((currency.balance / currency.targetAmount) * 100)
//     currency.needGoal = false // 목표 설정이 필요하지 않음
//   }
// })

const getPoeessionCurrency = async () => {
  isLoding.value = true
  try {
    await axios.get(`http://34.47.69.197:8081/api/user/manage/possession/${userId}`).then((res) => {
      possessionCurrency.value = res.data.data
      possessionCurrency.value.forEach((currency) => {
        if (currency.targetAmount === 0 || currency.targetAmount === null) {
          currency.progress = null // 또는 0으로 설정할 수 있습니다.
          currency.needGoal = true // 목표 설정이 필요함을 표시하는 플래그
        } else {
          currency.progress = Math.round((currency.balance / currency.targetAmount) * 100)
          currency.needGoal = false // 목표 설정이 필요하지 않음
        }
      })
      isLoding.value = false
    })
  } catch (error) {
    console.error(error)
  }
}
onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200,
    once: true
  })

  getPoeessionCurrency()

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
  margin: 0;
}

.account-wrapper {
  padding-top: 80px;
}

.foreign-currency-wrapper {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.foreign-currency-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* Two columns */
  gap: 20px; /* Adjust the gap between items */
  justify-items: center;
}

.foreign-currency {
  width: 500px;
  height: 200px;
  padding: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px;
  background-color: white;
  border: 2px solid #b5b5b5;
  border-radius: 30px;
  transition:
    background-color 0.3s ease,
    transform 0.3s ease;
}

.foreign-currency:hover {
  background-color: #f3fff0;
}

.possession-container {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
  flex-direction: column;
}
.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}
.country-wrapper {
  display: flex;
  align-items: center;
  gap: 20px;
}
.country-info {
  font-size: 20px;
  font-family: 'Hana2-Light';
}
.currency-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  /* padding-top: 20px; */
}
.target-content {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-direction: column;
  padding-top: 20px;
  align-items: flex-end;
}

.view-detail {
  font-size: 24px;
  font-family: Hana2-B;
  /* padding-top: 40px; */
  padding-right: 20px;
  color: #766666;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}

.foreign-currency:hover .view-detail {
  cursor: pointer;
  transform: translateX(10px);
  color: #03ac8e;
  /* background-color: #f3fff0; */
}

.foreign-currency:hover .mdi-logo {
  color: #03ac8e;
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
  margin: 20px;
  margin-top: 0;
}
.tabs-container {
  justify-content: center;
  display: flex;
  width: 100%;
}

.tab-content {
  margin-top: 30px;
  width: 100%;
  max-width: 1200px;
  text-align: left;
}

.v-window-item {
  transition: transform 0.6s ease-in-out !important;
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

.target-not-configured {
  font-size: 20px;
  font-family: 'Hana2-Light';
  color: #766666;
}
</style>
