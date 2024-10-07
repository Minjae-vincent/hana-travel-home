import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
// Vuetify
import 'vuetify/styles'

// BootstrapVue3
import BootstrapVue3 from 'bootstrap-vue-3'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { VDateInput } from 'vuetify/labs/VDateInput'

// MDI Icons
import '@mdi/font/css/materialdesignicons.css' // Import MDI icons

import App from './App.vue'
import router from './router'

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi' // Set default icon set to MDI
  }
  // locale: {
  //   locale: 'en-CA'
  // }
})

const app = createApp(App)

app.use(createPinia())
app.use(BootstrapVue3)
app.use(router)
app.use(vuetify)
app.component('VDateInput', VDateInput)

app.mount('#app')

window.Kakao.init('935b0417c731a4ced9abb18f3288369c')
