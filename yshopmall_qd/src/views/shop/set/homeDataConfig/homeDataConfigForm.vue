<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="isAdd ? '新增' : '编辑'" width="500px">
    <el-form ref="form" :model="form" :rules="rules" size="small" label-width="140px">
      <el-form-item label="栏目" prop="groupName">
        <el-select 
          v-model="form.groupName" 
          placeholder="请选择栏目" 
          class="filter-item" 
          style="width: 130px"
        >
          <el-option
            v-for="item in groupNameOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="标题" prop="name">
        <el-input placeholder="请输入标题"  v-model="form.name" style="width: 300px;" maxlength="50" />
      </el-form-item>

      <el-form-item label="售价" prop="price">
        <el-input  placeholder="请输入售价"  v-model="form.price" style="width: 300px;" maxlength="10" />
      </el-form-item>

      <el-form-item label="划线价" prop="crossPrice">
        <el-input placeholder="请输入划线价" v-model="form.crossPrice" style="width: 300px;" maxlength="10" />
      </el-form-item>

      <el-form-item label="小程序跳转page">
        <el-input placeholder="请输入小程序跳转page" v-model="form.url" style="width: 300px;" maxlength="255" />
      </el-form-item>

      <el-form-item label="图片" prop="pic">
        <MaterialList v-model="form.imageArr" style="width: 300px" type="image" :num="1" :width="150" :height="150" 
        @setValue='(val)=>{form.imageArr=val;this.form.pic = val.join(",") ;$refs.form.validateField("pic")}'/>
      </el-form-item>

      <el-form-item label="排序" prop="sort">
        <el-input placeholder="请输入排序" v-model="form.sort" style="width: 300px;" maxlength="3" />
      </el-form-item>

      <el-form-item label="是否显示">
        <el-radio v-model="form.status" :label="1">显示</el-radio>
        <el-radio v-model="form.status" :label="0" style="width: 200px;">不显示</el-radio>
      </el-form-item>

      <el-form-item label="展示方式">
        <el-radio v-model="form.showType" :label="1">横向</el-radio>
        <el-radio v-model="form.showType" :label="0" style="width: 200px;">纵向</el-radio>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <!--<el-input v-model="form.groupName" />-->
      <el-button type="text" @click="cancel">取消</el-button>
      <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { add, edit } from '@/api/yxSystemGroupData'
import picUpload from '@/components/pic-upload'
import MaterialList from '@/components/material'
export default {
  components: { picUpload, MaterialList },
  props: {
    isAdd: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      loading: false, dialog: false,
      //  栏目
      groupNameOptions: [
        { value: 'local_live_module1', label: '模块1' },
        { value: 'local_live_module2', label: '模块2' },
        { value: 'local_live_module3', label: '模块3_1' },
        { value: 'local_live_module4', label: '模块3_2' },
        { value: 'local_live_module5', label: '模块3_3' }
      ],
      form: {
        id: '',
        groupName: '',
        name: '',
        url: '',
        price: '',
        crossPrice: '',
        wxapp_url: '',
        pic: '',
        imageArr: [],
        sort: 0,
        status: 0,
        showType: 0
      },
      rules: {
        groupName: [
          {
            required: true,
            message: '请选择栏目',
            trigger: 'change'
          }
        ],
        name: [
          {
            required: true,
            message: '请输入标题',
            trigger: 'blur'
          }
        ],
        price: [
          {
            pattern: /^\d+(\.\d{1,1})?$/,
            message: '请输入正整数有且只有1位小数'
          }
        ],
        crossPrice: [
          {
            pattern: /^\d+(\.\d{1,1})?$/,
            message: '请输入正整数有且只有1位小数'
          }
        ],
        sort: [
          {
            pattern: /^\d*$/,
            message: '请输入整数'
          }
        ],
        pic: [
          {
            required: true,
            message: '必选项',
            trigger: 'change'
          }
        ]
      }
    }
  },
  watch: {
    'form.imageArr': function(val) {
      if (val) {
        this.form.pic = val.join(',')
      }
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
        groupName: '',
        name: '',
        price: '',
        crossPrice: '',
        url: '',
        wxapp_url: '',
        pic: '',
        imageArr: [],
        sort: 0,
        status: 0,
        showType: 0
      }
    }
  }
}
</script>

<style scoped>

</style>
