<template>
  <div class="container">
    <div class="slider-login-container">
      <!-- Carousel Section -->
      <v-carousel
        height="400"
        hide-delimiter-background
        cycle
        :interval="3000"
        show-arrows
        class="slider-section"
      >
        <template v-slot:prev="{ props }">
          <v-btn variant="plain" @click="props.onClick" class="arrow-button">
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
        </template>
        <template v-slot:next="{ props }">
          <v-btn variant="plain" @click="props.onClick" class="arrow-button">
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </template>
        <v-carousel-item v-for="(slide, i) in slides" :key="i">
          <v-img
            :src="slide.image"
            class="slider-image"
            height="100%"
            width="100%"
            :alt="slide.alt"
            cover
          ></v-img>
        </v-carousel-item>
      </v-carousel>

      <!-- Login Form Section -->
      <div v-if="!authStore.isAuthenticated" class="login-section">
        <h1 class="title">로그인</h1>
        <form class="login-form" @submit.prevent="handleLogin">
          <div class="input-group">
            <input type="text" placeholder="아이디" v-model="username" />
          </div>
          <div class="input-group">
            <input type="password" placeholder="비밀번호" v-model="password" />
          </div>
          <div class="divider"></div>
          <button type="submit" class="login-button">로그인</button>
          <button type="button" class="kakao-login-button" @click="signInWithKakao">
            <img src="@/assets/img/kakao_icon.png" alt="Kakao Icon" class="kakao-icon" />
            카카오로 로그인하기
          </button>
        </form>
      </div>
      <div v-else class="welcome-section">
        <div style="display: flex; align-items: center; gap: 10px">
          <img :src="authStore.imgURL" alt="Profile Image" class="profile-image" />
          <h1 class="welcome-message">
            <span style="font-family: 'Hana2-B'; color: black">{{ authStore.userName }}님,</span>
            환영합니다!
          </h1>
        </div>
        <div class="welcome-message-container">
          <div class="welcome-actions">
            <div class="welcome-action-item" @click="moveToMyPage()">
              <span>보유 외화</span>
              <span class="hilight-info">{{ numOfForeignCurrency }} 개</span>
            </div>
            <div class="welcome-action-item" @click="moveToMyhouse()">
              <span>내 여행기</span>
              <span class="hilight-info">{{ numOfHistory }} 개</span>
            </div>
            <div class="welcome-action-item" @click="moveToMyPage()">
              <span>환율 알림</span>
              <span class="hilight-info">{{ numOfNotifications }} 개</span>
            </div>
            <div class="welcome-action-item" @click="moveToMyPage()">
              <span>자동 구매/환급</span>
              <span class="hilight-info">{{ numOfAutoPurchaseRefund }} 개</span>
            </div>
          </div>
        </div>

        <div class="logout-button" @click.prevent="handleLogoutClick">로그아웃</div>

        <!-- <button type="submit" class="logout-button" @click.prevent="handleLogoutClick">
          로그아웃
        </button> -->
      </div>
    </div>

    <div class="service-container">
      <!-- 마이하우스 -->
      <div class="service-info-1">
        <img src="/assets/img/main-1.png" class="main-gif" data-aos="fade-right" />
        <div class="service-wrapper" data-aos="fade-up">
          <div style="display: flex; flex-direction: column; align-items: flex-end">
            <div class="service-caption">맞춤 추천</div>
            <div class="service-header">마이하우스</div>
          </div>
          <div class="service-content">
            <span>다음 여행지를 고민 중이신가요?</span>
            <span> 하나 트래블홈은 사용자의 결제 데이터를 분석해,</span>
            <span> 당신에게 <span class="hilight">딱 맞는 다음</span> 여행지를 추천합니다</span>
            <span>편리하고 <span class="hilight">맞춤형 여행 추천 서비스</span>로,</span>
            <span>잊지 못할 추억을 만들어 보세요!</span>
          </div>
          <div class="move-page" @click="moveToMyhouse()">
            <div class="view-location">
              바로 가기
              <v-icon class="mdi-logo">mdi-arrow-right-bold</v-icon>
            </div>
          </div>
        </div>
      </div>

      <!-- 환율 하우스 -->
      <div class="service-info-2">
        <img src="/assets/img/main-2.png" class="main-gif" data-aos="fade-left" />
        <div class="service-wrapper" data-aos="fade-up">
          <div style="display: flex; flex-direction: column; align-items: flex-start">
            <div class="service-caption">실시간 환율</div>
            <div class="service-header">환율 하우스</div>
          </div>
          <div class="service-content">
            <span>여행지 고민은 이제 그만!</span>
            <span> <span class="hilight"> 실시간 환율 데이터</span>를 바탕으로</span>
            <span
              ><span class="hilight">저렴한 여행지</span>를 찾아주는
              <span class="hilight">스마트한 서비스</span></span
            >
            <span>실시간 그래프와 <span class="hilight">다양한 분석 도구</span>로</span>
            <span>똑똑하게 여행 준비를 시작해보세요!</span>
          </div>
          <div class="move-page" @click="moveToExchageHouse()">
            <div class="view-location2">
              <v-icon class="mdi-logo">mdi-arrow-left-bold</v-icon>
              바로 가기
            </div>
          </div>
        </div>
      </div>

      <!-- 여행 하우스 -->
      <div class="service-info-3" style="margin-bottom: 0px">
        <img src="/assets/img/main-3.png" class="main-gif" data-aos="fade-right" />
        <div class="service-wrapper" data-aos="fade-up">
          <div style="display: flex; flex-direction: column; align-items: flex-end">
            <div class="service-caption">모아보기</div>
            <div class="service-header">여행 하우스</div>
          </div>
          <div class="service-content">
            <span><span class="hilight">한 눈</span>에 보고싶으신가요?</span>
            <span>하나 트래블홈 사용자들이 어디를 여행하고 있는지!</span>
            <span>어느 나라의 환율이 지금 가장 저렴한지!</span>
            <span>트래블홈이 제공해주는 서비스를</span>
            <span>한 눈에 <span class="hilight">모아보세요</span>!</span>
          </div>
          <div class="move-page" @click="moveToTravelHouse()">
            <div class="view-location">
              바로 가기
              <v-icon class="mdi-logo">mdi-arrow-right-bold</v-icon>
            </div>
          </div>
        </div>
      </div>
    </div>

    <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { hashAndLowercase } from '@/assets/utils/hash'
import { useAuthStore } from '@/stores/auth.js'
import alertModal from '@/modal/alertModal.vue'
import AOS from 'aos'
import 'aos/dist/aos.css'

const router = useRouter()

const authStore = useAuthStore()
const username = ref('')
const userId = authStore.userId
const password = ref('')

const showDialog = ref(false)
const notificationMessage = ref('')
let callback = null

const numOfForeignCurrency = ref(0)
const numOfHistory = ref(0)
const numOfNotifications = ref(0)
const numOfAutoPurchaseRefund = ref(0)

const slides = ref([
  { image: 'assets/img/slide1.png', alt: 'First Slide' },
  { image: 'assets/img/slide2.png', alt: 'Second Slide' },
  { image: 'assets/img/slide3.png', alt: 'Third Slide' }
])

const signInWithKakao = () => {
  window.Kakao.Auth.authorize({
    redirectUri: 'http://travel-home.s3-website.ap-northeast-2.amazonaws.com/login-redirect'
  })
}

const showNotification = (message, cb = null) => {
  notificationMessage.value = message
  callback = cb
  showDialog.value = true
}

const handleLogoutClick = () => {
  showNotification('로그아웃 되었습니다.', () => {
    authStore.logout()
    window.location.reload()
  })
}

const handleClose = () => {
  if (callback) {
    callback()
  }
  showDialog.value = false
}

const handleLogin = async () => {
  try {
    const response = await axios.post('http://34.47.69.197:8081/api/user/login', {
      id: username.value,
      password: hashAndLowercase(password.value)
    })

    if (response.data.success) {
      useAuthStore().login(response.data.data)
      showNotification('로그인에 성공하였습니다.', () => {
        router.push({ name: 'home' })
      })
    } else {
      showNotification('로그인에 실패하였습니다.\n' + response.data.message)
    }
  } catch (error) {
    console.error(error)
    showNotification('서버에서 에러가 발생하였습니다.')
  }
}

const getUserInfoBrief = async () => {
  axios
    .get(`http://34.47.69.197:8081/api/user/${userId}/info-brief`)
    .then((response) => {
      numOfForeignCurrency.value = response.data.data.numOfForeignCurrency
      numOfHistory.value = response.data.data.numOfHistory
      numOfNotifications.value = response.data.data.numOfNotifications
      numOfAutoPurchaseRefund.value = response.data.data.numOfAutoPurchaseRefund
    })
    .catch((error) => {
      console.error(error)
    })
}

const moveToMyhouse = () => {
  router.push({ name: 'myhouse' })
}

const moveToExchageHouse = () => {
  router.push({ name: 'exchange-rate' })
}

const moveToTravelHouse = () => {
  router.push({ name: 'travel-house' })
}

const moveToMyPage = () => {
  router.push({ name: 'my-page' })
}

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })

  if (userId) {
    getUserInfoBrief()
  }
})
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
  font-family: 'Pretendard-Regular';
  src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')
    format('woff');
  font-weight: 400;
  font-style: normal;
}

.container {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.slider-login-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  width: 100%;
}

.divider {
  width: 350px;
  margin-top: 10px;
  margin-bottom: 20px;
  border: none;
  border-top: 1px solid #ddd;
}

h1.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  font-family: sans-serif;
}

.login-section {
  flex: 1;
  padding: 20px;
  text-align: center;
  max-width: 400px;
  height: 400px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-button {
  width: 100%;
  max-width: 750px;
  height: 50px;
  background-color: #03ac8e; /* Original background color */
  color: white; /* Original text color */
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 20px;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
}

.logout-button {
  text-decoration: underline;
  font-size: 20px;
  color: #8a8a8a;
  font-family: 'Hana2-Bold';
  padding-top: 5px;
  cursor: pointer;
}

.kakao-login-button {
  width: 100%;
  height: 50px;
  background-color: #fee500;
  color: #3c1e1e;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 25px;
  transition:
    background-color 0.3s ease,
    box-shadow 0.3s ease;
}

.logout-button {
  margin: 0;
}

.kakao-login-button img {
  width: 24px;
  height: 24px;
}

.kakao-login-button:hover {
  background-color: #ffd600;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.slider-section {
  flex: 2;
  height: 100%;
  margin-right: 50px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.slider-item {
  width: 100%;
  height: 300px;
}

.arrow-button {
  min-width: auto;
  padding: 0;
  background: none;
  box-shadow: none;
}

.arrow-button .v-icon {
  font-size: 32px;
}

.arrow-button:hover {
  background: none;
  box-shadow: none;
}

.hover-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background-color: rgba(255, 255, 255, 0.7);
  color: #03ac8e;
  font-size: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translate(-50%, -50%);
  overflow: hidden;
  opacity: 0;
}

.hovered-title {
  color: #03ac8e;
}

.hovered-description {
  color: #03ac8e;
}

.hover-button {
  background-color: #03ac8e;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;

  padding: 12px 24px;
}

.hover-button:hover {
  background-color: white;
  color: #03ac8e;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border: 1px solid #03ac8e;
}

.service-category:hover .hover-overlay {
  animation: expand-circle 0.8s ease forwards;
  opacity: 1;
}

@keyframes expand-circle {
  0% {
    width: 0;
    height: 0;
    opacity: 0;
  }
  100% {
    width: 100%;
    height: 100%;
    border-radius: 500px; /* Adjust to rectangular at the end */
    opacity: 1;
  }
}

/* Service Info Styling */

.service-category {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: 500px;
  margin-bottom: 40px;
  padding: 20px;
  border-radius: 8px;
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.service-category {
  position: relative;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 15px;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.service-category:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.service-image {
  width: 500px;
  height: 500px;
  object-fit: cover;
  border-radius: 50%;
  margin-right: 20px;
  flex-shrink: 0;
}

.service-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 350px;
}

.service-title {
  font-size: 36px; /* Adjust font size to 36px */
  /* font-weight: bold; */
  color: #666;
  margin-bottom: 20px; /* Adds space below the title */
  text-align: center; /* Ensures the text is centered */
}

.service-description {
  font-size: 24px;
  color: #000000;
}

.input-group {
  width: 100%;
  margin-bottom: 15px;
}

input[type='text'],
input[type='password'] {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 16px;
  box-sizing: border-box;
  background-color: #f9f9f9;
}

/* Custom CSS for controlling transition duration */
.slider-section .v-window-item {
  transition-duration: 1s !important; /* Adjust the duration here */
}

.flex-container {
  display: flex;
  width: 100%;
  justify-content: space-between;
  margin-top: 20px;
}

.flex-item {
  flex: 1;
  text-align: center;
  padding: 0px;
  margin-right: 00px;
  width: 100%;
}

.flex-item:last-child {
  margin-right: 0;
}

.welcome-section {
  padding: 20px;
  text-align: center;
  max-width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #f8e2dc, #dff8f6);
  border-radius: 8px;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.profile-image {
  width: 50px;
  border-radius: 50%;
  margin-bottom: 20px;
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin: 0;
}

.welcome-message {
  font-size: 24px;
  color: #666;
  font-family: 'Hana2-L';
  margin: 0;
}

.welcome-message-container {
  text-align: center;
  color: #333; /* Adjust text color if needed */
  margin: 0;
}

.welcome-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  width: 100%;
}

.welcome-action-item {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border-radius: 8px;
  padding: 10px 20px;
  margin: 10px;
  font-size: 16px;
  color: #333;
  width: 160px;
  height: 80px;
  font-family: 'Hana2-L';
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  flex-direction: column;
  cursor: pointer;
}

.welcome-action-item v-icon {
  margin-left: 10px;
}

.hilight-info {
  font-family: 'Hana2-Bold';
  font-size: 20px;
}

.main-gif {
  width: 650px;
  height: 800px;
  border-radius: 20px;
  box-shadow: 5px 5px 5px 5px rgba(0, 0, 0, 0.1);
}

.service-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 70px;
  width: 100%;
  height: 100%;
  padding: 20px;
  box-sizing: border-box;
}

.service-info-1 {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 250px;
  padding: 20px;
  gap: 20px;
}

.service-info-2 {
  display: flex;
  justify-content: space-between;
  flex-direction: row-reverse;
  width: 100%;
  margin-bottom: 250px;
  padding: 20px;
}

.service-info-3 {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 250px;
  padding: 20px;
}

.service-wrapper {
  display: flex;
  width: 100%;
  height: 100%;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 40px;
  justify-content: flex-start;
}
.service-header {
  font-size: 50px;
  font-family: 'Hana2-Bold';
  font-weight: bold;
  color: #666;
}

.service-content {
  display: flex;
  flex-direction: column;
  font-size: 32px;
  font-family: 'Hana2-Light';
  color: #333;
  gap: 20px;
}

.service-caption {
  font-size: 24px;
  font-family: 'Pretendard-Regular';
  font-weight: bold;
  color: #03ac8e;
}

.hilight {
  font-family: 'Hana2-B', sans-serif;
  font-size: 34px;
  color: #03ac8e;
}

.view-location,
.view-location2 {
  font-family: 'Hana2-B', sans-serif;
  font-size: 60px;
  color: #766666;
  margin-right: 20px;
  display: flex;
  align-items: center;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}

.move-page:hover .view-location {
  transform: translateX(10px);
  color: #03ac8e;
  cursor: pointer;
}

.move-page:hover .view-location2 {
  transform: translateX(-10px);
  color: #03ac8e;
  cursor: pointer;
}

.move-page {
  margin-top: 50px;
}
</style>
