<template>
  <div v-if="!isLoded">
    <div class="recommend">
      <div style="display: flex; align-items: center; justify-content: center">
        <div class="headline">{{ authStore.userName }}님은</div>
        <CountrySlider />
        <div class="headline">을 다녀오셨네요!</div>
      </div>
      <div class="headline">취향 비슷한 트래블로거들은 여기를 다녀왔어요!</div>
      <div class="country-list">
        <div class="country-row" v-for="(group, index) in countryGroups" :key="index">
          <CountryInfo v-for="country in group" :key="country.flag" :country="country" />
        </div>
      </div>

      <br />
    </div>
  </div>
  <div v-else>
    <v-skeleton-loader height="350" />
  </div>
</template>
<script setup>
import { useAuthStore } from '@/stores/auth'
import CountrySlider from './CountrySlider.vue'
import CountryInfo from './CountryInfo.vue'
import axios from 'axios'
import { onMounted, ref } from 'vue'

const authStore = useAuthStore()
const userId = authStore.userId
const isLoded = ref(false)

const countries = ref([])

const countryGroups = ref([])

const getUserCustomRecommendation = async () => {
  isLoded.value = true
  await axios
    .get(`http://34.47.69.197:8081/api/user/travel/recommendation/${userId}`)
    .then((response) => {
      countries.value.push(...response.data.data)
      for (let i = 0; i < countries.value.length; i += 3) {
        countryGroups.value.push(countries.value.slice(i, i + 3))
      }
      isLoded.value = false
    })
    .catch((error) => {
      console.error(error)
    })
}

onMounted(() => {
  getUserCustomRecommendation()
})
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
  background-color: #03ac8e;
  color: white;
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

.recommend {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.country-list {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.country-row {
  display: flex;
  justify-content: space-around;
  width: 100%;
  max-width: 800px;
  margin-bottom: 20px;
}
</style>
