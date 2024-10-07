<!-- GoogleMap.vue -->

<template>
  <div v-if="isLoaded">
    <v-btn class="route-make" @click="drawRoute">경로 보기</v-btn>
    <v-btn class="route-delete" @click="deleteRoute">경로 삭제</v-btn>
    <div class="travel-journal-screen">
      <div class="map-container">
        <GoogleMap
          :center="center"
          api-key="AIzaSyDaSd49M-qsajhf0ntYOyExHDPAZMuSkVA"
          :zoom="zoom"
          style="width: 100%; height: 100%"
          mapId="9acad609211503d1"
        >
          <MarkerCluster>
            <Marker
              v-for="item in itineraryItems"
              :key="item.id"
              :options="{ position: { lat: item.latitude, lng: item.longitude } }"
              :title="item.name"
            />
          </MarkerCluster>
          <Polyline
            v-if="path.length"
            :options="{
              path: path,
              strokeColor: '#FF0000',
              strokeOpacity: 1.0,
              strokeWeight: 3
            }"
          />
        </GoogleMap>
      </div>

      <div class="divider"></div>

      <div class="itinerary-container">
        <v-timeline align="start" side="end">
          <v-timeline-item
            v-for="item in itineraryItems"
            :key="item.id"
            dot-color="teal"
            size="small"
          >
            <p class="itinerary-item-time">{{ item.date }}</p>
            <div
              class="itinerary-item"
              :class="{ 'selected-item': item.id === selectedItemId }"
              @click="moveToLocation(item.latitude, item.longitude, item.id)"
            >
              <div class="itinerary-item-details">
                <p class="itinerary-item-name">{{ item.name }}</p>
                <p class="itinerary-item-cost">
                  {{ item.cost.toLocaleString() }} {{ currencySymbols[code] }}
                </p>
                <p
                  :class="{
                    'status-normal': item.status === '완료',
                    'status-other': item.status !== '완료'
                  }"
                >
                  {{ item.status }}
                </p>
              </div>
            </div>
          </v-timeline-item>
        </v-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import axios from 'axios'
import { GoogleMap, Marker, MarkerCluster, Polyline } from 'vue3-google-map'

const isLoaded = ref(false)

const currencySymbols = {
  USD: '$', // 미국 달러
  JPY: '¥', // 일본 엔화
  EUR: '€', // 유로
  GBP: '£', // 영국 파운드
  CNY: '¥', // 중국 위안화
  SGD: 'S$', // 싱가포르 달러
  CAD: 'C$', // 캐나다 달러
  AUD: 'A$', // 호주 달러
  HKD: 'HK$', // 홍콩 달러
  THB: '฿', // 태국 바트
  CHF: 'CHF', // 스위스 프랑
  SEK: 'kr', // 스웨덴 크로나
  IDR: 'Rp', // 인도네시아 루피아
  NZD: 'NZ$', // 뉴질랜드 달러
  NOK: 'kr', // 노르웨이 크로네
  DKK: 'kr', // 덴마크 크로네
  MYR: 'RM', // 말레이시아 링깃
  AED: 'د.إ', // 아랍에미리트 디르함
  SAR: '﷼', // 사우디아라비아 리얄
  KWD: 'KD', // 쿠웨이트 디나르
  BHD: 'BD', // 바레인 디나르
  BND: 'B$' // 브루나이 달러
}

const zoom = ref(10)
var center = ref({ lat: 0, lng: 0 })
const selectedItemId = ref(null)
const path = ref([])

const props = defineProps({
  travelRecordId: String,
  code: String
})

const itineraryItems = ref([])

const moveToLocation = (lat, lng, id) => {
  center.value = { lat, lng }
  zoom.value = 15
  nextTick(() => {
    zoom.value = 17
  })
  selectedItemId.value = id
}

const drawRoute = () => {
  path.value = itineraryItems.value.map((item) => ({
    lat: item.latitude,
    lng: item.longitude
  }))
  zoom.value = 11
  nextTick(() => {
    zoom.value = 14
  })
}

const deleteRoute = () => {
  path.value = []
  zoom.value = 11
  nextTick(() => {
    zoom.value = 14
  })
}

onMounted(async () => {
  await axios
    .get(`http://34.47.69.197:8081/api/user/travel-record/${props.travelRecordId}`)
    .then((res) => {
      itineraryItems.value = res.data.data
      itineraryItems.value.forEach((item) => {
        item.id = item.paymentHistoryId
        item.name = item.location
        item.date = item.paymentDt
        item.latitude = item.lat
        item.longitude = item.lon
        item.cost = item.paymentAmount
      })
      center.value = {
        lat: itineraryItems.value[0].latitude,
        lng: itineraryItems.value[0].longitude
      }
      isLoaded.value = true
    })
})
</script>

<style scoped>
.travel-journal-screen {
  display: flex;
  height: 75vh;
  transition: all 0.3s ease-in-out;
  overflow-x: hidden;
  position: relative;
}

.route-make {
  position: absolute;
  top: 780px;
  left: 250px;
  z-index: 5;
}

.route-delete {
  position: absolute;
  top: 780px;
  left: 400px;
  z-index: 5;
}

.map-container {
  flex: 3;
  border: 2px solid #ddd;
  max-height: 100%;
  overflow: hidden;
  border-radius: 20px 0 0 20px;
}

.divider {
  width: 2px;
  height: 100%;
  background: repeating-linear-gradient(
    to bottom,
    transparent,
    transparent 5px,
    #ddd 5px,
    #ddd 10px
  );
  position: relative;
  z-index: 2;
  border-radius: 10px 0 0 10px;
}

.itinerary-container {
  flex: 2;
  padding: 20px;
  max-height: 100%;
  background-color: white;
  border: 2px solid #ddd;
  border-left: none;
  border-radius: 0 20px 20px 0;
  transition: transform 0.3s ease-in-out;
  display: flex;
  flex-direction: column;
}

.v-timeline {
  flex: 1;
  overflow-y: auto;
  padding: 0;
  position: relative;
  margin-bottom: 20px;
}

.v-timeline-item {
  padding-left: 0;
  position: relative;
}

.v-timeline-item:before {
  content: '';
  position: absolute;
  top: 0;
  left: -15px;
  height: 100%;
  width: 2px;
  background-color: #009688;
}

.itinerary-item {
  display: flex;
  cursor: pointer;
  align-items: flex-start;
  margin-bottom: 20px;
  animation: fadeInUp 0.6s ease forwards;
  opacity: 0;
  width: 350px;
  transition:
    transform 0.2s,
    background-color 0.2s,
    box-shadow 0.2s;
}

.itinerary-item:hover {
  transform: scale(1.02);
  background-color: #f0f8ff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.itinerary-item-time {
  font-size: 14px;
  color: #757575;
  font-weight: bold;
  margin-right: 20px;
  align-self: flex-start;
}

.itinerary-item-details {
  background-color: #fff;
  border-radius: 10px;
  padding: 15px;
  flex: 1;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  margin: 5px 0;
}

.status-normal {
  font-family: 'Spoqa Han Sans', sans-serif;
  color: blue;
  font-weight: 600;
  font-size: 14px;
  margin: 0;
}

.status-other {
  color: red;
  font-weight: 600;
  margin: 0;
  font-size: 14px;
}

.itinerary-item-name {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 5px;
}

.itinerary-item-cost {
  font-size: 16px;
  font-weight: bold;
  color: #03ac8e;
  margin-top: 10px;
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.selected-item {
  background-color: #e0f7fa;
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.route-make,
.route-delete {
  padding: 12px 24px;
  height: 44px;
  border: none;
  background-color: white;
  color: #03ac8e;
  border-radius: 25px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
  border: 1px solid #03ac8e;
}

.route-make:hover,
.route-delete:hover {
  background-color: #03ac8e;
  color: white;
}
</style>
