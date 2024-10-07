<template>
  <div style="display: flex; flex-direction: column; align-items: center">
    <div class="headline">아직 로그인을 하지 않으셨군요!</div>
    <div class="headline">로그인을 하시면 더욱 상세한 추천이 가능합니다!</div>
    <br />

    <button @click="login" class="login-button">로그인</button>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
const login = () => {
  router.push({
    path: '/login'
  })
}
</script>
<style scoped>
.headline {
  text-align: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
}
.login-button {
  width: 20%;
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
</style>
