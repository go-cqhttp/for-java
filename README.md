```
      _                          _
  ___| |__  _   _  __      _____(_)  _   _  ___  _   _        ___ ___  _ __ ___
 |_  / '_ \| | | | \ \ /\ / / _ \ | | | | |/ _ \| | | |      / __/ _ \| '_ ` _ \
  / /| | | | |_| |  \ V  V /  __/ | | |_| | (_) | |_| |  _  | (_| (_) | | | | | |
 /___|_| |_|\__,_|   \_/\_/ \___|_|  \__, |\___/ \__,_| (_)  \___\___/|_| |_| |_|
                                     |___/
```

[![Build Status](https://travis-ci.org/zhuweiyou/zhuweiyou.com.svg?branch=master)](https://travis-ci.org/zhuweiyou/zhuweiyou.com)

## 项目说明

- 使用 [nuxt.js](https://github.com/nuxt/nuxt.js) 开发
- 使用 [cloudflare](https://www.cloudflare.com/) 开启 cdn / https / http2
- 使用 [travis](https://travis-ci.org/zhuweiyou/zhuweiyou.com) 自动部署到 [gh-pages](https://github.com/zhuweiyou/zhuweiyou.com/tree/gh-pages)

## 开发姿势
创建新分支进行开发
```bash
npm i
npm run dev
```

需要上线时，合并到 master 即可自动部署
