<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="query.type" clearable placeholder="类型" class="filter-item" style="width: 130px">
        <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="query.isBest" clearable placeholder="精品推荐"
                 style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in pstatusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-select v-model="query.isHot" clearable placeholder="热销榜单"
                 style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in pstatusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
<!--      <el-select v-model="query.isShow" clearable placeholder="状态"-->
<!--                 style="width: 200px;" class="filter-item">-->
<!--        <el-option-->
<!--          v-for="item in statusList"-->
<!--          :key="item.value"-->
<!--          :label="item.label"-->
<!--          :value="item.value">-->
<!--        </el-option>-->
<!--      </el-select>-->
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
         v-permission="permission.edit"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add"
        >新增</el-button>
        <el-button
          type="danger"
          class="filter-item"
          size="mini"
          icon="el-icon-refresh"
          @click="toQuery"
        >刷新</el-button>
      </div>
    </div>
    <!--表单组件-->
    <h5Form ref="h5" />
    <eForm ref="form" :is-add="isAdd" />
    <eAttr ref="form2" :is-attr="isAttr" />
    <comForm ref="form3" :is-add="isAdd" />
    <killForm ref="form4" :is-add="isAdd" />
    <bargainForm ref="form5" :is-add="isAdd" />
    <commission ref="form6"/>
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <el-table-column prop="id" label="商品id" />
      <el-table-column ref="table" prop="image" label="商品图片">
        <template slot-scope="scope">
          <a :href="scope.row.image" style="color: #42b983" target="_blank"><img :src="scope.row.image" alt="点击打开" class="el-avatar"></a>
        </template>
      </el-table-column>
      <el-table-column prop="store.storeName" label="商铺名称" />
      <el-table-column prop="storeName" label="商品名称" />
      <el-table-column prop="storeCategory.cateName" label="分类名称" />
      <el-table-column prop="price" label="商品价格" />
      <el-table-column prop="sales" label="销量" />
      <el-table-column prop="stock" label="库存" />
      <el-table-column prop="commission" label="佣金" />
      <el-table-column prop="isBest" label="精品推荐" >
          <template slot-scope="scope">
            <div @click="changeHotStatus(scope.row.id,scope.row.isBest,hotType.best)">
              <el-tag v-if="scope.row.isBest == 1">是</el-tag>
              <el-tag v-else-if="scope.row.isBest == 0" :type=" 'info' ">否</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column>
      <el-table-column prop="isHot" label="热销榜单" >
          <template slot-scope="scope">
            <div @click="changeHotStatus(scope.row.id,scope.row.isHot,hotType.hot)">
              <el-tag v-if="scope.row.isHot == 1">是</el-tag>
              <el-tag v-else-if="scope.row.isHot == 0" :type=" 'info' ">否</el-tag>
              <el-tag v-else></el-tag>
            </div>
          </template>
        </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div @click="onSale(scope.row.id,scope.row.isShow)">
            <el-tag v-if="scope.row.isShow === 1" style="cursor: pointer" :type="''">已上架</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">已下架</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="205px" align="center">
        <template slot-scope="scope">
          <el-button v-permission="permission.edit" slot="reference" type="danger" size="mini" @click="attr(scope.row)">规格属性</el-button>
          <el-dropdown v-permission="permission.edit" size="mini" split-button type="primary" trigger="click">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  @click="edit(scope.row)"
                >编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-popover
                  :ref="scope.row.id"
                  placement="top"
                  width="180"
                >
                  <p>确定删除本条数据吗？</p>
                  <div style="text-align: right; margin: 0">
                    <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
                    <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
                  </div>
                  <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini">删除</el-button>
                </el-popover>
              </el-dropdown-item>
              <!--<el-dropdown-item>
                <el-button
                  size="mini"
                  type="success"
                  @click="editC(scope.row)"
                >促销单品</el-button>
              </el-dropdown-item>-->
              <!--<el-dropdown-item>
                <el-button
                  size="mini"
                  type="success"
                  @click="editC(scope.row)"
                >开启拼团</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="primary"
                  @click="editD(scope.row)"
                >开启秒杀</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  size="mini"
                  type="warning"
                  @click="editE(scope.row)"
                >开启砍价</el-button>
              </el-dropdown-item>-->
            </el-dropdown-menu>
          </el-dropdown>
          <el-button v-permission="permission.edit" slot="reference" type="info" plain size="mini" @click="h5(scope.row)">预览</el-button>
          <el-button v-permission="permission.commission"plain type="primary"  @click="commission(scope.row)" style="margin-top:5px">分佣配置</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <el-pagination
      :total="total"
      :current-page="page + 1"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"
    />
  </div>
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del, onsale, changeStatus } from '@/api/yxStoreProduct'
import eForm from './form'
import h5Form from './h5'
import eAttr from './attr'
import commission from './commission'
import comForm from '@/views/activity/combination/form'
import killForm from '@/views/activity/seckill/form'
import bargainForm from '@/views/activity/bargain/form'
import yxCustomizeRate from '../../../api/yxCustomizeRate'
export default {
  components: { eForm, eAttr, comForm, killForm, bargainForm, commission,h5Form },
  mixins: [initData],
  data() {
    return {
      permission: {
        edit: ['admin', 'YXSTOREPRODUCT_EDIT'],
        change: ['admin', 'YXSTOREPRODUCT_CHANGE'],
        commission: ['admin', 'YXSTOREPRODUCT_RATE'],
      },
      delLoading: false,
      visible: false,
      hotType:{
        benefit:{
          value:'benefit',
          label:'促销'
        },
        best :{
          value:'best',
          label:'精品'
        },
        hot :{
          value:'hot',
          label:'热卖'
        },
        new :{
          value:'new',
          label:'新品'
        },
      },
      showFlg:0,//精品热销显示状态
      queryTypeOptions: [
        { key: 'storeName', display_name: '商品名称' },
        { key: 'merUsername', display_name: '商户用户名' }
      ],
      isAttr: false,
      statusList:[
        {value:0,label:'已下架'},
        {value:1,label:'已上架'}
      ],
      pstatusList:[
        {value:1,label:'是'},
        {value:0,label:'否'}
      ]
    }
  },
  created() {
    this.$nextTick(() => {
      this.init().then(res=>{
        this.showFlg=res.showFlg
      })
    })
  },
  methods: {
    checkPermission,
    beforeInit() {
      this.url = 'api/yxStoreProduct'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort, isShow: 1, isDel: 0 }
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
        console.log(err.response.data.message)
      })
    },
    onSale(id, status) {
      let ret=checkPermission(this.permission.edit)
      if(!ret){
        return ret
      }
      this.$confirm(`确定进行[${status ? '下架' : '上架'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          onsale(id, { status: status }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.init()
              }
            })
          })
        })
        .catch(() => { })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
      this.$refs.form.getCates()
    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.getCates()
      console.log('jianan')
      console.log(data.storeCategory)
      console.log(data.settlement)
      _this.form = {
        id: data.id,
        merId: data.merId,
        image: data.image,
        sliderImage: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        sliderVideo:data.video!=''?data.video.split(','):[],
        storeName: data.storeName,
        storeInfo: data.storeInfo,
        keyword: data.keyword,
        barCode: data.barCode,
        storeCategory: data.cateFlg?( data.storeCategory || {id:null}):{id:null},
        storeCategoryId: data.cateFlg?data.storeCategory.id:null,
        price: data.price,
        vipPrice: data.vipPrice,
        otPrice: data.otPrice,
        postage: data.postage,
        unitName: data.unitName,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isHot: data.isHot,
        isBenefit: data.isBenefit,
        isBest: data.isBest,
        isNew: data.isNew,
        description: data.description,
        addTime: data.addTime,
        isPostage: data.isPostage,
        isDel: data.isDel,
        merUse: data.merUse,
        giveIntegral: data.giveIntegral,
        cost: data.cost,
        isSeckill: data.isSeckill,
        isBargain: data.isBargain,
        isGood: data.isGood,
        ficti: data.ficti,
        browse: data.browse,
        codePath: data.codePath,
        soureLink: data.soureLink,
        settlement: data.settlement,
        commission: data.commission
      }
      _this.dialog = true
    },
    editC(data) {
      this.isAdd = false
      const _this = this.$refs.form3
      _this.form = {
        productId: data.id,
        isBenefit: data.isBenefit,
        /*merId: data.merId,
        image: data.image,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isHost: data.isHot,*/
        /*description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0,
        effectiveTime: 24,
        combination: 1,
        cost: data.cost,
        isDel: 0,
        browse: 0*/
      }
      _this.dialog = true
    },
    editD(data) {
      this.isAdd = false
      const _this = this.$refs.form4
      _this.form = {
        productId: data.id,
        merId: data.merId,
        image: data.image,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        status: 1,
        isHot: data.isHot,
        description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0.01,
        effectiveTime: 24,
        otPrice: data.otPrice,
        cost: data.cost,
        num: 1,
        giveIntegral: 0,
        isDel: 0,
        browse: 0
      }
      _this.dialog = true
    },
    editE(data) {
      this.isAdd = false
      const _this = this.$refs.form5
      _this.form = {
        productId: data.id,
        merId: data.merId,
        image: data.image,
        images: data.sliderImage,
        imageArr: data.image.split(','),
        sliderImageArr: data.sliderImage.split(','),
        title: data.storeName,
        info: data.storeInfo,
        postage: data.postage,
        unitName: data.unitName,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        status: 1,
        isHot: data.isHot,
        description: data.description,
        isPostage: data.isPostage,
        people: 0,
        price: 0.01,
        effectiveTime: 24,
        otPrice: data.otPrice,
        cost: data.cost,
        num: 1,
        giveIntegral: 0,
        isDel: 0,
        browse: 0
      }
      _this.dialog = true
    },
    attr(data) {
      console.log(3333)
      this.isAttr = false
      const _this = this.$refs.form2
      _this.form = {
        id: data.id,
        merId: data.merId,
        image: data.image,
        sliderImage: data.sliderImage,
        storeName: data.storeName,
        storeInfo: data.storeInfo,
        keyword: data.keyword,
        barCode: data.barCode,
        storeCategory: data.storeCategory,
        price: data.price,
        vipPrice: data.vipPrice,
        otPrice: data.otPrice,
        postage: data.postage,
        unitName: data.unitName,
        sort: data.sort,
        sales: data.sales,
        stock: data.stock,
        isShow: data.isShow,
        isHot: data.isHot,
        isBenefit: data.isBenefit,
        isBest: data.isBest,
        isNew: data.isNew,
        description: data.description,
        addTime: data.addTime,
        isPostage: data.isPostage,
        isDel: data.isDel,
        merUse: data.merUse,
        settlement:data.settlement,
        giveIntegral: data.giveIntegral,
        cost: data.cost,
        isSeckill: data.isSeckill,
        isBargain: data.isBargain,
        isGood: data.isGood,
        ficti: data.ficti,
        browse: data.browse,
        codePath: data.codePath,
        soureLink: data.soureLink
      }
      _this.dialog = true
      this.$refs.form2.getAttrs(data.id)
    },
    h5(data) {
      this.$refs.h5.dialog = true
      this.$refs.h5.id=data.id
    },
    changeHotStatus(id,status,type){//设置精品或热销
      let ret=checkPermission(this.permission.change)
      if(!ret){
        return ret
      }
      this.$confirm(`确定 [${status ? '取消' : '设为'}  `+type.label+` ]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          changeStatus({
            id,
            changeStatus:status?0:1,
            changeType:type.value}).then(res => {
              if(res){
                this.init()
                this.$notify({
                  title: '设置成功',
                  type: 'success',
                  duration: 2500
                })
              }else{
                this.$notify({
                  title: '设置失败，请重试',
                  type: 'error',
                  duration: 2500
                })
              }
          }).catch(err => {
            this.$notify({
              title: err.response.data.msg,
              type: 'error',
              duration: 2500
            })
            console.log(err.response.data.msg)
          })
        })
        .catch(() => { })
    },
    commission(data) {
      const _this = this.$refs.form6
      _this.form = {
        id: data.id,
        customizeType:data.customizeType,
        yxCustomizeRate:data.yxCustomizeRate?data.yxCustomizeRate:{}
      }
      if(data.customizeType===2){
        Object.assign(_this.form2,data.yxCustomizeRate)
      }
      console.log('**********')
      console.log(_this.form)
      _this.dialog = true
    }
  }
}
</script>

<style scoped>

</style>
