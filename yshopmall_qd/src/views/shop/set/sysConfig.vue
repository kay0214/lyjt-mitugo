<template>
  <!--提现费率配置页-->
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--表单组件-->
        <el-form ref="form" :model="form" :rules="rules" size="small" label-width="180px"
          style="margin-top:30px">
          <el-row :gutter='20'>
            <el-col :span='6'>
              <el-form-item label="商户提现费率" prop='storeExtractRate'>
                <el-input-number style="width:100%" :precision="0" :min="0" :max="100" v-model="form.storeExtractRate" /><span style="position: absolute;margin-left:10px">%</span>
              </el-form-item>
              <el-form-item label="商户最低提现金额" prop='storeExtractMinPrice'>
                <el-input v-model="form.storeExtractMinPrice" >
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
              <el-form-item label="分享达人提现费率" prop='userExtractRate'>
                <el-input-number style="width:100%" :precision="0" :min="0" :max="100" v-model="form.userExtractRate" /><span style="position: absolute;margin-left:10px">%</span>
              </el-form-item>
              <el-form-item label="分享达人最低提现金额" prop='userExtractMinPrice'>
                <el-input v-model="form.userExtractMinPrice" >
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
      </el-form>
        <el-col slot="footer" :offset='3' class="dialog-footer" style='marginTop:30px'>
          <el-button size='medium' :loading="sublLoading" type="primary" @click="submit">保存</el-button>
        </el-col>
    </div>
  </div>
</template>

<script>
import { getExtractSet as get,updateExtractSet as edit } from '@/api/yxCommissionRate'
import { amountValid } from '@/utils/validate'
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
        storeExtractRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        userExtractRate: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^(100)$|^((\d|[1-9]\d)(\.\d{1,2})?)$/,  //正则
            message: '请输入0~100的数字，最多两位小数',
            trigger: 'blur'
          },
        ],
        storeExtractMinPrice: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            validator:amountValid,
            trigger: 'blur'
          },
        ],
        userExtractMinPrice: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            validator:amountValid,
            trigger: 'blur'
          },
        ]
      }
    }
  },
  watch: {
  },
  mounted(){
    this.$nextTick(() => {
      get().then(ret=>{
        if(ret && ret.userExtractRate){
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
<style>
.el-form-item__error{
  z-index: 2;
}
</style>
