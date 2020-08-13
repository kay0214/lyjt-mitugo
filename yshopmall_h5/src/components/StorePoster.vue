<template>
  <div v-if="posterImageStatus" class="poster-first">
    <div class="poster-pop" v-show="!canvasStatus">
      <img
        src="@assets/images/poster-close.png"
        class="close"
        @click="posterImageClose"
      />
      <div class="canvas" ref="poster">
        <img
                ref="posterDataImage"
          class="image"
          :src="posterData.codeBase"
        />
      </div>
    </div>
    <div class="poster-pop" v-show="canvasStatus">
      <img
        src="@assets/images/poster-close.png"
        class="close"
        @click="posterImageClose"
      />
      <img :src="posterImage" class="poster-image" crossorigin="anonymous" />
        <div class="keep">长按图片可以保存到手机</div>
    </div>
    <div class="mask"></div>

  </div>
</template>
<style scoped>
.poster-first {
  overscroll-behavior: contain;
}
.poster-pop {
  width: 4.5rem;
  height: 1rem;
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  z-index: 99;
  top: 50%;
  margin-top: -4.6rem;
}
.poster-pop .canvas {
  background-color: #ffffff;
  height: 8rem;
}
.poster-pop .poster-image {
  width: 100%;
  height: 8rem;
}
.poster-pop .canvas .image {
  width: 4.5rem;
  height: 8rem;
  display: block;
}
.poster-pop .canvas .text {
  text-align: center;
  color: #000000;
  margin-top: 0.32rem;
}
.poster-pop .canvas .text.black {
  height: 0.68rem;
}
.poster-pop .canvas .text.rad {
  color: #ff0000;
}
.poster-pop .canvas .code {
  height: 1.4rem;
  display: inline-flex;
}
.poster-pop .canvas .code .code-img {
  width: 40%;
  padding: 0.06rem;
}
.poster-pop .canvas .code .code-img img {
  width: 100%;
}
.poster-pop .canvas .code .code-text {
  width: 70%;
  font-size: 0.12rem;
  line-height: 1.8rem;
}
.poster-pop .close {
  width: 0.46rem;
  height: 0.75rem;
  position: fixed;
  right: 0;
  top: -0.73rem;
  display: block;
}
.poster-pop .save-poster {
  background-color: #df2d0a;
  font-size: 0.22rem;
  color: #fff;
  text-align: center;
  height: 0.76rem;
  line-height: 0.76rem;
  width: 100%;
  margin-top: -0.04rem;
}
.poster-pop .keep {
  color: #fff;
  text-align: center;
  font-size: 0.25rem;
  margin-top: 0.1rem;
}
.mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 9;
}
</style>
<script>
import { Loading } from 'vant';
export default {
  name: "StorePoster",
  props: {
    posterImageStatus: Boolean,
    posterData: Object
  },
  components: {
    Loading
  },
  data: function() {
    return {
      canvasStatus: false
    };
  },
  watch: {
    posterImageStatus: function() {
      var that = this;
      if (that.posterImageStatus === true) {
        this.$dialog.loading.open();
      }
    },
    "posterData.codeBase": function() {
      if(this.posterData.codeBase) this.$dialog.loading.close();
    }
  },
  mounted: function() {},
  methods: {
    posterImageClose: function() {
      this.posterImageStatus = false;
      this.canvasStatus = false;
      this.$emit("setPosterImageStatus");
    }
  }
};
</script>
