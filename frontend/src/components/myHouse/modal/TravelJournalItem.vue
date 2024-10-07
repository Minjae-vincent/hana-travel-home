<template>
  <v-hover v-slot="{ isHovering, props }">
    <div class="ticket-card" v-bind="props">
      <!-- Overlay on hover -->
      <div v-if="isHovering" class="hover-overlay"></div>

      <!-- Header Section -->
      <div class="ticket-header">
        <div class="ticket-header-left">
          <div class="boarding-pass">BOARDING PASS</div>
          <div class="airline-name">{{ destination }}</div>
        </div>
      </div>

      <!-- Main Section -->
      <div class="ticket-main">
        <div class="ticket-location">
          <div class="location-code">ICN</div>
          <div class="location-name">KOR</div>
        </div>
        <div class="ticket-icon">
          <v-icon class="airplane-icon" size="48px">mdi-airplane</v-icon>
        </div>
        <div class="ticket-location">
          <div class="location-code">HANA</div>
          <div class="location-name">{{ code }}</div>
        </div>
      </div>

      <!-- Time Section -->
      <div class="time-section">
        <div class="time-block">
          <div class="label">PERIOD</div>
          <div class="time">{{ period }}</div>
        </div>
        <div class="time-block">
          <div class="label">EXPENSE</div>
          <div class="time">{{ expense.toLocaleString() }} KRW</div>
        </div>
      </div>

      <!-- Footer Section -->
      <div class="ticket-footer">
        <div class="flight-info">
          <div class="label">FLIGHT</div>
          <div class="info">OZ114</div>
        </div>
        <div class="gate-info">
          <div class="label">GATE</div>
          <div class="info">41A</div>
        </div>
        <div class="seat-info">
          <div class="label">SEAT NO.</div>
          <div class="info">10C</div>
        </div>
        <div class="class-info">
          <div class="label">CLASS</div>
          <div class="info">2024 BUSINESS</div>
        </div>
      </div>

      <!-- Button on hover -->
      <v-expand-transition>
        <div v-if="isHovering" class="hover-button-container">
          <button type="button" class="hover-button" @click="openModal">자세히 보기</button>
        </div>
      </v-expand-transition>

      <TravelJournalModal
        v-model="isModalOpen"
        :destination="destination"
        :period="period"
        :expense="expense"
        :participants="participants"
        :travelRecordId="travelRecordId"
        :code="code"
      />
    </div>
  </v-hover>
</template>

<script setup>
import { ref } from 'vue'
import TravelJournalModal from '/src/components/myHouse/modal/TravelJournalModal.vue'

const props = defineProps({
  destination: String,
  period: String,
  expense: Number,
  participants: Number,
  travelRecordId: String,
  code: String
})

const isModalOpen = ref(false)

function openModal() {
  isModalOpen.value = true
}
</script>

<style scoped>
.ticket-card {
  width: 750px;
  height: auto;
  background-color: #fff;
  border-radius: 15px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-bottom: 50px;
  position: relative;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
}

.ticket-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.2);
}

/* Hover Overlay */
.hover-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.7);
  color: #03ac8e;
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translate(-50%, -50%) scale(0); /* Start scaled down */
  opacity: 0;
  transition:
    transform 0.5s ease,
    opacity 0.5s ease; /* Smooth opacity change */
}

.ticket-card:hover .hover-overlay {
  transform: translate(-50%, -50%) scale(1); /* Scale to full size */
  opacity: 1;
}

/* Button Styling */
.hover-button-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 3; /* Ensure button is on top */
}

.hover-button {
  padding: 12px 24px;
  border: none;
  background-color: #03ac8e;
  color: white;
  border-radius: 25px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition:
    background-color 0.3s ease,
    color 0.3s ease,
    box-shadow 0.3s ease;
}

.hover-button:hover {
  background-color: white;
  color: #03ac8e;
  border: 1px solid #03ac8e;
}

.ticket-header {
  background-color: #fff;
  padding: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ticket-header-left {
  display: flex;
  flex-direction: column;
}

.boarding-pass {
  font-size: 16px;
  color: #03ac8e;
  font-family: 'Spoqa-Han-Sans-Bold', sans-serif;
}

.airline-name {
  font-size: 24px;
  color: #333;
  font-family: 'Spoqa-Han-Sans-Bold', sans-serif;
  margin-top: 5px;
}

.ticket-main {
  background-color: #03ac8e;
  color: white;
  padding: 15px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.ticket-location {
  text-align: center;
}

.location-code {
  font-size: 48px;
  font-family: 'Hana2-Bold', sans-serif;
}

.location-name {
  font-size: 16px;
  font-family: 'Spoqa-Han-Sans-Regular', sans-serif;
}

.ticket-icon v-icon {
  font-size: 48px;
}

.time-section {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
  background-color: #fff;
  border-top: 2px solid #ddd;
}

.time-block {
  text-align: center;
}

.label {
  font-size: 12px;
  color: #333;
  font-family: 'Spoqa-Han-Sans-Regular', sans-serif;
}

.time {
  font-size: 14px;
  font-family: 'Spoqa-Han-Sans-Regular', sans-serif;
  color: #03ac8e;
}

.ticket-footer {
  background-color: #f5f5f5;
  padding: 10px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  font-family: 'Spoqa-Han-Sans-Regular', sans-serif;
  font-size: 14px;
}

.ticket-footer .label {
  font-size: 12px;
  color: #333;
}

.ticket-footer .info {
  font-size: 16px;
  font-weight: bold;
  margin-top: 5px;
}
</style>
