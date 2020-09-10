<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="120px">
      <el-form-item label="快递公司编号" prop="code">
        <el-input v-model="form.code" style="width: 300px;" :maxlength="10" />
      </el-form-item>
      <el-form-item label="快递公司名称" prop="name">
        <el-input v-model="form.name" style="width: 300px;" :maxlength="20" />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input v-model="form.sort" style="width: 300px;" :maxlength="3" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxExpress'
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
        code: '',
        name: '',
        sort: 0
      },
      rules: {
        code: [
          { required: true, message: '请输入快递公司编号', trigger: 'blur' },
          {
            pattern: /^[a-zA-Z]+$/,  //正则
            message: '请输入英文',
            trigger: 'blur'
          }
        ],
        name: [
          { required: true, message: '请输入快递公司名称', trigger: 'blur' }
        ],
        sort: [
          {
            pattern: /^[0-9]+$/,  //正则
            message: '请输入数字',
            trigger: 'blur'
          }
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
        code: '',
        name: '',
        sort: 0
      }
    }
  }
}
</script>

<style scoped>

</style>
