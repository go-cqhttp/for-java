<template>
  <nuxt />
</template>

<script>
  import favicon from '../assets/favicon.png'

  export default {
    head () {
      return {
        title: 'zhuweiyou',
        meta: [
          {charset: 'utf-8'},
          {'http-equiv': 'x-ua-compatible', content: 'ie=edge'},
          {name: 'description', content: 'Web Developer'},
          {name: 'renderer', content: 'webkit'},
          {name: 'apple-mobile-web-app-capable', content: 'yes'},
          {name: 'apple-mobile-web-app-status-bar-style', content: 'black'},
          {name: 'format-detection', content: 'telephone=no,address=no,email=no'},
          {name: 'viewport', content: 'width=device-width,initial-scale=1,user-scalable=0'}
        ],
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
