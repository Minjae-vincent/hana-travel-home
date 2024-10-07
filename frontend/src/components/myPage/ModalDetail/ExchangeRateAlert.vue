<template>
  <v-card class="pa-4">
    <v-card-text>
      <div>
        <v-btn-toggle v-model="selectedNotification" class="mb-4" mandatory borderless>
          <v-btn
            value="more"
            :style="selectedNotification === 'more' ? selectedStyle : unselectedStyle"
          >
            환율 알림(이상)
          </v-btn>
          <v-btn
            value="less"
            :style="selectedNotification === 'less' ? selectedStyle : unselectedStyle"
          >
            환율 알림(이하)
          </v-btn>
        </v-btn-toggle>

        <div v-if="selectedNotification === 'more'">
          <v-switch
            v-model="enableTargetCurrencyMore"
            label="알림 금액 설정"
            inset
            :class="enableTargetCurrencyMore ? 'info--text' : 'red--text'"
            :color="enableTargetCurrencyMore ? 'success' : 'red'"
          ></v-switch>
          <div class="target-currency-more" :class="{ disabled: !enableTargetCurrencyMore }">
            <div class="target-currency-container">
              <div class="target-info">
                <div class="country-info">
                  <v-avatar
                    size="24"
                    class="avatar mr-2"
                    :image="`/assets/img/${props.info.code}.gif`"
                  ></v-avatar>
                  <span class="rate-text">{{ props.info.code }}</span>
                </div>
                <div class="alert-notice">
                  <span style="font-weight: bold; color: black; font-size: 20px">{{
                    targetCurrencyMore
                  }}</span>
                  이상 일 때 알림
                </div>
              </div>
              <!-- <div class="delete-alert" @click="viewDetails(transaction)">
                삭제하기
                <v-icon class="mdi-logo">mdi-trash-can</v-icon>
              </div> -->
            </div>
            <v-btn color="primary" @click="openModal('이상')">알림 설정</v-btn>
          </div>
        </div>
        <div v-else-if="selectedNotification === 'less'">
          <v-switch
            v-model="enableTargetCurrencyLess"
            label="알림 금액 설정"
            inset
            :class="enableTargetCurrencyLess ? 'info--text' : 'red--text'"
            :color="enableTargetCurrencyLess ? 'success' : 'red'"
          ></v-switch>
          <div class="target-currency-less" :class="{ disabled: !enableTargetCurrencyLess }">
            <div class="target-currency-container">
              <div class="target-info">
                <div class="country-info">
                  <v-avatar
                    size="24"
                    class="avatar mr-2"
                    :image="`/assets/img/${props.info.code}.gif`"
                  ></v-avatar>
                  <span class="rate-text">{{ props.info.code }}</span>
                </div>
                <div class="alert-notice">
                  <span style="font-weight: bold; color: black; font-size: 20px">{{
                    targetCurrencyLess
                  }}</span>
                  이하 일 때 알림
                </div>
              </div>
              <!-- <div class="delete-alert" @click="viewDetails(transaction)">
                삭제하기
                <v-icon class="mdi-logo">mdi-trash-can</v-icon>
              </div> -->
            </div>
            <v-btn color="primary" @click="openModal('이하')">알림 설정</v-btn>
          </div>
        </div>

        <v-dialog v-model="isDialogVisible" max-width="400">
          <v-card class="pa-4">
            <v-card-text>
              <div class="d-flex justify-space-between align-center mb-0">
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
                <div class="text-right">
                  <span v-if="!editing" class="rate-value" @click="startEditing">
                    {{
                      editedRate
                        ? editedRate
                        : conditionType === '이상'
                          ? upperCurrency
                          : lowCurrency
                    }}
                  </span>
                  <input
                    v-else
                    type="text"
                    :value="editedRate"
                    class="rate-value-input"
                    @blur="saveRate"
                    @keydown.enter="saveRate"
                    @input="updateRate"
                  />
                </div>
              </div>
              <div class="divider"></div>

              <div class="grey--text caption text-end">
                {{ priceMessage }}
                <span :style="{ color: priceDifference > 0 ? 'blue' : 'red' }">
                  <span v-if="priceDifference > 0 || priceDifference < 0">
                    {{ priceDifference > 0 ? '-' : '+'
                    }}{{ Math.abs(priceDifference).toFixed(2) }}원
                  </span>
                </span>
              </div>

              <div class="text-h6 text-start notice">
                <span
                  :style="{ color: conditionType === '이상' ? 'red' : 'blue' }"
                  class="toggle-condition"
                >
                  {{ conditionType }}
                </span>
                일 때 알림을 받을래요.
              </div>
            </v-card-text>

            <v-card-actions>
              <v-btn color="primary" block @click="configAmount(conditionType)">설정하기</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" block @click="confirmAlert()">설정하기</v-btn>
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

const authStore = useAuthStore()
const userId = authStore.userId
let callback = null

const selectedNotification = ref('more')
const isDialogVisible = ref(false)

const editing = ref(false)
const editedRate = ref('')

const conditionType = ref('이상')

const lowCurrency = ref('0')
const upperCurrency = ref('0')
const nowCurrency = ref('0')

const enableTargetCurrencyMore = ref(false)
const enableTargetCurrencyLess = ref(false)

const targetCurrencyMore = ref('0')
const targetCurrencyLess = ref('0')

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

const configAmount = (type) => {
  if (type === '이상') {
    if (editedRate.value == '') {
      editedRate.value = upperCurrency.value
    }
    targetCurrencyMore.value = editedRate.value
  } else {
    if (editedRate.value == '') {
      editedRate.value = lowCurrency.value
    }
    targetCurrencyLess.value = editedRate.value
  }
  editedRate.value = ''
  isDialogVisible.value = false
}

// 여기 밑에 추가적인 api 연동 필요
const confirmAlert = async () => {
  if (selectedNotification.value === 'more') {
    await axios
      .post(
        `http://34.47.69.197:8081/api/user/manage/possession/alert/${userId}/${props.info.code}`,
        {
          conditionType: '이상',
          alertFlag: enableTargetCurrencyMore.value,
          targetCurrency: targetCurrencyMore.value
        }
      )
      .then((res) => {
        if (res.data.code === 200) {
          showNotification('환율 알림이 설정되었습니다.', () => {
            window.location.reload()
          })
        }
      })
  } else {
    await axios
      .post(
        `http://34.47.69.197:8081/api/user/manage/possession/alert/${userId}/${props.info.code}`,
        {
          conditionType: '이하',
          alertFlag: enableTargetCurrencyLess.value,
          targetCurrency: targetCurrencyLess.value
        }
      )
      .then((res) => {
        if (res.data.code === 200) {
          showNotification('환율 알림이 설정되었습니다.', () => {
            window.location.reload()
          })
        }
      })
  }
}

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

const openModal = (type) => {
  conditionType.value = type
  isDialogVisible.value = true
}

const saveRate = () => {
  editing.value = false
}

const startEditing = () => {
  editing.value = true
}

const updateRate = (event) => {
  editedRate.value = event.target.value
}

const priceDifference = computed(() => parseFloat(nowCurrency.value) - parseFloat(editedRate.value))

const priceMessage = computed(() => {
  if (conditionType.value === '이상') {
    if (priceDifference.value > 0) {
      return '현재가'
    } else if (priceDifference.value < 0) {
      return '현재가'
    } else {
      return '최근 3개월 기준 최고가'
    }
  } else {
    if (priceDifference.value > 0) {
      return '현재가'
    } else if (priceDifference.value < 0) {
      return '현재가'
    } else {
      return '최근 3개월 기준 최저가'
    }
  }
})

const getCurrentAlertInfo = async () => {
  try {
    await axios
      .get(`http://34.47.69.197:8081/api/user/manage/possession/alert/${userId}/${props.info.code}`)
      .then((res) => {
        if (res.data.code === 200) {
          targetCurrencyMore.value = res.data.data.alertUpAmount
          targetCurrencyLess.value = res.data.data.alertDownAmount
          lowCurrency.value = res.data.data.minAmountThreeMonth
          upperCurrency.value = res.data.data.maxAmountThreeMonth
          nowCurrency.value = res.data.data.exchangeRate
          if (res.data.data.alertUpFlag === 'N') {
            enableTargetCurrencyMore.value = false
          } else {
            enableTargetCurrencyMore.value = true
          }
          if (res.data.data.alertDownFlag === 'N') {
            enableTargetCurrencyLess.value = false
          } else {
            enableTargetCurrencyLess.value = true
          }
        }
      })
  } catch (error) {
    console.error(error)
  }
}

onMounted(async () => {
  await getCurrentAlertInfo()
})
</script>

<style scoped>
.d-flex {
  display: flex;
  align-items: center;
}

.justify-space-between {
  justify-content: space-between;
}

.avatar {
  border: 2px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.rate-text {
  font-size: 20px;
  font-weight: bold;
  margin-right: 8px;
}

.rate-value {
  font-size: 24px;
  font-weight: bold;
  color: #009688;
  cursor: pointer;
}

.rate-value-input {
  font-size: 24px;
  font-weight: bold;
  text-align: right;
  width: 100px;
}

.caption {
  font-size: 12px;
  color: #999;
}

.divider {
  height: 1px;
  background-color: #e0e0e0;
  margin-top: 10px;
  margin-bottom: 0;
}

.notice {
  color: rgb(76, 75, 75);
}

.toggle-condition {
  border: 1px solid #ddd;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.toggle-condition:hover {
  background-color: #f0f0f0;
}

.target-currency-more.disabled,
.target-currency-less.disabled {
  opacity: 0.5;
  pointer-events: none;
}

.target-currency-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f0f0f0;
  padding: 16px;
  padding-right: 20px;
  padding-left: 20px;
  border-radius: 10px;
  width: 100%;
}

.country-info {
  display: flex;
  align-items: center;
}

.target-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-direction: column;
}

.target-currency-more,
.target-currency-less {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
  gap: 20px;
}

/* .delete-alert {
  font-family: 'Hana2-L', sans-serif;
  font-size: 16px;
  color: #766666;
  margin-right: 10px;
  display: flex;
  align-items: center;
  transition:
    transform 0.3s ease,
    color 0.3s ease;
  cursor: pointer;
}
.delete-alert:hover {
  transform: translateX(10px);
  color: #03ac8e;
}

.delete-alert:hover .mdi-logo {
  color: #03ac8e;
} */

.alert-notice {
  font-size: 18px;
  color: #666666;
}
</style>
