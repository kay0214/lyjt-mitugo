<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 搜索 -->
      <el-input v-model="query.value" clearable placeholder="输入搜索内容" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="query.type" clearable placeholder="类型" class="filter-item" style="width: 130px">
        <el-option v-for="item in queryTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="userType" clearable placeholder="用户来源" class="filter-item" style="width: 130px">
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        icon="el-icon-refresh"
        @click="toQuery"
      >刷新</el-button>
    </div>
    <!--表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <pForm ref="formp" :is-add="isAdd" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <el-table-column prop="uid" label="用户id" />
      <el-table-column prop="nickname" label="用户昵称" />
      <el-table-column ref="table" prop="avatar" label="用户头像">
        <template slot-scope="scope">
          <a :href="scope.row.avatar" style="color: #42b983" target="_blank"><img :src="scope.row.avatar" alt="点击打开" class="el-avatar"></a>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号码" />
      <el-table-column prop="realName" label="真实姓名" />
      <el-table-column prop="nowMoney" label="用户余额" />
      <el-table-column prop="brokeragePrice" label="佣金金额" />
      <el-table-column label="用户角色" align="center">
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.userRole == 1">分销客</el-tag>
            <el-tag v-else-if="scope.row.userRole == 0">普通会员</el-tag>
            <el-tag v-else></el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="addTime" label="创建日期">
        <template slot-scope="scope">
          <span>{{ formatTime(scope.row.addTime) }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div @click="onStatus(scope.row.uid,scope.row.status)">
            <el-tag v-if="scope.row.status == 1" style="cursor: pointer" :type="''">正常</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">禁用</el-tag>
          </div>
        </template>
      </el-table-column> -->
      <el-table-column prop="spreadUid" label="推荐人" />
      <el-table-column prop="payCount" label="购买次数" />
      <el-table-column prop="pushCount" label="直推人数" />
      <!-- <el-table-column prop="bankMobile" label="银行预留手机号" /> -->
      <el-table-column v-if="checkPermission(['admin','YXUSER_ALL','YXUSER_EDIT','YXUSER_DELETE'])" label="操作" width="185" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button
            v-permission="['admin','YXUSER_ALL','YXUSER_EDIT']"
            size="mini"
            type="primary"
            icon="el-icon-edit"
            @click="edit(scope.row)"
          />
          <!-- <el-dropdown size="mini" split-button type="primary" trigger="click">
            操作
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  v-permission="['admin','YXUSER_ALL','YXUSER_EDIT']"
                  size="mini"
                  type="primary"
                  @click="editP(scope.row)"
                >修改余额</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          -->
          <!--
         <el-popover
            v-permission="['admin','YXUSER_ALL','YXUSER_DELETE']"
            :ref="scope.row.uid"
            placement="top"
            width="180">
            <p>确定删除本条数据吗？</p>
            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="$refs[scope.row.uid].doClose()">取消</el-button>
              <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.uid)">确定</el-button>
            </div>
            <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"/>
          </el-popover>
          -->
          <br/>
          <el-popover
  placement="left"
  width="260"
trigger="click">
  <el-form ref='formWithdraw' :model="formWithdraw" :rules="rules" style='padding:10px 20px;'>
  <p>佣金调整</p>
    <el-form-item label="类型" prop='ptype'>
      <el-radio v-model="formWithdraw.ptype" :label="1" style='margin-left:20px;'>增</el-radio>
      <el-radio v-model="formWithdraw.ptype" :label="0">减</el-radio>
    </el-form-item>
    <el-form-item label="金额" prop='money'>
      <el-input v-model="formWithdraw.money " placeholder="金额" :maxlength='12'/>
    </el-form-item>
  <div style="text-align: right; margin: 0">
    <el-button type="primary" size="mini" @click="modiyfUserCommission($event,formWithdraw,scope.row.uid)">提交</el-button>
  </div>
  </el-form>
  <el-button v-permission="['admin','YXUSER_MODIFY']" size="small" type="primary" icon="el-icon-edit" slot="reference" style='marginTop:10px' plain>修改金额</el-button>
</el-popover>
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
import { del, onStatus,modiyfUserCommission } from '@/api/yxUser'
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
      delLoading: false,
      userType: '',
      formWithdraw:{},
      queryTypeOptions: [
        { key: 'nickname', display_name: '用户昵称' },
        { key: 'phone', display_name: '手机号码' }
      ],
      statusOptions: [
        { value: 'routine', label: '小程序' },
        { value: 'wechat', label: '公众号' },
        { value: 'H5', label: 'H5' }
      ],
      rules:{
        money: [
          { required: true, message: '必填项', trigger: 'blur' },
          { validator: amountValid, trigger: 'blur'},
        ],
        ptype: [
          { required: true, message: '必填项', trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
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
                this.init()
              }
            })
          })
        })
        .catch(() => { })
    },
    beforeInit() {
      this.url = 'api/yxUser'
      const sort = 'uid,desc'
      this.params = { page: this.page, size: this.size, sort: sort, userType: this.userType }
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
        this.init()
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
        nickname: data.nickname,
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
        userRole: data.userRole,
        bankName: data.bankName,
        bankNo: data.bankNo,
        payCount: data.payCount,
        spreadCount: data.spreadCount,
        cleanTime: data.cleanTime,
        addres: data.addres,
        adminid: data.adminid,
        loginType: data.loginType,
        bankMobile: data.bankMobile,
        cnapsCode: data.cnapsCode
      }
      _this.dialog = true
    },
    editP(data) {
      this.isAdd = false
      const _this = this.$refs.formp
      _this.form = {
        uid: data.uid,
        nickname: data.nickname,
        ptype: 1,
        money: 0
      }
      _this.dialog = true
    },
    modiyfUserCommission(btn,formData,uid){
      this.$refs.formWithdraw.validate(function(ret,obj){
        if(ret){
          modiyfUserCommission(Object.assign(formData,{uid})).then(res=>{
            console.log('xxxxxx')
            console.log(res)
            if(res){
              Notification.success({
              title: '提交成功'
              })
            }else{
              Notification.error({
              title: '提交失败'
              })
            }
          }).catch(err=>{
            // Notification.error({
            //   title: err
            //   })
          })
        }else{
          Notification.error({
            title: '请查看必填项是否输入正确'
            })
        }
      })

    }
  }
}
</script>

<style scoped>

</style>
