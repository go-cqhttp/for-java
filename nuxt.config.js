module.exports = {
  srcDir: 'src/',
  loading: false,
  modules: [
    '~/modules/lint',
    '~/modules/offline'
  ],
  plugins: [
    {src: '~/plugins/console', ssr: false},
    {src: '~/plugins/offline', ssr: false}
  ],
  build: {
    extractCSS: false,
    publicPath: '/'
  }
}
