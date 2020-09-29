<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--表单组件-->
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="150px">
          <el-row :gutter='20'>
            <el-col :span='6'>
              <el-form-item label="平台抽成" prop='fundsRate'>
                <el-input class='percent' v-model="form.fundsRate" />
              </el-form-item>
              <el-form-item label="分享人" prop='shareRate'>
                <el-input class='percent' v-model="form.shareRate" />
              </el-form-item>
              <el-form-item label="商户" prop='merRate'>
                <el-input class='percent score' v-model="form.merRate"/>
              </el-form-item>
            </el-col>
            <el-col :span='6'>
              <el-form-item label="拉新池" prop='referenceRate'>
                <el-input class='percent score' v-model="form.referenceRate" />
              </el-form-item>
              <el-form-item label="分享人上级抽成" prop='shareParentRate'>
                <el-input class='percent' v-model="form.shareParentRate" />
              </el-form-item>
              <el-form-item label="合伙人" prop='partnerRate'>
                <el-input class='percent score' v-model="form.partnerRate" />
              </el-form-item>
            </el-col>
            <el-col :span='6'>
              <el-form-item label="购买人上级抽成" prop='parentRate'>
                <el-input class='percent' v-model="form.parentRate" />
              </el-form-item>
            </el-col>
          </el-row>
      </el-form>
        <el-col slot="footer" :offset='10' class="dialog-footer" style='marginTop:30px'>
          <el-button size='medium' :loading="sublLoading" type="primary" @click="submit">保存</el-button>
        </el-col>
    </div>
  </div>
</template>

<script>
import { edit,get } from '@/api/yxCommissionRate'
export default {
  name: 'YxCommissionRate',
  data() {
    return {
      sublLoading:false,
      permission: {
        add: ['admin', 'yxCommissionRate:edit'],
        edit: ['admin', 'yxCommissionRate:edit']
      },
      form: {},
      rules: {
        fundsRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
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
    this.$nextTick(() => {
      get().then(ret=>{
        if(ret.id){
          this.form=ret
        }
      }).catch(err => {
        this.sublLoading = false
        this.$notify({
          title: '设置失败：'+err.response.data.message,
          type: 'success',
          duration: 2500
        })
      })
    })
  },
  methods: {
    async submit() {
       this.$refs.form.validate(ret=>{
        if(!ret){
          return
        }else{
          if(this.form.fundsRate*1+this.form.shareRate*1+this.form.
  shareParentRate*1+this.form.parentRate*1+this.form.merRate*1+this.form.
  partnerRate*1+this.form.referenceRate*1===100){
            this.sublLoading = true
            edit(this.form).then(res => {
              this.sublLoading = false
              this.$notify({
                title: '设置成功',
                type: 'success',
                duration: 2500
              })
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
