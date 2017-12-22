const OfflinePlugin = require('offline-plugin')

module.exports = function (moduleOptions) {
  this.extendBuild((config, {dev, isClient}) => {
    if (!dev && isClient) {
      config.plugins.push(new OfflinePlugin({
        externals: [
          '/'
        ],
        excludes: [
          'LICENSES',
          'index.spa.html',
          'index.ssr.html',
          'vue-ssr-client-manifest.json',
          '**/*.map'
        ],
        AppCache: false
      }))
    }
  })
}
