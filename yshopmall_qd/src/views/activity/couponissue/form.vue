<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '发布' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="110px">
      <el-form-item label="优惠券ID" prop='cid'>
        <el-input v-model="form.cid" style="width: 300px;" :disabled="true" />
      </el-form-item>
      <el-form-item label="优惠券名称" prop='cname'>
        <el-input v-model="form.cname" style="width: 300px;" :disabled="true" />
      </el-form-item>
      <el-form-item label="领取开启时间" prop='startTimeDate'>
        <template>
          <el-date-picker
            v-model="form.startTimeDate"
            :picker-options="expireStartTimeOption"
            type="datetime"
            placeholder="选择日期时间"
          />
        </template>
      </el-form-item>
      <el-form-item label="券领结束时间" prop='endTimeDate'>
        <template>
          <el-date-picker
            v-model="form.endTimeDate"
            :picker-options="expireTimeOption"
            type="datetime"
            placeholder="选择日期时间"            
          />
        </template>
      </el-form-item>
      <el-form-item label="发布数量" prop='totalCount' :rules="totalCountRules()">
        <el-input v-model="form.totalCount" style="width: 300px;" maxlength='9' />
      </el-form-item>
      <el-form-item label="是否不限量" prop='isPermanent' style='display:none'>
        <el-radio v-model="form.isPermanent" :label="1" disabled >不限量</el-radio>
        <el-radio v-model="form.isPermanent" :label="0" disabled >限量</el-radio>
      </el-form-item>
      <el-form-item label="状态" prop='status'>
        <el-radio v-model="form.status" :label="1">开启</el-radio>
        <el-radio v-model="form.status" :label="0">关闭</el-radio>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxStoreCouponIssue'


export default {
  name:'CouponissueForm',
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    return {
      loading: false, dialog: false,
      isPermanentStatus: true,
      form: {
        id: '',
        cid: '',
        cname: '',
        startTimeDate: '',
        endTimeDate: '',
        totalCount: 0,
        remainCount: 0,
        isPermanent: 0,
        status: 1,
        isDel: 0,
        addTime: ''
      },
      rules: {
        cid:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
        cname:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
        startTimeDate:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
        endTimeDate:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
        // isPermanent:[
        //   {required:true,message:'必填项',trigger:'blur'},
        // ],
        status:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
      },
      expireStartTimeOption: this.expireStartTimeOptionFun(),
      expireTimeOption: this.expireTimeOptionFun(),
    }
  }, 
  watch: {
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    totalCountRules() {
      return [
          {
            required: this.isPermanentStatus,
            message: '必填项',
            trigger: 'blur'
          },
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  
            message: '请输入数字',
            trigger: 'blur'
          },
        ]
    },
    doSubmit() {
      this.$refs.form.validate(ret=>{
        console.log('form:'+ret)
        if(!ret){
          return
        }
        this.loading = true
        if (this.isAdd) {
          this.doAdd()
        } else this.doEdit()
      })
    },
    doAdd() {
      add(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '发布成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.$parent.init()
      }).catch(err => {
        this.loading = false
        console.log(err.response.data.message)
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
        console.log(err.response.data.message)
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        cid: '',
        startTimeDate: '',
        endTimeDate: '',
        totalCount: '',
        remainCount: '',
        isPermanent: 0,
        status: '',
        isDel: '',
        addTime: ''
      }
    },
    expireTimeOptionFun(){
      let that=this
      return{
        disabledDate : function(date) {
          return date.getTime() < new Date(that.form.startTimeDate).getTime()
        }                    
      }
    },
    expireStartTimeOptionFun(){
      let that=this
      return{
        disabledDate : function(date) {
          return date.getTime() > new Date(that.form.endTimeDate).getTime()
        }                    
      }
    }
  }
}
</script>

<style scoped>

</style>
