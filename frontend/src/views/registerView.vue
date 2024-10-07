<!-- registerView.vue -->
<template>
  <main class="main-content">
    <div class="spacer-header"></div>
    <div class="content">
      <h1 class="title">회원가입</h1>
      <div class="description-wrapper">
        <p class="description">카카오 간편 회원가입으로 보다 간편하게 회원가입이 가능합니다.</p>
      </div>
      <button class="kakao-button" @click="signUpWithKakao">
        <img src="@/assets/img/kakao_icon.png" alt="Kakao Icon" class="kakao-icon" />
        카카오 1초 간편가입
      </button>
      <button class="signup-button" @click="navigateToRegisterInfo">본인인증 후 회원가입</button>
    </div>
    <div class="spacer-footer"></div>
  </main>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

// eslint-disable-next-line vue/no-export-in-script-setup
const navigateToRegisterInfo = () => {
  router.push({ name: 'register-info' })
}

const signUpWithKakao = () => {
  window.Kakao.Auth.authorize({
    redirectUri: 'http://travel-home.s3-website.ap-northeast-2.amazonaws.com/register-redirect'
  })
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

.spacer-header {
  height: 103px;
}

.spacer-footer {
  height: 70px;
}

.main-content {
  padding-top: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  text-align: center;
}

.title {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 50px;
  font-family: 'Hana2-Bold', sans-serif;
}

.description-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.description {
  margin: 0;
  font-size: 18px;
  font-family: 'Hana2-Light', sans-serif;
  width: 750px;
  text-align: left;
}

.kakao-button {
  width: 750px;
  height: 42px;
  background-color: #fee500;
  border: none;
  border-radius: 21px;
  font-size: 18px;
  font-weight: bold;
  color: #3c1e1e;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    box-shadow 0.3s ease;
}

.kakao-button:hover {
  background-color: #ffd600;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.kakao-button .kakao-icon {
  width: 24px;
  height: 24px;
}

.signup-button {
  width: 750px;
  height: 42px;
  background-color: transparent;
  border: 1px solid #03ac8e;
  border-radius: 21px;
  font-size: 18px;
  font-weight: bold;
  color: #000;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
}

.signup-button:hover {
  background-color: #03ac8e;
  color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
