<template>
  <v-dialog v-model="props.isDialogVisible" max-width="600px">
    <v-card>
      <v-card-title class="headline">외화 정보 자세히보기 </v-card-title>

      <v-card-text>
        <!-- Select box to switch between screens -->
        <v-select
          v-model="selectedScreen"
          :items="screenOptions"
          label="메뉴 선택"
          outlined
          bg-color="white"
        ></v-select>

        <!-- Dynamically load components based on the selected option -->
        <History v-if="selectedScreen === '내역 조회'" :info="props.details"> </History>
        <Buy v-if="selectedScreen === '충전'" :info="props.details" />
        <Refund v-if="selectedScreen === '환급'" :info="props.details" />
        <AutoBuy v-if="selectedScreen === '자동 구매'" :info="props.details" />
        <AutoRefund v-if="selectedScreen === '자동 환급'" :info="props.details" />
        <ExchangeRateAlert v-if="selectedScreen === '목표 환율 알림'" :info="props.details" />
        <TargetAmout v-if="selectedScreen === '목표 금액 설정'" :info="props.details" />
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" text @click="closeModal">닫기</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { defineProps, ref, watch, defineEmits, onMounted } from 'vue'
import History from '/src/components/myPage/ModalDetail/History.vue'
import Buy from '/src/components/myPage/ModalDetail/Buy.vue'
import Refund from '/src/components/myPage/ModalDetail/Refund.vue'
import AutoBuy from '/src/components/myPage/ModalDetail/AutoBuy.vue'
import AutoRefund from '/src/components/myPage/ModalDetail/AutoRefund.vue'
import ExchangeRateAlert from '/src/components/myPage/ModalDetail/ExchangeRateAlert.vue'
import TargetAmout from '/src/components/myPage/ModalDetail/TargetAmount.vue'

// Define props
const props = defineProps({
  details: Object,
  isDialogVisible: Boolean
})

const emit = defineEmits(['close'])

const selectedScreen = ref('내역 조회')
const screenOptions = [
  '내역 조회',
  '충전',
  '환급',
  '목표 금액 설정',
  '목표 환율 알림',
  '자동 구매',
  '자동 환급'
]

watch(
  () => props.isDialogVisible,
  (newVal) => {
    if (!newVal) {
      selectedScreen.value = '내역 조회'
    }
  }
)

const closeModal = () => {
  selectedScreen.value = '내역 조회'
  emit('close')
}
</script>
