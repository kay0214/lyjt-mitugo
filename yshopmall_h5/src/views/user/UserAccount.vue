<template>
  <div class="my-account">
    <div class="wrapper">
      <div class="header">
        <div class="headerCon">
          <div class="account acea-row row-top row-between">
            <div class="assets">
              <div>总资产(元)</div>
              <div class="money">{{ now_money }}</div>
            </div>
            <router-link :to="'/user/Recharge/'" class="recharge font-color-red"
              >充值
            </router-link>
          </div>
          <div class="cumulative acea-row row-top">
            <div class="item">
              <div>累计消费(元)</div>
              <div class="money">{{ orderStatusSum }}</div>
            </div>
            <div class="item">
              <div>累计充值(元)</div>
              <div class="money">{{ recharge }}</div>
            </div>
          </div>
        </div>
      </div>
      <div class="nav acea-row row-middle">
        <router-link class="item" :to="'/user/bill/0'">
          <div class="pictrue"><img src="@assets/images/record1.png" /></div>
          <div>账单记录</div>
        </router-link>
        <router-link class="item" :to="'/user/bill/1'">
          <div class="pictrue"><img src="@assets/images/record2.png" /></div>
          <div>消费记录</div>
        </router-link>
        <router-link class="item" :to="'/user/bill/2'">
          <div class="pictrue"><img src="@assets/images/record3.png" /></div>
          <div>充值记录</div>
        </router-link>
      </div>
      <div class="advert acea-row row-between-wrapper"></div>
    </div>
    <Recommend></Recommend>
  </div>
</template>
<script>
import Recommend from "@components/Recommend";
import { getBalance } from "../../api/user";
export default {
  name: "UserAccount",
  components: {
    Recommend
  },
  props: {},
  data: function() {
    return {
      now_money: 0,
      orderStatusSum: 0,
      recharge: 0,
      activity: {
        is_bargin: false,
        is_pink: false,
        is_seckill: false
      }
    };
  },
  mounted: function() {
    this.getIndex();
  },
  methods: {
    getIndex: function() {
      let that = this;
      getBalance().then(
        res => {
          that.now_money = res.data.now_money;
          that.orderStatusSum = res.data.orderStatusSum;
          that.recharge = res.data.recharge;
        },
        err => {
          that.$dialog.message(err.msg);
        }
      );
    }
  }
};
</script>
