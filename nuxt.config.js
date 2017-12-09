module.exports = {
  srcDir: 'src/',
  loading: false,
  modules: [
    '~/modules/eslint',
    '~/modules/head',
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
