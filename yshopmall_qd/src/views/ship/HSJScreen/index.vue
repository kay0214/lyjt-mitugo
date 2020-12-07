<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <el-cascader
        class="filter-item"
        v-model="query.shipInfoId"
        :options="shipSeriesTree"
        :props="{ expandTrigger: 'hover' }"
        clearable
        @change="(val)=>{
          if(val&&val.length){
            query.shipId=val[1]
          }else{
            query.shipId=''
            $route.query.id=''
          }
        }"></el-cascader>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="crud.toQuery">搜索</el-button>
<!--      <crudOperation :permission="permission" />-->
      <!--表单组件-->
      <!--表格渲染-->
      <div ref="list" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;padding:20px;"
                @selection-change="crud.selectionChangeHandler">
        <div v-if="crud.data.length" ref="listbox" >
          <el-row type='flex' style="font-size: 12px;flex-wrap:wrap;">
            <el-col :span="7" v-for="(item,idx) in crud.data" :key='idx' style="border:1px solid #F2F6FC;border-radius: 5px;margin: 20px;">
              <div style="padding:20px;">
                <el-alert
                  :title="item.shipName+' ('+item.statusValue+') '"
                  type="info"
                  center
                  :closable="false"
                >
                </el-alert>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-row :gutter="20">
                    <el-col :span="12" style="text-align: right">
                      <p>船长</p>
                    </el-col>
                    <el-col :span="12">
                      <p>{{item.captainName}}</p>
                    </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row :gutter="20">
                      <el-col :span="10" style="text-align: right">
                        <p>价格</p>
                      </el-col>
                      <el-col :span="14">
                        <p>{{item.totlePricet}}元</p>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <el-row :gutter="20">
                      <el-col :span="12" style="text-align: right">
                        <p>承载人数</p>
                      </el-col>
                      <el-col :span="12">
                        <p>{{item.totalPassenger}}人</p>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row :gutter="20">
                      <el-col :span="10" style="text-align: right">
                        <p>未成年</p>
                      </el-col>
                      <el-col :span="14">
                        <p>{{item.underagePassenger}}人</p>
                      </el-col>
                    </el-row>
                  </el-col>
                  <el-col :span="8">
                    <el-row :gutter="20">
                      <el-col :span="10" style="text-align: right">
                        <p>老年人</p>
                      </el-col>
                      <el-col :span="14">
                        <p>{{item.oldPassenger}}人</p>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="24">
                    <el-row :gutter="20">
                      <el-col :span="4" style="text-align: right">
                        <p>出港时间</p>
                      </el-col>
                      <el-col :span="12">
                        <p>{{item.leaveForTime}}</p>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="24">
                    <el-row :gutter="20">
                      <el-col :span="4" style="text-align: right">
                        <p>回港时间</p>
                      </el-col>
                      <el-col :span="12">
                        <p>{{item.returnForTime}}</p>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="3" style="text-align: right">
                    <p>乘客</p>
                  </el-col>
                  <el-col :span="24" style="padding:0 0 16px 15px;">
                    <el-table
                      row-class-name="list-passenger-row"
                      border
                      :data="item.listPassenger"
                      style="width: 100%">
                      <el-table-column
                        prop="passengerName"
                        label="姓名">
                      </el-table-column>
                      <el-table-column
                        prop="name"
                        label="身份证">
                        <template slot-scope="scope">
                          <p v-if="scope.row.isAdult"><!-- /** 0:未成年 1:成年人 2：老年人 */-->
                            {{scope.row.idCard}}
                          </p>
                          <p v-else>
                            未成年
                          </p>
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="phone"
                        label="联系电话">
                      </el-table-column>
                    </el-table>
                  </el-col>
                </el-row >
              </div>
            </el-col>
          </el-row>
        </div>
        <div v-else class="el-table__empty-block" style="height: 100%; width: 100%;font-size: 12px">
          <span class="el-table__empty-text">暂无数据</span>
        </div>
      </div>
      <!--分页组件-->
      <pagination ref="pagination" :pageSizes="[9,18,27,36,45,90]" :limit.sync="crud.page.size"
                  :layout="`total, prev, pager, next, sizes`" :background="false"
      :total="crud.page.total" :page.sync="crud.page.page" @pagination="pagination"/>
    </div>
    <el-table ref="table" v-show="false" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
      <el-table-column v-if="columns.visible('shipName')" prop="shipName" label="船只名" />
      <el-table-column v-if="columns.visible('captainName')" prop="captainName" label="船长名" />
    </el-table>
  </div>
</template>

<script>
import crudYxShipOperation from '@/api/yxShipOperation'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@/components/Pagination'
import MaterialList from "@/components/material"
import { initData } from '@/api/data'

// crud交由presenter持有
const defaultCrud = CRUD({ title: '海事局大屏', url: 'api/yxShipOperation/hazdShowList', sort: 'id,desc',
  optShow:{
    add: false,
    edit: false,
    del: false,
    download: true
  },
  queryOnPresenterCreated:false,
  crudMethod: { ...crudYxShipOperation }
})
const defaultForm = { id: null, batchNo: null, shipId: null, shipName: null, captainId: null,
  captainName: null, totalPassenger: null, oldPassenger: null, underagePassenger: null,
  leaveTime: null, returnTime: null, status: null, delFlag: null, createUserId: null,
  updateUserId: null, createTime: null, updateTime: null }
export default {
  name: 'YxShipOperation',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      shipSeriesTree:[],
      permission: {
        add: ['admin', 'yxShipOperation:add'],
        edit: ['admin', 'yxShipOperation:edit'],
        del: ['admin', 'yxShipOperation:del']
      },
      rules: {
        batchNo: [
          { required: true, message: '船只出港批次号不能为空', trigger: 'blur' }
        ],
        shipId: [
          { required: true, message: '船只id不能为空', trigger: 'blur' }
        ],
        captainId: [
          { required: true, message: '船长id不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '船只状态 0:待出港 1：出港 2：回港不能为空', trigger: 'blur' }
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
      }
    }
  },
  watch: {
  },
  created() {
    this.crud.sizeChangeHandler(9)
    this.$nextTick(()=>{
      let that=this
      initData('api/yxShipInfo/getShipSeriseTree').then(res=>{
        if(res && res.options){
          that.shipSeriesTree=res.options
        }else{
          that.shipSeriesTree=[]
        }
      })
    })
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
    },
    pagination(e){
      if(this.$refs.pagination.pageSize!==e.limit){
        this.crud.page.page=1
      }
      this.crud.refresh()
    }
  }
}



</script>

<style>
 .list-passenger-row td{
    padding:0;
  }
</style>
