<template>
  <div class="wrapper">
    <!-- 환율 변동률 -->
    <div class="headline">
      <span style="font-family: 'Hana2-M'; color: #333">환율 데이터로 똑똑하게 여행지 선택!</span>
    </div>
    <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
      변동률, 안정성, 월별 패턴, 예측 그래프까지 한눈에 확인하고
    </div>
    <div class="headline-info" ref="targetSection" data-aos="fade-up" data-aos-once="true">
      나만의 맞춤 여행지를 찾아보세요!
    </div>

    <div class="section" data-aos="fade-up" data-aos-once="true">
      <div class="title">환율 변동률</div>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">🙋🏻 환율 변동률이란?</div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true"
              >환율 변동률은 일정 기간 동안 <span class="hilight">환율이 얼마나 변화했는지</span>를
              나타내는 지표예요.</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >저희 트래블홈은 <span class="hilight">3개월 전 환율과 현재 환율을 비교</span>해</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >환율이 얼마나 올랐거나 내렸는지를 퍼센트로 보여주죠</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >변동률이 낮을수록 <span class="hilight">환율이 더 저렴해졌다</span>는 것을
              의미해요!</span
            >
          </div>
        </template>
      </Accordion>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">
            ❓ 왜 환율 변동률을 고려해서 여행지를 선택해야 할까?
          </div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true"
              >환율 변동률을 확인하면, <span class="hilight">여행 경비를 절약</span>할 수 있는 좋은
              타이밍을 알 수 있어요.</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >3개월 전보다 현재 환율이 더 저렴한 나라를 선택하면
            </span>
            <span data-aos="fade-up" data-aos-once="true"
              >같은 금액으로 더 많은 현지 통화를 받을 수 있어
              <span class="hilight">여행 경비를 아낄 수 있죠!</span>
            </span>
          </div>
        </template>
      </Accordion>

      <Fluctuation />
    </div>

    <!-- 환율 안정성 -->
    <div class="section" data-aos="fade-up" data-aos-once="true">
      <div class="title">환율 안정성</div>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">🙋🏻 환율 안정성이란?</div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true"
              >환율 안정성은 <span class="hilight">환율 변동률의 표준편차</span>를 나타내는
              지표예요.</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >환율 변동률이 낮을수록 환율이 <span class="hilight">안정적</span>이라는 것을
              의미해요.</span
            >
            <span data-aos="fade-up" data-aos-once="true"
              >환율 안정성이 높은 나라를 선택하면
              <span class="hilight">환율 변동에 따른 위험을 줄일</span> 수 있어요!</span
            >
          </div>
        </template>
      </Accordion>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">
            ❓ 왜 환율 안정성을 고려해서 여행지를 선택해야 할까?
          </div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true">
              환율 변동이 큰 나라를 선택하면 <span class="hilight">예기치 못한 환율 변화</span>로
              인해 여행 경비가 예상보다 크게 달라질 수 있어요.
            </span>
            <span data-aos="fade-up" data-aos-once="true">
              안정적인 환율을 가진 나라로 여행을 떠나면 환전 시 발생할 수 있는
              <span class="hilight">위험을 줄이고</span>
            </span>
            <span data-aos="fade-up" data-aos-once="true">
              <span class="hilight">예산을 더 정확하게 계획할 수</span> 있어 불필요한 지출을 방지할
              수 있죠.
            </span>
          </div>
        </template>
      </Accordion>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">
            🔍 그래프를 어떻게 봐야할까?
          </div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true">
              박스 플롯은 <span class="hilight">환율 변동성을 한 눈</span>에 보여주는 그래프에요.
            </span>
            <span data-aos="fade-up" data-aos-once="true">
              <span style="font-size: 22px; color: blue; font-family: 'Hana2-B'">짧은 박스</span>는
              <span class="hilight">안정</span>적인 환율을 의미해요!
              <span class="hilight">박스가 짧다</span>는 건 환율이 큰 변화 없이
              <span class="hilight">일정한 범위에서 움직였다</span>는 것이에요.
            </span>
            <v-alert
              text="박스가 짧고 중앙에 가까울수록 환율이 안정적이에요. 안정적인 나라를 찾고 있다면, 박스가 짧은 곳을 선택해보세요!"
              title="TIP!"
              type="info"
              variant="tonal"
            ></v-alert>
            <span data-aos="fade-up" data-aos-once="true" style="margin-top: 10px">
              <span style="font-size: 22px; color: red; font-family: 'Hana2-B'">긴 박스</span>는
              <span class="hilight">변동성이 크다</span>는 뜻이에요. 환율이 크게 출렁였다는 것을
              의미하지요.
            </span>
            <span
              >이건 여행 <span class="hilight">경비를 예상하기가 힘들 수 있다</span>는 얘기에요!.
              여행하기엔 조금 불안할 수도 있겠는걸요?
              <v-alert
                text="수염이 길수록 예상치 못한 변화가 생길 수 있으니, 변동성에 민감한 사람이라면 주의해야 해요!"
                title="TIP!"
                type="error"
                variant="tonal"
              ></v-alert>
            </span>
            <span data-aos="fade-up" data-aos-once="true" style="margin-top: 10px">
              <span style="font-size: 22px; color: #666; font-family: 'Hana2-B'">수염</span>은
              극단적인 환율 변동을 나타내요!
            </span>
            <span
              >수염이 <span class="hilight">길면</span> 그 나라의 환율이 가끔씩 갑자기
              <span class="hilight">크게 변동한 적이 있다</span>는 뜻이에요</span
            >
            <span> 이건 예상치 못한 변동이 생길 수 있다는 신호일 수 있어요</span>
          </div>
        </template>
      </Accordion>
      <Boxplot />
    </div>

    <!-- 월 별 패턴 -->
    <div class="section" data-aos="fade-up" data-aos-once="true">
      <div class="title">월 별 환율 히트맵</div>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">
            🙋🏻 월 별 환율 히트맵이란?
          </div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true"
              >환율이 언제 가장 유리할지 궁금한가요?</span
            >
            <span data-aos="fade-up" data-aos-once="true">
              월별 환율 히트맵은
              <span class="hilight">각 나라의 월 평균 환율 변동성을 한눈에</span> 확인할 수 있는
              기능이에요.
            </span>
            <span data-aos="fade-up" data-aos-once="true"
              >"이번 달에 환전하는 게 좋을까?" 고민할 필요 없이, 히트맵이 환율 흐름을 한눈에
              보여줘요!</span
            >
          </div>
        </template>
      </Accordion>
      <Accordion>
        <template #title>
          <div class="sub-title" data-aos="fade-up" data-aos-once="true">
            🔍 그래프를 어떻게 봐야할까?
          </div>
        </template>
        <template #content>
          <div class="info">
            <span data-aos="fade-up" data-aos-once="true">
              히트맵에서는 <span class="hilight">색상이 핵심</span>이에요!
            </span>
            <span data-aos="fade-up" data-aos-once="true">
              <span style="font-size: 22px; color: blue; font-family: 'Hana2-B'">저렴한 구간</span
              >은 <span class="hilight">더 차가운 색상</span>으로 표시돼서, 지금 환전하기에 좋은
              타이밍을 쉽게 알 수 있어요.
            </span>
            <span data-aos="fade-up" data-aos-once="true">
              <span style="font-size: 22px; color: red; font-family: 'Hana2-B'">비싼 구간</span>은
              <span class="hilight">더 따뜻한 색상</span> 으로 나타나니까, 환전 타이밍을 조금 미뤄도
              될 때를 알 수 있어요.
            </span>
          </div>
        </template>
      </Accordion>
      <Hitmap />
    </div>
  </div>
</template>

<script setup>
import Fluctuation from './Fluctuation.vue'
import Hitmap from './Hitmap.vue'
import Graph from '../oneEye/Graph.vue'
import Boxplot from './Boxplot.vue'
import Accordion from '/src/components/Accordion.vue'
</script>

<style scoped>
.wrapper {
  width: 1000px;
  margin: 0 auto;
}

.headline {
  display: flex;
  flex-direction: column;
  text-align: center;
  font-family: 'Hana2-Light', sans-serif;
  font-size: 32px;
  color: #333;
  margin-top: 20px;
  line-height: 1.5;
  margin-bottom: 20px;
}

.section {
  margin-top: 30px;
}

.title {
  text-align: left;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 28px;
  color: #015042;
  margin-top: 60px;
  margin-bottom: 10px;
  margin-left: 10px;
}

.sub-title {
  text-align: left;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 22px;
  font-weight: 400;
  color: #4e4d4d;
  margin-top: 5px;
  margin-left: 10px;
}

.info {
  margin-top: 10px;
  margin-left: 20px;
  margin-bottom: 30px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 18px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.hilight {
  font-family: 'Hana2-B', sans-serif;
  font-size: 20px;
  color: #03ac8e;
}

.headline-info {
  text-align: center;
  margin-top: 5px;
  font-family: 'Hana2-Medium', sans-serif;
  font-size: 20px;
  color: #666;
}
</style>
