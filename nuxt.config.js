module.exports = {
  srcDir: 'src/',
  loading: false,
  modules: [
    '~/modules/css',
    '~/modules/head',
    '~/modules/lint',
    '~/modules/pwa'
  ],
  plugins: [
    {src: '~/plugins/console', ssr: false},
    {src: '~/plugins/pwa', ssr: false}
  ],
  build: {
    extractCSS: false,
    publicPath: '/'
  }
}
