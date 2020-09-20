<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="username" clearable placeholder="输入用户昵称" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="category" clearable placeholder="明细种类" class="filter-item" style="width: 130px">
        <el-option
          v-for="item in categoryOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select v-model="type" clearable placeholder="明细类型" class="filter-item" style="width: 130px">
        <template v-for="item in typeOptions">
        <el-option
          v-for="(val,key) in item"
          :key="key"
          :label="val"
          :value="key"
        />
        </template>
      </el-select>
      <el-select v-model="pm" clearable placeholder="收支类型" class="filter-item" style="width: 130px">
        <el-option
          v-for="item in pmOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
          type="daterange"
          v-model="searchTime"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          placeholder="选择时间范围"
          value-format='yyyy-MM-dd'
          style="verticalAlign:top;marginRight:20px;"
          >
        </el-date-picker>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        icon="el-icon-refresh"
        @click="toQuery"
      >刷新</el-button>

      <el-row :gutter='6' style="margin:20px;">
        <el-col :span='4'>账户总收入: <span>{{remainPrice}}</span></el-col>
        <el-col :span='4'>账户总支出: <span>{{expenditurePrice}}</span></el-col>
        <el-col :span='4'>账户余额: <span>{{totalPrice}}</span></el-col>
        <el-popover
          placement="top"
          width="160"
          v-model="visible">
          <el-form ref='formWithdraw' :model="formWithdraw" :rules="rules">
          <p>
            提现金额
          </p>
          <el-form-item prop='extractPrice'>
              <el-input type='number' v-model="formWithdraw.extractPrice" placeholder="输入提现金额" :maxlength='12'/>
            </el-form-item>
          <p style="font-size:12px">提现收取1%手续费</p>
          <div style="text-align: right; margin: 0">
            <el-button type="primary" size="mini" @click="withdraw($event,formWithdraw.extractPrice)">提现</el-button>
          </div>
          </el-form>
          
          <el-button v-permission='permission.withdraw' slot="reference" type="primary">提现</el-button>
          
        </el-popover>
      </el-row>

    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <pForm ref="formp" :is-add="isAdd" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <el-table-column prop="username" label="用户昵称" />
      <el-table-column prop="title" label="账单标题" />
      <el-table-column prop="linkId" label="订单号" />
      <el-table-column prop="category" label="明细种类">
        <template slot-scope="scope">
          <span v-if="scope.row.category == 'now_money'">余额</span>
          <span v-else-if="scope.row.category == 'integral'">积分</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="明细类型">
        <template slot-scope="scope">
          {{
            typeLabel(scope.row.type)
          }}
        </template>
      </el-table-column>
      <el-table-column prop="pm" label="收支类型">
        <template slot-scope="scope">
          <span v-if="scope.row.pm == '0'">支出</span>
          <span v-else-if="scope.row.pm == '1'">收入</span>
          <span v-else>未知</span>
        </template>
      </el-table-column>
      <el-table-column prop="number" label="明细数字">
        <template slot-scope="scope">
          <span v-if="scope.row.pm == 1">+</span>
          <span v-else>-</span>
          <span>{{ scope.row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="addTime" label="创建日期">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.addTime) }}</span>
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
import {getType} from '@/api/yxUserBill'
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del, onStatus, withdraw } from '@/api/yxUser'
import eForm from './form'
import pForm from './formp'
import { formatTime } from '@/utils/index'
import { Notification } from 'element-ui'
import { amountValid } from '@/utils/validate'
export default {
  components: { eForm, pForm },
  mixins: [initData],

  data() {
    return {
      delLoading: false, username: '', category: '', type: '',pm:'',
      addTimeStart:'',addTimeEnd:'',searchTime:'',
      visible:false,formWithdraw:{},
      remainPrice:0,// 账户余额
      totalPrice :0,//累计总金额金额
      expenditurePrice:0,//累计支出
      permission: {
        withdraw: ['admin', 'YXUSERBILL_WITHDRAW'],
      },
      queryTypeOptions: [
        { key: 'username', display_name: '用户昵称' },
        { key: 'phone', display_name: '手机号码' }
      ],
      categoryOptions: [
        { value: 'now_money', label: '余额' },
        { value: 'integral', label: '积分' }
      ],
      typeOptions: [],
      pmOptions: [
        { value: '0', label: '支出 ' },
        { value: '1', label: '收入' }
      ],
      rules:{
        extractPrice:[
          {required:true, message: '必填项', trigger: 'blur'},
          { validator: amountValid, trigger: 'blur'},
        ]
      }
    }
  },
  created() {
    this.$nextTick(() => {
      this.init().then(res=>{
        res.remainPrice?this.remainPrice=res.remainPrice:{}
        res.totalPrice?this.totalPrice=res.totalPrice:{}
        res.expenditurePrice?this.expenditurePrice=res.expenditurePrice:{}
      })
    })
  },
  mounted() {
    this.$nextTick(() => {
    })
  },
  computed:{
    typeLabel:function(){
      return function(type){
      if(this.typeOptions.length){
        let i= this.typeOptions.filter(function(item){
          for(let key in item){
            if(key===type){
              return JSON.parse(JSON.stringify(item))
            }
          }
        })
        if(i.length){
          return i[0][type]
        }
      }else{
        return ""
      }
    }
    }
  },
  methods: {
    formatTime,
    checkPermission,
    onStatus(id, status) {
      this.$confirm(`确定进行[${status ? '禁用' : '开启'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          onStatus(id, { status: status }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.init().then(res=>{
                  res.remainPrice?this.remainPrice=res.remainPrice:{}
                  res.totalPrice?this.totalPrice=res.totalPrice:{}
                  res.expenditurePrice?this.expenditurePrice=res.expenditurePrice:{}
                })
              }
            })
          })
        })
        .catch(() => { })
    },
    beforeInit() {

      getType().then(res=>{
        if(res){
          this.typeOptions=res
        }
      }).catch(err=>{
        Message({ message: err, type: 'error' })
      })

      this.url = 'api/yxUserBill'
      const sort = 'id,desc'
      this.params = {
        page: this.page,
        size: this.size,
        username: this.username,
        category: this.category,
        type: this.type,
        pm: this.pm,
        addTimeStart:this.searchTime?this.searchTime[0]:null,
        addTimeEnd:this.searchTime?this.searchTime[1]:null
      }
      const query = this.query
      const type = query.type
      const value = query.value
      if (type && value) { this.params[type] = value }
      return true
    },
    subDelete(uid) {
      this.delLoading = true
      del(uid).then(res => {
        this.delLoading = false
        this.$refs[uid].doClose()
        this.dleChangePage()
        this.init().then(res=>{
          res.remainPrice?this.remainPrice=res.remainPrice:{}
          res.totalPrice?this.totalPrice=res.totalPrice:{}
          res.expenditurePrice?this.expenditurePrice=res.expenditurePrice:{}
        })
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[uid].doClose()
        console.log(err.response.data.message)
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        uid: data.uid,
        account: data.account,
        pwd: data.pwd,
        realName: data.realName,
        birthday: data.birthday,
        cardId: data.cardId,
        mark: data.mark,
        partnerId: data.partnerId,
        groupId: data.groupId,
        username: data.username,
        avatar: data.avatar,
        phone: data.phone,
        addTime: data.addTime,
        addIp: data.addIp,
        lastTime: data.lastTime,
        lastIp: data.lastIp,
        nowMoney: data.nowMoney,
        brokeragePrice: data.brokeragePrice,
        integral: data.integral,
        signNum: data.signNum,
        status: data.status,
        level: data.level,
        spreadUid: data.spreadUid,
        spreadTime: data.spreadTime,
        userType: data.userType,
        isPromoter: data.isPromoter,
        payCount: data.payCount,
        spreadCount: data.spreadCount,
        cleanTime: data.cleanTime,
        addres: data.addres,
        adminid: data.adminid,
        loginType: data.loginType
      }
      _this.dialog = true
    },
    editP(data) {
      this.isAdd = false
      const _this = this.$refs.formp
      _this.form = {
        uid: data.uid,
        username: data.username,
        ptype: 1,
        money: 0
      }
      _this.dialog = true
    },
    withdraw(btn,val){
      let that=this
      this.$refs.formWithdraw.validate(function(ret,obj){
        if(ret){
          withdraw({extractPrice:val}).then(res=>{
            if(res){
              Notification.success({
              title: '提现申请成功'
              })
              that.init().then(res=>{
                res.remainPrice?that.remainPrice=res.remainPrice:{}
                res.totalPrice?that.totalPrice=res.totalPrice:{}
                res.expenditurePrice?that.expenditurePrice=res.expenditurePrice:{}
              })
            }else{
              Notification.error({
              title: '提现申请失败'
              })
            }
          })
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
