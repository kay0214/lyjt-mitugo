<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="title" width="1200px">
    <!--规则编辑-->
    <div style='border:1px solid #e4e7ec;border-radius:5px;padding:20px'>
      <!--表单组件-->
        <el-form ref="form" :model="form2" :rules="rules" size="small" label-width="150px">

          <el-form-item label="分佣类型">
          <el-radio-group v-model="form.customizeType" @change="change">
            <el-radio :label="0">按平台</el-radio>
            <el-radio :label="1">不分佣</el-radio>
            <el-radio :label="2">自定义分佣</el-radio>
          </el-radio-group>
          </el-form-item>

          <el-row id="commision-box" :gutter='24' v-show="form.customizeType!=1">
            <el-col :span='8'>
              <el-form-item label="平台抽成" prop='fundsRate'>
                <span v-if="form.customizeType===0">{{form2.fundsRate}}  %</span>
                <el-input class='percent' v-else v-model="form2.fundsRate"/>
              </el-form-item>
              <el-form-item label="分享人" prop='shareRate'>
                <span v-if="form.customizeType===0">{{form2.shareRate}}  %</span>
                <el-input class='percent' v-else v-model="form2.shareRate" />
              </el-form-item>
              <el-form-item label="商户" prop='merRate'>
                <span v-if="form.customizeType===0">{{form2.merRate}}  % 积分</span>
                <el-input class='percent score' v-else v-model="form2.merRate"/>
              </el-form-item>
            </el-col>
            <el-col :span='8'>
              <el-form-item label="拉新池" prop='referenceRate'>
                <span v-if="form.customizeType===0">{{form2.referenceRate}}  % 积分</span>
                <el-input class='percent score' v-else v-model="form2.referenceRate"/>
              </el-form-item>
              <el-form-item label="分享人上级抽成" prop='shareParentRate'>
                <span v-if="form.customizeType===0">{{form2.shareParentRate}}  %</span>
                <el-input class='percent' v-else v-model="form2.shareParentRate"/>
              </el-form-item>
              <el-form-item label="合伙人" prop='partnerRate'>
                <span v-if="form.customizeType===0">{{form2.partnerRate}}  % 积分</span>
                <el-input class='percent score' v-else v-model="form2.partnerRate"/>
              </el-form-item>
            </el-col>
            <el-col :span='8'>
              <el-form-item label="购买人上级抽成" prop='parentRate'>
                <span v-if="form.customizeType===0">{{form2.parentRate}}  %</span>
                <el-input class='percent' v-else v-model="form2.parentRate"/>
              </el-form-item>
            </el-col>
          </el-row>
      </el-form>
        <div slot="footer" :offset='10' class="dialog-footer" style='marginTop:30px;text-align: center'>
          <el-button size='medium' :loading="sublLoading" type="primary" @click="submit">提交</el-button>
        </div>
    </div>
  </el-dialog>
</template>

<script>
import { get,updateRate } from '@/api/yxCommissionRate'
import { Loading } from 'element-ui';
export default {
  name: 'YxCommissionRate',
  data() {
    return {
      sublLoading: false, dialog: false,  title: '分佣配置',
      permission: {
        add: ['admin', 'yxCommissionRate:edit'],
        edit: ['admin', 'yxCommissionRate:edit']
      },
      form: {
        customizeType:''
      },
      form2:{
        fundsRate: '',
        merRate: '',
        parentRate: '',
        partnerRate: '',
        referenceRate: '',
        shareParentRate: '',
        shareRate: ''
      },
      rules: {
        customizeType:[
          { required: true, message: '必填项', trigger: 'blur' },
        ],
        fundsRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          }
        ],
        shareRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        shareParentRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        parentRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        merRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        partnerRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        referenceRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
      }
    }
  },
  watch: {
  },
  mounted(){
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    change(val){
      let that=this
      if(val!==1){
        let loading=Loading.service({
          target:'#commision-box'
        });
        if(val===0){
          get().then(ret=>{
            if(ret.id){
              that.form2=Object.assign({},that.form2,ret)
            }
            loading.close()
          }).catch(err => {
            loading.close()
          })
        }else{
          if(Object.values(that.form.yxCustomizeRate).length){
            that.form2=Object.assign({},that.form2,that.form.yxCustomizeRate)
          }else{
            that.form2= {}
          }
          loading.close()
        }
      }
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        customizeType:'',
        yxCustomizeRate: {}
      }
      this.form2={}
    },
    async submit() {
      if(this.form.customizeType==2){
        this.$refs.form.validate(ret=>{
          if(!ret){
            return
          }else{
            let rateList=this.form2
            if(rateList.fundsRate*1+rateList.shareRate*1+rateList.
              shareParentRate*1+rateList.parentRate*1+rateList.merRate*1+rateList.
              partnerRate*1+rateList.referenceRate*1===100){
              Object.assign(this.form.yxCustomizeRate,this.form2)
              this.sublLoading = true
              updateRate(this.form).then(res => {
                this.sublLoading = false
                this.resetForm()
                this.$notify({
                  title: '设置成功',
                  type: 'success',
                  duration: 2500
                })
                this.dialog=false
                this.$parent.toQuery()
              }).catch(err => {
                this.sublLoading = false
                this.$notify({
                  title: '设置失败：'+err.response.data.message,
                  type: 'success',
                  duration: 2500
                })
              })
            }else{
              this.$notify({
                title: '添加失败：平台抽成+拉新池+分享人+分享人上级抽成+购买人上级抽成+商户+合伙人=100%',
                type: 'error',
                duration: 2500
              })
            }
          }
        })
      }else{
        this.sublLoading = true
        updateRate(this.form).then(res => {
          this.sublLoading = false
          this.resetForm()
          this.$notify({
            title: '设置成功',
            type: 'success',
            duration: 2500
          })
          this.dialog=false
          this.$parent.toQuery()
        }).catch(err => {
          this.sublLoading = false
          this.$notify({
            title: '设置失败：'+err.response.data.message,
            type: 'success',
            duration: 2500
          })
        })
      }

    },
  }
}



</script>

<style scoped>
  .percent{
    width:60%;
  }
  .percent::after {
    content:"%";
    position: absolute;
    right:-20px;
  }
  .score::after{
    content:'% 积分';
    position: absolute;
    right:-50px;
  }
</style>
<style>
.el-form-item__error{
  z-index: 2;
}
</style>
