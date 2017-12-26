const OfflinePlugin = require('offline-plugin')

module.exports = function (moduleOptions) {
  this.extendBuild((config, {dev, isClient}) => {
    if (!dev && isClient) {
      config.plugins.push(new OfflinePlugin({
        ServiceWorker: {
          events: true
        },
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
