<template>
  <div class="distribution-posters">
    <div class="slider-banner banner">
      <swiper class="swiper-wrapper" :options="swiperPosters" ref="mySwiper">
        <swiperSlide
          class="swiper-slide"
          v-for="(item, index) in info"
          :key="index"
        >
          <img class="slide-image" :src="item.wap_poster" />
        </swiperSlide>
      </swiper>
    </div>
    <div class="preserve acea-row row-center-wrapper">
      <div class="line"></div>
      <div class="tip">长按保存图片</div>
      <div class="line"></div>
    </div>
  </div>
</template>
<style>
.preserve {
  color: #fff;
  text-align: center;
  margin-top: 0.2rem;
}
.preserve .line {
  width: 1rem;
  height: 0.01rem;
  background-color: #fff;
}
.preserve .tip {
  margin: 0 0.3rem;
}
</style>
<script>
import { swiper, swiperSlide } from "vue-awesome-swiper";
import "@assets/css/swiper.min.css";
import { getSpreadImg } from "../../../api/user";
export default {
  name: "Poster",
  components: {
    swiper,
    swiperSlide
  },
  props: {},
  data: function() {
    return {
      swiperPosters: {
        speed: 1000,
        effect: "coverflow",
        slidesPerView: "auto",
        centeredSlides: true,
        coverflowEffect: {
          rotate: 0,
          stretch: -20,
          depth: 100,
          modifier: 3,
          slideShadows: false
        },
        observer: true,
        observeParents: true
      },
      info: [],
      activeIndex: 0
    };
  },
  mounted: function() {
    this.getIndex();
    let that = this;
    this.swiper.on("slideChange", function() {
      that.activeIndex = that.swiper.activeIndex;
    });
  },
  computed: {
    swiper() {
      return this.$refs.mySwiper.swiper;
    }
  },
  methods: {
    getIndex: function() {
      let that = this;
      getSpreadImg().then(
        res => {
          that.info = res.data;
        },
        err => {
          that.$dialog.message(err.msg);
        }
      );
    }
  }
};
</script>
