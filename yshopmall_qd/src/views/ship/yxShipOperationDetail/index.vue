<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <!--      <crudOperation :permission="permission" />-->
      <!--表单组件-->
      <el-dialog
                v-loading="loading" :visible.sync="dialog" :title="crud.status.title" width="1000px">
        <el-form ref="form" :model="form" :rules="rules" disabled size="small" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-form-item label="批次号">
                <el-input v-model="form.batchNo" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="船只名称">
                <el-input v-model="form.shipName" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="船长姓名">
                <el-input v-model="form.captainName"  />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="承载人数">
                <el-input v-model="form.totalPassenger" >
                  <template slot="append">人</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="老年人人数">
                <el-input v-model="form.oldPassenger"  >
                  <template slot="append">人</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="未成年人数">
                <el-input v-model="form.underagePassenger"  >
                  <template slot="append">人</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="出港时间">
                <el-input v-model="form.leaveForTime">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="回港时间">
                <el-input v-model="form.returnForTime" >
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="1" style="width:100px;text-align: right">
              <p>订单号</p>
            </el-col>
            <el-col :span="15" style="padding:16px 0 16px 15px;">
              <template v-for="(item,ind) in form.orderList">
                <span>{{item}}</span>
                <el-divider v-if="ind!=(form.orderList.length-1)" direction="vertical"></el-divider>
              </template>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="1" style="width:100px;text-align: right">
              <p>乘客</p>
            </el-col>
            <el-col :span="15" style="padding:16px 0 16px 15px;">
              <el-row :gutter="20">
                <el-col :span="10"v-for="(item,idx) in form.listPassenger" :key="idx">
                  <div style="border:1px solid #F2F6FC;border-radius: 5px;margin-bottom: 20px;padding:20px;" >
                    <p>{{item.passengerName}}</p>
                    <p v-if="item.isAdult"><!-- /** 0:未成年 1:成年人 2：老年人 */-->
                      身份证：{{item.idCard}}
                    </p>
                    <p v-else>
                      未成年
                    </p>
                    <p>联系电话：{{item.phone}}</p>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row >
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialog=false">关闭</el-button>
        </div>
      </el-dialog>
      <!--分页组件-->
<!--      <pagination />-->
    </div>
  </div>
</template>

<script>
import crudYxShipOperationDetail from '@/api/yxShipOperationDetail'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import MaterialList from "@/components/material";
import { initData, download } from '@/api/data'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '运营记录详情', url: 'api/yxShipOperationDetail', sort: 'id,desc',
  crudMethod: { ...crudYxShipOperationDetail }})
const defaultForm = { id: null, couponOrderId: null, shipId: null, batchNo: null, shipName: null,
  captainId: null, captainName: null, useId: null, useName: null, healthStatus: null, totalPassenger: null, oldPassenger: null, underagePassenger: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipOperationDetail',
  components: { MaterialList},
  mixins: [header(), form(defaultForm), crud()],
  data() {
    return {
      dialog: false,loading: false,
      form:{},
      permission: {
        add: ['admin', 'yxShipOperationDetail:add'],
        edit: ['admin', 'yxShipOperationDetail:edit'],
        del: ['admin', 'yxShipOperationDetail:del']
      },
      rules: {
        couponOrderId: [
          { required: true, message: '卡券订单id不能为空', trigger: 'blur' }
        ],
        shipId: [
          { required: true, message: '船只id不能为空', trigger: 'blur' }
        ],
        batchNo: [
          { required: true, message: '船只出港批次号不能为空', trigger: 'blur' }
        ],
        captainId: [
          { required: true, message: '船长id不能为空', trigger: 'blur' }
        ],
        useId: [
          { required: true, message: '核销人id不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ]
      }    }
  },
  watch: {
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
    init(id) {
      initData('/api/yxShipOperation/getOperationDetailInfo/'+id,{}).then(res=>{
        if(res){
          this.form=res
        }
        this.loading=false
      }).catch(err=>{
        this.loading=false
      })
    }
  }
}



</script>

<style scoped>
  .table-img {
    display: inline-block;
    text-align: center;
    background: #ccc;
    color: #fff;
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    vertical-align: middle;
    width: 32px;
    height: 32px;
    line-height: 32px;
  }
</style>
