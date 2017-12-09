<template>
  <nuxt />
</template>

<script>
  import favicon from '../assets/favicon.png'

  export default {
    head () {
      return {
        link: [
          {rel: 'icon', type: 'image/png', href: favicon}
        ]
      }
    }
  }
</script>

<style lang="less">
  * {
    margin: 0;
    padding: 0;
    outline: 0;
    -webkit-tap-highlight-color: transparent;
  }

  html,
  body,
  #__nuxt {
    height: 100%;
  }

  body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
  }
</style>
