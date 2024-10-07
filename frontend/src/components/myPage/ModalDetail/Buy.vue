<template>
  <v-card class="pa-4">
    <v-card-text>
      <div class="exchange-rate-section">
        <span class="rate">{{ props.info.code }} {{ currentExchangeRate.toLocaleString() }}원</span>
      </div>

      <div class="currency-input-section py-4">
        <div class="d-flex justify-space-between align-center">
          <div class="nation-info">
            <v-avatar
              size="32"
              class="avatar mr-2"
              :image="`/assets/img/${props.info.code}.gif`"
            ></v-avatar>
            <span style="font-family: 'Hana2-Light'">{{ props.info.code }}</span>
          </div>
          <div class="text-right">
            <div class="custom-input-container">
              <input
                type="number"
                v-model.number="foreignCurrency"
                :max="enableAmount"
                min="0"
                placeholder="외화로 입력"
                class="custom-input"
              />
            </div>
          </div>
        </div>

        <div class="divider"></div>

        <div class="guide-text mt-2 d-flex justify-space-between">
          <div class="grey--text caption">보유 {{ props.info.code }} {{ props.info.balance }}</div>
          <div class="grey--text caption text-right">
            충전 가능 {{ props.info.code }}
            <span>
              {{ enableAmount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
            </span>
          </div>
        </div>
      </div>

      <div class="bank-selection-section py-4">
        <div class="d-flex justify-space-between align-center">
          <div class="d-flex align-center">
            <v-icon class="mr-2">mdi-bank</v-icon>
            <div class="select-container">
              <select ref="bankSelect" v-model="selectedBank" class="custom-select">
                <option v-for="bank in banks" :key="bank" :value="bank">
                  {{ bank }}
                </option>
              </select>
              <v-icon class="select-icon" @click="openSelect">mdi-chevron-down</v-icon>
            </div>
          </div>
          <div class="custom-input-container">
            <input
              type="number"
              v-model.number="localCurrency"
              :max="enableAmountKRW"
              min="0"
              placeholder="원화로 입력"
              class="custom-input"
            />
          </div>
        </div>
      </div>

      <v-divider></v-divider>
      <div class="info-section mt-4">
        <v-alert type="info" elevation="2">
          <strong>알려드립니다!</strong>
          <p class="mb-0">
            트래블로그 서비스로 외화 환전 시 <strong>무료환전 혜택</strong>을 제공합니다.
          </p>
          <p class="mb-0">- 상시 무료환전 종목: 미국 USD, 일본 JPY, 유럽연합 EUR, 영국 GBP</p>
          <p>무료환전 이벤트 54종 (2024년 12월 31일까지)</p>
        </v-alert>
      </div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="primary" block @click="chargeCurrency()">충전하기</v-btn>
    </v-card-actions>
  </v-card>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import axios from 'axios'
import { onMounted, ref, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRoute, useRouter } from 'vue-router'
import alertModal from '@/modal/alertModal.vue'

const showDialog = ref(false)
const notificationMessage = ref('')
const authStore = useAuthStore()
const userId = authStore.userId
const foreignCurrency = ref(0)
const localCurrency = ref(0)
const selectedBank = ref('하나은행')
const banks = ['하나은행']
const bankSelect = ref(null)
const currentExchangeRate = ref(0)
const enableAmount = ref(0)
const enableAmountKRW = ref(0)
let callback = null

const props = defineProps({
  info: Object
})

const handleClose = () => {
  if (callback) {
    callback()
  }
  showDialog.value = false
}

const showNotification = (message, cb = null) => {
  notificationMessage.value = message
  callback = cb
  showDialog.value = true
}

const openSelect = () => {
  if (bankSelect.value) {
    bankSelect.value.focus()
    bankSelect.value.click()
  }
}

const getExchangeRate = async () => {
  try {
    const res = await axios.get(
      `http://34.47.69.197:8081/api/user/exchange-rate/now/${props.info.code}`
    )
    currentExchangeRate.value = res.data.data
  } catch (err) {
    console.log(err)
  }
}

const chargeCurrency = async () => {
  try {
    await axios
      .post('http://34.47.69.197:8081/api/user/manage/possession/charge', {
        memberId: userId,
        foreignAmount: foreignCurrency.value,
        countryCode: props.info.code,
        localAmount: localCurrency.value
      })
      .then((res) => {
        console.log(res)
        if (res.data.code === 200) {
          showNotification('충전이 완료되었습니다.', () => {
            window.location.reload()
          })
        } else {
          showNotification('충전에 실패했습니다.', () => {
            window.location.reload()
          })
        }
      })
      .catch((err) => {
        console.error(err)
      })
  } catch (err) {
    console.log(err)
  }
}

// 환율 계산 함수
const calculateLocalCurrency = (foreignAmount) => {
  if (props.info.code === 'JPY') {
    return Math.round(foreignAmount * (currentExchangeRate.value / 100))
  } else {
    return Math.round(foreignAmount * currentExchangeRate.value)
  }
}

const calculateForeignCurrency = (localAmount) => {
  if (props.info.code === 'JPY') {
    return Math.round((localAmount / (currentExchangeRate.value / 100)) * 100) / 100
  } else {
    return Math.round((localAmount / currentExchangeRate.value) * 100) / 100
  }
}

onMounted(async () => {
  await getExchangeRate()

  if (props.info.code === 'JPY') {
    enableAmount.value = Math.floor(2000000 / (currentExchangeRate.value / 100))
    enableAmountKRW.value = enableAmount.value * (currentExchangeRate.value / 100)
  } else {
    enableAmount.value = Math.floor(2000000 / currentExchangeRate.value)
    enableAmountKRW.value = enableAmount.value * currentExchangeRate.value
  }
})

let isUpdatingForeign = false
let isUpdatingLocal = false

watch(foreignCurrency, (newValue) => {
  if (isUpdatingLocal) return
  isUpdatingForeign = true

  if (newValue > enableAmount.value) {
    foreignCurrency.value = enableAmount.value
  } else if (newValue < 0) {
    foreignCurrency.value = 0
  }

  localCurrency.value = calculateLocalCurrency(foreignCurrency.value)
  isUpdatingForeign = false
})

watch(localCurrency, (newValue) => {
  if (isUpdatingForeign) return
  isUpdatingLocal = true

  if (newValue > enableAmountKRW.value) {
    localCurrency.value = enableAmountKRW.value
  } else if (newValue < 0) {
    localCurrency.value = 0
  }

  foreignCurrency.value = calculateForeignCurrency(localCurrency.value)
  isUpdatingLocal = false
})
</script>

<style scoped>
.exchange-rate-section {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-family: 'Hana2-B';
  margin-bottom: 24px;
}
.currency-input-section,
.bank-selection-section,
.switch-section {
  font-size: 16px;
}
.guide-text .grey--text {
  font-size: 12px;
  line-height: 1.4;
}
.info-section p {
  font-size: 14px;
  line-height: 1.5;
}
.v-alert {
  font-size: 14px;
}

.custom-input {
  width: 100%;
  font-size: 16px;
  text-align: right;
  outline: none;
}

.custom-input::placeholder {
  text-align: right;
  color: #999;
}

.custom-input-container {
  width: 300px;
}

.divider {
  height: 1px;
  background-color: #e0e0e0;
  margin: 16px 0;
  margin-top: 10px;
}

.custom-select {
  width: auto;
  padding: 8px;
  font-size: 18px;
  font-family: 'Hana2-Light';
  border: none;
  appearance: none;
}

.custom-select:focus {
  outline: none;
}

.select-icon {
  cursor: pointer;
  pointer-events: all;
}

.v-field__outline {
  border: none;
}
.avatar {
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin-right: 10px;
}
.nation-info {
  display: flex;
  align-items: center;
}
</style>
