export default {
  beforeMount(el, binding) {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: 0.1
    }

    const callback = (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          binding.value(entry)
        }
      })
    }

    const observer = new IntersectionObserver(callback, options)
    observer.observe(el)
  }
}
