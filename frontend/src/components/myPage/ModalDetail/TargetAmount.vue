<template>
  <v-card class="pa-4">
    <v-card-text>
      <v-switch
        v-model="enableTargetCurrency"
        label="목표 금액 설정"
        inset
        class="mb-4"
        :class="enableTargetCurrency ? 'info--text' : 'red--text'"
        :color="enableTargetCurrency ? 'success' : 'red'"
      ></v-switch>

      <!-- Target Currency Section -->
      <div class="target-currency-section mt-4" :class="{ disabled: !enableTargetCurrency }">
        <div class="d-flex justify-space-between align-center">
          <div class="d-flex align-center">
            <v-avatar
              size="32"
              class="avatar mr-2"
              :image="`/assets/img/${props.info.code}.gif`"
            ></v-avatar>
            <span class="rate-text">{{ props.info.code }}</span>
          </div>
          <input
            type="text"
            v-model="targetCurrency"
            class="rate-value-input"
            placeholder="목표 금액을 입력하세요"
            :disabled="!enableTargetCurrency"
          />
        </div>
        <div class="divider"></div>
        <div class="grey--text caption text-end">원화: {{ convertedCurrency }} 원</div>
      </div>
    </v-card-text>

    <v-card-actions>
      <v-btn color="primary" block @click="submitTargetCurrency">설정하기</v-btn>
    </v-card-actions>
  </v-card>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import alertModal from '@/modal/alertModal.vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const showDialog = ref(false)
const notificationMessage = ref('')

// 현재 환율
const currentExchangeRate = ref(0)

// 목표 금액
const targetCurrency = ref('')

// 목표 금액 설정 여부
const enableTargetCurrency = ref(false)

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

const submitTargetCurrency = async () => {
  try {
    await axios
      .post('http://34.47.69.197:8081/api/user/manage/possession/target', {
        memberId: userId,
        currencyCode: props.info.code,
        targetAmount: targetCurrency.value,
        isEnabled: enableTargetCurrency.value
      })
      .then((res) => {
        if (res.data.code === 200) {
          showNotification('목표 금액이 설정되었습니다.', () => {
            window.location.reload()
          })
        } else {
          showNotification('목표 금액 설정에 실패했습니다.', () => {
            window.location.reload()
          })
        }
      })
  } catch (err) {
    showNotification('목표 금액 설정에 실패했습니다.')
    window.location.reload()
  }
}

const getTargetCurrency = async () => {
  try {
    const res = await axios.get(
      `http://34.47.69.197:8081/api/user/manage/possession/target/${userId}/${props.info.code}`
    )
    targetCurrency.value = res.data.data
    if (targetCurrency.value) {
      enableTargetCurrency.value = true
    }
  } catch (err) {
    console.log(err)
  }
}

const convertedCurrency = computed(() => {
  const amount = parseFloat(targetCurrency.value) || 0

  if (props.info.code === 'JPY') {
    return ((amount * currentExchangeRate.value) / 100).toFixed(2)
  }

  return (amount * currentExchangeRate.value).toFixed(2)
})

onMounted(async () => {
  await getExchangeRate()
  await getTargetCurrency()
})
</script>

<style scoped>
.target-currency-section {
  margin-top: 16px;
  transition: opacity 0.3s ease; /* Smooth transition when disabling */
}

/* Styling to simulate a disabled UI */
.target-currency-section.disabled {
  opacity: 0.5; /* Lower opacity when disabled */
  pointer-events: none; /* Disable interactions */
}

.avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.target-currency-section .rate-value-input {
  width: 250px;
  padding: 4px;
  font-size: 18px;
  text-align: right;
}

.rate-text {
  font-size: 24px;
  font-weight: bold;
  margin-right: 8px;
}

.divider {
  height: 1px;
  background-color: #e0e0e0;
  margin-top: 10px;
  margin-bottom: 0;
}

.caption {
  font-size: 12px;
  color: #999;
}
</style>
