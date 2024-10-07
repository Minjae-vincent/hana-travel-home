<!-- Header.vue -->
<template>
  <header class="header">
    <nav class="menu-bar">
      <div class="logo">
        <router-link to="/">
          <img src="@/assets/img/logo.png" alt="Logo" class="logo-image" />
        </router-link>
      </div>
      <ul class="menu">
        <li><router-link to="/" exact-active-class="active-link">홈</router-link></li>
        <li>
          <router-link to="/my-house" active-class="active-link">마이하우스</router-link>
        </li>
        <li>
          <router-link to="/exchange-rate-house" active-class="active-link">환율하우스</router-link>
        </li>
        <li>
          <router-link to="/travel-house" active-class="active-link">여행하우스</router-link>
        </li>
      </ul>
      <ul class="actions">
        <li v-if="authStore.isAuthenticated">
          <router-link to="/my-page" active-class="active-link">마이페이지</router-link>
        </li>
        <li v-if="!authStore.isAuthenticated">
          <router-link to="/login" active-class="active-link">로그인</router-link>
        </li>
        <li v-else>
          <a href="#" @click.prevent="handleLogoutClick">로그아웃</a>
        </li>
        <li v-if="!authStore.isAuthenticated">
          <router-link to="/register" active-class="active-link">회원가입</router-link>
        </li>
        <li v-else>
          <a href="#notifications">알림</a>
        </li>
      </ul>
    </nav>
  </header>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import alertModal from '@/modal/alertModal.vue'

const showDialog = ref(false)
const notificationMessage = ref('')
const authStore = useAuthStore()
let logoutCallback = null

const showNotification = (message, callback) => {
  notificationMessage.value = message
  logoutCallback = callback
  showDialog.value = true
}

const handleClose = () => {
  if (logoutCallback) {
    logoutCallback()
  }
  showDialog.value = false
}

const handleLogoutClick = () => {
  showNotification('로그아웃 되었습니다.', () => {
    authStore.logout()
    window.location.reload()
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

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: white;
  padding: 1rem 0;
  color: white;
  text-align: center;
  font-family: 'Hana2-Bold', sans-serif;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.menu-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 1rem;
}

.logo a {
  color: white;
  text-decoration: none;
  font-size: 1.5rem;
  font-weight: bold;
  font-family: inherit;
}

.logo-image {
  width: 130px;
  height: auto;
}

.menu,
.actions {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
}

.menu li,
.actions li {
  margin: 0 1rem;
}

.menu a,
.actions a {
  color: black;
  text-decoration: none;
  transition: color 0.2s ease;
}

.menu a:hover,
.actions a:hover {
  color: #03ac8e;
  text-decoration: none;
}

.active-link {
  color: #03ac8e !important;
}
</style>
