<!-- alertModal.vue -->
<template>
  <v-dialog v-model="dialog" max-width="400">
    <v-card>
      <v-card-title class="headline">알림</v-card-title>
      <v-card-text>{{ message }}</v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" text @click="closeDialog">확인</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, toRefs } from 'vue'

const props = defineProps({
  show: Boolean,
  message: String
})

const emit = defineEmits(['close'])

const dialog = ref(false)

const { show } = toRefs(props)

watch(show, (newVal) => {
  dialog.value = newVal
})

const closeDialog = () => {
  dialog.value = false
  emit('close')
}
</script>
