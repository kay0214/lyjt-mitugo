<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="900px">
    <el-form ref="form" :model="form" :inline="true" :rules="rules" size="small" label-width="80px">
      <el-form-item label="商品分类" prop='storeCategory'>
        <treeselect v-model="form.storeCategory.id" :options="cates" style="width: 370px;" placeholder="选择商品分类" noOptionsText='暂无数据' noResultsText='无数据'/>
      </el-form-item>

      <el-form-item label="商品名称" prop='storeName'>
        <el-input v-model="form.storeName" style="width: 500px;" maxlength="42" />
      </el-form-item>
      <!-- <el-form-item label="关键字" prop='keyword'>
        <el-input v-model="form.keyword" style="width: 500px;" />
      </el-form-item> -->
      <el-form-item label="单位名" prop='unitName'>
        <el-input v-model="form.unitName" style="width: 320px;" maxlength="3" />
      </el-form-item>
      <el-form-item label="产品条码" prop='barCode'>
        <el-input v-model="form.barCode" style="width: 320px;" maxlength="30" />
      </el-form-item>
      <el-form-item label="商品图片" prop='imageArr'>
        <MaterialList v-model="form.imageArr" style="width: 500px" type="image" :num="1" :width="150" :height="150" @setValue='(val)=>{form.imageArr=val;$refs.form.validateField("imageArr")}'/>
      </el-form-item>
      <el-form-item label="轮播图" prop='sliderImageArr'>
        <MaterialList v-model="form.sliderImageArr" style="width: 500px" type="image" :num="8" :width="150" :height="150" @setValue='(val)=>{form.sliderImageArr=val;$refs.form.validateField("sliderImageArr")}'/>
      </el-form-item>
      <el-form-item label="商品简介" prop='storeInfo'>
        <el-input v-model="form.storeInfo" style="width: 500px;" rows="5" type="textarea" maxlength="200" />
      </el-form-item>
      <el-form-item label="产品描述" prop='description'>
        <editor v-model="form.description" @change="(val)=>{form.description=val;$refs.form.validateField('description')}"/>
      </el-form-item>
      <el-form-item label="销售价" prop='price'>
        <el-input v-model="form.price" οnkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" maxlength="12"/>
      </el-form-item>
      <el-form-item label="原价" prop='otPrice'>
        <el-input v-model="form.otPrice" οnkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" maxlength="12"/>
      </el-form-item>
     <!-- <el-form-item label="成本价">
        <el-input v-model="form.cost" />
      </el-form-item>-->
      <el-form-item label="平台结算价" prop='settlement'>
        <el-input v-model="form.settlement" οnkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" maxlength="12"/>
      </el-form-item>

      <el-form-item label="排序" prop='sort'>
        <el-input v-model="form.sort" />
      </el-form-item>
     <!-- <el-form-item label="销量">
        <el-input v-model="form.sales" />
      </el-form-item>-->

      <el-form-item label="库存" prop='stock'>
        <el-input v-model="form.stock" οnkeyup="this.value=this.value.replace(//D/g,'')" onafterpaste="this.value=this.value.replace(//D/g,'')" maxlength="12"/>
      </el-form-item>
      <el-form-item label="佣金" prop='commission'>
        <el-input v-model="commission" readonly/>
      </el-form-item>
      <el-form-item label="是否包邮" prop='isPostage'>
        <el-radio v-model="form.isPostage" :label="1">是</el-radio>
        <el-radio v-model="form.isPostage" :label="0" style="width: 200px;">否</el-radio>
      </el-form-item>
      <el-form-item v-if='!form.isPostage' prop='postage' :rules="form.isPostage?[{required:false}]:rules.postage" label="邮费">
        <el-input v-model="form.postage" maxlength="12"/>
      </el-form-item>
     <!-- <el-form-item label="优品推荐">
        <el-radio v-model="form.isGood" :label="1">是</el-radio>
        <el-radio v-model="form.isGood" :label="0" style="width: 200px;">否</el-radio>
      </el-form-item>-->
      <!--<el-form-item label="获得积分">
        <el-input v-model="form.giveIntegral" />
      </el-form-item>-->
      <el-form-item label="虚拟销量" prop='ficti'>
        <el-input v-model="form.ficti" maxlength="12"/>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getCates } from '@/api/yxStoreCategory'
import { add, edit } from '@/api/yxStoreProduct'
import editor from '../../components/Editor'
import picUpload from '@/components/pic-upload'
import mulpicUpload from '@/components/mul-pic-upload'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import MaterialList from '@/components/material'
export default {
  components: { editor, picUpload, mulpicUpload, Treeselect, MaterialList },
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  computed: {
    commission: function () {
      if(!isNaN(this.form.price) && !isNaN(this.form.settlement)) {
        return this.form.price - this.form.settlement
      } else return ''
    }
  },
  data() {
    //浮点数上限校验
    const validateNum=(r,value,callback)=>{
      if(parseFloat(value)>999999.99){
        callback(new Error("最大值为：999999.99"));
      }else if(parseFloat(value)<0){
        callback(new Error("不能为负值"));
      }else{
        callback()
      }
    };
    const validateInt=(r,value,callback,max=16777215)=>{
      if(parseInt(value)>max){
        callback(new Error("最大值为："+max));
      }else if(parseInt(value)<0){
        callback(new Error("不能为负值"));
      }else{
        callback()
      }
    };
    //佣金校验 销售价price-平台结算价settlement>=0
    let commissionValue=(r,value,callback)=>{
      let val=this.form.price*1-this.form.settlement*1
      if(val<0){
        callback(new Error("佣金=销售价-平台结算价 (佣金>=0)"));
      }else{
        if(!isNaN(val)){
          this.$set(this.form,'commission',val)
        }
        callback()
      }
    };
    return {
      loading: false, dialog: false, cates: [],
      form: {
        id: '',
        merId: 0,
        image: '',
        sliderImage: '',
        imageArr: [],
        sliderImageArr: [],
        storeName: '',
        storeInfo: '',
        keyword: '',
        barCode: '',
        cateId: 1,
        storeCategory: {id:null},
        vipPrice: 0,
        unitName: '',
        sales: 0,
        isShow: 1,
        isHot: 0,
        isBenefit: 0,
        isBest: 0,
        isNew: 0,
        description: '',
        addTime: '',
        isPostage: 0,
        isDel: 0,
        merUse: 0,
        giveIntegral: 0,
        cost: 0,
        isSeckill: 0,
        isBargain: 0,
        isGood: 0,
        browse: 0,
        codePath: '',
        soureLink: ''
      },
      rules: {
        storeCategory:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        storeName:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        // keyword:[
        //   { required: true,message: '必填项', trigger: 'blur'}
        // ],
        unitName:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        barCode:[
          { required: true,message: '必填项', trigger: 'blur'},
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          },
          {max:15,message:'15个字符以内', trigger: 'blur'}
        ],
        imageArr:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        sliderImageArr:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        storeInfo:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        description:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        price:[
          { required: true,message: '必填项', trigger: 'blur'},
          { validator: validateNum, trigger: 'blur'},
          { validator: commissionValue, trigger: 'blur'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
            message: '请输入数字'
          }

        ],
        otPrice:[
          { required: true,message: '必填项', trigger: 'blur'},
          { validator: validateNum, trigger: 'blur'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
            message: '请输入数字'
          }
        ],
        settlement:[
          { required: true,message: '必填项', trigger: 'blur'},
          { validator: validateNum, trigger: 'blur'},
          { validator: commissionValue, trigger: 'blur'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
            message: '请输入数字'
          }
        ],
        sort:[
          { required: true,message: '必填项', trigger: 'blur'},
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: (rule, value, callback)=>{validateInt(rule, value, callback,32767)}, trigger: 'blur'}
          // {max:32767,message:'最大：32767', trigger: 'blur'}
        ],
        stock:[
          { required: true,message: '必填项', trigger: 'blur'},
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: (rule, value, callback)=>{validateInt(rule, value, callback)}, trigger: 'blur'}
        ],
        commission:[
          // { required: true,message: '必填项', trigger: 'blur'}
        ],
        isPostage:[
          { required: true,message: '必填项', trigger: 'blur'}
        ],
        postage:[
          { required: true,message: '必填项'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
            message: '请输入邮费金额'
          },
          {validator: validateNum, trigger: 'blur'},
        ],
        ficti:[
          { required: true,message: '必填项', trigger: 'blur'},
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: (rule, value, callback)=>{validateInt(rule, value, callback,8388607)}, trigger: 'blur'}
        ],
      }
    }
  },
  watch: {
    'form.imageArr': function(val) {
      if (val) {
        this.form.image = val.join(',')
      }
      // this.$nextTick(()=>{
      //   this.$refs['form'].validateField('imageArr')
      // })
    },
    'form.sliderImageArr': function(val) {
      if (val) {
        this.form.sliderImage = val.join(',')
      }
      // this.$nextTick(()=>{
      //   this.$refs['form'].validateField('sliderImageArr')
      // })
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
     this.$refs['form'].validate(valid=>{
       if(!valid){
         return;
       }else{
        this.loading = true
        if (this.isAdd) {
          this.doAdd()
        } else this.doEdit()
       }
     })
    },
    doAdd() {
      add(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '添加成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
        /*this.$notify({
          title: '添加失败：'+err.response.data.message,
          type: 'error',
          duration: 2500
        })*/
      })
    },
    doEdit() {
      edit(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '修改成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
        /*this.$notify({
          title: '修改失败：'+err.response.data.message,
          type: 'error',
          duration: 2500
        })*/
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        merId: 0,
        image: '',
        sliderImage: '',
        imageArr: [],
        sliderImageArr: [],
        storeName: '',
        storeInfo: '',
        keyword: '',
        barCode: '',
        cateId: 1,
        storeCategory: {},
        vipPrice: 0,
        unitName: '',
        sales: 0,
        isShow: 1,
        isHot: 0,
        isBenefit: 0,
        isBest: 0,
        isNew: 0,
        description: '',
        addTime: '',
        isPostage: 0,
        isDel: 0,
        merUse: 0,
        giveIntegral: 0,
        cost: 0,
        isSeckill: 0,
        isBargain: 0,
        isGood: 0,
        browse: 0,
        codePath: '',
      }
    },
    getCates() {
      getCates({ isShow: 1 }).then(res => {
        this.cates = res.content
      })
    }
  }
}
</script>

<style scoped>

</style>
