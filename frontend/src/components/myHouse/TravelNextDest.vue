<template>
  <div class="custom-centered-container">
    <div class="headline">
      <span>{{ cityName }}</span
      >의 <span>{{ placeName }}</span
      >을 가셨군요? 다음 행선지로 여기는 어떠세요?
    </div>
    <div class="map-wrapper-1">
      <GoogleMap
        :center="mainPosition"
        :zoom="18"
        api-key="AIzaSyDaSd49M-qsajhf0ntYOyExHDPAZMuSkVA"
        mapId="9acad609211503d1"
        style="width: 100%; height: 500px; border-radius: 15px; margin-top: 20px"
      >
        <Marker :options="{ position: mainPosition }" />
      </GoogleMap>
    </div>
    <div class="place-cards-wrapper">
      <div
        v-for="(place, index) in recommendedPlaces"
        :key="index"
        class="place-card"
        @click="viewPlaceOnMap(place)"
        data-aos="fade-right"
        data-aos-once="true"
      >
        <div class="place-info">
          <div class="place-city">{{ place.city }}</div>
          <div class="place-name">{{ place.name }}</div>
          <div class="place-details">#거리 {{ place.distance }}km #{{ place.category }}</div>
        </div>

        <div class="view-location">
          위치 보러가기
          <v-icon class="mdi-logo">mdi-map-marker</v-icon>
        </div>
      </div>
    </div>

    <div v-if="showMap" class="map-modal">
      <div class="map-container">
        <v-icon class="close-button" @click="closeMap">mdi-close-circle-outline</v-icon>
        <div class="map-wrapper">
          <GoogleMap
            :center="mapCenter"
            :zoom="15"
            api-key="AIzaSyDaSd49M-qsajhf0ntYOyExHDPAZMuSkVA"
            mapId="9acad609211503d1"
            style="width: 100%; height: 90%; border-radius: 15px; margin-top: 40px"
          >
            <Marker :options="{ position: mapCenter }" />
            <InfoWindow :options="{ position: mapCenter }">
              <div>
                <h4>{{ selectedPlace.name }}</h4>
                <p>거리: {{ selectedPlace.distance }}km</p>
                <p>카테고리: {{ selectedPlace.category }}</p>
              </div>
            </InfoWindow>
          </GoogleMap>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { GoogleMap, Marker, InfoWindow } from 'vue3-google-map'
import AOS from 'aos'
import 'aos/dist/aos.css'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
var mainPosition = ref({ lat: 34.6937, lng: 135.5023 })

var cityName = ref('오사카')
var placeName = ref('이치란 라멘')
const recommendedPlaces = ref([
  {
    city: '오사카',
    name: '돈키호테',
    distance: 0.2,
    category: '상점',
    lat: 34.6704,
    lng: 135.5023
  },
  {
    city: '오사카',
    name: '유니버설 스튜디오',
    distance: 7.5,
    category: '놀이공원',
    lat: 34.6654,
    lng: 135.4323
  },
  {
    city: '오사카',
    name: '오사카성',
    distance: 3.1,
    category: '유적지',
    lat: 34.6873,
    lng: 135.5262
  }
])

// const recommendedPlaces = ref([])

const selectedPlace = ref(null)
const mapCenter = ref({ lat: 0, lng: 0 })
const showMap = ref(false)

const viewPlaceOnMap = (place) => {
  selectedPlace.value = place
  mapCenter.value = { lat: place.lat, lng: place.lng }
  showMap.value = true
}

const closeMap = () => {
  showMap.value = false
}

onMounted(async () => {
  AOS.init({
    duration: 1000,
    offset: 200
  })

  axios.get(`http://34.47.69.197:8081/api/user/travel-now/${authStore.userId}`).then((response) => {
    cityName.value = response.data.data.codeName
    placeName.value = response.data.data.location
    mainPosition.value = { lat: response.data.data.lat, lng: response.data.data.lon }
  })

  axios
    .get(`http://34.47.69.197:8081/api/user/travel-recommendation/${authStore.userId}`)
    .then((response) => {
      const result = response.data.data
      result.forEach((place) => {
        place.city = place.codeName
        place.name = place.location
        place.distance = place.distance / 1000
        place.lng = place.lon
      })
      recommendedPlaces.value = result
    })
})
</script>

<style scoped>
.custom-centered-container {
  padding: 20px;
  width: 100%;
  margin: 0 auto;
  text-align: center;
}

.headline {
  text-align: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
  padding-bottom: 20px;
}

.headline > span {
  font-family: 'Hana2-M', sans-serif;
  font-size: 32px;
}

.place-cards-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.place-card {
  cursor: pointer;
  width: 650px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border: 2px solid #00336629;
  border-radius: 15px;
  margin-top: 10px;
  background-color: #ffffff;
  transition:
    background-color 0.3s ease,
    transform 0.3s ease;
}

.place-card:hover {
  background-color: #f3fff0;
}

.place-info {
  text-align: left;
}

.place-city {
  font-family: 'Hana2-M', sans-serif;
  font-size: 18px;
  color: #333;
}

.place-name {
  font-family: 'Hana2-M', sans-serif;
  font-size: 22px;
  color: #000;
  margin: 5px 0;
}

.place-details {
  font-family: 'Hana2-Light', sans-serif;
  font-size: 14px;
  color: #777;
}

.view-location {
  font-family: 'Hana2-B', sans-serif;
  font-size: 20px;
  color: #766666;
  margin-right: 20px;
  display: flex;
  align-items: center;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}

.place-card:hover .view-location {
  transform: translateX(10px);
  color: #03ac8e;
}

.place-card:hover .mdi-logo {
  color: #03ac8e;
}

.map-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.map-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 800px;
  height: 500px;
  position: relative;
  z-index: 1;
}

.map-wrapper {
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 15px;
}

.map-wrapper-1 {
  width: 100%;
  height: 100%;
  overflow: hidden;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.close-button {
  position: absolute;
  color: #333;
  top: 10px;
  right: 10px;
  cursor: pointer;
  font-size: 40px;
  transition:
    transform 0.3s ease,
    font-size 0.3s ease;
}

.close-button:hover {
  transform: rotate(90deg);
}
</style>
