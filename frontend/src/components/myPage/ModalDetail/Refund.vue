<template>
  <v-card class="pa-4">
    <v-card-text>
      <!-- Exchange Rate Section -->
      <div class="exchange-rate-section d-flex align-center mb-4">
        <v-avatar
          size="32"
          class="avatar mr-2"
          :image="`/assets/img/${props.info.code}.gif`"
        ></v-avatar>
        <span class="rate">{{ props.info.code }} = {{ currentExchangeRate }}원</span>
        <v-icon small class="ml-1 grey--text">mdi-information-outline</v-icon>
        <v-chip label color="green lighten-4" class="ml-2">1% 수수료 있음</v-chip>
      </div>

      <div class="currency-input-section py-4">
        <div class="d-flex justify-space-between align-center">
          <div class="custom-input-container">
            <input
              type="number"
              v-model="foreignCurrency"
              placeholder="얼마를 바꿀까요?"
              class="custom-input"
            />
          </div>
        </div>
        <div class="divider"></div>

        <div class="guide-text mt-2 d-flex justify-space-between">
          <div class="grey--text caption right-align">
            내가 보유한 {{ props.info.code }}
            {{ props.info.balance.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}
          </div>
        </div>
      </div>

      <v-alert type="info" elevation="2" dense class="mt-4">
        환급 시 환급수수료(1%)가 부과되니 신중히 거래해 주세요.
      </v-alert>
    </v-card-text>

    <v-card-actions>
      <v-btn :disabled="!canConvert" color="primary" block @click="refundCurrency()"
        >환급하기</v-btn
      >
    </v-card-actions>
  </v-card>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import alertModal from '@/modal/alertModal.vue'
import axios from 'axios'

const showDialog = ref(false)
const notificationMessage = ref('')
const foreignCurrency = ref('')
const canConvert = ref(false)
const currentExchangeRate = ref(0)
const authStore = useAuthStore()
const userId = authStore.userId
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

const refundCurrency = async () => {
  try {
    await axios
      .post('http://34.47.69.197:8081/api/user/manage/possession/refund', {
        countryCode: props.info.code,
        foreignAmount: foreignCurrency.value,
        localAmount: (foreignCurrency.value * currentExchangeRate.value * 0.99).toFixed(0),
        memberId: userId
      })
      .then((res) => {
        if (res.data.code === 200) {
          showNotification('환급이 완료되었습니다.', () => {
            window.location.reload()
          })
        } else {
          showNotification('환급에 실패했습니다. 다시 시도해 주세요.', () => {
            window.location.reload()
          })
        }
      })
  } catch (err) {
    console.log(err)
  }
}

watch(foreignCurrency, (newVal) => {
  if (newVal > 0 && newVal <= props.info.balance) {
    canConvert.value = true
  } else {
    canConvert.value = false
    foreignCurrency.value = 0
  }
})

onMounted(async () => {
  await getExchangeRate()
})
</script>

<style scoped>
/* Exchange Rate Section */
.exchange-rate-section {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-family: 'Hana2-B';
  margin-bottom: 16px;
}

/* Currency Input Section */
.currency-input-section {
  font-size: 16px;
  padding-bottom: 16px;
}

.right-align {
  margin-left: auto; /* Aligns the text to the right */
}

.custom-input-container {
  width: 100%;
}

.custom-input {
  width: 100%;
  font-size: 18px;
  padding: 10px;
  padding-bottom: 0;
  text-align: right;
  outline: none;
}

.custom-input::placeholder {
  color: #999;
  font-size: 18px;
  text-align: right;
}

/* Guide Text */
.guide-text {
  font-size: 14px;
  line-height: 1.5;
  color: #666;
}

/* Bank Selection Section */
.bank-selection-section {
  font-size: 16px;
  padding-top: 20px;
  padding-bottom: 20px;
}

.select-container {
  position: relative;
  display: flex;
  align-items: center;
}

.custom-select {
  width: 100%;
  padding: 10px;
  font-size: 18px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  background-color: #f9f9f9;
  appearance: none;
}

.custom-select:focus {
  outline: none;
  border-color: #666;
}

.select-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  cursor: pointer;
  color: #666;
}

.info-section p {
  font-size: 14px;
  line-height: 1.5;
}

.v-alert {
  font-size: 14px;
  color: #666;
  background-color: #f9f9f9;
  padding: 10px;
}

.avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  margin-right: 8px;
}
.divider {
  height: 1px;
  background-color: #e0e0e0;
  margin-top: 10px;
  margin-bottom: 0;
}
</style>
