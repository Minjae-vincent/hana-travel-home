<template>
  <div class="text">
    <span class="time">27세 {{ authStore.userName }}</span> &nbsp;
    <span>님!</span>
  </div>
  <div class="text">
    <span>맞춤 여행자 보험을 추천해드려요!</span>
  </div>

  <div class="insurance-card" @click="moveToHanaInsurance()">
    <div class="insurance-info">
      <v-avatar size="60" class="avatar" image="/assets/img/insurance.png"></v-avatar>
      <div class="text-info">
        <div style="font-family: 'Hana2-B', sans-serif; font-size: 24px; color: #03ac8e">
          일반형
        </div>
        <div style="font-family: 'Hana2-L'; font-size: 20px; color: black">실속</div>
      </div>
      <div class="view-location">
        자세히 보러 가기
        <v-icon class="mdi-logo">mdi-arrow-right</v-icon>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const moveToHanaInsurance = () => {
  window.open(
    'https://www.googleadservices.com/pagead/aclk?sa=L&ai=DChcSEwig6uWLttiIAxUs0BYFHXXNAsUYABAAGgJ0bA&co=1&ase=2&gclid=Cj0KCQjwgL-3BhDnARIsAL6KZ6-5dyIVRzTZKbdR7wSiVpBy7mses7IWH74iTvsow8ua8i8y4GyMGOkaAn-ZEALw_wcB&ohost=www.google.com&cid=CAESVeD26TTMbeaRp0OIUR8U9OSfshcIHo1OVkPRnLPEesS3H1lnd1-NwPDGfaFHnAcGVMFGO-gJ_itQRHWP5dCqkR3JpEDx0ret6aSUyJ_OQr3fnco31FE&sig=AOD64_0nif6IKvv7KgZMZsCKd8g15kPn9w&q&nis=4&adurl&ved=2ahUKEwjx2OGLttiIAxUPh68BHXdLAK4Q0Qx6BAgJEAE',
    '_blank'
  )
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

.time {
  font-family: 'Hana2-Bold';
  font-size: 48px;
  color: #03ac8e;
}

.text {
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
}

.insurance-card {
  cursor: pointer;
  width: 500px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 2px solid #00336629;
  border-radius: 40px;
  margin-top: 10px;
  background-color: #ffffff;
  transition:
    background-color 0.3s ease,
    transform 0.3s ease;
}

.insurance-card:hover {
  background-color: #f3fff0;
}

.insurance-card:hover .view-location {
  transform: translateX(10px);
  color: #03ac8e;
}

.insurance-card:hover .mdi-logo {
  color: #03ac8e;
}

.view-location {
  font-family: 'Hana2-B', sans-serif;
  font-size: 20px;
  color: #766666;
  margin-right: 20px;
  display: flex;
  align-items: center;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}

.text-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 18px;
  padding: 10px;
}

.insurance-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
  margin-left: 20px;
}
</style>
