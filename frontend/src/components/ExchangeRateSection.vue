<!-- src/components/PhotoSection.vue -->
<template>
  <section class="photo-section" ref="ExchangeRateSection">
    <div class="overlay"></div>
    <div class="content">
      <h1 class="overlay-text">
        <span> 06 </span> 월 지금!
        <br />
        <span>이 나라는 어떠신가요?</span>
        <br />
        <span>환율을 확인하고 그 동안 미뤄왔던 여행을 떠나보세요!</span>
      </h1>
      <button class="overlay-button">환율하우스</button>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const ExchangeRateSection = ref(null)

onMounted(() => {
  const handleIntersection = (entry) => {
    if (entry.isIntersecting) {
      ExchangeRateSection.value.classList.add('visible')
    }
  }

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        handleIntersection(entry)
      })
    },
    { threshold: 0.1 }
  )

  observer.observe(ExchangeRateSection.value)
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
  font-family: 'Noto Sans KR';
  src: url('@/assets/fonts/NotoSansKR-Light.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.photo-section {
  position: relative;
  width: 100%;
  height: 700px;
  background-image: url('@/assets/img/homeImg2.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  opacity: 0; /* Initially hidden */
  transition: opacity 1s ease-in-out; /* Smooth transition */
}

.photo-section.visible {
  opacity: 1; /* Make visible when intersecting */
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(76, 76, 76, 0.59);
}

.content {
  position: absolute;
  top: 50%;
  right: 145px;
  transform: translateY(-50%);
  text-align: right;
  color: white;
}

.overlay-text {
  font-size: 50px;
  margin-bottom: 30px;
  font-family: 'Hana2-Bold', sans-serif;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.overlay-button {
  width: 259px;
  height: 78px;
  font-size: 32px;
  padding: 0.75rem 1.5rem;
  border: 3px solid white;
  background-color: transparent;
  border-radius: 10px;
  color: white;
  font-family: 'Noto Sans KR';
  cursor: pointer;
  transition:
    background-color 0.2s ease,
    color 0.2s ease,
    border-color 0.2s ease;
}

.overlay-button:hover {
  color: #03ac8e;
  border-color: #03ac8e;
}
</style>
