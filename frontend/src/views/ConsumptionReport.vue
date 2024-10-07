<template>
  <div class="container">
    <!-- 평균 지출 금액 비교 -->
    <div class="content-header" data-aos="fade-up" data-aos-once="true">
      <div style="display: flex; align-items: center; gap: 10px">
        다른 사람들과 비교하면 나는
        <span class="hilight"> 과소비러</span>? 아니면 <span class="hilight">알뜰소비러</span>?
      </div>
      <div
        style="display: flex; align-items: center; gap: 10px"
        data-aos="fade-up"
        data-aos-once="true"
      >
        내 여행 평균 지출 금액과 다른 사람들의
        <span class="hilight">평균 지출 금액</span>을 <span class="hilight">비교</span>해
        비교해보세요!
      </div>
    </div>
    <div class="average-travel-amount">
      <div class="average-travel-amount1" data-aos="fade-up" data-aos-once="true">
        <img src="/assets/img/test.png" alt="image" class="atm-img" />
        <div style="display: flex; flex-direction: column; align-items: center" class="mt-4">
          <div class="default-info">{{ userName }}님의 평균 소비 금액은</div>
          <div class="default-info">
            <span class="hilight-info"
              >{{ userSpending.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }} 원</span
            >
            입니다.
          </div>
        </div>
      </div>
      <div class="average-travel-amount2" data-aos="fade-up" data-aos-once="true">
        <VueApexCharts
          type="radialBar"
          height="305"
          :options="radialChartOptions"
          :series="radialSeries"
        ></VueApexCharts>
        <div>
          <div class="default-info">평균적으로 다른 사용자들의</div>
          <div class="default-info">
            <span class="hilight-info">{{ radialSeries[0] }}%</span>
            를 소비하셨군요!
          </div>
        </div>
      </div>
    </div>

    <!-- 카테고리별 소비 금액 -->
    <div class="content-header">
      <div style="display: flex; align-items: center; gap: 10px">
        <span class="hilight">내 소비 트렌드</span>
        를 한눈에!
      </div>
      <div style="display: flex; align-items: center; gap: 10px">
        <span class="hilight">카테고리별</span>
        로 내가 어디에 돈을 썼는지, 얼마나 썼는지 한 번에 확인하세요.
      </div>

      <div style="display: flex; align-items: center; gap: 10px">
        똑똑한 소비 습관, 여기서 시작해봐요!
      </div>
    </div>
    <div class="consumption-wrapper">
      <div class="consumption-graph">
        <VueApexCharts
          type="donut"
          height="400"
          :options="pieChartOptions"
          :series="pieSeries"
        ></VueApexCharts>

        <VueApexCharts
          type="bar"
          height="400"
          :options="barChartOptions"
          :series="barSeries"
        ></VueApexCharts>
      </div>
      <div style="display: flex; flex-direction: column; align-items: center; gap: 10px">
        <div class="default-info">
          {{ userName }} 님이 가장 <span class="hilight-info">많이</span> 쓴 건
          <span class="hilight-info">{{ firstKey }}!</span>
        </div>
        <div class="default-info">
          가장
          <span class="hilight-info">적게</span>
          쓴 건
          <span class="hilight-info">{{ lastKey }}!</span>
        </div>
        <div class="default-info">나머지 카테고리도 확인해보세요!</div>
      </div>
    </div>

    <!-- 국가별 소비 금액  TOP3-->
    <div class="content-header">
      <div style="display: flex; align-items: center; gap: 10px">
        {{ userName }}님이 <span class="hilight">가장 많이 </span> 소비한 나라
        <span class="hilight">TOP3!</span>
      </div>
      <div style="display: flex; align-items: center; gap: 10px">
        어디에서 가장 많은
        <span class="hilight">추억</span>
        을 만드셨을까요?
      </div>
    </div>
    <div class="podium-container">
      <div class="podium">
        <div class="podium-spot second">
          <div class="podium-info">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${countries[1].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ countries[1].codeName }}
              <br />
              <span>{{ countries[1].totalAmountSpent.toLocaleString() }} 원</span>
            </div>
          </div>
        </div>
        <div class="podium-spot first">
          <div class="podium-info">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${countries[0].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ countries[0].codeName }}
              <br />
              <span>{{ countries[0].totalAmountSpent.toLocaleString() }} 원</span>
            </div>
          </div>
        </div>
        <div class="podium-spot third">
          <div class="podium-info">
            <v-avatar
              size="72"
              class="avatar"
              :image="`/assets/img/${countries[2].code}.gif`"
            ></v-avatar>
            <div class="position">
              {{ countries[2].codeName }}
              <br />
              <span>{{ countries[2].totalAmountSpent.toLocaleString() }} 원</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- <v-btn class="mt-10" color="primary" @click="downloadPDF"> PDF 다운로드 </v-btn> -->
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { gsap } from 'gsap'
import AOS from 'aos'
import { useAuthStore } from '@/stores/auth'
import VueApexCharts from 'vue3-apexcharts'
import html2pdf from 'html2pdf.js'
import axios from 'axios'

const authStore = useAuthStore()
const userName = authStore.userName
const userId = authStore.userId
const isMostSpentLoading = ref(false)

const keysArray = computed(() => Object.keys(categoryData.value))
const firstKey = computed(() => keysArray.value[0])
const lastKey = computed(() => keysArray.value[keysArray.value.length - 1])

const userSpending = ref(1000000)
const averageSpending = ref(1800000)
const categoryData = ref({})
const countries = ref([
  {
    codeName: '미국',
    totalAmountSpent: 10000000,
    code: 'US'
  },
  {
    codeName: '미국',
    totalAmountSpent: 10000000,
    code: 'US'
  },
  {
    codeName: '미국',
    totalAmountSpent: 10000000,
    code: 'US'
  }
])

// const countries = ref([])

const getMostSpentCountry = async () => {
  isMostSpentLoading.value = true
  await axios
    .get(`http://34.47.69.197:8081/api/user/manage/consumption-report/${userId}/most-spent`)
    .then((res) => {
      countries.value = res.data.data
      countries.value = countries.value.map((country) => {
        return {
          codeName: country.codeName,
          totalAmountSpent: Math.round(country.totalAmountSpent),
          code: country.code
        }
      })
      isMostSpentLoading.value = false
    })
}

const getCategorySpent = async () => {
  await axios
    .get(`http://34.47.69.197:8081/api/user/manage/consumption-report/${userId}/category-statics`)
    .then((res) => {
      categoryData.value = res.data.data
      categoryData.value = categoryData.value.reduce((acc, cur) => {
        acc[cur.category] = cur.totalAmountSpent
        return acc
      }, {})

      barSeries.value = [
        {
          name: '소비 금액',
          data: Object.values(categoryData.value)
        }
      ]

      barChartOptions.value = {
        chart: {
          type: 'bar',
          height: 350
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '45%',
            endingShape: 'rounded'
          }
        },
        dataLabels: {
          enabled: false
        },
        xaxis: {
          categories: Object.keys(categoryData.value)
        },
        title: {
          text: '카테고리별 소비 금액',
          align: 'center'
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val.toLocaleString() + ' 원'
            }
          }
        }
      }

      pieSeries.value = Object.values(categoryData.value).map((value) => {
        return (value / totalConsumption.value) * 100
      })

      pieChartOptions.value = {
        labels: Object.keys(categoryData.value),
        chart: {
          type: 'donut'
        },
        legend: {
          position: 'bottom'
        },
        responsive: [
          {
            breakpoint: 480,
            options: {
              chart: {
                width: 200
              },
              legend: {
                position: 'bottom'
              }
            }
          }
        ],
        title: {
          text: '소비 카테고리별 비율',
          align: 'center'
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return val.toFixed(2) + '%'
            }
          }
        }
      }
    })
}

const getCompareAmount = async () => {
  await axios
    .get(`http://34.47.69.197:8081/api/user/manage/consumption-report/${userId}/compare`)
    .then((res) => {
      userSpending.value = res.data.data.myAvg.toFixed(0)
      averageSpending.value = res.data.data.otherAvg.toFixed(0)

      radialSeries.value = [Math.round((userSpending.value / averageSpending.value) * 100)]
    })
}

const downloadPDF = () => {
  const element = document.querySelector('.container')
  html2pdf(element)
}

const totalConsumption = computed(() => {
  return Object.values(categoryData.value).reduce((a, b) => a + b, 0)
})

const pieSeries = computed(() => {
  return Object.values(categoryData.value).map((value) => {
    return (value / totalConsumption.value) * 100
  })
})

const barSeries = ref([])

const pieChartOptions = ref({})

const barChartOptions = ref({})

const radialChartOptions = ref({
  chart: {
    type: 'radialBar',
    height: 350
  },
  plotOptions: {
    radialBar: {
      startAngle: -135,
      endAngle: 225,
      hollow: {
        margin: 0,
        size: '70%',
        background: '#fff',
        image: undefined,
        imageOffsetX: 0,
        imageOffsetY: 0,
        position: 'front',
        dropShadow: {
          enabled: true,
          top: 3,
          left: 0,
          blur: 4,
          opacity: 0.24
        }
      },
      track: {
        background: '#fff',
        strokeWidth: '67%',
        margin: 0,
        dropShadow: {
          enabled: true,
          top: -3,
          left: 0,
          blur: 4,
          opacity: 0.35
        }
      },
      dataLabels: {
        name: {
          offsetY: -10,
          show: true,
          color: '#888',
          fontFamily: 'Pretendard-Regular',
          fontSize: '15px'
        },
        value: {
          fontSize: '32px',
          fontFamily: 'Pretendard-Regular',
          show: true,
          formatter: function (val) {
            return val + '%'
          }
        }
      }
    }
  },
  fill: {
    type: 'gradient',
    gradient: {
      shade: 'dark',
      type: 'horizontal',
      shadeIntensity: 0.5,
      gradientToColors: ['#ABE5A1'],
      inverseColors: true,
      opacityFrom: 1,
      opacityTo: 0.8,
      stops: [0, 100]
    }
  },
  stroke: {
    lineCap: 'round'
  },
  labels: ['타 사용자 대비 소비금액']
})

const radialSeries = ref([])

onMounted(async () => {
  const wiggleTimeline = gsap.timeline({ repeat: -1, repeatDelay: 2 })

  wiggleTimeline.to('.atm-img', {
    rotate: 5,
    duration: 0.1,
    yoyo: true,
    repeat: 5,
    ease: 'power1.inOut'
  })

  AOS.init({
    duration: 1000,
    offset: 200
  })

  await getMostSpentCountry()
  await getCategorySpent()
  await getCompareAmount()
})
</script>

<style scoped>
@font-face {
  font-family: 'Pretendard-Regular';
  src: url('https://fastly.jsdelivr.net/gh/Project-Noonnu/noonfonts_2107@1.1/Pretendard-Regular.woff')
    format('woff');
  font-weight: 400;
  font-style: normal;
}

.container {
  padding-top: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0;
}
.average-travel-amount1,
.average-travel-amount2 {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.atm-img {
  width: 400px;
}
.default-info {
  font-size: 20px;
  font-family: 'Pretendard-Regular';
}

.average-travel-amount {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-direction: row;
  gap: 100px;
  padding-bottom: 120px;
}

.consumption-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 0px;
  padding-bottom: 120px;
}

.consumption-graph {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  gap: 120px;
}
.content-header {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  gap: 20px;
  padding-bottom: 50px;
  font-family: 'Pretendard-Regular';
  font-size: 30px;
  font-weight: 550;
}

.hilight {
  color: #03ac8e;
  font-size: 35px;
  /* font-weight: 600; */
  color: #03ac8e;
  font-family: 'Hana2-M', sans-serif;
}

.hilight-info {
  color: #03ac8e;
  font-size: 30px;
  color: #03ac8e;
  font-family: 'Hana2-M', sans-serif;
}
.podium-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: auto;
  margin-top: 30px;
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
  left: 85px;
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
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
}
</style>
