<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="130px">
      <el-form-item label="优惠券名称" prop='title'>
        <el-input v-model="form.title" style="width: 300px;" :maxlength='20'/>
      </el-form-item>
      <el-form-item label="优惠券面值" prop='couponPrice'>
        <el-input v-model="form.couponPrice" style="width: 300px;" :maxlength='20'/>
      </el-form-item>
      <el-form-item label="优惠券最低消费" prop='useMinPrice'>
        <el-input v-model="form.useMinPrice" style="width: 300px;" :maxlength='20' />
      </el-form-item>
      <el-form-item label="优惠券有效期限(天)" prop='couponTime'>
        <el-input v-model="form.couponTime" style="width: 300px;" :maxlength='12' />
      </el-form-item>
      <el-form-item label="排序" prop='sort'>
        <el-input v-model="form.sort" style="width: 300px;" :maxlength='3'/>
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
import { add, edit } from '@/api/yxStoreCoupon'
import { amountValid} from '@/utils/validate'
export default {
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
      form: {
        id: '',
        title: '',
        integral: 0,
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: 1,
        sort: 0,
        status: 1,
        addTime: ''
        // isDel: 0
      },
      rules: {
        title:[
          {required:true,message:'必填项',trigger:'blur'},

        ],
        couponPrice:[
          {required:true,message:'必填项',trigger:'blur'},          
          { validator: amountValid, trigger: 'blur'},
        ],
        useMinPrice:[
          {required:true,message:'必填项',trigger:'blur'},          
          { validator: amountValid, trigger: 'blur'},
        ],
        couponTime:[
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]+$/,  
            message: '请输入数字',
            trigger: 'blur'
          },,
        ],
        sort:[
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]+$/,  
            message: '请输入数字',
            trigger: 'blur'
          },,
        ],
        status:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.$refs.form.validate(ret=>{
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
          title: '添加成功',
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
        title: '',
        integral: 0,
        couponPrice: 0,
        useMinPrice: 0,
        couponTime: 1,
        sort: 0,
        status: 1,
        addTime: ''
      }
    }
  }
}
</script>

<style scoped>

</style>
