<template>
  <div class="logistics">
    <div
      class="header acea-row row-between row-top"
      v-for="cart in cartInfo"
      :key="cart.id"
    >
      <div class="pictrue"><img :src="cart.productInfo.image" /></div>
      <div class="text acea-row row-between">
        <div class="name line2">
          {{ cart.productInfo.store_name }}
        </div>
        <div class="money">
          <div>￥{{ cart.truePrice }}</div>
          <div>x{{ cart.cart_num }}</div>
        </div>
      </div>
    </div>
    <div class="logisticsCon" style="margin-bottom: 5px">
      <div class="company acea-row row-between-wrapper">
        <div class="picTxt acea-row row-between-wrapper">
          <div class="iconfont icon-wuliu"></div>
          <div class="text">
            <div>
              <span class="name line1">物流公司：</span>
              {{ orderInfo.deliveryName }}
            </div>
            <div class="express line1">
              <span class="name">快递单号：</span> {{ orderInfo.deliveryId }}
            </div>
          </div>
        </div>
        <div
          class="copy acea-row row-center-wrapper copy-data"
          :data-clipboard-text="orderInfo.deliveryId"
        >
          复制单号
        </div>
      </div>
      <div class="item" v-for="(express, index) in expressList" :key="index">
        <div class="circular" :class="index === 0 ? 'on' : ''"></div>
        <div class="text">
          <div :class="index === 0 ? 'font-color-red' : ''">
            {{ express.status }}
          </div>
          <div class="data">{{ express.time }}</div>
        </div>
      </div>
    </div>

    <!-- 物流进度条 -->
    <div class="div-bg bg-white" style="font-size:12px; background:#fff;">
      <!--物流跟踪-->
      <div style="margin-bottom:5px;">
        <div
          class="bg-white"
          style="width: 92%; margin-left: 4%;margin: auto;padding-left: 15px;padding-right: 15px;padding-top: 10px"
        >
          <div style="font-size: .26rem;color: #111111; margin: 5px 0">
            物流跟踪<!--物流跟踪-->
          </div>
          <div>
            <div class="track-rcol">
              <div class="track-list">
                <ul>
                  <div v-for="(item, index) in logisticsList" :key="index">
                    <li class="active" v-if="index === 0">
                      <div></div>
                      <i class="node-icon"></i>
                      <span class="txt">{{ item.acceptStation }}</span>
                      <span class="time">{{ item.acceptTime }}</span>
                    </li>
                    <li v-if="index > 0 && index !== logisticsList.length - 1">
                      <i class="node-icon"></i>
                      <span class="txt">{{ item.acceptStation }}</span>
                      <span class="time">{{ item.acceptTime }}</span>
                    </li>
                    <li
                      v-if="index === logisticsList.length - 1"
                      class="finall"
                    >
                      <i class="div-spilander"></i>
                      <i class="node-icon"></i>
                      <span class="txt">{{ item.acceptStation }}</span>
                      <span class="time">{{ item.acceptTime }}</span>
                    </li>
                  </div>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="no-express" v-if="loaded && !expressList.length">
      <img src="@assets/images/noExpress.png" />
    </div>
    <Recommend></Recommend>
  </div>
</template>
<script>
import Recommend from "@components/Recommend";
import ClipboardJS from "clipboard";
import { express, orderDetail } from "@api/order";

const NAME = "Logistics";

export default {
  name: NAME,
  components: {
    Recommend
  },
  data: function() {
    return {
      id: this.$route.params.id,
      cartInfo: [],
      orderInfo: {},
      expressList: [],
      loaded: false,
      logisticsList: [
        {
          message: "暂无数据",
          messageDate: ""
        }
      ]
    };
  },
  watch: {
    $route(n) {
      if (n.name === NAME && this.$route.params.id !== this.id) {
        this.id = this.$route.params.id;
        this.getExpress();
      }
    }
  },
  mounted: function() {
    this.getExpress();
  },
  methods: {
    getExpressInfo() {
      let params = {
        orderCode: this.id,
        shipperCode: this.orderInfo.deliverySn,
        logisticCode: this.orderInfo.deliveryId
      };
      express(params)
        .then(res => {
          this.logisticsList = res.data.traces.reverse();
        })
        .catch(e => {
          this.$dialog.error(e.msg || "加载失败");
        });
    },
    getExpress() {
      if (!this.id) return this.$dialog.error("订单不存在");
      this.loaded = false;
      orderDetail(this.id)
        .then(res => {
          this.orderInfo = {
            deliveryId: res.data.deliveryId,
            deliveryName: res.data.deliveryName,
            deliverySn: res.data.deliverySn
          };
          this.getExpressInfo();
          this.$nextTick(function() {
            var copybtn = document.getElementsByClassName("copy-data");
            const clipboard = new ClipboardJS(copybtn);
            clipboard.on("success", () => {
              this.$dialog.success("复制成功");
            });
          });
        })
        .catch(e => {
          this.$dialog.error(e.msg || "加载失败");
        });
    }
  }
};
</script>

<style scoped>
.no-express {
  margin: 1.5rem 0;
}

.no-express img {
  width: 6rem;
  margin: 0 auto;
  display: block;
}
.message-text {
  font-family: MicrosoftYaHei;
  font-size: 1rem;
  font-weight: normal;
  font-stretch: normal;
  line-height: 3rem;
  letter-spacing: 0rem;
  color: #333333;
  width: 50%;
}
.fontblack {
  color: #999999;
}
.img2 {
  width: 0.81rem;
  height: 0.8rem;
  float: right;
}
.addressshow2 {
  height: auto;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  width: 75%;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-all;
  font-size: 1rem;
}
.addressshow1 {
  height: auto;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  width: 75%;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-all;
  font-size: 1rem;
}
.orderTitle {
  font-size: 1rem;
  color: #333333;
  height: auto;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  white-space: normal;
  word-wrap: break-word;
  word-break: break-all;
  height: 2.5rem;
}
.orderDetail {
  font-size: 0.26rem;
  color: #666666;
  text-align: left;
}
.border-ceter {
  width: 92%;
  padding-left: 15px;
  padding-right: 15px;
}
.pay-button {
  width: 88%;
  height: 2.6rem;
  position: relative;
  background-color: red;
  color: white;

  margin-left: 6%;
}
ul li {
  list-style: none;
  font-size: 0.24rem;
}
ul {
}
.track-rcol {
}
.track-list {
  position: relative;
}
.track-list li {
  position: relative;
  padding: 0 0 0.5rem 5px;
  line-height: 0.3rem;
  border-left: 1px solid #d9d9d9;
  color: #999;
}
.track-list li.first {
  color: red;
  padding-top: 0;
  width: 100%;
  text-align: left;
  border-left: 1px solid #d9d9d9;
}
.track-list li .node-icon {
  position: absolute;
  left: -6.5px;
  border-radius: 50%;
  width: 0.2rem;
  height: 0.2rem;
  top: 4px;
  background-color: #b2b2b2;
}
.track-list li.active .node-icon {
  background-position: 0-72px;
  background-color: #ea7c0a;
  width: 0.3rem;
  z-index: 2;
  height: 0.3rem;
  position: absolute;
  left: -0.19rem;
  top: 0;
  border-radius: 50%;
}
.track-list li .time {
  margin-right: 20px;
  position: relative;
  top: 4px;
  display: inline-block;
  vertical-align: middle;
  color: #999;
  width: 100%;
  text-align: left;
}
.track-list li .txt {
  position: relative;
  display: inline-block;
  vertical-align: top;
  color: #999;
  left: 0.2rem;
  top: 0.04rem;
}
.track-list li.first .time {
  text-align: left;
  width: 94%;
  color: red;
}
.track-list li.first .txt {
  color: red;
  text-align: left;
  width: 94%;
}
.track-list li.finall {
  position: relative;
  padding: 0px 0 0.5rem 5px;
  line-height: 18px;
  border-color: white;
  border-left: 1px solid #ffffff;
  color: #999;
}
.track-list li.finall .div-spilander {
  width: 1px;
  position: absolute;
  left: -1.5px;
  height: 0.5rem;
  background-color: #d9d9d9;
}
</style>
