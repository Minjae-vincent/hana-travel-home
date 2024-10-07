<template>
  <v-card class="country-card" elevation="2" width="200" height="200">
    <div class="avatar-container">
      <v-avatar size="100" class="avatar" :image="`/assets/img/${country.code}.gif`"></v-avatar>
    </div>
    <v-card-text>
      <div class="info-container">
        <div class="country-name">{{ country.codeName }}</div>
        <div>현재 환율: {{ country.exchangeRate }}원</div>
        <div>평균 소비 금액: {{ country.averageSpending.toLocaleString() }}원</div>
      </div>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue'

// Define props for the component
const props = defineProps({
  country: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.country-card {
  margin: 10px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100px;
  width: 100%;
  padding-top: 30px;
}

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.country-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
}
.info-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
