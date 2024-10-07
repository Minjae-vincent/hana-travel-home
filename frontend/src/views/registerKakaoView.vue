<template>
  <main class="main-content">
    <div class="spacer-header"></div>
    <div class="content">
      <h1 class="title">회원가입</h1>
      <div class="description-wrapper">
        <p class="description">카카오 간편 인증이 완료되었습니다.</p>
        <p class="description">아이디 및 비밀번호를 입력하시면 회원가입이 완료됩니다.</p>
      </div>
      <hr class="divider" />
      <div class="form-container">
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <input
              type="text"
              id="name"
              class="form-input"
              v-model="user.kakao_account.name"
              readonly
            />
            <select class="form-input" disabled>
              <option>내국인</option>
            </select>
          </div>
          <div class="form-group">
            <input
              type="text"
              id="dob"
              class="form-input"
              v-model="user.kakao_account.birthDate"
              readonly
            />
            <select class="form-input" disabled>
              <option>{{ user.kakao_account.gender }}</option>
            </select>
          </div>
          <div class="form-group full-width">
            <input
              type="text"
              id="phone-second"
              class="form-input full-width"
              v-model="user.kakao_account.phone_number"
              readonly
            />
          </div>
          <hr class="divider" />

          <v-text-field
            v-model="userInfo.id"
            :counter="20"
            :maxlength="20"
            :error-messages="v$.id.$errors.map((e) => e.$message)"
            label="아이디(대/소문자 반드시 확인해주세요.)"
            required
            @blur="v$.id.$touch"
            @input="v$.id.$touch"
          ></v-text-field>
          <v-text-field
            :counter="35"
            :maxlength="35"
            v-model="userInfo.password"
            :error-messages="v$.password.$errors.map((e) => e.$message)"
            :type="visible ? 'text' : 'password'"
            label="비밀번호(8-16자: 영문, 숫자, 특수문자 포함)"
            required
            @click:append-inner="visible = !visible"
            @blur="v$.password.$touch"
            @input="v$.password.$touch"
          ></v-text-field>
          <v-text-field
            v-model="userInfo.email"
            :error-messages="v$.email.$errors.map((e) => e.$message)"
            label="E-mail"
            required
            @blur="v$.email.$touch"
            @input="v$.email.$touch"
          ></v-text-field>
          <v-text-field
            :counter="7"
            :maxlength="7"
            v-model="userInfo.ssnLast"
            :error-messages="v$.ssnLast.$errors.map((e) => e.$message)"
            label="주민등록번호 뒷자리(7자)"
            type="password"
            required
            @blur="v$.ssnLast.$touch"
            @input="v$.ssnLast.$touch"
          ></v-text-field>
          <button :disabled="v$.$invalid" class="submit-button">가입하기</button>
        </form>
      </div>
    </div>
    <div class="spacer-footer"></div>
  </main>
  <alertModal :show="showDialog" :message="notificationMessage" @close="handleClose" />
</template>

<style scoped>
@import '../assets/styles/registerInfoView.css';
</style>
<script setup>
import { reactive, ref } from 'vue'
import axios from 'axios'
import { useVuelidate } from '@vuelidate/core'
import { email, required, helpers } from '@vuelidate/validators'
import { useRouter } from 'vue-router'
import { hashAndLowercase } from '@/assets/utils/hash'
import alertModal from '@/modal/alertModal.vue'

const { user } = history.state

const showDialog = ref(false)
const notificationMessage = ref('')
let callback = null

const showNotification = (message, cb = null) => {
  notificationMessage.value = message
  callback = cb
  showDialog.value = true
}

const handleClose = () => {
  if (callback) {
    callback()
  }
  showDialog.value = false
}

const router = useRouter()

user.kakao_account.birthDate = (
  user.kakao_account.birthyear + user.kakao_account.birthday
).substring(2)

const initialState = reactive({
  id: '',
  password: '',
  email: '',
  ssnLast: '',
  name: user.kakao_account.name,
  gender: user.kakao_account.gender,
  birthDate: user.kakao_account.birthDate,
  phoneNumber: user.kakao_account.phone_number
})

const customRequired = helpers.withMessage('필수로 입력하셔야 합니다.', required)

// 아이디 유효성 검사
const idValidators = {
  noNonEnglishCharacters: helpers.withMessage('아이디는 영문과 숫자만 가능합니다.', (value) =>
    /^[A-Za-z0-9]*$/.test(value)
  ),
  mustContainNumber: helpers.withMessage('아이디에는 반드시 숫자가 포함되어야 합니다.', (value) =>
    /[0-9]/.test(value)
  )
}

// 비밀번호 유효성 검사
const passwordValidators = {
  minLength: helpers.withMessage('8자 이상이어야 합니다.', (value) => value.length >= 8),
  maxLength: helpers.withMessage('16자 이하여야 합니다.', (value) => value.length <= 16),
  matches: helpers.withMessage('영문, 숫자, 특수문자를 포함해야 합니다.', (value) =>
    /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]+$/.test(value)
  )
}

// 이메일 유효성 검사
const emailValidators = {
  mustContainAt: helpers.withMessage('유효한 이메일 형식이 아닙니다.', email)
}

const ssnLastValidators = {
  mustContainNumber: helpers.withMessage('숫자만 입력 가능합니다.', (value) =>
    /^[0-9]*$/.test(value)
  ),
  length: helpers.withMessage('7자리를 입력해야 합니다.', (value) => value.length === 7)
}

const userInfo = reactive({ ...initialState })

const rules = {
  id: { customRequired, ...idValidators },
  password: { customRequired, ...passwordValidators },
  email: { customRequired, ...emailValidators },
  ssnLast: { customRequired, ...ssnLastValidators }
}

const visible = ref(false)

const v$ = useVuelidate(rules, userInfo)

const submitForm = async () => {
  var userRequest = Object.assign({}, userInfo)

  userRequest.password = hashAndLowercase(userRequest.password)
  userRequest.ssnLast = hashAndLowercase(userRequest.ssnLast)
  try {
    const response = await axios.post('http://34.47.69.197:8081/api/user/register', userRequest)
    if (response.data.code === 200) {
      showNotification('회원가입이 완료되었습니다.', () => {
        router.push({ name: 'login' })
      })
    } else {
      showNotification('회원가입에 실패했습니다.\n' + response.data.message)
    }
  } catch (error) {
    console.error(error)
    showNotification('서버에서 에러가 발생하였습니다.')
  }
}
</script>
