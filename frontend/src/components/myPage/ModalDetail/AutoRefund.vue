<template>
  <v-container>
    <v-card class="pa-4">
      <v-switch
        v-model="enableAutoRefund"
        label="자동 환급 활성화"
        inset
        :class="enableAutoRefund ? 'info--text' : 'red--text'"
        :color="enableAutoRefund ? 'success' : 'red'"
      ></v-switch>
      <v-card-text class="auto-buy-class" :class="{ disabled: !enableAutoRefund }">
        <div class="exchange-rate-box" @click="openBottomSheet">
          <div class="label-text grey--text mb-2">기준환율 (매매기준율)</div>

          <div class="d-flex align-center justify-space-between">
            <div class="d-flex align-center">
              <v-avatar
                size="32"
                class="avatar mr-2"
                :image="`/assets/img/${props.info.code}.gif`"
              ></v-avatar>
              <span v-if="props.info.code === 'JPY'" class="rate-text"
                >{{ props.info.code }} 100</span
              >
              <span v-else class="rate-text">{{ props.info.code }}</span>
            </div>

            <div class="d-flex align-center">
              <span class="highlight-text mr-2">
                {{ selectedRate ? selectedRate : rates[0].value }} 원 이상일 때
              </span>
              <v-icon>mdi-chevron-down</v-icon>
            </div>
          </div>
        </div>

        <div class="exchange-rate-box" @click="openRechargeOptions">
          <div class="label-text grey--text mb-2">자동으로</div>
          <div class="d-flex align-center justify-space-between">
            <div class="d-flex align-center">
              <span class="highlight-text">
                {{ selectedRechargeAmount ? selectedRechargeAmount : rechargeOptions[0] }}</span
              >
            </div>
            <v-icon>mdi-chevron-down</v-icon>
          </div>
        </div>

        <div class="exchange-rate-box" @click="openPeriodSelection">
          <div class="label-text grey--text mb-2">기간선택</div>
          <div class="d-flex align-center justify-space-between">
            <span class="highlight-text">
              {{ selectedPeriod ? selectedPeriod : '한 번에 환급' }}
            </span>
            <v-icon>mdi-chevron-down</v-icon>
          </div>
        </div>

        <div class="exchange-rate-box">
          <div class="label-text grey--text mb-2">환급방식</div>
          <div class="d-flex align-center justify-space-between">
            <span class="highlight-text"> 연결계좌에서 송금하기 </span>
          </div>
        </div>

        <v-bottom-sheet v-model="periodSheet">
          <v-sheet>
            <v-toolbar flat>
              <v-toolbar-title class="headline">환급 기간 선택</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn icon @click="periodSheet = false">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-toolbar>

            <v-list>
              <v-list-item-group v-model="selectedPeriod">
                <v-list-item
                  v-for="(option, index) in periodOptions"
                  :key="index"
                  @click="selectPeriod(option)"
                >
                  <v-list-item-content>
                    <div v-if="option === '도달할 때마다 환급'">
                      <v-list-item-title class="period-option">
                        {{ option }}
                        <span class="sub-text">하루에 한 번만 환급</span>
                      </v-list-item-title>
                    </div>
                    <div v-else>
                      <v-list-item-title class="period-option">
                        {{ option }}
                      </v-list-item-title>
                    </div>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-sheet>
        </v-bottom-sheet>

        <v-bottom-sheet v-model="rechargeSheet">
          <v-sheet>
            <v-toolbar flat>
              <v-toolbar-title class="headline">얼마를 환급할까요?</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn icon @click="rechargeSheet = false">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-toolbar>

            <v-list>
              <v-list-item-group v-model="selectedRechargeAmount">
                <v-list-item
                  v-for="(option, index) in rechargeOptions"
                  :key="index"
                  @click="selectRechargeAmount(option)"
                >
                  <v-list-item-content>
                    <v-list-item-title class="recharge-option">
                      {{ option }}
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-sheet>
        </v-bottom-sheet>

        <v-bottom-sheet v-model="bottomSheet">
          <v-sheet>
            <v-toolbar flat>
              <v-toolbar-title class="headline">환율 빠른 선택</v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn icon @click="bottomSheet = false">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-toolbar>

            <v-list>
              <v-list-item-group v-model="selectedRate">
                <v-list-item v-for="(rate, index) in rates" :key="index" @click="selectRate(rate)">
                  <v-list-item-content>
                    <v-list-item-title class="rate">
                      {{ rate.value }}
                      <span class="sub-text">{{ rate.description }}</span>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>

              <v-list-item @click="openDirectInput">
                <v-list-item-content>
                  <v-list-item-title>직접입력</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-sheet>
        </v-bottom-sheet>

        <v-dialog v-model="directInputDialog" persistent max-width="400">
          <v-card>
            <v-card-title>
              <span class="headline">환율 직접 입력</span>
            </v-card-title>
            <v-card-text>
              <v-text-field
                label="환율 직접 입력"
                v-model="customRate"
                type="number"
                placeholder="자동 환급받고자 하는 환율을 입력해주세요."
              ></v-text-field>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="primary" text @click="submitCustomRate">확인</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-card-text>
      <v-card-actions>
        <v-btn color="primary" block @click="setAutoRefund()">저장하기</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import alertModal from '@/modal/alertModal.vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const showDialog = ref(false)
const notificationMessage = ref('')
const enableAutoRefund = ref(false)

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

const bottomSheet = ref(false)
const rechargeSheet = ref(false)
const directInputDialog = ref(false)
const customRate = ref('')
const selectedRate = ref(null)
const selectedRechargeAmount = ref(null)
const periodSheet = ref(false)
const selectedPeriod = ref(null)

const currentRate = ref('')

const rates = ref([
  { value: '852.26', description: '최근 3개월 최고가' },
  { value: '905.79', description: '최근 1개월 최고가' },
  { value: '910.42', description: '최근 1주일 최고가' }
])

const rechargeOptions = ['전액 환급', '50% 환급', '25% 환급']

const periodOptions = ['한 번만 환급', '도달할 때마다 환급']

const getCurrencyInfo = async () => {
  try {
    await axios
      .get(`http://34.47.69.197:8081/api/user/manage/possession/auto-refund/${props.info.code}`)
      .then((response) => {
        rates.value[0].value = response.data.data.minExchangeRate3m
        rates.value[1].value = response.data.data.minExchangeRate1m
        rates.value[2].value = response.data.data.minExchangeRate1w
        currentRate.value = response.data.data.currentExchangeRate
      })
  } catch (error) {
    console.error('error', error)
  }
}

const setAutoRefund = async () => {
  selectedPeriod.value = selectedPeriod.value ? selectedPeriod.value : '한 번만 충전'
  let realPeriod = ''

  if (selectedPeriod.value === '한 번만 충전') {
    realPeriod = 'ONCE'
  } else {
    realPeriod = 'ALL_TIME'
  }

  let realAmount = ''

  selectedRechargeAmount.value = selectedRechargeAmount.value
    ? selectedRechargeAmount.value
    : '전액 환급'

  if (selectedRechargeAmount.value === '전액 환급') {
    realAmount = 'ALL'
  } else if (selectedRechargeAmount.value === '50% 환급') {
    realAmount = 'HALF'
  } else {
    realAmount = 'QUARTER'
  }

  try {
    await axios
      .post(`http://34.47.69.197:8081/api/user/manage/possession/auto-refund/${props.info.code}`, {
        memberId: userId,
        enableAuto: enableAutoRefund.value,
        exchangeRate: selectedRate.value ? selectedRate.value : rates.value[0].value,
        refundAmount: realAmount,
        period: realPeriod
      })
      .then((response) => {
        if (response.data.code === 200) {
          showNotification('자동 환급 설정이 저장되었습니다.', () => {
            window.location.reload()
          })
        } else {
          showNotification('자동 환급 설정 저장에 실패했습니다.', () => {
            window.location.reload()
          })
        }
      })
  } catch (error) {
    console.error('error', error)
  }
}

const getAutoRefundInfo = async () => {
  try {
    await axios
      .get(
        `http://34.47.69.197:8081/api/user/manage/possession/auto-refund/${props.info.code}/${userId}`
      )
      .then((response) => {
        enableAutoRefund.value = response.data.data.enableAuto
        selectedRate.value = response.data.data.exchangeRate

        if (response.data.data.refundAmount === 'ALL') {
          selectedRechargeAmount.value = rechargeOptions[0]
        } else if (response.data.data.refundAmount === 'HALF') {
          selectedRechargeAmount.value = rechargeOptions[1]
        } else {
          selectedRechargeAmount.value = rechargeOptions[2]
        }

        if (response.data.data.period === 'ONCE') {
          selectedPeriod.value = periodOptions[0]
        } else {
          selectedPeriod.value = periodOptions[1]
        }
      })
  } catch (error) {
    console.error('error', error)
  }
}

// Functions
const openBottomSheet = () => {
  bottomSheet.value = true
}

const openPeriodSelection = () => {
  periodSheet.value = true
}

const openRechargeOptions = () => {
  rechargeSheet.value = true
}

const selectRate = (rate) => {
  selectedRate.value = rate.value
  bottomSheet.value = false
}

const selectRechargeAmount = (option) => {
  selectedRechargeAmount.value = option
  rechargeSheet.value = false
}

const openDirectInput = () => {
  bottomSheet.value = false
  directInputDialog.value = true
}

const selectPeriod = (option) => {
  selectedPeriod.value = option
  periodSheet.value = false // Close sheet after selection
}

const submitCustomRate = () => {
  if (customRate.value) {
    selectedRate.value = { value: customRate.value, description: '직접 입력' }
    selectedRate.value = customRate.value
    directInputDialog.value = false
  }
}

onMounted(async () => {
  await getCurrencyInfo()
  await getAutoRefundInfo()
})
</script>

<style scoped>
.exchange-rate-box {
  background-color: #f5f5f5;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  cursor: pointer;
  margin-bottom: 16px;
}

.label-text {
  font-size: 12px;
  font-weight: bold;
  color: #999;
}

.rate-text {
  font-size: 16px;
  font-weight: bold;
}

.highlight-text {
  font-size: 20px;
  color: #009688;
  font-weight: bold;
}

.v-icon {
  font-size: 20px;
  color: #666;
}

.headline {
  font-weight: bold;
}

.rate {
  font-size: 20px;
  font-weight: bold;
}

.sub-text {
  font-size: 14px;
  color: #999;
}

.recharge-option {
  font-size: 20px;
  font-weight: bold;
}

.sub-text2 {
  font-size: 14px;
  color: black;
}
.period-option {
  font-size: 20px;
  font-weight: bold;
}

.avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.auto-buy-class.disabled {
  opacity: 0.5;
  pointer-events: none;
}
</style>
