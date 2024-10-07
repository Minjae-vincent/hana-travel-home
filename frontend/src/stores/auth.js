import { defineStore } from 'pinia'
import { ref } from 'vue'
import { parseJwt } from '@/assets/utils/parseJwt'

export const useAuthStore = defineStore('auth', () => {
  // State
  const isAuthenticated = ref(false)
  const accessToken = ref('')
  const userName = ref('')
  const userId = ref('')
  const imgURL = ref('')
  const countryName = ref('')
  const isTraveling = ref(false)

  // Initial state when vue app is loaded
  // logout()

  // Actions
  function login(token) {
    try {
      const decodedToken = parseJwt(token)
      isAuthenticated.value = true
      accessToken.value = token
      userName.value = decodedToken.name
      userId.value = decodedToken.memberId
      imgURL.value = decodedToken.imgURL
      if (decodedToken.countryName) {
        countryName.value = decodedToken.countryName
        isTraveling.value = true
      }
      localStorage.setItem(
        'auth',
        JSON.stringify({
          isAuthenticated: true,
          accessToken: token,
          userName: decodedToken.name,
          userId: decodedToken.memberId,
          imgURL: decodedToken.imgURL,
          countryName: countryName.value,
          isTraveling: isTraveling.value
        })
      )
    } catch (error) {
      console.error(error)
      logout()
    }
  }

  function logout() {
    isAuthenticated.value = false
    accessToken.value = ''
    userName.value = ''
    userId.value = ''
    imgURL.value = ''
    countryName.value = ''
    isTraveling.value = false
    localStorage.removeItem('auth')
  }

  if (localStorage.getItem('auth')) {
    const authData = JSON.parse(localStorage.getItem('auth'))
    isAuthenticated.value = authData.isAuthenticated
    accessToken.value = authData.accessToken
    userName.value = authData.userName
    userId.value = authData.userId
    imgURL.value = authData.imgURL
    countryName.value = authData.countryName
    isTraveling.value = authData.isTraveling
  }

  return {
    isAuthenticated,
    accessToken,
    userName,
    userId,
    imgURL,
    isTraveling,
    countryName,
    login,
    logout
  }
})
