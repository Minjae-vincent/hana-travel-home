<template>
  <v-window v-model="onboarding" direction="vertical" show-arrows="hover">
    <v-window-item v-for="(country, index) in countries" :key="`card-${index}`">
      <v-card
        class="custom-padding d-flex ma-2"
        elevation="2"
        height="100"
        width="auto"
        color="#474444"
      >
        <div
          style="
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
          "
        >
          <div>
            <v-avatar
              size="32"
              class="small-avatar"
              :image="`/assets/img/${country.code}.gif`"
            ></v-avatar>
          </div>
          <div class="country-name">{{ country.codeName }}</div>
        </div>
      </v-card>
    </v-window-item>
  </v-window>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()
const userId = authStore.userId
const onboarding = ref(0)
const isLoaded = ref(false)

let intervalId: ReturnType<typeof setInterval>

const countries = reactive([])

const getUserHistory = async () => {
  isLoaded.value = true
  await axios
    .get(`http://34.47.69.197:8081/api/user/travel/history/${userId}`)
    .then((response) => {
      countries.push(...response.data.data)
      isLoaded.value = false
    })
    .catch((error) => {
      console.error(error)
    })
}

onMounted(() => {
  intervalId = setInterval(() => {
    onboarding.value = (onboarding.value + 1) % countries.length
  }, 2000)

  getUserHistory()
})

onBeforeUnmount(() => {
  clearInterval(intervalId)
})
</script>

<style scoped>
.custom-padding {
  padding: 40px;
}
.small-avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}
.country-name {
  font-size: 32px;
  font-family: 'Hana2-M';
}
</style>
