<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="900px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="120px">
          <el-form-item label="卡券主键" v-show="false">
            <el-input v-model="form.id" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="卡券编号" prop="couponNum" v-show="crud.status.edit === 1">
            <span>{{form.couponNum}}</span>
          </el-form-item>
          <el-form-item label="卡券名称" prop="couponName">
            <el-input v-model="form.couponName" style="width: 100%;" />
          </el-form-item>
         
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="text" @click="crud.cancelCU">取消</el-button>
          <el-button :loading="crud.cu === 2" type="primary" @click="crud.submitCU">确认</el-button>
        </div>
      </el-dialog>
      <!--表格渲染-->
      
      <el-table ref="table" v-loading="crud.loading" :data="crud.data" size="small" style="width: 100%;" @selection-change="crud.selectionChangeHandler">
        <el-table-column type="selection" width="55" />
        <!-- <el-table-column v-if="false && columns.visible('id')" prop="id" label="卡券主键" /> -->
        <el-table-column fixed="left" v-if="columns.visible('couponNum')" prop="couponNum" label="卡券编号" />
        <el-table-column fixed="left" v-if="columns.visible('couponName')" prop="couponName" label="卡券名称" />
        <el-table-column v-if="columns.visible('couponType')" prop="couponType" label="卡券类型">
          <!-- 1:代金券, 2:折扣券, 3:满减券 -->
          <template slot-scope="scope">
            {{
              selections.couponType.find(item=>{
                return item.value === scope.row.couponType
              }).label
            }}
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('couponCategory')" prop="couponCategory" label="卡券分类">
          <!-- 卡券分类 -->
          <template slot-scope="scope">
            {{
              selections.couponCategory.find(item=>{
                return item.value === scope.row.couponType
              }).label
            }}
          </template>
        </el-table-column>
        <!-- coupon_type === 1 -->
        <el-table-column v-if="columns.visible('denomination')" prop="denomination" label="代金券面额">
          <template slot-scope="scope">
            {{scope.row.couponType === 1 ? scope.row.denomination : "-" }}
          </template>
        </el-table-column>
        <!-- coupon_type === 2 -->
        <el-table-column v-if="columns.visible('discount')" prop="discount" label="折扣率">
          <template slot-scope="scope">
            {{scope.row.couponType === 2 ? scope.row.discount : "-" }}
          </template>
        </el-table-column>
        <!-- coupon_type === 3 -->
        <el-table-column v-if="columns.visible('threshold') && columns.visible('discountAmount')" prop="threshold" label="满减额度">
          <template slot-scope="scope">
            {{scope.row.couponType === 3 ? `满${scope.row.threshold}减${scope.row.discountAmount}` : "-" }}
          </template>
        </el-table-column>
        
        <!-- <el-table-column v-if="columns.visible('threshold')" prop="threshold" label="使用门槛">
          <template slot-scope="scope">
            {{scope.row.couponType === 3 ? scope.row.threshold : "-" }}
          </template>
        </el-table-column> -->
        <!-- coupon_type === 3 -->
        <!-- <el-table-column v-if="columns.visible('discountAmount')" prop="discountAmount" label="优惠金额">
          <template slot-scope="scope">
            {{scope.row.couponType === 3 ? scope.row.discountAmount : "-" }}
          </template>
        </el-table-column> -->
        <el-table-column v-if="columns.visible('sellingPrice')" prop="sellingPrice" label="销售价格" />
        <el-table-column v-if="columns.visible('originalPrice')" prop="originalPrice" label="原价" />
        <el-table-column v-if="columns.visible('settlementPrice')" prop="settlementPrice" label="平台结算价" />
        <el-table-column v-if="columns.visible('commission')" prop="commission" label="佣金" />
        <el-table-column v-if="columns.visible('quantityLimit')" prop="quantityLimit" label="每人限购数量" />
        <el-table-column v-if="columns.visible('inventory')" prop="inventory" label="库存" />
        <el-table-column v-if="columns.visible('sales')" prop="sales" label="销量" />
        <el-table-column v-if="columns.visible('ficti')" prop="ficti" label="虚拟销量" />
        <el-table-column v-if="columns.visible('writeOff')" prop="writeOff" label="核销次数" />


        <el-table-column label="有效期">
          <template slot-scope="scope">
            {{parseTime(scope.row.expireDateStart,"{y}-{m}-{d}")}} ~ {{parseTime(scope.row.expireDateEnd,"{y}-{m}-{d}")}}
          </template>
        </el-table-column>
        <!-- <el-table-column v-if="columns.visible('expireDateStart')" prop="expireDateStart" label="有效期始">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expireDateStart) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('expireDateEnd')" prop="expireDateEnd" label="有效期止">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.expireDateEnd) }}</span>
          </template>
        </el-table-column> -->
        <el-table-column v-if="columns.visible('isHot')" prop="isHot" label="热门优惠">
          <!-- 1:是, 0否 -->
          <template slot-scope="scope">
            <span v-if="scope.row.isHot === 1">是</span>
            <span v-if="scope.row.isHot === 0">否</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('isShow')" prop="isShow" label="状态">
          <!-- 0：未上架，1：上架 -->
          <template slot-scope="scope">
            <span v-if="scope.row.isShow === 0">未上架</span>
            <span v-if="scope.row.isShow === 1">已上架</span>
          </template>
        </el-table-column>
        <el-table-column prop="outtimeRefund" label="服务支持">
          <!--  0:不支持 1支持 -->
          <template slot-scope="scope">
            <el-tag v-if="scope.row.outtimeRefund === 1">过期退</el-tag>
            <el-tag v-if="scope.row.needOrder === 1">免预约</el-tag>
            <el-tag v-if="scope.row.awaysRefund === 1">随时退</el-tag>
          </template>
        </el-table-column>
        <!--  0:不支持 1支持 -->
        <!-- <el-table-column v-if="columns.visible('outtimeRefund')" prop="outtimeRefund" label="过期退">
          <template slot-scope="scope">
            <span v-if="scope.row.outtimeRefund === 0">不支持</span>
            <span v-if="scope.row.outtimeRefund === 1">支持</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('needOrder')" prop="needOrder" label="免预约">
          <template slot-scope="scope">
            <span v-if="scope.row.needOrder === 0">不支持</span>
            <span v-if="scope.row.needOrder === 1">支持</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('awaysRefund')" prop="awaysRefund" label="随时退">
          <template slot-scope="scope">
            <span v-if="scope.row.awaysRefund === 0">不支持</span>
            <span v-if="scope.row.awaysRefund === 1">支持</span>
          </template>
        </el-table-column> -->
        <el-table-column v-if="columns.visible('useCondition')" prop="useCondition" label="使用条件 描述" />
        <el-table-column label="可用时段">
          <template slot-scope="scope">
            {{scope.row.availableTimeStart}} ~ {{scope.row.availableTimeEnd}}
          </template>
        </el-table-column>
        <!-- <el-table-column v-if="columns.visible('availableTimeStart')" prop="availableTimeStart" label="可用时间始" />
        <el-table-column v-if="columns.visible('availableTimeEnd')" prop="availableTimeEnd" label="可用时间止" /> -->
        <!-- 0：未删除，1：已删除 -->
        <!-- <el-table-column v-if="columns.visible('delFlag')" prop="delFlag" label="是否删除">
          <template slot-scope="scope">
            <span v-if="scope.row.awaysRefund === 0">未删除</span>
            <span v-if="scope.row.awaysRefund === 1">已删除</span>
          </template>
        </el-table-column> -->
        <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人" />
        <el-table-column v-if="columns.visible('updateUserId')" prop="updateUserId" label="修改人" />
        <el-table-column v-if="columns.visible('createTime')" prop="createTime" label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('updateTime')" prop="updateTime" label="更新时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column v-if="columns.visible('content')" prop="content" label="卡券详情" /> -->
        <el-table-column fixed="right" v-permission="['admin','yxCoupons:edit','yxCoupons:del']" label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <udOperation
              :data="scope.row"
              :permission="permission"
            />
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <pagination />
    </div>
  </div>
</template>

<script>
import crudYxCoupons from '@/api/yxCoupons'
import CRUD, { presenter, header, form, crud } from '@crud/crud'
import rrOperation from '@crud/RR.operation'
import crudOperation from '@crud/CRUD.operation'
import udOperation from '@crud/UD.operation'
import pagination from '@crud/Pagination'
import MaterialList from "@/components/material";
import picUploadTwo from '@/components/pic-upload-two'
import mulpicUpload from '@/components/mul-pic-upload'
import editor from '@/views/components/Editor'
import { parseTime } from '@/utils/index'
// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券表', url: 'api/yxCoupons', sort: 'id,desc', crudMethod: { ...crudYxCoupons }})
const defaultForm = { id: null, couponNum: null, couponName: null, couponType: 1, couponCategory: null, denomination: null, discount: null, threshold: null, discountAmount: null, sellingPrice: null, originalPrice: null, settlementPrice: null, commission: null, quantityLimit: null, inventory: null, sales: null, ficti: null, writeOff: null, expireDateStart: null, expireDateEnd: null, isHot: null, isShow: null, outtimeRefund: null, needOrder: null, awaysRefund: null, useCondition: null, availableTimeStart: null, availableTimeEnd: null, delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, content: null,expireDate:null }
export default {
  name: 'YxCoupons',
  components: { pagination, crudOperation, rrOperation, udOperation ,MaterialList ,picUploadTwo, mulpicUpload,editor},
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      expireDate:defaultForm.expireDateStart && defaultForm.expireDateEnd ? [defaultForm.expireDateStart,defaultForm.expireDateEnd] : null, // 有效期
      availableTime: [defaultForm.availableTimeStart ? defaultForm.availableTimeStart: new Date(2020,9,10,9,0),defaultForm.availableTimeEnd ? defaultForm.availableTimeEnd : new Date(2020,9,10,22,0)], // 可用时段
      selections:{
        couponType: [{label :"代金券" , value:1},{label: "折扣券",value:2},{label:"满减券",value:3}], // 卡券类型
        couponCategory: [{label :"美食" , value:1},{label: "理发",value:2},{label:"游戏",value:3}], // 卡券分类
      },
      permission: {
        add: ['admin', 'yxCoupons:add'],
        edit: ['admin', 'yxCoupons:edit'],
        del: ['admin', 'yxCoupons:del']
      },
      rules: {
        // couponNum: [
        //   { required: true, message: '卡券编号不能为空', trigger: 'blur' }
        // ],
        couponName: [
          { required: true, message: '卡券名称不能为空', trigger: 'blur' }
        ],
        couponType: [
          { required: true, message: '卡券类型不能为空', trigger: 'blur' }
        ],
        couponCategory: [
          { required: true, message: '卡券分类不能为空', trigger: 'blur' }
        ],
        sellingPrice: [
          { required: true, message: '销售价格不能为空', trigger: 'blur' }
        ],
        originalPrice: [
          { required: true, message: '原价不能为空', trigger: 'blur' }
        ],
        settlementPrice: [
          { required: true, message: '平台结算价不能为空', trigger: 'blur' }
        ],
        commission: [
          { required: true, message: '佣金不能为空', trigger: 'blur' }
        ],
        quantityLimit: [
          { required: true, message: '每人限购数量不能为空', trigger: 'blur' }
        ],
        inventory: [
          { required: true, message: '库存不能为空', trigger: 'blur' }
        ],
        writeOff: [
          { required: true, message: '核销次数不能为空', trigger: 'blur' }
        ],
        expireDate: [{
          required: true, message:"有效期不能为空",trigger: 'blur'
        }],
        expireDateStart: [
          { required: true, message: '有效期始不能为空', trigger: 'blur' }
        ],
        expireDateEnd: [
          { required: true, message: '有效期止不能为空', trigger: 'blur' }
        ],
        useCondition: [
          { required: true, message: '使用条件不能为空', trigger: 'blur' }
        ],
        availableTimeStart: [
          { required: true, message: '可用时间始不能为空', trigger: 'blur' }
        ],
        availableTimeEnd: [
          { required: true, message: '可用时间止不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ],
        content: [
          { validator:(rule, value, callback)=>{
          if (!value || value === "<p><br></p>" || value === "<p></p>") {
            return callback(new Error('卡券详情不能为空'));
          }
        }, trigger: 'blur' }
        ],
        
        denomination: [{ validator:(rule, value, callback)=>{
          if (this.form.couponType === 1 && !value) {
            return callback(new Error('代金券面额不能为空不能为空'));
          }
        }, trigger: 'blur' }],
        discount: [{ validator:(rule, value, callback)=>{
          if (this.form.couponType === 2 && !value) {
            return callback(new Error('折扣不能为空'));
          }
        }, trigger: 'blur' }],
        threshold: [{ validator:(rule, value, callback)=>{
          if (this.form.couponType === 3 && !value) {
            return callback(new Error('使用门槛不能为空不能为空'));
          }
        }, trigger: 'blur' }],
        discountAmount: [{ validator:(rule, value, callback)=>{
          if (this.form.couponType === 3 && !value) {
            return callback(new Error('优惠金额不能为空'));
          }
        },trigger: 'blur' }],
      }    }
  },
  watch: {
    // 有效期监听
    expireDate(newValue){
      this.form.expireDate = newValue;
      this.form.expireDateStart = parseTime(newValue[0]);
      this.form.expireDateEnd = parseTime(newValue[1]);
    },
    // 过期时间监听
    availableTime(newValue){
      this.form.availableTime = newValue;
      this.form.availableTimeStart = parseTime(newValue[0]);
      this.form.availableTimeEnd = parseTime(newValue[1]);
    }
  },
  computed:{
    
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      return true;
    },
    [CRUD.HOOK.beforeSubmit]() {
    },
    setCommission(){
      const commission = this.form.sellingPrice - this.form.settlementPrice;
      this.form.commission = isNaN(commission) ? null : commission;
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
<style scoped>

  .demo-upload{
    display: block;
    /*//height: 50px;*/
    text-align: center;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0,0,0,.2);
    margin-right: 4px;
  }
  .demo-upload img{
    width: 100%;
    height: 100%;
    display: block;
  }

  .demo-upload-cover{
    display: block;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,.6);
  }
  .demo-upload:hover .demo-upload-cover{
    display: block;
  }
  .demo-upload-cover i{
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }

</style>
