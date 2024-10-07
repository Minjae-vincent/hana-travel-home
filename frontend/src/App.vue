<template>
  <div id="app">
    <Header />
    <main class="main-content">
      <div class="container">
        <router-view />
      </div>
    </main>
    <Footer />
  </div>
</template>

<script setup>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
</script>

<style>
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  /* background-color: #f4f8f9; */
  background-color: #f9fafb;
}

.main-content {
  flex: 1;
  padding-top: 70px;
  padding-bottom: 70px;
  display: flex;
  justify-content: center;
}

.container {
  /* width: 1440px; */
  max-width: 100%;
}
</style>
