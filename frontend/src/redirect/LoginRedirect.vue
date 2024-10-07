<template>
  <main class="main-content"></main>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth.js'
import { ref, onMounted } from 'vue'
import alertModal from '@/modal/alertModal.vue'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const showDialog = ref(false)
const notificationMessage = ref('')
let callback = null

const queryParams = route.query

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

onMounted(() => {
  axios
    .get('http://34.47.69.197:8081/api/auth/kakao/login?code=' + queryParams.code)
    .then((res) => {
      if (res.data.code !== 200) {
        showNotification('카카오 로그인에 실패했습니다.\n' + res.data.message, () => {
          router.push({ name: 'login' })
        })
      } else {
        authStore.login(res.data.data)
        showNotification('로그인에 성공했습니다.', () => {
          router.push({ name: 'home' })
        })
      }
    })
    .catch((err) => {
      console.log(err)
      if (err.response.status === 500) {
        showNotification('내부 서버 에러입니다. 관리자에게 문의 바랍니다.', () => {
          router.push({ name: 'login' })
        })
      } else if (err.response.status === 409) {
        showNotification('이미 가입된 계정입니다. \n로그인 페이지로 이동합니다.', () => {
          router.push({ name: 'login' })
        })
      } else {
        showNotification('내부 서버 에러입니다. 관리자에게 문의 바랍니다.', () => {
          router.push({ name: 'home' })
        })
      }
    })
})
</script>
