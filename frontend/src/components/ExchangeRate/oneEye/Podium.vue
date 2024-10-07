<template>
  <div class="headline" ref="targetSection" data-aos="fade-up" data-aos-once="true">
    환율 변동에 따른 최적의 여행지 TOP 10!
  </div>
  <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
    최근 3개월간 환율 변동률을 기준으로 여행하기 좋은 나라들을 랭킹으로 보여드립니다.
  </div>
  <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
    여행 경비 절감을 위한 정보로 스마트한 여행을 준비하세요!
  </div>
  <div v-if="!isLoading">
    <div class="podium-container" data-aos="fade-up" data-aos-once="true">
      <div class="podium">
        <div class="podium-spot second">
          <div class="podium-info" data-aos="fade-up" data-aos-once="true">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${fluctuationRange[1].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ fluctuationRange[1].codeName }}
              <br />
              <span>{{ fluctuationRange[1].fluctuationRange.toFixed(2) }}%</span>
            </div>
          </div>
        </div>
        <div class="podium-spot first">
          <div class="podium-info" data-aos="fade-up" data-aos-once="true">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${fluctuationRange[0].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ fluctuationRange[0].codeName }}
              <br />
              <span>{{ fluctuationRange[0].fluctuationRange.toFixed(2) }}%</span>
            </div>
          </div>
        </div>
        <div class="podium-spot third">
          <div class="podium-info" data-aos="fade-up" data-aos-once="true">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${fluctuationRange[2].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ fluctuationRange[2].codeName }}
              <br />
              <span>{{ fluctuationRange[2].fluctuationRange.toFixed(2) }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <v-table
      style="background-color: #f0f0f0; border-radius: 20px"
      data-aos="fade-up"
      data-aos-once="true"
    >
      <tbody data-aos="fade-up" data-aos-once="true">
        <tr
          v-for="(item, index) in fluctuationRange.slice(3, 10)"
          :key="item.codeName"
          style="display: flex; justify-content: space-between"
        >
          <td style="border: none; display: flex">
            <div class="nation-info">
              <span style="margin-right: 10px; color: #03ac8e">{{ index + 4 }}. </span>
              <v-avatar
                size="24"
                class="small-avatar"
                :image="`/assets/img/${item.code}.gif`"
              ></v-avatar>
              {{ item.codeName }}
            </div>
          </td>
          <td class="nation-info" style="border: none">{{ item.fluctuationRange.toFixed(2) }} %</td>
        </tr>
      </tbody>
    </v-table>
  </div>
  <div class="real-time-context">{{ formattedDate }} 기준 통계 자료입니다.</div>
  <div class="headline-more" ref="targetSection" data-aos="fade-up" data-aos-once="true">
    더 다양한 환율 기반 여행지 추천법이 궁금하시나요?
  </div>
  <div
    class="headline-more"
    style="margin-top: 10px"
    ref="targetSection"
    data-aos="fade-up"
    data-aos-once="true"
  >
    <span style="font-size: 35px; color: #03ac8e">자세히 보기</span>를 통해 더 다양한 정보를
    확인해보세요!
  </div>
</template>

<script setup>
import { onMounted, ref, computed } from 'vue'
import AOS from 'aos'
import 'aos/dist/aos.css'

import axios from 'axios'

const fluctuationRange = ref([])
const isLoading = ref(true)

const getFluctuationRange = async () => {
  isLoading.value = true
  try {
    await axios
      .get('http://34.47.69.197:8081/api/user/exchange-rate/fluctuation')
      .then((response) => {
        fluctuationRange.value = response.data.data
        isLoading.value = false
      })
  } catch (error) {
    console.error(error)
    isLoading.value = false
  }
}

const today = new Date()

const formattedDate = computed(() => {
  const year = today.getFullYear()
  const month = String(today.getMonth() + 1).padStart(2, '0') // 월은 0부터 시작하므로 +1
  const day = String(today.getDate()).padStart(2, '0')

  return `${year}년 ${month}월 ${day}일`
})

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })
  getFluctuationRange()
})
</script>

<style scoped>
.podium-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: auto;
  margin-top: 80px;
}

.podium {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  gap: 30px;
  height: 200px;
  margin-top: 20px;
}

.podium-spot {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 250px;
  background-color: #f2f2f2;
  border-radius: 10px 10px 0 0;
  position: relative;
  padding-top: 10px;
  transition: transform 0.3s ease;
}

.podium-spot:hover {
  transform: translateY(-10px);
}

.avatar {
  position: absolute;
  top: -32px;
  transform: translateY(-50%);
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.small-avatar {
  margin-right: 5px;
  border: 3px solid #fff;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

.first,
.second,
.third {
  background: linear-gradient(180deg, #53c6b5, #6360e7);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  transform: translateY(-10px);
}

.first {
  height: 200px;
}

.second {
  height: 160px;
}

.third {
  height: 120px;
}

.position {
  margin-top: 10px;
  padding-bottom: 10px;
  font-family: 'Hana2-Medium', sans-serif;
  font-weight: bold;
  font-size: 1.5rem;
  color: #fff;
  text-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.podium-info span {
  font-size: 1.2rem;
  color: #fff;
  text-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
}

.nation-info {
  display: flex;
  align-items: center;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 24px;
}

.headline {
  text-align: center;
  font-family: 'Hana2-M', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  margin-bottom: 20px;
}

.headline-info {
  text-align: center;
  margin-top: 5px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 20px;
  color: #666;
}

.headline-more {
  text-align: center;
  margin-top: 30px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 25px;
  color: #666;
}

.real-time-context {
  text-align: right;
  margin-top: 10px;
  margin-right: 10px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 16px;
  color: #666;
}
</style>
