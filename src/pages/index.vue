<template>
  <div :class="$style.wrap">
    <div :class="$style.body">
      <h1>Zhu Wei You</h1>
      <h2 :class="$style.desc">Web Developer</h2>
    </div>
  </div>
</template>

<style lang="less" module>
  .wrap {
    height: 100%;
    background: #505050 url(../assets/img/map.svg) no-repeat center;
    background-size: 200% auto;
    user-select: none;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .body {
    color: #fff;
    text-transform: uppercase;
    text-align: center;
  }

  .desc {
    opacity: 0.8;
    font-weight: normal;
    font-size: 14px;
    padding-top: 20px;
    height: 40px;
  }
</style>
