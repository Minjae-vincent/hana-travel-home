<template>
  <div class="travel-journal">
    <div v-if="travelJournals.length === 0" class="no-data">
      <img src="/assets/img/nodata.png" alt="no-data" class="no-data-img" />
      <div class="no-data-info">아직 다녀온 여행이 없습니다!</div>
      <div class="no-data-info">하나 트래블홈과 함께 신나는 여행 어떠신가요?</div>
    </div>
    <div v-else>
      <TravelJournalItem
        v-for="(journal, index) in travelJournals"
        :key="index"
        :destination="journal.travelName"
        :period="journal.period"
        :expense="journal.totalAmountSpent"
        :participants="journal.participants"
        :travelRecordId="journal.travelRecordId"
        :code="journal.code"
      >
      </TravelJournalItem>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TravelJournalItem from '/src/components/myHouse/modal/TravelJournalItem.vue'
import axios from 'axios'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const travelJournals = ref([])

const convertToKoreanTime = (dateString) => {
  const date = new Date(dateString)
  const options = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    timeZone: 'Asia/Seoul'
  }
  return new Intl.DateTimeFormat('ko-KR', options)
    .format(date)
    .replace('.', '년 ')
    .replace('.', '월 ')
    .replace('.', '일')
}

onMounted(async () => {
  const response = await axios.get('http://34.47.69.197:8081/api/user/travel-journals', {
    params: {
      userId: authStore.userId
    }
  })
  // console.log(response.data)
  travelJournals.value = response.data.data
  travelJournals.value.forEach((journal) => {
    const formattedStartDt = convertToKoreanTime(journal.startDt)
    const formattedEndDt = convertToKoreanTime(journal.endDt)
    journal.period = `${formattedStartDt} ~ ${formattedEndDt}`
    journal.totalAmountSpent = Math.round(journal.totalAmountSpent)
  })
})
</script>

<style scoped>
@font-face {
  font-family: 'Hana2-Bold';
  src: url('@/assets/fonts/Hana2-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Spoqa-Han-Sans-Bold';
  src: url('@/assets/fonts/Spoqa-Han-Sans-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Spoqa-Han-Sans-Regular';
  src: url('@/assets/fonts/Spoqa-Han-Sans-Regular.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Light';
  src: url('@/assets/fonts/Hana2-Light.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'Hana2-Medium';
  src: url('@/assets/fonts/Hana2-Medium.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.travel-journal {
  padding: 20px;
}
.no-data {
  width: 800px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 20px;
}
.no-data-img {
  height: 300px;
  margin-bottom: 3cqmin;
}

.no-data-info {
  font-family: 'Hana2-Light', sans-serif;
  font-size: 24px;
  color: #333;
}
</style>
