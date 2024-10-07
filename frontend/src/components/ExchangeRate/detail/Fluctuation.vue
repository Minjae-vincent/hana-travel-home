<template>
  <div class="container" data-aos="fade-up" data-aos-once="true">
    <div v-if="!isLoading">
      <VueApexCharts type="bar" :options="chartOptions" :series="series" />
    </div>
  </div>
</template>
<script setup>
import VueApexCharts from 'vue3-apexcharts'
import { ref, onMounted } from 'vue'
import axios from 'axios'
import nationColor from '/src/assets/styles/nationColor.js'

const isLoading = ref(false)
const fluctuationRange = ref([])

function getColor(code) {
  const colorObj = nationColor.find((item) => item.name === code)
  return colorObj ? colorObj.color : '#000000' // Default to black if no color is found
}

const series = ref([])

const getFluctuationRange = async () => {
  isLoading.value = true
  try {
    await axios
      .get('http://34.47.69.197:8081/api/user/exchange-rate/fluctuation')
      .then((response) => {
        fluctuationRange.value = response.data.data
        series.value = [
          {
            name: '환율 변동률 (%)',
            data: fluctuationRange.value.map((item) => {
              return {
                x: item.codeName,
                y: item.fluctuationRange.toFixed(2),
                fillColor: getColor(item.code)
              }
            })
          }
        ]
        isLoading.value = false
      })
  } catch (error) {
    console.error(error)
    isLoading.value = false
  }
}

const chartOptions = {
  chart: {
    type: 'bar',
    height: 350
  },
  title: {
    text: '근 3개월 환율 변동률',
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
      text: '환율 변동률(%)'
    }
  },
  dataLabels: {
    enabled: true,
    formatter: function (val) {
      return val.toFixed(2) + '%'
    }
  }
}

onMounted(() => {
  getFluctuationRange()
})
</script>
<style scoped>
.container {
  margin-top: 50px;
  width: 100%;
  height: 100%;
}
</style>
