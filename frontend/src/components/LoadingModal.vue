<!-- src/components/LoadingModal.vue -->
<template>
  <v-dialog v-model="visible" persistent max-width="300">
    <v-card>
      <v-card-text class="text-center">
        <v-progress-circular indeterminate color="primary" size="64"></v-progress-circular>
        <div>처리 중 입니다 ...</div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useLoading } from '@/composables/useLoading'

const { loading } = useLoading()
const visible = ref(false)

// Watch the loading state and update visibility of the modal
watch(loading, (newValue) => {
  visible.value = newValue
})
</script>
