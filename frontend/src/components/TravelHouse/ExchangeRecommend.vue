<template>
  <div class="text">
    <span class="time">{{ currentTime }}</span> &nbsp;
    <span>현재!</span>
  </div>

  <div v-if="!isLoaded">
    <div class="text">
      <v-card
        class="custom-flex d-flex ma-2"
        elevation="2"
        height="100"
        width="auto"
        color="#474444"
      >
        <div class="content-container">
          <v-avatar size="48" class="avatar" :image="`/assets/img/${code}.gif`"></v-avatar>
          <div class="country-name">{{ nationName }}</div>
        </div>
      </v-card>
      <span>의 환율이 상대적으로 저렴해요!</span>
    </div>
    <div class="bar-chart">
      <VueApexCharts type="bar" :options="chartOptions" :series="series" />
    </div>
  </div>
  <div v-else>
    <v-progress-circular indeterminate color="primary"></v-progress-circular>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import AOS from 'aos'
import VueApexCharts from 'vue3-apexcharts'
import axios from 'axios'

const nationName = ref('')
const code = ref('')
const isLoaded = ref(false)
const series = ref([])

function getRandomColor() {
  const letters = '0123456789ABCDEF'
  let color = '#'
  for (let i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)]
  }
  return color
}

const getRateOfChange = async () => {
  isLoaded.value = true
  await axios
    .get('http://34.47.69.197:8081/api/user/travel/recommendation')
    .then((response) => {
      nationName.value = response.data.data.codeName
      const now = parseFloat(response.data.data.currentRate.toFixed(2))
      const average = parseFloat(response.data.data.threeMonthRate.toFixed(2))
      code.value = response.data.data.code

      series.value.push({
        name: '환율 (원)',
        data: [
          { x: '평균', y: average, fillColor: getRandomColor() },
          { x: '현재', y: now, fillColor: getRandomColor() }
        ]
      })

      const minValue = Math.min(now, average)
      const maxValue = Math.max(now, average)
      const range = maxValue - minValue

      const minMargin = 10 // Minimum margin to ensure visibility
      const margin = Math.max(range * 0.1, minMargin)

      chartOptions.yaxis.min = minValue - margin
      chartOptions.yaxis.max = maxValue + margin

      isLoaded.value = false
    })
    .catch((error) => {
      console.error(error)
    })
}

const chartOptions = {
  chart: {
    type: 'bar',
    height: 350
  },
  title: {
    text: '환율 변동 (지난 3개월)',
    align: 'left'
  },
  xaxis: {
    type: 'category',
    labels: {
      rotate: -45
    }
  },
  yaxis: {
    title: {
      text: '환율 (원)'
    },
    min: 0,
    max: 100
  },
  dataLabels: {
    enabled: true,
    formatter: function (val) {
      return val.toFixed(2) + '원'
    }
  }
}

const currentTime = ref(getFormattedCurrentTime())

function getFormattedCurrentTime() {
  const now = new Date()
  const utc = now.getTime() + now.getTimezoneOffset() * 60000
  const kstTime = new Date(utc + 3600000 * 9)
  const options = {
    year: 'numeric',
    month: '2-digit'
  }
  return new Intl.DateTimeFormat('ko-KR', options)
    .format(kstTime)
    .replace('.', '년 ')
    .replace('.', '월 ')
}

function updateTime() {
  currentTime.value = getFormattedCurrentTime()
}

let timer = null

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })
  timer = setInterval(updateTime, 1000000)

  getRateOfChange()
})

onUnmounted(() => {
  clearInterval(timer)
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

.country-name {
  font-size: 36px;
  font-family: 'Hana2-M';
}

.custom-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}

.content-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}

.bar-chart {
  width: 800px;
  margin-top: 20px;
}
</style>
