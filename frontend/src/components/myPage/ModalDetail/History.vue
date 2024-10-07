<template>
  <div class="search-bar-container">
    <!-- Search Bar -->
    <div class="search-condition d-flex align-center" @click="openFilterModal">
      <span>{{ selectedPeriodDisplay }} · {{ selectedSortOrder }}</span>
      <v-icon small class="ml-1">mdi-tune</v-icon>
    </div>
    <v-autocomplete
      :items="storeNames"
      class="mx-auto search-bar"
      density="comfortable"
      placeholder="가맹점 이름 검색"
      prepend-inner-icon="mdi-magnify"
      theme="light"
      variant="outlined"
      auto-select-first
      v-model="selectedStore"
      bg-color="#f6f8f9"
      rounded
      style="width: 100%"
    ></v-autocomplete>

    <v-dialog v-model="showFilterModal" max-width="600">
      <v-card>
        <v-card-title>
          <div style="display: flex">
            <span class="headline">필터 설정</span>
            <v-icon class="ml-auto" @click="closeFilterModal">mdi-close</v-icon>
          </div>
        </v-card-title>
        <v-card-text>
          <!-- 조회 기간 -->
          <div class="mb-4">
            <label class="title">조회 기간</label>
            <v-btn-toggle v-model="tempSelectedPeriod" mandatory class="d-flex" borderless>
              <v-btn
                v-for="period in periods"
                :key="period.value"
                :value="period.value"
                :style="tempSelectedPeriod === period.value ? selectedStyle : unselectedStyle"
              >
                {{ period.label }}
              </v-btn>
            </v-btn-toggle>

            <!-- Show date range picker if '직접입력' is selected -->
            <div v-if="tempSelectedPeriod === '직접입력'" class="mt-4">
              <v-date-input
                v-model="customDateRange"
                label="기간 선택"
                width="100%"
                multiple="range"
                placeholder="날짜를 선택하세요"
              ></v-date-input>
            </div>
          </div>

          <!-- 거래내역 정렬 -->
          <div class="mb-4">
            <label class="title">거래내역 정렬</label>
            <v-btn-toggle v-model="tempSelectedSortOrder" mandatory class="d-flex" borderless>
              <v-btn
                v-for="order in sortOrders"
                :key="order.value"
                :value="order.value"
                :style="tempSelectedSortOrder === order.value ? selectedStyle : unselectedStyle"
              >
                {{ order.label }}
              </v-btn>
            </v-btn-toggle>
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="applyFilter">적용하기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>

  <div>
    <div v-if="!isLoding">
      <div v-if="filteredDetails.length > 0">
        <v-virtual-scroll :items="filteredDetails" height="400">
          <template v-slot:default="{ item }">
            <div class="date-section">
              <div class="date">{{ item.date }}</div>
              <div
                v-for="(transaction, idx) in item.data"
                :key="idx"
                class="transaction-item d-flex justify-space-between align-center"
              >
                <div>
                  <div class="d-flex align-center mb-2">
                    <v-avatar
                      size="32"
                      class="mr-2 avatar"
                      :image="`/assets/img/${props.info.code}.gif`"
                    ></v-avatar>
                    <div class="transaction-amount">
                      <span class="currency">{{ props.info.code }}</span>
                      {{ transaction.amount.toLocaleString() }}
                    </div>
                    <span class="grey--text caption ml-2">결제</span>
                  </div>
                  <div class="transaction-info grey--text caption">
                    {{
                      new Date(transaction.timestamp).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit'
                      })
                    }}
                    |
                    {{ transaction.store }}
                  </div>
                </div>
                <div class="view-location" @click="viewDetails(transaction)">
                  위치 보기
                  <v-icon class="mdi-logo">mdi-map-marker</v-icon>
                </div>
              </div>
            </div>
          </template>
        </v-virtual-scroll>
      </div>
      <div v-else>
        <div class="modal-component">
          <img class="no-data-img" src="/assets/img/nodata.png" />
          <div class="no-data-info">데이터가 없습니다.</div>
        </div>
      </div>
    </div>
    <div v-else>
      <v-skeleton-loader height="300" />
    </div>
  </div>

  <div v-if="showMap" class="map-modal">
    <div class="map-container">
      <v-icon class="close-button" @click="closeMap">mdi-close-circle-outline</v-icon>
      <GoogleMap
        :center="mapCenter"
        :zoom="18"
        api-key="AIzaSyDaSd49M-qsajhf0ntYOyExHDPAZMuSkVA"
        mapId="9acad609211503d1"
        style="width: 100%; height: 90%; border-radius: 15px; margin-top: 40px"
      >
        <Marker :options="{ position: mapCenter }" />
        <InfoWindow :options="{ position: mapCenter }">
          <div style="width: 200px">
            <p style="font-family: 'Hana2-B'; font-size: 16px">{{ selectedPlace.store }}</p>
            <p>{{ selectedPlace.amount.toLocaleString() }} VND</p>
            <p>{{ selectedPlace.timestamp }}</p>
          </div>
        </InfoWindow>
      </GoogleMap>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { GoogleMap, Marker, InfoWindow } from 'vue3-google-map'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()
const userId = authStore.userId
const isLoding = ref(false)

const props = defineProps({
  info: Object
})

const getUserTransactions = async () => {
  isLoding.value = true
  try {
    await axios
      .get(
        `http://34.47.69.197:8081/api/user/manage/possession/${userId}/${props.info.code}/payment-history`
      )
      .then((response) => {
        const sqlResults = response.data.data // 서버에서 가져온 데이터

        // 데이터를 날짜별로 그룹화합니다.
        const groupedData = {}

        sqlResults.forEach((item) => {
          // 날짜를 한국어 형식으로 변환합니다.
          const dateObj = new Date(item.paymentTimeStamp)
          const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }
          const formattedDate = dateObj.toLocaleDateString('ko-KR', options)

          if (!groupedData[formattedDate]) {
            groupedData[formattedDate] = []
          }

          groupedData[formattedDate].push({
            timestamp: item.paymentTimeStamp,
            store: item.store,
            amount: item.amount,
            lat: item.lat,
            lng: item.lng
          })
        })

        // groupedData를 details 배열로 변환합니다.
        details.value = Object.keys(groupedData).map((date) => ({
          date,
          data: groupedData[date]
        }))
        isLoding.value = false
      })
  } catch (error) {
    console.error(error)
  }
}

// const details = ref([
//   {
//     date: '2024년 09월 09일 금요일',
//     data: [
//       {
//         timestamp: '2024-09-09T12:00:00',
//         store: 'Store 1',
//         amount: 22490,
//         lat: 34.6937,
//         lng: 135.5023
//       },
//       {
//         timestamp: '2024-09-09T14:30:00',
//         store: 'Store 2',
//         amount: 513000,
//         lat: 34.6654,
//         lng: 135.4323
//       }
//     ]
//   },
//   {
//     date: '2024년 09월 08일 목요일',
//     data: [
//       {
//         timestamp: '2024-09-08T10:15:00',
//         store: 'Store 3',
//         amount: 46000,
//         lat: 34.6873,
//         lng: 135.5262
//       }
//     ]
//   },
//   {
//     date: '2024년 10월 08일 목요일',
//     data: [
//       {
//         timestamp: '2024-09-08T10:15:00',
//         store: 'Store 3',
//         amount: 46000,
//         lat: 34.6873,
//         lng: 135.5262
//       }
//     ]
//   }
// ])

const details = ref([])

const selectedStore = ref('')
const showFilterModal = ref(false)
var customDateRange = ref(null)

const periods = [
  { label: '1개월', value: '1개월', days: 30 },
  { label: '3개월', value: '3개월', days: 90 },
  { label: '6개월', value: '6개월', days: 180 },
  { label: '직접입력', value: '직접입력', days: Infinity }
]

const sortOrders = [
  { label: '최신순', value: '최신순' },
  { label: '과거순', value: '과거순' }
]

const selectedPeriod = ref('6개월')
const selectedSortOrder = ref('최신순')

const tempSelectedPeriod = ref(selectedPeriod.value)
const tempSelectedSortOrder = ref(selectedSortOrder.value)

const selectedStyle = {
  backgroundColor: 'white',
  border: '1px solid #03ac8e',
  color: 'black',
  fontWeight: 'bold'
}

const unselectedStyle = {
  backgroundColor: '#f0f0f0',
  border: 'none',
  color: '#666666'
}

const openFilterModal = () => {
  tempSelectedPeriod.value = selectedPeriod.value
  tempSelectedSortOrder.value = selectedSortOrder.value
  showFilterModal.value = true
}

const closeFilterModal = () => {
  showFilterModal.value = false
}

const applyFilter = () => {
  selectedPeriod.value = tempSelectedPeriod.value
  selectedSortOrder.value = tempSelectedSortOrder.value
  closeFilterModal()
}

const parseKoreanDate = (dateString) => {
  if (!dateString) return null // dateString이 null 또는 undefined인 경우 처리
  const matches = dateString.match(/\d+/g)
  if (!matches || matches.length < 3) return null // 매칭되는 결과가 부족한 경우 처리
  const [year, month, day] = matches.map(Number)
  return new Date(year, month - 1, day)
}

const calculatePastDate = (days) => {
  const now = new Date()
  return new Date(now.setDate(now.getDate() - days))
}

const filteredDetails = computed(() => {
  if (!details.value || details.value.length === 0) {
    return []
  }

  if (selectedPeriod.value !== '직접입력') {
    customDateRange = ref(null)
  }

  let startDate, endDate

  if (selectedPeriod.value === '직접입력' && customDateRange.value) {
    const length = customDateRange.value.length
    startDate = new Date(customDateRange.value[0])
    endDate = new Date(customDateRange.value[length - 1])
  } else {
    const periodDays = periods.find((period) => period.value === selectedPeriod.value)?.days || 0
    startDate = calculatePastDate(periodDays)
    endDate = new Date()
  }

  // Filter details based on selected period
  let filtered = details.value.filter((detail) => {
    const detailDate = parseKoreanDate(detail.date)
    return detailDate >= startDate && detailDate <= endDate
  })

  // Filter by selected store from the autocomplete
  if (selectedStore.value) {
    filtered = filtered
      .map((detail) => ({
        ...detail,
        data: detail.data.filter((transaction) => transaction.store.includes(selectedStore.value))
      }))
      .filter((detail) => detail.data.length > 0)
  }

  // Sort the filtered details
  filtered = filtered.sort((a, b) => {
    const dateA = parseKoreanDate(a.date)
    const dateB = parseKoreanDate(b.date)
    return selectedSortOrder.value === '최신순' ? dateB - dateA : dateA - dateB
  })

  filtered = filtered.map((detail) => ({
    ...detail,
    data: detail.data.sort((a, b) => {
      return selectedSortOrder.value === '최신순'
        ? new Date(b.timestamp) - new Date(a.timestamp)
        : new Date(a.timestamp) - new Date(b.timestamp)
    })
  }))

  return filtered
})

const storeNames = computed(() => {
  if (!filteredDetails.value || filteredDetails.value.length === 0) {
    return []
  }
  const stores = new Set()
  filteredDetails.value.forEach((detail) => {
    detail.data.forEach((transaction) => stores.add(transaction.store))
  })
  return Array.from(stores)
})

const showMap = ref(false)
const selectedPlace = ref(null)
const mapCenter = ref({ lat: 0, lng: 0 })

const viewDetails = (transaction) => {
  selectedPlace.value = transaction
  mapCenter.value = { lat: transaction.lat, lng: transaction.lng }
  showMap.value = true
}

const closeMap = () => {
  showMap.value = false
}

const formattedCustomDateRange = computed(() => {
  if (customDateRange.value) {
    const length = customDateRange.value.length
    const start = new Date(customDateRange.value[0])
    const end = new Date(customDateRange.value[length - 1])

    const formatDate = (date) => {
      const year = date.getFullYear()
      const month = ('0' + (date.getMonth() + 1)).slice(-2)
      const day = ('0' + date.getDate()).slice(-2)
      return `${year}년 ${month}월 ${day}일`
    }

    return `${formatDate(start)} ~ ${formatDate(end)}`
  }
  return ''
})

const selectedPeriodDisplay = computed(() => {
  return selectedPeriod.value === '직접입력' && customDateRange.value
    ? formattedCustomDateRange.value
    : selectedPeriod.value
})

onMounted(async () => {
  await getUserTransactions()
})
</script>

<style scoped>
.date {
  font-size: 14px;
  color: #888;
  margin-top: 20px;
  margin-bottom: 10px;
}

.transaction-item {
  display: flex;
  flex-direction: row;
  padding: 10px 0;
  padding-bottom: 20px;
  position: relative;
}

.transaction-amount {
  font-size: 24px;
  font-weight: bold;
  color: #000;
}

.currency {
  font-size: 24px;
  font-weight: bold;
  color: #ff5252;
  margin-right: 4px;
}

.transaction-info {
  margin-top: 2px;
}

.view-location {
  font-family: 'Hana2-L', sans-serif;
  font-size: 16px;
  color: #766666;
  margin-right: 20px;
  display: flex;
  align-items: center;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
  cursor: pointer;
}

.view-location:hover {
  transform: translateX(10px);
  color: #03ac8e;
}

.view-location:hover .mdi-logo {
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

.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}

.search-bar-container {
  display: flex;
  align-items: end;
  flex-direction: column;
  padding: 10px;
  gap: 15px;
}

.search-bar {
  flex-grow: 1;
  max-width: 600px;
}

.search-condition {
  color: #888;
  font-size: 14px;
  display: flex;
  align-items: center;
  cursor: pointer;
}
.v-btn-toggle .v-btn {
  flex-grow: 1;
}

.title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
}

.modal-component {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 10px;
}

.no-data-img {
  height: 200px;
}

.no-data-info {
  font-family: 'Hana2-Light', sans-serif;
  font-size: 24px;
  color: #333;
}
</style>
