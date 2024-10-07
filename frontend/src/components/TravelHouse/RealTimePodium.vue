<template>
  <div class="headline">
    <span class="time">{{ currentTime }}</span
    >, 현재!
  </div>
  <div class="headline">아래 세 나라를 가장 많이 여행 중이네요!</div>
  <div class="podium-container">
    <div class="podium">
      <div class="podium-spot second">
        <div class="podium-info">
          <v-avatar
            size="72"
            class="avatar"
            :image="`/assets/img/${topThreeCountries[1].code}.gif`"
          ></v-avatar>
          <div class="position">
            {{ topThreeCountries[1].codeName }}
            <br />
            <span>{{ topThreeCountries[1].numOfBloggers.toLocaleString() }} 명</span>
          </div>
        </div>
      </div>
      <div class="podium-spot first">
        <div class="podium-info">
          <v-avatar
            size="72"
            class="avatar"
            :image="`/assets/img/${topThreeCountries[0].code}.gif`"
          ></v-avatar>
          <div class="position">
            {{ topThreeCountries[0].codeName }}
            <br />
            <span>{{ topThreeCountries[0].numOfBloggers.toLocaleString() }} 명</span>
          </div>
        </div>
      </div>
      <div class="podium-spot third">
        <div class="podium-info">
          <v-avatar
            size="72"
            class="avatar"
            :image="`/assets/img/${topThreeCountries[2].code}.gif`"
          ></v-avatar>
          <div class="position">
            {{ topThreeCountries[2].codeName }}
            <br />
            <span>{{ topThreeCountries[2].numOfBloggers.toLocaleString() }} 명</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineProps } from 'vue'
import AOS from 'aos'

var topThreeCountries = defineProps({
  topThreeCountries: Object
})

topThreeCountries = topThreeCountries.topThreeCountries

const currentTime = ref(getFormattedCurrentTime())

function getFormattedCurrentTime() {
  const now = new Date()
  const utc = now.getTime() + now.getTimezoneOffset() * 60000
  const kstTime = new Date(utc + 3600000 * 9)
  const options = {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }
  return (
    new Intl.DateTimeFormat('ko-KR', options)
      .format(kstTime)
      .replace('.', '년 ')
      .replace('. ', '월 ')
      .replace('. ', '일 ')
      .replace(', ', ' ')
      .replace(':', '시 ')
      .replace(':', '분 ') + '초'
  )
}

function updateTime() {
  currentTime.value = getFormattedCurrentTime()
}

let timer = null

onMounted(() => {
  AOS.init({
    duration: 1000,
    offset: 200
  })
  timer = setInterval(updateTime, 1000)
})

onUnmounted(() => {
  clearInterval(timer)
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
  left: 90px;
  transform: translateY(-50%);
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

.headline {
  text-align: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
}

.time {
  font-family: 'Hana2-M', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
}
</style>
