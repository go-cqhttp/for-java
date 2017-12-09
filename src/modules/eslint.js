module.exports = function (moduleOptions) {
  this.extendBuild((config, {dev, isClient}) => {
    if (dev && isClient) {
      config.module.rules.push({
        enforce: 'pre',
        test: /\.(js|vue)$/,
        loader: 'eslint-loader',
        exclude: /(node_modules)/
      })
    }
  })
}
