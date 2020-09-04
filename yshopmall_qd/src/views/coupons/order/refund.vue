<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '退款审核'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="120px">
        <el-input v-model="form.id" style="width: 320px;" type='hidden'/>
      <el-form-item label="订单号">
        <el-input v-model="form.orderId" :disabled="true" style="width: 320px;" />
      </el-form-item>
      <el-form-item label="退款金额" prop="RefundPrice" οnkeyup="if(isNaN(value))execCommand('undo')" onafterpaste="if(isNaN(value))execCommand('undo')" maxlength="12">
        <el-input v-model="form.RefundPrice" style="width: 320px;" />
      </el-form-item>      
      <el-form-item label="认证类型" prop="RefundStatus">
        <el-radio v-model="form.RefundStatus" :label="0">不通过</el-radio>
        <el-radio v-model="form.RefundStatus" :label="2">通过</el-radio>
      </el-form-item>
      <el-form-item v-if='!form.RefundStatus' label="退款拒绝理由" prop="RefundReason">
        <el-input v-model="form.RefundReason" style="width: 320px;" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">退款</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { refund } from '@/api/yxCouponOrder'
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
        orderId: '',
        RefundPrice: '',
        RefundStatus: '',
        RefundReason: '',  
      },
      rules: {
        RefundPrice: [
          { required: true, message: '必填项', trigger: 'blur' },
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,  //正则
            message: '请输入数字'
          }
        ],
        RefundStatus: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
        RefundReason: [
          { required: true, message: '必填项', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.$refs['form'].validate(ret=>{
        if(!ret){
          return
        }
        this.loading = true
        this.doEdit()
      })
    },
    doEdit() {
      refund(this.form).then(res => {
        this.resetForm()
        this.$notify({
          title: '操作成功',
          type: 'success',
          duration: 2500
        })
        this.loading = false
        this.dialog = false
        this.$parent.crud.toQuery()
      }).catch(err => {
        this.loading = false
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = {
        id: '',
        orderId: '',
        RefundPrice: '',
        RefundStatus: '',
        RefundReason: '',  
      }
    }
  }
}
</script>

<style scoped>

</style>
