<template>
  <div>
    <div v-if="user.userRole == 2 || user.userRole == 0">
      <el-row :gutter="40" class="panel-group" v-if="user.userRole == 0">
        <div class="panel-group-head">
          <div class="division"></div>
          <div class="panel-group-title">平台用户</div>
          <div class="division"></div>
        </div>
        <div class="panel-col-box">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="peoples" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                会员数
              </div>
              <count-to :start-val="0" :end-val="count.userCount" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="permission" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                分享达人数
              </div>
              <count-to :start-val="0" :end-val="count.shareUserCount" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="pt" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                商户数
              </div>
              <count-to :start-val="0" :end-val="count.merCount" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="user" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                上架商户数
              </div>
              <count-to :start-val="0" :end-val="count.okMerCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        </div>
      </el-row>

      <el-row :gutter="40" class="panel-group">
        <div class="panel-group-head">
          <div class="division"></div>
          <div class="panel-group-title">本地生活数据</div>
          <div class="division"></div>
        </div>
        <div class="panel-col-box">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="coupon" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                卡券数量
              </div>
              <count-to :start-val="0" :end-val="count.localProduct" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="coupon" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待上架卡券
              </div>
              <count-to :start-val="0" :end-val="count.localProductUnder" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="orderinfo" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待核销订单
              </div>
              <count-to :start-val="0" :end-val="count.localOrderWait" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="money" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                今日营业额
              </div>
              <count-to :start-val="0" :end-val="count.localSumPrice" :duration="3600" :decimals="count.localSumPrice > 0 ? 2 : 0" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="seven" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近七日营业额
              </div>
              <count-to :start-val="0" :end-val="count.lastWeekLocalPrice" :decimals="count.lastWeekLocalPrice > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="monthlyview" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近一个月营业额
              </div>
              <count-to :start-val="0" :end-val="count.monthLocalPrice" :decimals="count.monthLocalPrice > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="today" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                今日成交量
              </div>
              <count-to :start-val="0" :end-val="count.localOrderCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="seven" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近七日成交量
              </div>
              <count-to :start-val="0" :end-val="count.lastWeekLocalCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="monthlyview" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近一个月成交量
              </div>
              <count-to :start-val="0" :end-val="count.monthLocalCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col" v-if="user.userRole == 0">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="error" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待处理退款
              </div>
              <count-to :start-val="0" :end-val="count.localOrderRefund" :decimals="count.localOrderRefund > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        </div>
      </el-row>

      <el-row :gutter="40" class="panel-group">
        <div class="panel-group-head">
          <div class="division"></div>
          <div class="panel-group-title">商城数据</div>
          <div class="division"></div>
        </div>
        <div class="panel-col-box">
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-people">
              <svg-icon icon-class="shopping" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                商品数量
              </div>
              <count-to :start-val="0" :end-val="count.shopProduct" :duration="2600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-message">
              <svg-icon icon-class="shopping" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待上架商品
              </div>
              <count-to :start-val="0" :end-val="count.shopProductUnder" :duration="3000" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel">
            <div class="card-panel-icon-wrapper icon-money">
              <svg-icon icon-class="orderinfo" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待发货订单
              </div>
              <count-to :start-val="0" :end-val="count.shopOrderSend" :duration="3200" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="money" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                今日营业额
              </div>
              <count-to :start-val="0" :end-val="count.shopSumPrice" :duration="3600" :decimals="count.shopSumPrice > 0 ? 2 : 0" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="seven" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近七日营业额
              </div>
              <count-to :start-val="0" :end-val="count.lastWeekShopPrice" :decimals="count.lastWeekShopPrice > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="monthlyview" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近一个月营业额
              </div>
              <count-to :start-val="0" :end-val="count.monthShopPrice" :decimals="count.monthShopPrice > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="today" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                今日成交量
              </div>
              <count-to :start-val="0" :end-val="count.shopOrderCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="seven" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近七日成交量
              </div>
              <count-to :start-val="0" :end-val="count.lastWeekShopCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="monthlyview" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                近一个月成交量
              </div>
              <count-to :start-val="0" :end-val="count.monthShopCount" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col" v-if="user.userRole == 0">
          <div class="card-panel" >
            <div class="card-panel-icon-wrapper icon-shopping">
              <svg-icon icon-class="error" class-name="card-panel-icon" />
            </div>
            <div class="card-panel-description">
              <div class="card-panel-text">
                待处理退款
              </div>
              <count-to :start-val="0" :end-val="count.shopOrderRefund" :decimals="count.shopOrderRefund > 0 ? 2 : 0" :duration="3600" class="card-panel-num" />
            </div>
          </div>
        </el-col>
        </div>
      </el-row>

    </div>
    <div v-else>
      <img width="100%" height="100%" :src="homeImage" alt="">
    </div>
  </div>
</template>
<script>
import CountTo from 'vue-count-to'
import { mapGetters } from 'vuex'
import { gett } from '@/api/visits'
import homeImage from '@/assets/images/home.png'
export default {
  components: {
    CountTo
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  data() {
    return {
      homeImage: homeImage,
      count: {
        userCount: 0, //  会员数
        shareUserCount: 0, //  分享达人
        merCount: 0,  //  商户数
        okMerCount: 0,  //  上架商户数

        localProduct: 0,  //  卡券数量
        localProductUnder: 0,  //  待上架卡券
        localOrderWait: 0,  //  待核销订单
        localSumPrice: 0,  //  今日营业额
        lastWeekLocalPrice: 0,  //  近七日营业额
        monthLocalPrice: 0,  //  近一个月营业额
        localOrderCount: 0,  //  今日成交量
        lastWeekLocalCount: 0,  //  近七日成交量
        monthLocalCount: 0,  //  近一个月成交量
        localOrderRefund: 0,  //  待处理退款

        shopProduct: 0, //  商品数量
        shopProductUnder: 0,  //  待上架商品
        shopOrderSend: 0,   //待发货订单
        shopSumPrice: 0,  //  今日营业额
        lastWeekShopPrice: 0,  //  近七日营业额
        monthShopPrice: 0,  //  近一个月营业额
        shopOrderCount: 0,  //  今日成交量
        lastWeekShopCount: 0,  //  近七日成交量
        monthShopCount: 0,  //  近一个月成交量
        shopOrderRefund: 0,  //  待处理退款
      }
    }
  },
  mounted() {
    console.log(this.user.userRole, 'userRole')
    gett().then(res => {
      //  平台用户
      this.count.userCount = res.userCount || 0;
      this.count.shareUserCount = res.shareUserCount || 0;
      this.count.merCount = res.merCount || 0;
      this.count.okMerCount = res.okMerCount || 0;
      //  本地生活
      this.count.localProduct = res.localProduct || 0;
      this.count.localProductUnder = res.localProductUnder || 0;
      this.count.localOrderWait = res.localOrderWait || 0;
      this.count.localSumPrice = res.localSumPrice || 0;
      this.count.lastWeekLocalPrice = res.lastWeekLocalPrice || 0;
      this.count.monthLocalPrice = res.monthLocalPrice || 0;
      this.count.localOrderCount = res.localOrderCount || 0;
      this.count.lastWeekLocalCount = res.lastWeekLocalCount || 0;
      this.count.monthLocalCount = res.monthLocalCount || 0;
      this.count.localOrderRefund = res.localOrderRefund || 0;
      //  商城
      this.count.shopProduct = res.shopProduct || 0;
      this.count.shopProductUnder = res.shopProductUnder || 0;
      this.count.shopOrderSend = res.shopOrderSend || 0;
      this.count.shopSumPrice = res.shopSumPrice || 0;
      this.count.lastWeekShopPrice = res.lastWeekShopPrice || 0;
      this.count.monthShopPrice = res.monthShopPrice || 0;
      this.count.shopOrderCount = res.shopOrderCount || 0;
      this.count.lastWeekShopCount = res.lastWeekShopCount || 0;
      this.count.monthShopCount = res.monthShopCount || 0;
      this.count.shopOrderRefund = res.shopOrderRefund || 0;
    })
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .panel-group {
    margin-top: 18px;

    .panel-group-head {
      display: flex;
      align-items: center;
      padding-left: 30px;
      .panel-group-title{
        line-height: 40px;
        color: rgba(0,0,0,0.45);
        font-size: 18px;
        font-weight: bold;
        padding: 0 10px;
      }
      .division {
        width: 50px;
        height: 1px;
        background: rgba(0,0,0,0.45);
      }
    }

    .card-panel-col {
      margin-bottom: 32px;
    }

    .card-panel {
      height: 108px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);



      .icon-people {
        color: #40c9c6;
      }

      .icon-message {
        color: #36a3f7;
      }

      .icon-money {
        color: #f4516c;
      }

      .icon-shopping {
        color: #34bfa3
      }

      .card-panel-icon-wrapper {
        float: left;
        margin: 14px 0 0 14px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }

      .card-panel-icon {
        float: left;
        font-size: 48px;
      }

      .card-panel-description {
        float: right;
        font-weight: bold;
        margin: 26px;
        margin-left: 0px;
        text-align: right;

        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }

        .card-panel-num {
          font-size: 20px;
          text-align: right;
        }
      }
    }
  }
.panel-col-box{
  border:1px solid #E4E7ED;
  width:100%;
  padding-top:20px;
}
  @media (max-width:550px) {
    .card-panel-description {
      display: none;
    }

    .card-panel-icon-wrapper {
      float: none !important;
      width: 100%;
      height: 100%;
      margin: 0 !important;

      .svg-icon {
        display: block;
        margin: 14px auto !important;
        float: none !important;
      }
    }
  }
</style>
