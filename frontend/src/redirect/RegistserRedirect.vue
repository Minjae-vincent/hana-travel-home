<template>
  <main class="main-content"></main>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ref, onMounted } from 'vue'
import alertModal from '@/modal/alertModal.vue'

const route = useRoute()
const router = useRouter()

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
    .get('http://34.47.69.197:8081/api/auth/kakao/callback?code=' + queryParams.code)
    .then((res) => {
      if (res.status !== 200) {
        showNotification('카카오 로그인에 실패했습니다.', () => {
          router.push({ name: 'login' })
        })
      } else {
        router.push({
          name: 'register-info-kakao',
          state: {
            user: res.data.data
          }
        })
      }
    })
    .catch((err) => {
      if (err.response?.status === 500) {
        showNotification('내부 서버 에러입니다. 관리자에게 문의 바랍니다.', () => {
          router.push({ name: 'login' })
        })
      } else if (err.response?.status === 409) {
        showNotification('이미 가입된 계정입니다. \n로그인 페이지로 이동합니다.', () => {
          router.push({ name: 'login' })
        })
      } else {
        console.log(err)
        showNotification('내부 서버 에러입니다. 관리자에게 문의 바랍니다.', () => {
          router.push({ name: 'home' })
        })
      }
    })
})
</script>
