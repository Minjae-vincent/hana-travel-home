<template>
  <main class="main-content">
    <div class="content">
      <h1 class="title">로그인</h1>
      <form class="login-form" @submit.prevent="handleLogin">
        <div class="input-group">
          <input type="text" placeholder="아이디" v-model="username" />
        </div>
        <div class="input-group">
          <input type="password" placeholder="비밀번호" v-model="password" />
        </div>
        <div class="checkbox-group">
          <input type="checkbox" id="remember" v-model="rememberMe" />
          <label for="remember">아이디 저장</label>
        </div>
        <button type="submit" class="login-button">로그인</button>
        <button type="button" class="kakao-login-button" @click="signInWithKakao">
          <img src="@/assets/img/kakao_icon.png" alt="Kakao Icon" class="kakao-icon" />
          카카오로 로그인하기
        </button>
      </form>
      <div class="divider"></div>
      <button type="button" class="register-button" @click="navigateToRegister">회원가입</button>
    </div>
    <div class="spacer-footer"></div>
  </main>

  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { hashAndLowercase } from '@/assets/utils/hash'
import { useAuthStore } from '@/stores/auth.js'
import alertModal from '@/modal/alertModal.vue'

const router = useRouter()

const username = ref('')
const password = ref('')
const rememberMe = ref(false)

const showDialog = ref(false)
const notificationMessage = ref('')
let callback = null

const navigateToRegister = () => {
  router.push({ name: 'register' })
}

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
</script>

<style scoped>
@font-face {
  font-family: 'Hana2-Bold';
  src: url('@/assets/fonts/Hana2-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Light';
  src: url('@/assets/fonts/Hana2-Light.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.spacer-footer {
  height: 70px;
}

.main-content {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.content {
  text-align: center;
  width: 400px;
}

.title {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 20px;
  font-family: 'Hana2-Bold', sans-serif;
}

.login-form {
  width: 100%;
  max-width: 750px;
  display: flex;
  flex-direction: column;
  align-items: center;
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
  border-radius: 25px;
  font-size: 16px;
  box-sizing: border-box;
  background-color: #f9f9f9;
}

.checkbox-group {
  width: 100%;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.checkbox-group input[type='checkbox'] {
  margin-right: 10px;
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

.login-button:hover {
  background-color: white;
  color: #03ac8e;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border: 1px solid #03ac8e;
}
.kakao-login-button {
  width: 100%;
  max-width: 750px;
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

.kakao-login-button img {
  width: 24px;
  height: 24px;
}

.kakao-login-button:hover {
  background-color: #ffd600;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.divider {
  width: 100%;
  height: 1px;
  background-color: #ddd;
  margin: 20px 0;
}

.register-button {
  width: 100%;
  max-width: 750px;
  height: 50px;
  background-color: transparent;
  color: #000;
  border: 1px solid #03ac8e;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
}

.register-button:hover {
  background-color: #03ac8e;
  color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
