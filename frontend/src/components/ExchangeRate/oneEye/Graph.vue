<template>
  <div v-if="!isLoading">
    <div class="headline" data-aos="fade-up" data-aos-once="true">실시간 환율 그래프</div>
    <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
      <span style="font-size: 24px; color: #03ac8e">총 22종</span>의 실시간 환율 변동 그래프를 통해
      현명한 여행 계획을 세워보세요!
    </div>
    <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
      각 나라별 환율 변동을 한눈에 확인하고, 최적의 타이밍에 맞춰 스마트하게 여행지를 선택하세요.
    </div>

    <v-sheet class="mx-auto custom-bg" max-width="800" data-aos="fade-up" data-aos-once="true">
      <v-slide-group v-model="model" class="pa-4" selected-class="bg-primary" show-arrows>
        <v-slide-group-item
          v-for="(item, index) in nations"
          :key="item.name"
          v-slot="{ isSelected, toggle, selectedClass }"
        >
          <v-card
            :class="['ma-4', selectedClass, { 'overlay-card': isSelected }]"
            color="surface-variant"
            class="nation-flag"
            height="100"
            width="200"
            @click="
              () => {
                toggle()
                handleSelection(index)
              }
            "
          >
            <img :src="`/assets/img/${item.code}.gif`" alt="nation flag" class="card-img" />
            <div class="d-flex fill-height align-center justify-center">
              <v-fade-transition>
                <div v-if="isSelected" class="overlay">
                  <span class="country-name">{{ item.codeName }}</span>
                </div>
              </v-fade-transition>
              <v-scale-transition>
                <v-icon
                  v-if="isSelected"
                  color="white"
                  icon="mdi-check-circle-outline"
                  size="48"
                  class="position-absolute"
                ></v-icon>
              </v-scale-transition>
            </div>
          </v-card>
        </v-slide-group-item>
      </v-slide-group>

      <v-expand-transition class="pa-4 custom-bg">
        <v-sheet v-if="model != null">
          <VueApexCharts
            type="line"
            height="350"
            :options="realtimeChartOptions"
            :series="lineChartSeries"
          />
        </v-sheet>
      </v-expand-transition>
    </v-sheet>
  </div>
  <div v-else>
    <v-skeleton-loader height="350" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

// Import all datasets
// import usData from '/src/sample/us.js'
// import deData from '/src/sample/de.js'
// import frData from '/src/sample/fr.js'
import axios from 'axios'

const isLoading = ref(true)

const nations = ref([])

const model = ref(null)
const lineChartSeries = ref([])

const handleSelection = (index) => {
  model.value = index
  updateChartData(index)
}

const updateChartData = (index) => {
  lineChartSeries.value.splice(0, lineChartSeries.value.length)

  const selectedData =
    nations.value[index].exchangeRateTime?.map((item) => {
      const date = new Date(parseInt(item.x) * 1000)
      const kstDate = new Date(date.getTime() + 9 * 60 * 60 * 1000)
      return {
        x: kstDate,
        y: item.y
      }
    }) || []

  lineChartSeries.value.push({
    name: `${nations.value[index].codeName} Exchange Rate`,
    data: selectedData
  })
}

const realtimeChartOptions = {
  chart: {
    id: 'line-chart',
    type: 'line',
    zoom: {
      enabled: true,
      type: 'x',
      autoScaleYaxis: true
    },
    toolbar: {
      autoSelected: 'zoom',
      tools: {
        download: true,
        selection: true,
        zoom: true,
        zoomin: true,
        zoomout: true,
        pan: true,
        reset: true
      }
    },
    animations: {
      enabled: true,
      easing: 'easeinout',
      dynamicAnimation: {
        speed: 500
      }
    }
  },
  dataLabels: {
    enabled: false
  },
  stroke: {
    curve: 'smooth'
  },
  title: {
    text: 'Zoomable Exchange Rate',
    align: 'left'
  },
  xaxis: {
    type: 'datetime'
  },
  yaxis: {
    labels: {
      formatter: (value) => value.toFixed(4)
    }
  },
  tooltip: {
    enabled: true,
    x: {
      format: 'yyyy년 MM월 dd일 HH시 mm분 ss초'
    }
  }
}

const getRealtimeExchangeRate = async () => {
  isLoading.value = true
  try {
    await axios
      .get('http://34.47.69.197:8081/api/user/exchange-rate/real-time')
      .then((response) => {
        nations.value = response.data.data
        isLoading.value = false
        handleSelection(0)
      })
  } catch (error) {
    console.error(error)
    isLoading.value = false
  }
}

onMounted(() => {
  getRealtimeExchangeRate()
})
</script>

<style scoped>
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

.overlay-card {
  position: relative;
  overflow: hidden;
}

.custom-bg {
  background-color: #f4f8f9;
}

.headline {
  text-align: center;
  font-family: 'Hana2-M', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  margin-bottom: 20px;
}

.headline-info {
  text-align: center;
  margin-top: 5px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 20px;
  color: #666;
}

.headline-more {
  margin-top: 30px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 25px;
  color: #666;
}

.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
