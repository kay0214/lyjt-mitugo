<template>
  <div
    class="home"
    :style="{ top: top + 'px' }"
    style="position:fixed;"
    id="right-nav"
    @touchmove="touchmove($event)"
  >
    <div class="homeCon bg-color-red1" :class="homeActive === true ? 'on' : ''">
      <router-link
        :to="'/'"
        class="iconfont icon-shouye-xianxing "
        style="color: green;"
      ></router-link>
      <router-link
        :to="'/cart'"
        class="iconfont icon-caigou-xianxing"
        style="color: green;"
      ></router-link>
      <!--<router-link :to="'/user'" class="iconfont icon-yonghu1"></router-link>-->
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
export default {
  name: "Home",
  props: {},
  data: function() {
    return {
      top: "",
      homeActive: true
    };
  },
  computed: mapGetters(["homeActive"]),
  methods: {
    touchmove(event) {
      event.preventDefault();
      let top =
        event.touches[0].pageY -
        (document.documentElement.scrollTop || document.body.scrollTop) -
        this.$el.clientHeight;

      if (top > 390) top = 390;
      else if (top < 55) top = 55;
      this.top = top;
    },
    open: function() {
      this.homeActive
        ? this.$store.commit("CLOSE_HOME")
        : this.$store.commit("OPEN_HOME");
    }
  }
};
</script>
<style scoped>
.mystyl {
  display: inline-block;
  width: 0.64rem;
  height: 0.64rem;
  margin-top: 0.12rem;
  box-sizing: border-box;
  border: 1px solid #e1e1e1;
  border-radius: 50%;
  background-size: 1.24rem auto;
  background-repeat: no-repeat;
  background-color: rgba(255, 255, 255, 0.9);
}
</style>
