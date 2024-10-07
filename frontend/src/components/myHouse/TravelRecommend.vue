<template>
  <div class="headline" ref="targetSection" data-aos="fade-up" data-aos-once="true">
    지금 나는, 하우스!<br />
    트래블홈이 추천드리는 곳으로 여행 어떠신가요?
  </div>
  <div
    class="d-flex align-items-center justify-content-center flex-wrap p-3"
    data-aos="fade-up"
    data-aos-once="true"
  >
    <b-form-select
      v-model="selectedMonth"
      :options="months"
      class="custom-select mx-2"
    ></b-form-select>

    <span class="static-text mx-2">지금!</span>

    <b-form-select
      v-model="selectedAgeGroup"
      :options="ageGroups"
      class="custom-select mx-2"
    ></b-form-select>

    <b-form-select
      v-model="selectedGender"
      :options="genders"
      class="custom-select mx-2"
    ></b-form-select>

    <span class="static-text mx-2">들은 여기를 가고 있어요!</span>
    <br />

    <!-- Wrapping the button in a div to force a new line -->
    <div class="button-container">
      <v-btn @click="handleClick" class="animated-button" elevation="2">
        <span class="button-text">확인하기</span>
      </v-btn>
    </div>
  </div>

  <div
    v-if="isContentVisible"
    class="custom-centered-container"
    data-aos="fade-up"
    data-aos-once="true"
  >
    <span
      class="static-text mx-2"
      style="
        font-size: 50px;
        color: #03ac8e;
        font-family: 'Hana2-Medium';
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
      "
    >
      <span id="numOfTravelers">{{ numOfTravelers }}</span>
    </span>
    <span class="static-text mx-2">명의</span>
    <br />
    <span
      class="static-text mx-2"
      style="
        font-size: 36px;
        margin: 0%;
        color: #03ac8e;
        font-family: 'Hana2-Medium';
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
      "
    >
      {{ selectedAgeGroup }}대 {{ displayedGender }}
    </span>
    <span class="static-text mx-2">트래블로거들이</span>
    <span
      class="static-text"
      style="
        font-size: 36px;
        color: #03ac8e;
        font-family: 'Hana2-Medium';
        margin: 0%;
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5);
      "
    >
      {{ selectedMonth }}월
    </span>
    <span class="static-text">에 여행을 떠났어요! </span>
    <br />
    <br />
    <span class="static-text mx-2" style="font-size: 24px">카드를 눌러 순위를 확인해보세요!</span>
    <!-- sample start -->
    <v-container class="pa-4 text-center">
      <v-row align="center" class="fill-height" justify="center">
        <template v-for="(item, i) in items" :key="i">
          <v-col cols="12" md="4">
            <v-hover v-slot="{ isHovering, props }">
              <v-card
                :class="{ 'on-hover': isHovering }"
                class="custom-card"
                :elevation="isHovering ? 11 : 1"
                v-bind="props"
                @click="handleCardClick(i)"
              >
                <v-img :src="item.img" height="225px" cover>
                  <v-card-title class="text-h6 text-white d-flex flex-column"> </v-card-title>
                </v-img>
              </v-card>
            </v-hover>
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
  <v-dialog v-model="isModalVisible" max-width="600" persistent>
    <v-card>
      <v-card-title>
        <span class="modal-title">추천 여행지</span>
      </v-card-title>
      <v-card-text>
        <div v-if="selectedDestination">
          <div class="modal-component">
            <v-avatar
              size="130"
              class="avatar"
              :image="`/assets/img/${selectedDestination.code}.gif`"
            ></v-avatar>
            <div class="modal-content">
              <div>
                트래블홈 이용자
                <span class="hilight-info">{{ selectedAgeGroup }}대 {{ displayedGender }}</span> 들
                중
              </div>
              <div>
                <span class="hilight-info"
                  >{{ Math.round((selectedDestination.totalNum / numOfTravelers) * 100) }}%</span
                >가
              </div>
              <div>
                <span class="hilight-info"
                  >{{ selectedMonth }}월 {{ selectedDestination.codeName }}</span
                >
                을 여행해요!
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="modal-component">
            <img class="no-data-img" src="/assets/img/nodata.png" />
            <div class="no-data-info">데이터가 없습니다.</div>
          </div>
        </div>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="isModalVisible = false">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, onMounted, nextTick, computed } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { CountUp } from 'countup.js'
import AOS from 'aos'
import axios from 'axios'

const authStore = useAuthStore()
const userId = authStore.userId

const isContentVisible = ref(false)
const targetSection = ref(null)

const selectedMonth = ref('6')
const selectedAgeGroup = ref('20')
const selectedGender = ref('GENDER_M')
const months = ref([
  { value: '1', text: '01월' },
  { value: '2', text: '02월' },
  { value: '3', text: '03월' },
  { value: '4', text: '04월' },
  { value: '5', text: '05월' },
  { value: '6', text: '06월' },
  { value: '7', text: '07월' },
  { value: '8', text: '08월' },
  { value: '9', text: '09월' },
  { value: '10', text: '10월' },
  { value: '11', text: '11월' },
  { value: '12', text: '12월' }
])

const ageGroups = ref([
  { value: '10', text: '10대' },
  { value: '20', text: '20대' },
  { value: '30', text: '30대' },
  { value: '40', text: '40대' },
  { value: '50', text: '50대' },
  { value: '60', text: '60대' },
  { value: '70', text: '70대' }
])

const numOfTravelers = ref(0)
const genders = ref([
  { value: 'GENDER_M', text: '남성' },
  { value: 'GENDER_F', text: '여성' }
])

const popularDestinations = ref([])

const isModalVisible = ref(false)
const selectedDestination = ref(null)

const handleCardClick = (index) => {
  selectedDestination.value = popularDestinations.value[index]
  isModalVisible.value = true
}

const displayedGender = computed(() => {
  return selectedGender.value === 'GENDER_M' ? '남성' : '여성'
})

const handleClick = async () => {
  try {
    await axios
      .get('http://34.47.69.197:8081/api/user/travel-recommendation', {
        params: {
          month: selectedMonth.value,
          ageRange: selectedAgeGroup.value,
          gender: selectedGender.value,
          userId: userId
        }
      })
      .then((response) => {
        numOfTravelers.value = response.data.data.totalNum
        popularDestinations.value = response.data.data.travelLocationList
      })
  } catch (error) {
    return
  }

  isContentVisible.value = true
  await nextTick()
  targetSection.value.scrollIntoView({ behavior: 'smooth' })
  animateNumOfTravelers()
}

const animateNumOfTravelers = async () => {
  await nextTick()

  const countUp = new CountUp('numOfTravelers', numOfTravelers.value)
  if (!countUp.error) {
    countUp.start()
  } else {
    console.error(countUp.error)
  }
}

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })
  numOfTravelers.value = 0
})

const items = ref([
  {
    img: '/assets/img/gold.jpg'
  },
  {
    img: '/assets/img/silver.jpg'
  },
  {
    img: '/assets/img/bronze.jpg'
  }
])
</script>

<style scoped>
@font-face {
  font-family: 'Hana2-Bold';
  src: url('@/assets/fonts/Hana2-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Spoqa-Han-Sans-Bold';
  src: url('@/assets/fonts/Spoqa-Han-Sans-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Spoqa-Han-Sans-Regular';
  src: url('@/assets/fonts/Spoqa-Han-Sans-Regular.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Light';
  src: url('@/assets/fonts/Hana2-Light.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Medium';
  src: url('@/assets/fonts/Hana2-Medium.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'Pretendard-Regular';
  src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')
    format('woff');
  font-weight: 400;
  font-style: normal;
}

.image-container {
  text-align: center;
  margin-bottom: 20px;
}

.illustration-image {
  max-width: 100%;
  height: auto;
}

.headline {
  text-align: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
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

.button-group {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.v-btn {
  margin: 0 10px;
  border: 1px solid #03ac8e;
  color: #03ac8e;
  background-color: transparent;
  border-radius: 20px;
  font-weight: bold;
}

.v-btn.active-button {
  background-color: #03ac8e;
  color: white;
}

.button-group .v-btn {
  margin: 0 20px;
  border: 3px solid #03ac8e;
  background-color: transparent;
  border-radius: 10px;
  font-family: 'Spoqa-Han-Sans-Bold';
  font-size: 16px;
}

.button-group .v-btn.active-button {
  background-color: white;
  color: #03ac8e;
}

.button-group .v-btn:not(.active-button) {
  color: #b9b6b6;
  border-color: #b9b6b6;
}

.v-tabs .v-tab {
  border: none;
}
.custom-select {
  border-radius: 5px;
  padding: 5px 30px 5px 10px;
  font-size: 18px;
  font-family: 'Spoqa-Han-Sans-light';
  width: auto;
  appearance: none;
  text-align: center;
}

.custom-select::after {
  display: inline-block;
  margin-left: 10px;
  vertical-align: middle;
  content: '';
  border-top: 0.3em solid;
  border-right: 0.3em solid transparent;
  border-bottom: 0;
  border-left: 0.3em solid transparent;
}

.static-text {
  font-size: 32px;
  font-family: 'Hana2-Light', sans-serif;
}

.custom-centered-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.button-container {
  width: 100%;
  text-align: center;
  margin-top: 30px;
}

.animated-button {
  font-size: 20px;
  padding: 15px 30px;
  border-radius: 30px;
  width: auto;
  height: auto;
}

.animated-button:hover {
  background-color: #03ac8e;
  color: white;
}

.custom-card {
  transition: opacity 0.4s ease-in-out;
  cursor: pointer;
}

.custom-card:not(.on-hover) {
  opacity: 0.6;
}

.show-btns {
  color: rgba(255, 255, 255, 1) !important;
}

.modal-title {
  font-family: 'Hana2-Bold', sans-serif;
  font-size: 24px;
  color: #333;
}

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.modal-component {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.modal-content {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  font-family: 'Pretendard-Regular', sans-serif;
  font-size: 24px;
}

.hilight-info {
  color: #03ac8e;
  font-size: 30px;
  color: #03ac8e;
  font-family: 'Hana2-M', sans-serif;
}

.no-data-img {
  height: 200px;
}

.no-data-info {
  font-family: 'Hana2-Light', sans-serif;
  font-size: 24px;
  color: #333;
}
</style>
