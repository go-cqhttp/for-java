module.exports = function (moduleOptions) {
  this.extendBuild((config, {dev}) => {
    const vueLoader = config.module.rules.find(rule => rule.loader === 'vue-loader')
    vueLoader.options.cssModules = {
      localIdentName: dev ? '[path][name]---[local]---[hash:base64:5]' : '[hash:base64]',
      camelCase: true
    }
  })
}
