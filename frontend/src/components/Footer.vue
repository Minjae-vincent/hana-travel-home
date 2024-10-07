<template>
  <footer class="footer">
    <div class="footer-top">
      <ul class="footer-links">
        <li><a href="#privacy">개인정보처리방침</a></li>
        <li><a href="#security">신용정보보호정책</a></li>
        <li><a href="#customer">고객정보취급방침</a></li>
        <li><a href="#report">사고신고</a></li>
      </ul>
    </div>
    <div class="footer-logo">
      <img src="@/assets/img/logo.png" alt="Logo" class="logo-image" />
    </div>
    <div class="footer-bottom">
      <p>고객센터(유료) 국내 1800-111 / 해외 82-1800-1111 평일 09:00 ~ 18:00 (은행 휴무일 제외)</p>
      <p>서울특별시 중구 을지로 66</p>
      <p>Copyright © 2022 HANA FINANCIAL GROUP. All rights Reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
@font-face {
  font-family: 'Hana2-Bold';
  src: url('@/assets/fonts/Hana2-Bold.ttf') format('truetype');
  font-weight: bold;
  font-style: normal;
}

.footer {
  background-color: #303030;
  height: 225px;
  width: 100%;
  padding: 20px;
  color: white;
  font-family: 'Hana2-Bold', sans-serif;
  font-size: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.footer-top {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  gap: 20px;
}

.footer-links li {
  margin: 0;
}

.footer-links a {
  color: white;
  text-decoration: none;
}

.footer-links a:hover {
  text-decoration: underline;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 20px;
}

.logo-image {
  width: 150px;
  height: auto;
}

.footer-bottom {
  text-align: left;
  margin-top: 20px;
}

.footer-bottom p {
  margin: 5px 0;
}
</style>
