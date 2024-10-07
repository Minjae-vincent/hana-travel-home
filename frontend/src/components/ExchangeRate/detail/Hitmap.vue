<template>
  <div class="container" data-aos="fade-up" data-aos-once="true">
    <div v-if="!isLoading">
      <VueApexCharts type="heatmap" :options="chartOptions" :series="series" />
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
const series = ref([])

// 변동률 계산을 위한 함수
const calculateRateChange = (monthlyRates) => {
  const averageRate = monthlyRates.reduce((sum, rate) => sum + rate, 0) / monthlyRates.length
  return monthlyRates.map((rate) => {
    const rateChange = ((rate - averageRate) / averageRate) * 100 // 변동률을 백분율로 계산
    return rateChange.toFixed(2)
  })
}

const getMonthlyExchangeRates = async () => {
  isLoading.value = true
  try {
    const response = await axios.get('http://34.47.69.197:8081/api/user/exchange-rate/seasonal')
    const exchangeRatesData = response.data.data

    series.value = exchangeRatesData.map((item) => {
      const rateChanges = calculateRateChange(item.exchangeList) // 변동률 계산
      return {
        name: item.name,
        data: rateChanges.map((rateChange, index) => ({
          x: new Date(2024, index).toLocaleString('default', { month: 'short' }),
          y: parseFloat(rateChange) // 변동률을 y 값으로
        }))
      }
    })

    isLoading.value = false
  } catch (error) {
    isLoading.value = false
    console.error(error)
  }
}

// 컴포넌트가 마운트될 때 데이터 가져오기
onMounted(() => {
  getMonthlyExchangeRates()
})

// 히트맵 옵션
const chartOptions = ref({
  chart: {
    type: 'heatmap'
  },
  title: {
    text: 'Seasonal Exchange Rate Favorability'
  },
  xaxis: {
    type: 'category'
  },
  yaxis: {
    title: {
      text: 'Country'
    }
  },
  plotOptions: {
    heatmap: {
      shadeIntensity: 0.5,
      radius: 0,
      useFillColorAsStroke: true,
      colorScale: {
        ranges: [
          {
            from: -3,
            to: -1,
            name: 'Cheaper',
            color: '#00A100' // 녹색: 저렴한 구간
          },
          {
            from: -1,
            to: 1,
            name: 'Neutral',
            color: '#128FD9' // 파란색: 변동 없음
          },
          {
            from: 1,
            to: 2,
            name: 'Slightly Expensive',
            color: '#FFB200' // 노란색: 약간 비쌈
          },
          {
            from: 2,
            to: 3,
            name: 'Expensive',
            color: '#FF6600' // 주황색: 비쌈
          },
          {
            from: 3,
            to: 10, // 상한선은 충분히 넓게 잡음
            name: 'Very Expensive',
            color: '#FF0000' // 빨간색: 매우 비쌈
          }
        ]
      }
    }
  }
})
</script>
<style scoped>
.container {
  margin-top: 50px;
  width: 100%;
  height: 100%;
}
</style>
