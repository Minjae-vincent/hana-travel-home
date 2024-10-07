<!-- TravelJournalModal.vue -->

<template>
  <v-dialog v-model="dialog" max-width="1200px">
    <v-card class="custom-card-background">
      <v-card-text>
        <!-- <v-btn icon  > -->
        <v-icon class="close-button" @click="closeDialog">mdi-close-circle-outline</v-icon>
        <!-- </v-btn> -->

        <div class="ticket-header">
          <div class="ticket-section">
            <div class="ticket-section-wrapper">
              <div class="ticket-left">
                <div class="ticket-destination">{{ destination }}</div>
                <div class="ticket-period">{{ period }}</div>
              </div>
              <div class="ticket-expense">
                총 금액 : <span>{{ expense.toLocaleString() }}</span> KRW
              </div>
            </div>
          </div>
        </div>
        <GoogleMap :travelRecordId="travelRecordId" :code="code" />
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import GoogleMap from '/src/components/googleMap/GoogleMap.vue'
import { ref, defineProps, defineEmits, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  destination: String,
  period: String,
  expense: Number,
  travelRecordId: String,
  code: String
})

const emit = defineEmits(['update:modelValue'])

const dialog = ref(props.modelValue)

const closeDialog = () => {
  dialog.value = false
  emit('update:modelValue', false)
}

watch(
  () => props.modelValue,
  (newVal) => {
    dialog.value = newVal
  }
)
</script>

<style scoped>
.custom-card-background {
  background-color: #fff;
  position: relative; /* For the close button positioning */
}

.ticket-header {
  background-color: #fff;
  border-radius: 10px;
  padding: 20px;
  padding-bottom: 0;
  margin-top: 15px;
}

.ticket-section {
  display: flex;
  flex-direction: column;
}

.ticket-destination {
  font-size: 26px;
  color: #333;
  font-family: 'Spoqa-Han-Sans-Bold', sans-serif;
}

.ticket-section-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.ticket-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.ticket-period {
  font-size: 18px;
  font-weight: bold;
  color: #7b7b7b;
}

.ticket-expense {
  text-align: right;
  font-size: 17.5px;
  font-weight: bold;
  color: #000;
}

.ticket-expense > span {
  color: #03ac8e;
  font-size: 25px;
}

.v-card-text {
  padding: 0;
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

.v-card-actions {
  justify-content: flex-end;
  padding: 0 20px 20px;
}
</style>
