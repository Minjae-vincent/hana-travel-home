<template>
  <div class="custom-centered-container">
    <div class="headline">
      실시간・분기 별 환율 데이터를 기반으로<br />
      여행 일정을 세워보세요!
    </div>
    <div class="standard-selector">
      <span class="standard-title">기준</span>
      <div class="level-3-button-group">
        <v-btn
          :class="{ 'level-3-active-button': selectedStandard === 'quarterly' }"
          @click="selectStandard('quarterly')"
          >분기</v-btn
        >
        <v-btn
          :class="{ 'level-3-active-button': selectedStandard === 'realtime' }"
          @click="selectStandard('realtime')"
          >실시간</v-btn
        >
      </div>
    </div>

    <!-- Seasonal Selector -->
    <div v-if="selectedStandard === 'quarterly'" class="season-selector">
      <b-form-select
        v-model="selectedSeason"
        :options="seasonOptions"
        class="custom-select mx-2"
        @change="handleSelectChange"
      ></b-form-select>
    </div>

    <!-- Quarterly Charts -->
    <div v-if="selectedStandard === 'quarterly' && selectedSeason">
      <VueApexCharts
        type="pie"
        height="350"
        :options="seasonChartOptions[selectedSeason]"
        :series="seasonChartSeries[selectedSeason]"
      />
    </div>

    <!-- Realtime Line Chart with Zoomable Time Series -->
    <div v-if="selectedStandard === 'realtime'">
      <VueApexCharts
        type="line"
        height="350"
        :options="realtimeChartOptions"
        :series="lineChartSeries"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import VueApexCharts from 'vue3-apexcharts'

const csvData = [
  { x: 1724371200000, y: 9.1644 },
  { x: 1724284800000, y: 9.1675 },
  { x: 1724198400000, y: 9.1718 },
  { x: 1724112000000, y: 9.1391 },
  { x: 1724025600000, y: 9.0823 },
  { x: 1723766400000, y: 9.1391 },
  { x: 1723680000000, y: 9.1368 },
  { x: 1723593600000, y: 9.2026 },
  { x: 1723507200000, y: 9.2708 },
  { x: 1723420800000, y: 9.2982 },
  { x: 1723161600000, y: 9.3024 },
  { x: 1723075200000, y: 9.3247 },
  { x: 1722988800000, y: 9.3693 },
  { x: 1722902400000, y: 9.5221 },
  { x: 1722816000000, y: 9.4744 },
  { x: 1722556800000, y: 9.2571 },
  { x: 1722470400000, y: 9.1657 },
  { x: 1722384000000, y: 9.1278 },
  { x: 1722297600000, y: 9.0516 },
  { x: 1722211200000, y: 8.9624 },
  { x: 1721952000000, y: 9.001 },
  { x: 1721865600000, y: 8.9778 },
  { x: 1721779200000, y: 8.9804 }
]

const selectedStandard = ref('quarterly')
const selectedSeason = ref('spring')
const lineChartSeries = ref([{ name: 'JPY to KRW', data: csvData }])

const selectStandard = (standard) => {
  selectedStandard.value = standard
  selectedSeason.value = 'spring'
}

const seasonOptions = ref([
  { value: 'spring', text: '봄' },
  { value: 'summer', text: '여름' },
  { value: 'fall', text: '가을' },
  { value: 'winter', text: '겨울' }
])

const handleSelectChange = () => {
  console.log('Selected Season:', selectedSeason.value)
}

const seasonChartOptions = {
  spring: {
    chart: {
      id: 'spring-chart',
      type: 'pie'
    },
    labels: ['Category A', 'Category B', 'Category C', 'Category D'],
    title: {
      text: 'Spring Season Data',
      align: 'center'
    }
  },
  summer: {
    chart: {
      id: 'summer-chart',
      type: 'pie'
    },
    labels: ['Category A', 'Category B', 'Category C', 'Category D'],
    title: {
      text: 'Summer Season Data',
      align: 'center'
    }
  },
  fall: {
    chart: {
      id: 'fall-chart',
      type: 'pie'
    },
    labels: ['Category A', 'Category B', 'Category C', 'Category D'],
    title: {
      text: 'Fall Season Data',
      align: 'center'
    }
  },
  winter: {
    chart: {
      id: 'winter-chart',
      type: 'pie'
    },
    labels: ['Category A', 'Category B', 'Category C', 'Category D'],
    title: {
      text: 'Winter Season Data',
      align: 'center'
    }
  }
}

const seasonChartSeries = {
  spring: [
    11.00212770932217260165072833126797446749, 6.68284789644012944983818770226537216828,
    6.54330919817645481362295521587556985787, 6.35214943002334844114819392940530146958,
    5.58383655962526101924487837914103504712, 5.30622122226873168689828380075345332775,
    5.11406398440488087747220885615229916966, 5.06896826662414287992345718386222293095,
    5.01941444888473202392086840688150100615, 5.00037767202960948712138379031648916081,
    4.99944512262789923426922650094329153257, 4.84687868080094228504122497055359246172,
    4.7772096757819349718051184554188624219, 4.28290766208251473477406679764243614931,
    4.16837068542450515295272370358252903648, 3.59130344613044830804327537935086930974,
    3.33333333333333333333333333333333333333, 3.24193548387096774193548387096774193548,
    2.70297802981425515297633196373391286631, 2.68517580301955618603708582429830783261,
    2.53663536113054704207030441595301213131, 2.53663536113054704207030441595301213131
  ],
  summer: [25, 35, 25, 15],
  fall: [20, 30, 30, 20],
  winter: [15, 25, 40, 20]
}

const realtimeChartOptions = {
  chart: {
    id: 'line-chart',
    type: 'line',
    zoom: {
      enabled: true,
      type: 'x', // Enable zooming along the x-axis
      autoScaleYaxis: true
    },
    toolbar: {
      autoSelected: 'zoom',
      tools: {
        download: true, // Optionally add a download button
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
      easing: 'linear',
      dynamicAnimation: {
        speed: 1000
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
    text: 'Zoomable JPY to KRW Exchange Rate',
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
      format: 'dd MMM yyyy'
    }
  }
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
.custom-centered-container {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  text-align: center;
}
.standard-selector {
  display: flex;
  align-items: center;
  padding-top: 10px;
  margin-top: 30px;
  width: 800px;
}

.standard-title {
  font-family: 'Hana2-M', sans-serif;
  font-size: 32px;
  margin-right: 50px;
}

.level-3-button-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.level-3-button-group .v-btn {
  border: 2px solid #b9b6b6;
  color: #b9b6b6;
  background-color: transparent;
  border-radius: 10px;
  font-weight: bold;
  padding: 2px 5px;
  font-size: 20px;
  height: 40px;
  width: 90px;
}

.level-3-button-group .level-3-active-button {
  border: 2px solid #03ac8e;
  background-color: white;
  color: #03ac8e;
  text-decoration: underline;
  font-size: 20px;
}

.season-selector {
  display: flex;
  gap: 10px;
  justify-content: left;
  margin-top: 20px;
}

.custom-select {
  border-radius: 5px;
  padding: 5px 30px 5px 10px;
  font-size: 18px;
  font-family: 'Spoqa-Han-Sans-light';
  width: auto;
  appearance: none;
  text-align: center;
}
</style>
