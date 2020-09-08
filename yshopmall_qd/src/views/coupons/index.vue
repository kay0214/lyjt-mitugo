<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <div v-if="crud.props.searchToggle">
        <!-- 搜索 -->
        <el-input v-model="query.couponName" clearable placeholder="卡券名称" style="width: 130px;" class="filter-item" maxlength="42" />
        <el-select v-model="query.couponType" clearable placeholder="卡券类型" class="filter-item" style="width: 130px">
          <el-option v-for="item in selections.couponType" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="query.couponCategory" clearable placeholder="卡券分类" class="filter-item" style="width: 130px">
          <el-option v-for="item in selections.couponCategory" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="query.isShow" clearable placeholder="上架状态" class="filter-item" style="width: 130px">
          <el-option
            v-for="item in [
              { key: 1, display_name: '是' },
              { key: 0, display_name: '否' }
            ]"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>
        <el-select v-model="query.isHot" clearable placeholder="热门状态" class="filter-item" style="width: 130px">
          <el-option
            v-for="item in [
              { key: 1, display_name: '是' },
              { key: 0, display_name: '否' }
            ]"
            :key="item.key"
            :label="item.display_name"
            :value="item.key"
          />
        </el-select>
        <rrOperation :crud="crud" />
      </div>
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
      <crudOperation :permission="permission" />
      <!--表单组件-->
      <el-dialog :close-on-click-modal="false" :before-close="crud.cancelCU" :visible.sync="crud.status.cu > 0" :title="crud.status.title" width="900px">
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="120px">
          <el-form-item v-show="crud.status.edit === 1" label="卡券id">
            <!-- <el-input v-model="form.id" style="width: 100%;" /> -->
            <span>{{ form.id }}</span>
          </el-form-item>
          <!-- <el-form-item v-show="crud.status.edit === 1" label="卡券编号" prop="couponNum">
            <span>{{ form.couponNum }}</span>
          </el-form-item> -->
          <el-form-item label="卡券名称" prop="couponName">
            <el-input v-model="form.couponName" style="width: 100%;" maxlength="42" />
          </el-form-item>
          <el-form-item label="卡券类型" prop="couponType">
            <el-select v-model="form.couponType" placeholder="请选择" style="width: 100%;">
              <el-option
                v-for="item in selections.couponType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>

          <!-- coupon_type为1时使用 -->
          <el-form-item v-if="form.couponType === 1" prop="denomination" label="代金券面额">
            <el-input v-model="form.denomination" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item v-show="form.couponType === 2" prop="discount" :required="form.couponType === 2" label="折扣券折扣率">
            <el-input v-model="form.discount" style="width: 100%;" maxlength="4" />
          </el-form-item>
          <el-form-item v-show="form.couponType === 3" prop="threshold" :required="form.couponType === 3" label="使用门槛">
            <el-input v-model="form.threshold" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item v-show="form.couponType === 3" prop="discountAmount" :required="form.couponType === 3" label="优惠金额">
            <el-input v-model="form.discountAmount" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item label="卡券分类" prop="couponCategory">
            <el-select v-model="form.couponCategory" placeholder="请选择" style="width: 100%;">
              <el-option
                v-for="item in selections.couponCategory"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="销售价格" prop="sellingPrice">
            <el-input v-model="form.sellingPrice" style="width: 100%;" @change="setCommission" maxlength="12" />
          </el-form-item>
          <el-form-item label="原价" prop="originalPrice">
            <el-input v-model="form.originalPrice" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item label="平台结算价" prop="settlementPrice">
            <el-input v-model="form.settlementPrice" style="width: 100%;" @change="setCommission" maxlength="12" />
          </el-form-item>
          <el-form-item label="佣金" prop="commission">
            <el-input v-model="form.commission" style="width: 100%;" readonly />
          </el-form-item>
          <el-form-item label="每人限购数量" prop="quantityLimit">
            <el-input v-model="form.quantityLimit" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item label="库存" prop="inventory">
            <el-input v-model="form.inventory" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item v-show="false" label="销量">
            <el-input v-model="form.sales" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="虚拟销量" prop="ficti">
            <el-input v-model="form.ficti" style="width: 100%;" maxlength="12" />
          </el-form-item>
          <el-form-item label="核销次数" prop="writeOff">
            <el-input v-model="form.writeOff" style="width: 100%;" maxlength="2" />
          </el-form-item>
          <el-form-item prop="expireDate" label="有效期">
            <el-date-picker
              v-model="expireDate"
              type="daterange"
              range-separator="至"
              start-placeholder="有效期开始日期"
              end-placeholder="有效期结束日期"
              style="width:100%;"
              @change="expireDateChange"
            />
          </el-form-item>

          <!-- <el-form-item label="有效期始" prop="expireDateStart">
            <el-input v-model="form.expireDateStart" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="有效期止" prop="expireDateEnd">
            <el-input v-model="form.expireDateEnd" style="width: 100%;" />
          </el-form-item> -->
          <el-form-item v-show="false" label="热门优惠; 1:是, 0否">
            <el-radio-group v-model="form.isHot">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-show="false" label="上架状态">
            <el-radio-group v-model="form.isShow">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="服务支持">
            <el-checkbox v-model="outtimeRefund">过期退</el-checkbox>
            <el-checkbox v-model="needOrder">免预约</el-checkbox>
            <el-checkbox v-model="awaysRefund">随时退</el-checkbox>
          </el-form-item>
          <!-- <el-form-item label="过期退" style="width: 100%;" v-if="false" >
            <el-radio-group v-model="form.outtimeRefund">
              <el-radio :label="0">不支持</el-radio>
              <el-radio :label="1">支持</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="免预约 0:不支持 1支持" v-if="false">
            <el-input v-model="form.needOrder" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="随时退 0:不支持 1支持" v-if="false">
            <el-input v-model="form.awaysRefund" style="width: 100%;" />
          </el-form-item> -->
          <el-form-item label="使用条件" prop="useCondition">
            <el-input v-model="form.useCondition" style="width: 100%;" maxlength="88" />
          </el-form-item>
          <el-form-item label="排序" prop='sort'>
            <el-input v-model="form.sort" οnkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" maxlength="6"/>
          </el-form-item>
          <el-form-item label="图片(260*260/416*214)" prop="image">
            <!-- <pic-upload-two v-model="form.pic" /> -->
            <MaterialList v-model="imageArr" style="width: 100%" type="image" :num="1" :width="150" :height="150" @setValue="(urls)=>{form.image = urls;imageArr=urls;$refs.form.validateField('image')}"/>
          </el-form-item>
          <el-form-item label="轮播图" prop="sliderImage">
            <MaterialList
              v-model="sliderImageArr"
              style="width: 100%"
              type="image"
              :num="8"
              :width="150"
              :height="150"
              @setValue="(urls)=>{form.sliderImage = urls;sliderImageArr=urls;$refs.form.validateField('sliderImage')}"
            />
          </el-form-item>
          <el-form-item prop="availableTime" label="可用时段">
            <el-time-picker
              v-model="availableTime"
              is-range
              :arrow-control="false"
              range-separator="至"
              start-placeholder="可用时段开始时间"
              end-placeholder="可用时段结束时间"
              placeholder="选择时间范围"
              value-format='HH:mm'
              format='HH:mm'
              style="width:100%;"
            />
          </el-form-item>
          <!-- <el-form-item label="可用时间始" prop="availableTimeStart">
            <el-input v-model="form.availableTimeStart" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="可用时间止" prop="availableTimeEnd">
            <el-input v-model="form.availableTimeEnd" style="width: 100%;" />
          </el-form-item> -->
          <el-form-item v-if="false" label="是否删除（0：未删除，1：已删除）" prop="delFlag">
            <el-input v-model="form.delFlag" style="width: 100%;" />
          </el-form-item>
          <el-form-item v-if="false" label="创建人 根据创建人关联店铺">
            <el-input v-model="form.createUserId" style="width: 100%;" />
          </el-form-item>
          <el-form-item v-if="false" label="修改人">
            <el-input v-model="form.updateUserId" style="width: 100%;" />
          </el-form-item>
          <el-form-item v-if="false" label="创建时间" prop="createTime">
            <el-input v-model="form.createTime" style="width: 100%;" />
          </el-form-item>
          <el-form-item v-if="false" label="更新时间" prop="updateTime">
            <el-input v-model="form.updateTime" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="卡券简介" prop="couponInfo">
            <el-input type="textarea" v-model="form.couponInfo" style="width: 100%;" maxlength="88" />
          </el-form-item>
          <el-form-item label="图文详情" prop="content" required>
            <editor v-model="form.content" @change="()=>{$refs.form.validateField('content')}" />
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
        <el-table-column v-if="columns.visible('id')" fixed="left" prop="id" label="卡券id" />
        <!-- <el-table-column v-if="columns.visible('couponNum')" fixed="left" prop="couponNum" label="卡券编号" width="160px" /> -->
        <el-table-column v-if="columns.visible('couponName')" fixed="left" prop="couponName" label="卡券名称" />
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
        <el-table-column v-if="columns.visible('couponCategoryName')" prop="couponCategoryName" label="卡券分类">
          <!-- 卡券分类 -->
          <template slot-scope="scope">
            {{ scope.row.couponCategoryName }}
          </template>
        </el-table-column>
        <!-- coupon_type === 1 -->
        <el-table-column v-if="columns.visible('denomination')" prop="denomination" label="代金券面额">
          <template slot-scope="scope">
            {{ scope.row.couponType === 1 ? scope.row.denomination : "-" }}
          </template>
        </el-table-column>
        <!-- coupon_type === 2 -->
        <el-table-column v-if="columns.visible('discount')" prop="discount" label="折扣率">
          <template slot-scope="scope">
            {{ scope.row.couponType === 2 ? scope.row.discount : "-" }}
          </template>
        </el-table-column>
        <!-- coupon_type === 3 -->
        <el-table-column v-if="columns.visible('threshold') && columns.visible('discountAmount')" prop="threshold" label="满减额度">
          <template slot-scope="scope">
            {{ scope.row.couponType === 3 ? `满${scope.row.threshold}减${scope.row.discountAmount}` : "-" }}
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

        <el-table-column label="有效期" width="180px">
          <template slot-scope="scope">
            {{ parseTime(scope.row.expireDateStart,"{y}-{m}-{d}") }} ~ {{ parseTime(scope.row.expireDateEnd,"{y}-{m}-{d}") }}
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
        <el-table-column v-if="columns.visible('isHot')" prop="isHot" label="热销榜单">
          <!-- 1:是, 0否 -->
          <template slot-scope="scope">
            <div @click="handleHot(scope.row.id,scope.row.isHot)">
              <el-tag v-if="scope.row.isHot === 1" style="cursor: pointer" :type="''">是</el-tag>
              <el-tag v-else style="cursor: pointer" :type=" 'info' ">否</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column v-if="columns.visible('isShow')" prop="isShow" label="状态">
          <!-- 0：未上架，1：上架 -->
          <template slot-scope="scope">
            <div @click="handleUpload(scope.row.id,scope.row.isShow)">
              <el-tag v-if="scope.row.isShow === 1" style="cursor: pointer" :type="''">已上架</el-tag>
              <el-tag v-else style="cursor: pointer" :type=" 'info' ">未上架</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="服务支持">
          <!--  0:不支持 1支持 -->
          <template slot-scope="scope">
            <el-tag v-if="scope.row.outtimeRefund === 1">过期退</el-tag> 
            <el-tag v-if="scope.row.needOrder === 1">免预约</el-tag> 
            <el-tag v-if="scope.row.awaysRefund === 1">随时退</el-tag> 
            <span v-if="scope.row.outtimeRefund !== 1 && scope.row.needOrder !== 1 && scope.row.awaysRefund !== 1"> - </span>
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
        <el-table-column v-if="columns.visible('useCondition')" prop="useCondition" label="使用条件" width="180px" />
        <el-table-column label="可用时段" width="150px">
          <template slot-scope="scope">
            {{ parseTime(scope.row.availableTimeStart,'{h}:{i}:{s}') }} ~ {{ parseTime(scope.row.availableTimeEnd,'{h}:{i}:{s}') }}
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
        <!-- <el-table-column v-if="columns.visible('createUserId')" prop="createUserId" label="创建人" />
        <el-table-column v-if="columns.visible('updateUserId')" prop="updateUserId" label="修改人" /> -->
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
        <el-table-column v-permission="['admin','yxCoupons:edit','yxCoupons:del']" fixed="right" label="操作" width="120px" align="center">
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
import MaterialList from '@/components/material'
import picUploadTwo from '@/components/pic-upload-two'
import mulpicUpload from '@/components/mul-pic-upload'
import editor from '@/views/components/Editor'
import { parseTime } from '@/utils/index'
import { Message } from 'element-ui'
// crud交由presenter持有
const defaultCrud = CRUD({ title: '卡券表', url: 'api/yxCoupons', sort: 'id,desc', crudMethod: { ...crudYxCoupons },optShow: {
      add: true,
      edit: false,
      del: true,
      download: false
    }})
const defaultForm = { 
  id: null, couponNum: null, couponName: null, couponType: null, couponCategory: null, 
  denomination: null, discount: null, threshold: null, discountAmount: null, sellingPrice: null, 
  originalPrice: null, settlementPrice: null, commission: null, quantityLimit: null, 
  inventory: null, sales: null, ficti: null, writeOff: null, expireDateStart: null, 
  expireDateEnd: null, isHot: 0, isShow: 0, outtimeRefund: null, needOrder: null, 
  awaysRefund: null, useCondition: null, availableTimeStart: null, availableTimeEnd: null, 
  delFlag: null, createUserId: null, updateUserId: null, createTime: null, updateTime: null, 
  content: null, expireDate: null, image: null, sliderImage: null,sort:null, couponInfo:null,}
const imageArr = []
if (defaultForm.image) { imageArr[0] = defaultForm.image }
export default {
  name: 'YxCoupons',
  components: { pagination, crudOperation, rrOperation, udOperation, MaterialList, picUploadTwo, mulpicUpload, editor },
  mixins: [presenter(defaultCrud), header(), form(defaultForm), crud()],
  data() {
    return {
      expireDate: defaultForm.expireDateStart && defaultForm.expireDateEnd ? [defaultForm.expireDateStart, defaultForm.expireDateEnd] : null, // 有效期
      availableTime: [defaultForm.availableTimeStart ? defaultForm.availableTimeStart : parseTime((new Date(2020, 9, 10, 9, 0)),'{h}:{i}'), defaultForm.availableTimeEnd ? defaultForm.availableTimeEnd : parseTime((new Date(2020, 9, 10, 22, 0)),'{h}:{i}')], // 可用时段
      imageArr: imageArr,
      sliderImageArr: defaultForm.sliderImage || [],
      selections: {
        couponType: [{ label: '代金券', value: 1 }, { label: '折扣券', value: 2 }, { label: '满减券', value: 3 }], // 卡券类型
        couponCategory: [] // 卡券分类
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
        denomination: [
          {
            // validator: (rule, value, callback) => {
            //   // if (!value) return callback(new Error('代金券面额不能为空'))
            //   if (this.form.couponType === 1 && (!value || value === '')) {
            //     return callback(new Error('代金券面额不能为空'))
            //   } else { callback() }
            // }, 
            required: true,
            message: '销售价格不能为空',
            trigger: 'blur'
          },
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
            message: '请输入数字--限定6位整数2位小数'
          }
        ],
        discount: [{ validator: (rule, value, callback) => {
          if (this.form.couponType === 2 && (!value || value === '')) {
            return callback(new Error('折扣不能为空'))
          } else { callback() }
        }, trigger: 'blur' },
        {
          pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
          message: '请输入数字'
        }],
        threshold: [{ validator: (rule, value, callback) => {
          if (this.form.couponType === 3 && (!value || value === '')) {
            return callback(new Error('使用门槛不能为空不能为空'))
          } else { callback() }
        }, trigger: 'blur' },
        {
          pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
          message: '请输入数字--限定6位整数2位小数'
        }],
        discountAmount: [{ validator: (rule, value, callback) => {
          if (this.form.couponType === 3 && (!value || value === '')) {
            return callback(new Error('优惠金额不能为空'))
          } else { callback() }
        }, trigger: 'blur' },
        {
          pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
          message: '请输入数字--限定6位整数2位小数'
        }],
        sellingPrice: [
          { required: true, message: '销售价格不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
            message: '请输入数字--限定6位整数2位小数'
          }
        ],
        originalPrice: [
          { required: true, message: '原价不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
            message: '请输入数字--限定6位整数2位小数'
          }
        ],
        settlementPrice: [
          { required: true, message: '平台结算价不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,  //正则
            message: '请输入数字--限定6位整数2位小数'
          }
        ],
        commission: [
          { required: true, message: '佣金不能为空', trigger: 'blur' }
        ],
        quantityLimit: [
          { required: true, message: '每人限购数量不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]{0,8}$/,  //正则
            message: '请输入数字-限制8位整数'
          }
        ],
        inventory: [
          { required: true, message: '库存不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]{0,8}$/,  //正则
            message: '请输入数字--限制8位整数'
          }
        ],
        ficti: [
          // { trigger: 'blur' },
          {
            pattern: /^[0-9]{0,8}$/,  //正则
            message: '请输入数字--限制8位整数',
            trigger: 'blur'
          }
        ],
        writeOff: [
          { required: true, message: '核销次数不能为空', trigger: 'blur' },
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          }
        ],

        expireDate: [{
          validator: (rule, value, callback) => {
            if (!this.form.expireDateStart || !this.form.expireDateEnd) {
              return callback(new Error('有效期不能为空'))
            }
            callback()
          }, trigger: 'blur'
        }],
        // expireDateStart: [
        //   { required: true, message: '有效期始不能为空', trigger: 'blur' }
        // ],
        // expireDateEnd: [
        //   { required: true, message: '有效期止不能为空', trigger: 'blur' }
        // ],
        useCondition: [
          { required: true, message: '使用条件不能为空', trigger: 'blur' }
        ],
        couponInfo: [
          { required: true, message: '卡券简介不能为空', trigger: 'blur' },
          { max: 88, message: '不超过88个字符', trigger: 'blur' }
        ],
        availableTime: [{
          validator: (rule, value, callback) => {
            if (!this.form.availableTimeStart || !this.form.availableTimeEnd) {
              return callback(new Error('可用时间不能为空'))
            }
            callback()
          }, trigger: 'blur' }],
        // availableTimeStart: [
        //   { required: true, message: '可用时间始不能为空', trigger: 'blur' }
        // ],
        // availableTimeEnd: [
        //   { required: true, message: '可用时间止不能为空', trigger: 'blur' }
        // ],
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
          { validator: (rule, value, callback) => {
            if (!value || value === '<p><br></p>' || value === '<p></p>') {
              return callback(new Error('卡券详情不能为空'))
            }
            callback()
          }, trigger: 'blur' }
        ],
        image: [{ required: true, message: '卡券图片不能为空' }],
        sliderImage: [{ required: true, message: '卡券轮播图不能为空' }]
      },
      queryTypeOptions: [
        { key: 'couponName', display_name: '卡券名称' }
      ]
    }
  },
  computed: {
    outtimeRefund:{
      set: function (v){
        this.form.outtimeRefund = v ? 1 : 0
      },
      get: function (){
        return this.form.outtimeRefund === 1 ? true: false;
      }
    },
    needOrder:{
      set: function (v){
        this.form.needOrder = v ? 1: 0
      },
      get: function (){
        return this.form.needOrder === 1 ? true: false;
      }
    },
    awaysRefund:{
      set: function (v){
        this.form.awaysRefund = v ? 1: 0
      },
      get: function (){
        return this.form.awaysRefund === 1 ? true: false;
      }
    }
  },
  watch: {
    // imageArr(value) {
    //   this.form.image = value.join(',')
    //   if (this.$refs.form) {
    //     this.$refs.form.validateField('image')
    //   }
    // },
    // sliderImageArr(value) {
    //   this.form.sliderImage = value.join(',')
    //   if (this.$refs.form) {
    //     this.$refs.form.validateField('sliderImage')
    //   }
    // },
    availableTime(value){
      this.form.availableTimeEnd = value[1]
      this.form.availableTimeStart = value[0]
    }
  },
  mounted() {
    // 获取卡券分类数据
    crudYxCoupons.categoryTree().then(res => {
      this.selections.couponCategory = res.content.map(item => {
        const { id, cateName } = item
        return { label: cateName, value: id }
      })
    })
  },
  methods: {
    // 获取数据前设置好接口地址
    [CRUD.HOOK.beforeRefresh]() {
      const query = this.query
      if (query.type && query.value) {
        this.crud.params[query.type] = query.value
      } else {
        delete this.crud.params.couponName
        delete this.crud.params.couponType
        delete this.crud.params.couponCategory
      }
      return true
    }, // 新增与编辑前做的操作
    [CRUD.HOOK.afterToCU](crud, form) {
      if(crud.status.edit>CRUD.STATUS.NORMAL){
      // 编辑时有效 设置默认 有效期
      this.expireDate=[form.expireDateStart,form.expireDateEnd]
      }
      //  设置默认 可用时段
      this.form.availableTimeEnd = this.availableTime[0]
      this.form.availableTimeStart = this.availableTime[1]
      // 设置默认图片
      form.imageArr = [form.image]
      const formImage = []
      let formSliderImageArr = []
      if (form.image) {
        formImage.push(form.image)
      }
      if (form.sliderImage) {
        formSliderImageArr = form.sliderImage
      }
      this.imageArr = formImage
      this.sliderImageArr = formSliderImageArr
      // 设置默认卡券类型
      if (!this.form.couponType) {
        this.form.couponType = 1
      }
    },
    //添加取消 - 之前
    [CRUD.HOOK.beforeAddCancel](crud,form) {
      this.expireDate=null
      this.availableTime=[parseTime((new Date(2020, 9, 10, 9, 0)),'{h}:{i}'),parseTime((new Date(2020, 9, 10, 22, 0)),'{h}:{i}')]
      form.content=""
    },
    /** 提交 - 之后 */
    [CRUD.HOOK.afterSubmit]() {
      this.expireDate=null
      this.availableTime=[parseTime((new Date(2020, 9, 10, 9, 0)),'{h}:{i}'),parseTime((new Date(2020, 9, 10, 22, 0)),'{h}:{i}')]
    },
    expireDateChange(newValue) {
      this.form.expireDateStart = this.expireDate[0]
      this.form.expireDateEnd = this.expireDate[1]
    },
    // availabalChange(newValue) {
    //   this.form.availableTimeStart = this.availableTime[0]
    //   this.form.availableTimeEnd = this.availableTime[1]
    // },
    setCommission() {
      const commission = this.form.sellingPrice - this.form.settlementPrice
      this.form.commission = isNaN(commission) ? null : commission
    },
    // 上下架操作
    handleUpload(id, status) {
      this.$confirm(`确定进行[${status ? '下架' : '上架'}]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          crudYxCoupons.pufOff(id, { status: status }).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.crud.refresh()
              }
            })
          })
        })
        .catch(() => { })
    },
    // 热销操作
    handleHot(id, status) {
      this.$confirm(`确定进行[${status ? '取消' : '设置'}热销榜单]操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          crudYxCoupons.popular(id).then(({ data }) => {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1000,
              onClose: () => {
                this.crud.refresh()
              }
            })
          })
        })
        .catch(() => { })
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
