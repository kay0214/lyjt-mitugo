<template>
  <div class="OrderCancellation">
    <div class="header"></div>
    <div class="whiteBg">
      <div class="input">
        <input type="number" placeholder="0" v-model="verify_code" />
      </div>
      <div class="bnt" @click="storeCancellation">立即核销</div>
    </div>
    <div class="scan" v-if="iswechat">
      <img src="@assets/images/scan.gif" @click="openQRCode" />
    </div>
    <WriteOff
      :iShidden="iShidden"
      :orderInfo="orderInfo"
      @cancel="cancel"
      @confirm="confirm"
    ></WriteOff>
  </div>
</template>
<style scoped>
.OrderCancellation .header {
  background: url("../../assets/images/writeOffBg.jpg") no-repeat;
  width: 100%;
  height: 3rem;
  background-size: 100% 100%;
}
.OrderCancellation .whiteBg {
  width: 6.9rem;
  background-color: #fff;
  margin: -0.93rem auto 0 auto;
  padding-top: 0.8rem;
  border-radius: 0.06rem 0.06rem 0 0;
}
.OrderCancellation .whiteBg .input {
  width: 5.8rem;
  margin: 0 auto;
  border-bottom: 0.01rem solid #eee;
}
.OrderCancellation .whiteBg .input input {
  padding-bottom: 0.25rem;
  font-size: 0.6rem;
  color: #282828;
  width: 100%;
  text-align: center;
}
.OrderCancellation .whiteBg .bnt {
  font-size: 0.32rem;
  color: #fff;
  width: 5.8rem;
  height: 0.86rem;
  border-radius: 0.43rem;
  background-image: linear-gradient(to right, #00c17b 0%, #00c17b 100%);
  background-image: -webkit-linear-gradient(to right, #00c17b 0%, #00c17b 100%);
  background-image: -moz-linear-gradient(to right, #00c17b 0%, #00c17b 100%);
  text-align: center;
  line-height: 0.86rem;
  margin: 0.55rem auto 0 auto;
}
.OrderCancellation .scan {
  width: 3rem;
  height: 3rem;
  margin: 1.1rem auto 0 auto;
}
.OrderCancellation .scan img {
  width: 100%;
  height: 100%;
  display: block;
}
</style>
<script>
import WriteOff from "@components/WriteOff";
import { isWeixin } from "@utils";
import { wechatEvevt } from "@libs/wechat";
import { orderVerific } from "@api/order";
const NAME = "OrderCancellation";

export default {
  name: NAME,
  components: {
    WriteOff
  },
  props: {},
  data: function() {
    return {
      iShidden: true,
      iswechat: isWeixin(),
      orderInfo: {},
      verify_code: ""
    };
  },
  mounted: function() {},
  methods: {
    cancel: function(res) {
      this.iShidden = res;
    },
    confirm: function() {
      orderVerific(this.verify_code, 1)
        .then(res => {
          this.iShidden = true;
          this.verify_code = "";
          this.$dialog.success(res.msg);
        })
        .catch(res => {
          this.$dialog.error(res.msg);
        });
    },
    storeCancellation: function() {
      let ref = /[0-9]{12}/;
      if (!this.verify_code) return this.$dialog.error("请输入核销码");
      if (!ref.test(this.verify_code))
        return this.$dialog.error("请输入正确的核销码");
      console.log(9999933);
      this.$dialog.loading.open("查询中");
      orderVerific(this.verify_code, 0)
        .then(res => {
          this.$dialog.loading.close();
          this.orderInfo = res.data;
          this.iShidden = false;
        })
        .catch(res => {
          this.$dialog.loading.close();
          this.$dialog.error(res.msg);
        });
    },
    openQRCode: function() {
      let that = this;
      wechatEvevt("scanQRCode", {
        needResult: 1,
        scanType: ["qrCode", "barCode"]
      })
        .then(res => {
          if (res.resultStr) {
            that.verify_code = res.resultStr;
            that.storeCancellation();
          } else that.$dialog.error("没有扫描到什么！");
        })
        .catch(res => {
          console.log(res);
          if (res.is_ready) {
            res.wx.scanQRCode({
              needResult: 1,
              scanType: ["qrCode", "barCode"],
              success: function(res) {
                that.verify_code = res.resultStr;
                that.storeCancellation();
              },
              fail: function(res) {
                that.$dialog.error(res.toString());
                console.log(res);
                if (res.errMsg == "scanQRCode:permission denied") {
                  that.$dialog.error("没有权限");
                }
              }
            });
          }
        });
    }
  }
};
</script>
