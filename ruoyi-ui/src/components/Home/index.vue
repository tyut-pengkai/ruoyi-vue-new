<template>
  <div>
    <svg-icon icon-class="home-filling" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      url: '/'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
