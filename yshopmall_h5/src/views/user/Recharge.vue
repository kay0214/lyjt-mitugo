<template>
  <div>
    <div class="payment-top acea-row row-column row-center-wrapper">
      <span class="name">我的余额</span>
      <div class="pic">
        ￥<span class="pic-font">{{ now_money || 0 }}</span>
      </div>
    </div>
    <div class="recharge">
      <div class="nav acea-row row-around row-middle">
        <div class="item on">
          账户充值
        </div>
      </div>
      <div class="info-wrapper">
        <div class="picList acea-row row-between mt-20">
          <div
            class="pic-box pic-box-color acea-row row-center-wrapper row-column"
            :class="activePic === index ? 'pic-box-color-active' : ''"
            v-for="(item, index) in picList"
            :key="index"
            @click="picCharge(index, item)"
          >
            <div class="pic-number-pic">
              {{ item.price }}<span class="pic-number"> 元</span>
            </div>
            <div class="pic-number">赠送：{{ item.give_price }} 元</div>
          </div>
          <div
            class="pic-box pic-box-color acea-row row-center-wrapper"
            :class="activePic == picList.length ? 'pic-box-color-active' : ''"
            @click="picCharge(picList.length, money)"
          >
            <input
              type="number"
              placeholder="其他"
              v-model="money"
              class="pic-box-money pic-number-pic"
              :class="activePic == picList.length ? 'pic-box-color-active' : ''"
            />
          </div>
        </div>
        <div class="tip">
          提示：充值后帐户的金额不能提现
        </div>
        <div class="pay-btn bg-color-red" @click="recharge">
          立即充值
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { pay } from "@libs/wechat";
import { isWeixin } from "@utils";
import { rechargeWechat, getRechargeApi } from "@api/user";
import { add, sub } from "@utils/bc";

export default {
  name: "Recharge",
  components: {},
  props: {},
  data: function() {
    return {
      active: 0,
      payType: ["weixin"],
      from: isWeixin() ? "weixin" : "weixinh5",
      money: "",
      now_money: "",
      picList: [],
      activePic: 0,
      numberPic: "",
      paid_price: ""
    };
  },
  computed: mapGetters(["userInfo"]),
  mounted: function() {
    this.now_money = this.userInfo.nowMoney;
    this.getRecharge();
  },
  methods: {
    /**
     * 充值额度选择
     */
    getRecharge() {
      getRechargeApi()
        .then(res => {
          this.picList = res.data.recharge_price_ways || [];
          if (this.picList[0]) {
            this.paid_price = this.picList[0].price;
            this.numberPic = this.picList[0].give_price;
          }
        })
        .catch(res => {
          this.$dialog.toast({ mes: res });
        });
    },
    /**
     * 选择金额
     */
    picCharge(idx, item) {
      this.activePic = idx;
      if (idx == this.picList.length) {
        this.paid_price = "";
        this.numberPic = "";
      } else {
        this.money = "";
        this.paid_price = item.give_price;
        this.numberPic = item.price;
      }
    },
    recharge: function() {
      let that = this,
        price = Number(this.money);
      if (this.picList.length == this.activePic && price === 0) {
        return that.$dialog.toast({ mes: "请输入您要充值的金额" });
      } else if (this.picList.length == this.activePic && price < 0.01) {
        return that.$dialog.toast({ mes: "充值金额不能低于0.01" });
      }
      let prices = "",
        paid_price = "";
      if (price) {
        prices = price;
        paid_price = 0;
      } else {
        prices = that.numberPic;
        paid_price = that.paid_price;
      }
      rechargeWechat({ price: prices, from: that.from, paidPrice: paid_price })
        .then(res => {
          var data = res.data;
          //console.log("aaa:"+data.data)
          if (data.type == "weixinh5") {
            location.replace(data.data);
          } else {
            pay(data.data)
              .then(() => {
                prices = add(prices, paid_price);
                that.now_money = add(
                  prices,
                  parseFloat(that.userInfo.nowMoney)
                );
                that.$dialog.toast({ mes: "支付成功" });
              })
              .finally(res => {
                //if(typeof(res) == "undefined") return
              })
              .catch(function() {
                that.$dialog.toast({ mes: "支付失败" });
              });
          }
        })
        .catch(res => {
          that.$dialog.toast({ mes: res.msg });
        });
    }
  }
};
</script>
<style scoped>
#iframe {
  display: none;
}
.pic-box-color-active {
  background-color: #00c17b !important;
  color: #fff !important;
}
.picList {
  margin-bottom: 0.3rem;
  margin-top: 0.3rem;
}
.font-color {
  color: #e83323;
}
.recharge {
  border-radius: 0.1rem;
  width: 100%;
  background-color: #fff;
  margin: 0.2rem auto 0 auto;
  padding: 0.3rem;
  border-top-right-radius: 0.39rem;
  border-top-left-radius: 0.39rem;
  margin-top: -0.45rem;
  box-sizing: border-box;
}
.recharge .nav {
  height: 0.75rem;
  line-height: 0.75rem;
  padding: 0 1rem;
}
.recharge .nav .item {
  font-size: 0.3rem;
  color: #333;
}
.recharge .nav .item.on {
  font-weight: bold;
  border-bottom: 0.04rem solid #e83323;
}
.recharge .info-wrapper {
}
.recharge .info-wrapper .money {
  margin-top: 0.6rem;
  padding-bottom: 0.2rem;
  border-bottom: 1px dashed #ddd;
  text-align: center;
}
.recharge .info-wrapper .money span {
  font-size: 0.56rem;
  color: #333;
  font-weight: bold;
}
.recharge .info-wrapper .money input {
  display: inline-block;
  width: 3rem;
  font-size: 0.84rem;
  text-align: center;
  color: #282828;
  font-weight: bold;
  padding-right: 0.7rem;
}
.recharge .info-wrapper .money input::placeholder {
  color: #ddd;
}
.recharge .info-wrapper .money input::-webkit-input-placeholder {
  color: #ddd;
}
.recharge .info-wrapper .money input:-moz-placeholder {
  color: #ddd;
}
.recharge .info-wrapper .money input::-moz-placeholder {
  color: #ddd;
}
.recharge .info-wrapper .money input:-ms-input-placeholder {
  color: #ddd;
}
.tip {
  font-size: 0.28rem;
  color: #333333;
  font-weight: 800;
  margin-bottom: 0.14rem;
}
.recharge .info-wrapper .tips span {
  color: #ef4a49;
}
.recharge .info-wrapper .pay-btn {
  display: block;
  width: 100%;
  height: 0.86rem;
  margin: 0.5rem auto 0 auto;
  line-height: 0.86rem;
  text-align: center;
  color: #fff;
  border-radius: 0.5rem;
  font-size: 0.3rem;
  font-weight: bold;
}
.payment-top {
  width: 100%;
  height: 3.5rem;
  background-color: #00c17b;
}
.payment-top .name {
  font-size: 0.26rem;
  color: rgba(255, 255, 255, 0.8);
  margin-top: -0.38rem;
  margin-bottom: 0.3rem;
}
.payment-top .pic {
  font-size: 0.32rem;
  color: #fff;
}
.payment-top .pic-font {
  font-size: 0.78rem;
  color: #fff;
}
.picList .pic-box {
  width: 32%;
  height: auto;
  border-radius: 0.2rem;
  margin-top: 0.21rem;
  padding: 0.2rem 0;
}
.pic-box-color {
  background-color: #f4f4f4;
  color: #656565;
}
.pic-number {
  font-size: 0.22rem;
}
.pic-number-pic {
  font-size: 0.38rem;
  margin-right: 0.1rem;
  text-align: center;
}
.pic-box-money {
  width: 100%;
  display: block;
}
</style>
