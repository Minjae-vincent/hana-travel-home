<template>
  <div class="container" data-aos="fade-up" data-aos-once="true">
    <div v-if="!isLoading">
      <VueApexCharts type="boxPlot" :options="chartOptions" :series="series" />
    </div>
    <div v-else>
      <v-skeleton-loader height="350" />
    </div>
  </div>
</template>

<script setup>
import VueApexCharts from 'vue3-apexcharts'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const isLoading = ref(false)
const stability = ref([])

const series = ref([
  {
    name: 'box',
    data: []
  }
])

const chartOptions = ref({
  chart: { type: 'boxPlot', height: 350 },
  title: { text: 'Currency Exchange Rate Stability', align: 'left' },
  plotOptions: { boxPlot: { colors: { upper: '#5C4742', lower: '#A5978B' } } },
  xaxis: { type: 'category', title: { text: '나라' } },
  yaxis: { title: { text: '정규화된 환율' } }
})

const normalizeData = (exchangeList) => {
  const min = Math.min(...exchangeList)
  const max = Math.max(...exchangeList)
  return exchangeList.map((value) => (value - min) / (max - min))
}

const zScoreNormalize = (exchangeList) => {
  const mean = exchangeList.reduce((sum, val) => sum + val, 0) / exchangeList.length
  const stdDev = Math.sqrt(
    exchangeList.reduce((sum, val) => sum + Math.pow(val - mean, 2), 0) / exchangeList.length
  )

  return exchangeList.map((value) => (value - mean) / stdDev)
}

const getStability = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('http://34.47.69.197:8081/api/user/exchange-rate/stability')
    stability.value = response.data.data

    const data = stability.value.map((item) => ({
      x: item.codeName,
      y: zScoreNormalize(item.exchangeList)
    }))
    series.value[0].data = data

    isLoading.value = false
  } catch (error) {
    console.error(error)
  }
}

// const handleSelection = (index) => {
//   model.value = index
//   selectedCountrySeries.value = series.value[index]
// }

onMounted(() => {
  getStability()
})
</script>

<style scoped>
.container {
  margin-top: 50px;
  width: 100%;
  height: 100%;
}

.overlay-card {
  position: relative;
  overflow: hidden;
}

.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.overlay {
  position: absolute;
  background-color: rgba(0, 0, 0, 0.5);
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.country-name {
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 20px;
  color: white;
  z-index: 1;
}

.custom-bg {
  background-color: #f4f8f9;
}
</style>
