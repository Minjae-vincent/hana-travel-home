<template>
  <div v-if="!isLoding">
    <div class="account-content">
      <div class="connected-account">
        <div class="account-notice">연결계좌</div>
        <div class="account-bank">{{ account.bank }}</div>
      </div>
      <div class="account-info">
        <div class="bankbook-info">
          <span style="font-family: 'Hana2-B'">{{ account.bankbook.name }}</span>
          <span>{{ account.bankbook.account }}</span>
        </div>
        <div class="balance-info">
          <span id="balance">{{ account.bankbook.balance.toLocaleString() }}</span> 원
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <v-skeleton-loader height="350" />
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { CountUp } from 'countup.js'
import { useAuthStore } from '@/stores/auth'
import AOS from 'aos'
import axios from 'axios'

const authStore = useAuthStore()
const userId = authStore.userId
const isLoding = ref(false)

const account = ref({
  bank: '하나은행',
  bankbook: {
    name: '',
    account: '',
    balance: 0
  }
})
const balance = ref(0)

const getUserAccount = async () => {
  isLoding.value = true
  try {
    await axios.get(`http://34.47.69.197:8081/api/user/manage/account/${userId}`).then((res) => {
      account.value.bankbook.name = res.data.data.name
      account.value.bankbook.account =
        res.data.data.accountNumber.slice(0, 4) +
        '-' +
        res.data.data.accountNumber.slice(4, 8) +
        '-' +
        res.data.data.accountNumber.slice(8, 12) +
        '-' +
        res.data.data.accountNumber.slice(12, 16)
      account.value.bankbook.balance = res.data.data.balance
      balance.value = account.value.bankbook.balance
      isLoding.value = false
      animateNumOfTravelers()
    })
  } catch (error) {
    console.error(error)
  }
}

const animateNumOfTravelers = async () => {
  await nextTick() // Wait for the DOM to update

  const countUp = new CountUp('balance', balance.value)
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
  getUserAccount()
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
  font-family: 'Hana2-Medium';
  src: url('@/assets/fonts/Hana2-Medium.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.account-content {
  width: 650px;
  height: auto;
  margin: 0 auto;
  border-radius: 30px;
  background: linear-gradient(to right, rgba(3, 172, 142, 0.75) 0%, rgba(0, 195, 160, 0.75) 100%);
  display: flex;
  flex-direction: column;
  box-shadow:
    0 20px 40px rgba(0, 0, 0, 0.15),
    /* 깊은 그림자 */ 0 5px 10px rgba(118, 118, 118, 0.704); /* 얕은 그림자 */
  border: 1px solid rgba(255, 255, 255, 0.2); /* 얇은 테두리 */
  backdrop-filter: blur(10px); /* 내부 배경 블러 */
  padding: 20px;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.account-content:hover {
  transform: translateY(-10px); /* 살짝 떠오르는 효과 */
  box-shadow:
    0 30px 60px rgba(0, 0, 0, 0.2),
    /* 더 강한 그림자 */ 0 10px 20px rgba(0, 0, 0, 0.1); /* 추가된 입체감 */
}

.connected-account {
  width: auto;
  height: auto;
  padding: 30px;
  border-radius: 30px 0 0 30px;
  display: flex;
  align-items: center;
  gap: 25px;

  color: white;
}
.account-notice {
  font-size: 30px;
  font-family: 'Hana2-L';
  color: #03ac8e;
  background: white;
  padding: 15px;
  border-radius: 30px;
}
.account-bank {
  font-family: 'Hana2-Bold';
  font-size: 35px;
}

.account-info {
  width: auto;
  height: auto;
  padding-left: 30px;
  padding-right: 30px;
  padding-bottom: 30px;
  border-radius: 0 30px 30px 0;
  display: flex;
  align-items: center;
  color: white;
  justify-content: space-between;
}
.bankbook-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-family: 'Spoqa-Han-Sans-Bold';
  font-size: 20px;
}
.balance-info {
  font-family: 'Hana2-Bold';
  font-size: 35px;
}
</style>
