<template>
  <el-dialog 
    :append-to-body="true" 
    :close-on-click-modal="false" 
    :before-close="cancel" 
    :visible.sync="dialog" 
    title="名称配置" 
    width="900px"
  >
    <el-form 
      ref="form" 
      :model="form" 
      :rules="rules" 
      size="small" 
      label-width="130px"
    >
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item 
            label="首页栏目一" 
            prop="title_1.title"
          >
            <el-input 
              placeholder="请输入首页栏目一"
              v-model="form.title_1.title" 
              maxlength="10" 
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item 
            label="跳转地址" 
            prop="linkUrl"
          >
            <el-input 
              placeholder="请输入跳转地址"
              v-model="form.title_1.linkUrl" 
              maxlength="200" 
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item 
            label="图片" 
            prop="title_1.imgUrl"
            label-width="60px"
          >
            <MaterialList 
              v-model="form.title_1.imageArr" 
              type="image" 
              :num="1" 
              :width="80" 
              :height="80" 
              @setValue='(val) => {
                form.title_1.imageArr = val; 
                this.form.title_1.imgUrl = val.join(","); 
                $refs.form.validateField("title_1.imgUrl")
              }'
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item 
            label="首页栏目二" 
            prop="title_2.title"
          >
            <el-input 
              placeholder="请输入首页栏目二"
              v-model="form.title_2.title" 
              maxlength="10" 
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item 
            label="跳转地址" 
            prop="linkUrl"
          >
            <el-input 
              placeholder="请输入跳转地址"
              v-model="form.title_2.linkUrl" 
              maxlength="200" 
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item 
            label="图片" 
            prop="title_2.imgUrl"
            label-width="60px"
          >
            <MaterialList 
              v-model="form.title_2.imageArr" 
              type="image" 
              :num="1" 
              :width="80" 
              :height="80" 
              @setValue='(val) => {
                form.title_2.imageArr = val; 
                this.form.title_2.imgUrl = val.join(","); 
                $refs.form.validateField("title_2.imgUrl")
              }'
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item 
            label="首页栏目三(1)" 
            prop="title_3_1.title"
          >
            <el-input 
              placeholder="请输入首页栏目三(1)"
              v-model="form.title_3_1.title" 
              maxlength="10" 
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item 
            label="首页栏目三(2)" 
            prop="title_3_2.title"
          >
            <el-input 
              placeholder="请输入首页栏目三(2)"
              v-model="form.title_3_2.title" 
              maxlength="10" 
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="10">
          <el-form-item 
            label="首页栏目三(3)" 
            prop="title_3_3.title"
          >
            <el-input 
              placeholder="请输入首页栏目三(3)"
              v-model="form.title_3_3.title" 
              maxlength="10" 
            />
          </el-form-item>
        </el-col>
      </el-row>

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
//  引入services
import { getNameConfigData, updateNameConfigData } from '@/api/dataConfig/homeConfig/index'
export default {
  components: { picUpload, MaterialList },
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
      imgUrl1: '',
      form: {
        title_1: {
          title: '',
          linkUrl: '',
          imgUrl: '',
          imageArr: []
        },
        title_2: {
          title: '',
          linkUrl: '',
          imgUrl: '',
          imageArr: []
        },
        title_3_1: {
          title: ''
        },
        title_3_2: {
          title: ''
        },
        title_3_3: {
          title: ''
        },
      },
      rules: {
        'title_1.title': [
          {
            required: true,
            message: '请输入首页栏目一',
            trigger: 'blur'
          }
        ],
        'title_2.title': [
          {
            required: true,
            message: '请输入首页栏目二',
            trigger: 'blur'
          }
        ],
        'title_3_1.title': [
          {
            required: true,
            message: '请输入首页栏目三（1）',
            trigger: 'blur'
          }
        ],
        'title_3_2.title': [
          {
            required: true,
            message: '请输入首页栏目三（2）',
            trigger: 'blur'
          }
        ],
        'title_3_3.title': [
          {
            required: true,
            message: '请输入首页栏目三（3）',
            trigger: 'blur'
          }
        ],
        'title_1.imgUrl': [
          {
            required: true,
            message: '必选项',
            trigger: 'change'
          }
        ],
        'title_2.imgUrl': [
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
    'form.title_1.imageArr': function(val) {
      if (val) {
        this.form.title_1.imgUrl = val.join(',')
      }
    },
    'form.title_2.imageArr': function(val) {
      if (val) {
        this.form.title_2.imgUrl = val.join(',')
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
        this.doEdit()
       }
     })
    },
    doEdit() {
      updateNameConfigData(this.form).then(res => {
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
        title_1: {
          title: '',
          linkUrl: '',
          imgUrl: '',
          imageArr: []
        },
        title_2: {
          title: '',
          linkUrl: '',
          imgUrl: '',
          imageArr: []
        },
        title_3_1: {
          title: ''
        },
        title_3_2: {
          title: ''
        },
        title_3_3: {
          title: ''
        },
      }
    }
  }
}
</script>

<style scoped>

</style>
