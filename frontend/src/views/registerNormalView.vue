<template>
  <main class="main-content">
    <LoadingModal />
    <div class="spacer-header"></div>
    <div class="content">
      <h1 class="title">회원가입</h1>
      <div class="description-wrapper">
        <p class="description">아이디 및 비밀번호를 입력하시면 회원가입이 완료됩니다.</p>
      </div>
      <hr class="divider" />
      <div class="input-container">
        <div class="form-group">
          <v-text-field
            id="name"
            v-model="userInfo.name"
            :counter="15"
            :maxlength="15"
            class="custom-v-text-field same-width"
            style="margin-right: 15px"
            :error-messages="v$2.name.$errors.map((e) => e.$message)"
            required
            @blur="v$2.name.$touch"
            @input="v$2.name.$touch"
            label="이름"
          ></v-text-field>
          <v-select
            v-model="userInfo.nationality"
            class="custom-v-select same-width"
            :items="['내국인', '외국인']"
            label="국적"
          ></v-select>
        </div>
        <div class="form-group">
          <v-text-field
            id="dob"
            v-model="userInfo.birthDate"
            :counter="6"
            :maxlength="6"
            class="custom-v-text-field same-width"
            label="생년월일(YYMMDD)"
            style="margin-right: 15px"
            :error-messages="v$2.birthDate.$errors.map((e) => e.$message)"
            required
            @blur="v$2.birthDate.$touch"
            @input="v$2.birthDate.$touch"
          ></v-text-field>
          <v-select
            v-model="userInfo.gender"
            class="custom-v-select same-width"
            :items="['male', 'female']"
            label="성별"
          ></v-select>
        </div>
        <div class="form-group full-width">
          <v-text-field
            id="phone-second"
            v-model="userInfo.phoneNumber"
            :counter="13"
            :maxlength="13"
            class="custom-v-text-field full-width"
            label="휴대폰 번호(숫자만 입력해주세요)"
            :error-messages="v$2.phoneNumber.$errors.map((e) => e.$message)"
            required
            @blur="v$2.phoneNumber.$touch"
            @input="formatPhoneNumber"
          ></v-text-field>
        </div>
        <button
          :disabled="v$2.$invalid || isVerified"
          class="submit-button"
          @click="identityVerification"
        >
          본인인증
        </button>
        <v-otp-input
          v-if="showOtpInput"
          v-model="otpValue"
          :disabled="isVerified"
          length="6"
          label="본인인증 코드 입력"
          variant="filled"
          class="mt-3"
        ></v-otp-input>
      </div>

      <div v-if="isVerified" class="form-container">
        <hr class="divider" />
        <form @submit.prevent="submitForm">
          <v-text-field
            v-model="userInfo.id"
            :counter="20"
            :maxlength="20"
            :error-messages="v$.id.$errors.map((e) => e.$message)"
            label="아이디(대/소문자 반드시 확인해주세요.)"
            required
            @blur="v$.id.$touch"
            @input="v$.id.$touch"
            class="full-width"
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
            class="full-width"
          ></v-text-field>
          <v-text-field
            v-model="userInfo.email"
            :error-messages="v$.email.$errors.map((e) => e.$message)"
            label="E-mail"
            required
            @blur="v$.email.$touch"
            @input="v$.email.$touch"
            class="full-width"
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
            class="full-width"
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

.custom-v-text-field,
.custom-v-select {
  background-color: white !important;
  border: 0px !important;
}

.custom-v-text-field input {
  background-color: white !important;
}

.same-width {
  width: calc(50% - 7.5px) !important;
}
</style>

<script setup>
import { reactive, ref } from 'vue'
import axios from 'axios'
import { useVuelidate } from '@vuelidate/core'
import { email, required, helpers } from '@vuelidate/validators'
import { useRouter } from 'vue-router'
import { watch } from 'vue'
import { useLoading } from '@/composables/useLoading'
import LoadingModal from '@/components/LoadingModal.vue'
import { hashAndLowercase } from '@/assets/utils/hash'
import alertModal from '@/modal/alertModal.vue'

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

const { loading } = useLoading() // Access the loading state
const router = useRouter()

const initialState = reactive({
  id: '',
  password: '',
  email: '',
  ssnLast: '',
  name: '',
  gender: 'male',
  birthDate: '',
  phoneNumber: '',
  nationality: '내국인'
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

// 주민등록번호 뒷자리 유효성 검사
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

const birthDateValidator = {
  mustContainNumber: helpers.withMessage('숫자만 입력 가능합니다.', (value) =>
    /^[0-9]*$/.test(value)
  ),
  length: helpers.withMessage('6자리를 입력해야 합니다.', (value) => value.length === 6)
}

const phoneNumberValidator = {
  formatPhoneNumber: helpers.withMessage('휴대폰 번호 형식에 맞게 입력해주세요.', (value) =>
    /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/.test(value)
  )
}

const rules2 = {
  name: { customRequired },
  birthDate: { customRequired, ...birthDateValidator },
  phoneNumber: { customRequired, ...phoneNumberValidator }
}

const visible = ref(false)

const v$ = useVuelidate(rules, userInfo)
const v$2 = useVuelidate(rules2, userInfo)

const formatPhoneNumber = () => {
  const cleaned = ('' + userInfo.phoneNumber).replace(/\D/g, '')
  const match = cleaned.match(/^(\d{3})(\d{4})(\d{4})$/)
  if (match) {
    userInfo.phoneNumber = `${match[1]}-${match[2]}-${match[3]}`
  }
}

const submitForm = async () => {
  const userRequest = Object.assign({}, userInfo)

  userRequest.password = hashAndLowercase(userRequest.password)
  userRequest.ssnLast = hashAndLowercase(userRequest.ssnLast)

  try {
    loading.value = true
    const response = await axios.post('http://34.47.69.197:8081/api/user/register', userRequest)
    if (response.data.code === 200) {
      showNotification('회원가입이 완료되었습니다.', () => {
        router.push({ name: 'login' })
      })
    } else {
      showNotification(response.data.message)
    }
  } catch (error) {
    console.error(error)
    showNotification('서버에서 에러가 발생하였습니다.')
  } finally {
    loading.value = false
  }
}

const isVerified = ref(false)
const otpValue = ref('')
const showOtpInput = ref(false)
const identityVerificationValue = ref('')

watch(otpValue, (newVal) => {
  if (newVal.length === 6) {
    verifyOtp()
  }
})

const verifyOtp = () => {
  const trimmedOtp = otpValue.value.trim()
  const expectedOtp = identityVerificationValue.value.trim()

  if (trimmedOtp === expectedOtp) {
    showNotification('본인인증이 성공적으로 완료되었습니다.', () => {
      isVerified.value = true
    })
  } else {
    showNotification('본인인증에 실패했습니다.')
  }
}

const identityVerification = async () => {
  loading.value = true
  try {
    const response = await axios.post(
      'http://34.47.69.197:8081/api/user/identity-verification',
      userInfo
    )
    if (response.data.code === 200) {
      console.log('Received OTP from server:', response.data.data)
      showNotification('본인 인증을 위한 OTP가 발송되었습니다. 아래 입력창에 입력해주세요.', () => {
        identityVerificationValue.value = response.data.data
        showOtpInput.value = true
      })

      // alert('본인 인증을 위한 OTP가 발송되었습니다. 아래 입력창에 입력해주세요.')
      // identityVerificationValue.value = response.data.data
      // showOtpInput.value = true
    } else {
      showNotification(response.data.message)
    }
  } catch (error) {
    console.error(error)
    showNotification('서버에서 에러가 발생하였습니다.')
  } finally {
    loading.value = false
  }
}
</script>
