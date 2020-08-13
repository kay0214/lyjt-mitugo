<template>
  <div class="payment-status">
    <div class="iconfont icon-duihao2 bg-color-red"></div>
    <!-- 失败时：订单支付失败 -->
    <div class="status">订单支付成功</div>
    <div class="wrapper">
      <div class="item acea-row row-between-wrapper">
        <div>订单编号</div>
        <div class="itemCom">wx3265361456134568</div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>下单时间</div>
        <div class="itemCom">2019-01-09 18:05</div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>支付方式</div>
        <div class="itemCom">微信支付</div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>支付金额</div>
        <div class="itemCom">2222.00</div>
      </div>
    </div>
    <!--失败时： 重新购买 -->
    <div class="returnBnt bg-color-red">返回查看订单</div>
    <div class="returnBnt cart-color">返回首页</div>
  </div>
</template>
