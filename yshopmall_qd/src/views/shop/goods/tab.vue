<template>
  <el-tabs v-model="activeName"  type="card" style="padding: 20px 20px 45px 20px;" @tab-click="tabClick">
    <!-- <el-tab-pane label="出售中产品" name="first">
      <onSale ref="onSale" />
    </el-tab-pane>
    <el-tab-pane label="待上架产品" name="second">
      <unonSale ref="unonSale" />
    </el-tab-pane>
    <el-tab-pane label="产品回收站" name="thrid">
      <recycle ref="recycle" />
    </el-tab-pane> -->
     <el-tab-pane  name="first">
         <onSale ref="onSale" />
      <span slot="label">出售中产品</span>
    </el-tab-pane>
    <el-tab-pane  name="second">
      <unonSale ref="unonSale" />

      <span slot="label">待上架产品</span>
    </el-tab-pane>
    <el-tab-pane name="thrid">
      <recycle ref="recycle" />

       <span slot="label">产品回收站</span>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import onSale from '@/views/shop/goods/index'
import unonSale from '@/views/shop/goods/index2'
import recycle from '@/views/shop/goods/index3'
import '@/assets/styles/description.scss'
export default {
  name: 'Tab',
  components: { onSale, unonSale, recycle },
  data() {
    return {
      activeName: 'first'
    }
  },
  methods: {
    tabClick(name) {
      if (this.activeName === 'first') {
        this.$refs.onSale.init()
      } else if (this.activeName === 'second') {
        this.$refs.unonSale.init()
      } else {
        this.$refs.recycle.init()
      }
    }
  }
}
</script>

<style scoped>
</style>
