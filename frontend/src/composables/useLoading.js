import { ref } from 'vue'

// Define the loading state
export const loading = ref(false)

export function useLoading() {
  return {
    loading
  }
}
