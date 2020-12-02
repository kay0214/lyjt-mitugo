<!--商品属性页面-->
<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="title" width="900px">
     <!--规则编辑-->
    <div style='border:1px solid #e4e7ec;border-radius:5px;padding:20px 20px 2px 20px'>
      <el-form ref="form" :model="form" :inline="true">
        <div>
          <el-row :gutter="10">
            <el-col style='width:80px'><span>规则名称</span></el-col>
            <el-col
              v-for="(item, index) in items"
              :key="index"
              style="position: relative;margin-right: 6px;width:auto"
            >
              <el-form-item :prop='`itemValue${index}`' :rules="rules.itemValue">
                <el-input
                  placeholder="设置名称" maxlength="10"
                  v-model="form['itemValue'+index]" style=""
                   @input="(val)=>{item.value=val}">
                  <i slot="suffix" v-if='!item.attrHidden && item.value && item.value !== ""' class="el-input__icon el-icon-check" @click='attrHiddenBool(item,index)'></i>
                  <i slot="suffix" class="el-input__icon el-icon-close" @click='handleRemove(index)'></i>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5"><el-button type="primary" @click="handleAdd">添加新规则</el-button></el-col>
          </el-row>
        </div>
        <div
        v-for="(item, index) in items"
        v-show="item.attrHidden == true"
        :key="index"
      >
        <el-row :gutter="13">
          <el-col style='width:80px'><span>{{''+item.value+':'}}</span></el-col>
          <el-col
            v-for="(attr,k) in item.detail"
            :key="attr"
            style='width:auto'
            :name="attr"
          >
            <el-tag closable @close="attrRemove(item,k)">{{ attr }}</el-tag>
          </el-col>
          <el-col style='width:auto'>
            <el-form-item>
              <el-input v-model="item.detailValue" style="" placeholder="设置属性" maxlength="10" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-button type="primary" @click="attrAdd(item)">添加</el-button>
          </el-col>
        </el-row>
      </div>
      <el-form-item v-show="1" style="margin-top:20px">
        <el-row :gutter="24">
          <el-col :span="24"><el-button :loading="loading" type="primary" @click="addGoods(true)">生成</el-button></el-col>
        </el-row>
      </el-form-item>
      </el-form>
    </div>
    <!--规则下产品属性设置-->
      <el-form ref='form2'  :model='form2' :inline='true' v-if="items[0].value!='' && items[0].detail.length>0 && attrs.length">
    <div style='border:1px solid #e4e7ec;border-radius:5px;padding:20px;margin-top:20px;'>
        <template v-for="(attr,index) in attrs">
          <div :key='index' style='border-bottom:1px dashed #eee;margin-bottom:10px;'>
            <el-row :gutter="24">
              <el-col :span="3">
              <template v-for="(val,key,idx) in attr.detail">
                <p :key='idx' style="margin:0 0 8px;font-size:12px;">
                  {{ key }}:{{ val }}
                </p>
              </template>
              </el-col>
              <el-col :span="4">
                <el-form-item :prop='`price${index}`' :class="attr.check ? 'check':''" label="金额:"
                :rules="rules.price">
                  <el-input v-model="form2['price'+index]" style="width: 100%" placeholder="金额" @input="(val)=>{attr.price=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item :prop='`sales${index}`' :class="attr.check ? 'check':''" label="库存:"
                :rules='rules.sales'>
                  <el-input v-model="form2['sales'+index]" placeholder="库存" style="width: 100%" @input="(val)=>{attr.sales=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item :prop='`cost${index}`' :class="attr.check ? 'check':''" label="平台结算价:"
                :rules='rules.cost'>
                  <el-input v-model="form2['cost'+index]" placeholder="平台结算价" style="width: 100%" @input="(val)=>{attr.cost=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item :prop='`commission${index}`' :class="attr.check ? 'check':''" label="佣金:"
                :rules='rules.commission'>
                  <el-input v-model="form2['commission'+index]" placeholder="佣金" style="width: 100%" @input="(val)=>{attr.commission=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="3" style="margin-right: 2px">
                <div class="demo-upload">
                  <!--<img :src="attr.pic">-->
                  <pic-upload-two v-model="attr.pic" />
                </div>
              </el-col>
              <el-col :span="2" style="margin-right: 3px">
                <el-button type="primary" @click="removeGoods(index)">删除</el-button>
              </el-col>
            </el-row>
          </div>
        </template>
        <div>
          <el-row :gutter="10">
            <el-col :span="2">
              <el-button type="primary" :loading="loading" @click="submit">提交</el-button>
            </el-col>
            <el-col :span="6">
              <el-button type="error" @click="clear">清空所有属性</el-button>
            </el-col>
          </el-row>
        </div>
    </div>
      </el-form>
  </el-dialog>
</template>

<script>
import { getCates } from '@/api/yxStoreCategory'
import { add, edit, isFormatAttr, setAttr, clearAttr, getAttr } from '@/api/yxStoreProduct'
import editor from '../../components/Editor'
import picUploadTwo from '@/components/pic-upload-two'
import mulpicUpload from '@/components/mul-pic-upload'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { Message } from 'element-ui'
import { sub } from '@/utils/math'
export default {
  components: { editor, picUploadTwo, mulpicUpload, Treeselect },
  props: {
    isAttr: {
      type: Boolean,
      required: true
    }
  },
  data() {
  // const validateRequire=(r,value,callback)=>{
  //     if(!value){
  //       callback(new Error("必填项"));
  //     }else{
  //       callback()
  //     }
  //   };
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
    const validateInt=(r,value,callback)=>{
      if(parseInt(value)>16777215){
        callback(new Error("最大值为：16777215"));
      }else if(parseInt(value)<0){
        callback(new Error("不能为负值"));
      }else{
        callback()
      }
    };
    return {
      loading: false, dialog: false, cates: [], title: '规则属性',subForm:false,
      form: {},
      form2:{},
      rules: {
        itemValue:[
          {required:true,message:'必填项',trigger:'blur'},
        ],
        price: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: validateNum, trigger: 'blur'},
          { validator: (rule, value, callback)=>{
            let index=rule.field.replace('price','')
            if (value <= 0) {
              callback(new Error('金额应大于0'));
            }
            if(value<this.form2['cost'+index]*1 + this.form2['commission'+index]*1){
                callback(new Error('金额应大于等于(平台结算价格+佣金)'));
              }else{
                if(!this.subForm){
                this.form2['commission'+index]= sub(value,this.form2['cost'+index])
                  this.attrs[index].commission=sub(value,this.form2['cost'+index])
                }
                callback()
              }
          }, trigger: 'blur'},
        ],
        sales: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]+$/,
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: validateInt, trigger: 'blur'},
        ],
        cost: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]+([.]{1}[0-9]+){0,1}$/,
            message: '请输入数字',
            trigger: 'blur'
          },
          { validator: validateNum, trigger: 'blur'},
          { validator: (rule, value, callback)=>{
            let index=rule.field.replace('cost','')
            if (value <= 0) {
              callback(new Error('金额应大于0'));
            }
            if(value>this.form2['price'+index]*1){
                callback(new Error('平台结算价格应小于等于金额'));
              }else{
                this.form2['commission'+index]= sub(this.form2['price'+index],value)
                this.attrs[index].commission=sub(this.form2['price'+index],value)
                callback()
              }
          }, trigger: 'blur'},
        ],
        commission: [
          {required:true,message:'必填项',trigger:'blur'},
          { validator: validateNum, trigger: 'blur'},
        ],
      },
      items: [{
        value: '',
        detailValue: '',
        attrHidden: false,
        detail: []
      }],
      attrs: [],
      hidden: false,
      attrHidden: false,
      submiting: false
    }
  },
  mounted() {
    // console.log('items'+this.items)
    // console.log('attrs'+this.attrs)
    // if(this.items && this.attrs) this.hidden = true;

    // window.changeIMG = (index,pic)=>{
    //   _vm.setAttrPic(index,pic);
    // };
  },
  methods: {
    getAttrs(id) {
      getAttr(id).then(res => {
        if (res) {
          this.hidden = true
          this.items = res.attr
          this.attrs = res.value
          if(res.attr.length){
            for(let i in res.attr){
              this.$set(this.form,['itemValue'+i],res.attr[i].value)
            }
          }
          if(res.value.length){
            for(let i in res.value){
              this.$set(this.form2,['price'+i],res.value[i].price)
              this.$set(this.form2,['sales'+i],res.value[i].sales)
              this.$set(this.form2,['cost'+i],res.value[i].cost)
              this.$set(this.form2,['commission'+i],res.value[i].commission)
            }
          }
        } else {
          this.hidden = false
          this.items = [{
            value: '',
            detailValue: '',
            attrHidden: false,
            detail: []
          }]
          this.attrs = []
          this.$set(this.form,['itemValue'+0],'')
        }
      })
    },
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.loading = true
      if (this.isAttr) {
        this.doAdd()
      } else this.doEdit()
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
      this.$refs['form2'].resetFields()
      this.form = {
        id: '',
        merId: '',
        image: '',
        sliderImage: '',
        storeName: '',
        storeInfo: '',
        keyword: '',
        barCode: '',
        cateId: '',
        price: '',
        vipPrice: '',
        otPrice: '',
        postage: '',
        unitName: '',
        sort: '',
        sales: '',
        stock: '',
        isShow: '',
        isHot: '',
        isBenefit: '',
        isBest: '',
        isNew: '',
        description: '',
        addTime: '',
        isPostage: '',
        isDel: '',
        merUse: '',
        giveIntegral: '',
        cost: '',
        isSeckill: '',
        isGood: '',
        ficti: '',
        browse: '',
        codePath: '',
        soureLink: '',
        settlement:''
      }
    },
    setAttrPic(index, pic) {
      this.$set(this.attrs[index], 'pic', pic)
    },
    attrHiddenBool(item,index) {
      item.value=this.form['itemValue'+index]
      if (item.value == '') {
        Message({ message: '请填写规则名称', type: 'error' })
      } else {
        item.attrHidden = true
      }
    },
    hiddenBool() {
      this.hidden = true
    },
    handleAdd() {
      if (!this.checkAttr()) return
      this.items.push({
        value: '',
        detailValue: '',
        attrHidden: false,
        detail: []
      })
    },
    checkAttr() {
      var bool = true
      this.items.map(function(item) {
        if (!bool) return
        if (!item.value) {
          Message({ message: '请填写规则名称', type: 'error' })
          bool = false
        } else if (!item.detail.length) {
          Message({ message: '请设置规则属性', type: 'error' })
          bool = false
        }
      })
      return bool
    },
    attrAdd(item) {
      if (!item.detailValue.trim()) return false
      item.detail.push(item.detailValue)
      item.detailValue = ''
    },
    handleRemove(index) {
      if (this.items.length > 1) { this.items.splice(index, 1) } else { Message({ message: '请设置至少一个规则', type: 'error' }) }
    },
    attrRemove(item, k) {
      console.log('item:')
      if (item.detail.length == 1) {
        Message({ message: '请设置至少一个属性', type: 'error' })
        return false
      }
      item.detail.splice(k, 1)
    },
    removeGoods(index) {
      this.attrs.splice(index, 1)
    },
    checkGoods() {
      var bool = true
      this.attrs.map(function(attr) {
        if (!bool) return
        if (!Object.keys(attr.detail).length) {
          Message({ message: '请选择至少一个属性', type: 'error' })
          bool = false
        } else if (attr.price != parseFloat(attr.price) || attr.price < 0) {
          Message({ message: '请输入正确的商品价格', type: 'error' })
          bool = false
        } else if (attr.sales != parseInt(attr.sales) || attr.sales < 0) {
          Message({ message: '请输入正确的商品库存', type: 'error' })
          bool = false
        }
      })
      return bool
    },
    addGoods(type) {
      this.$refs.form.validate(ret=>{
        console.log(ret)
        if(!ret){
          Message({ message: '请查看必填项是否填充正确', type: 'error' })
        }else{
          if (this.attrs.length) {
            if (!this.checkGoods()) return
          }
          var that = this
          isFormatAttr(this.form.id, { items: this.items, attrs: this.attrs }).then(res => {
            this.attrs = res
            if(res.length){
              for(let i in res){
                this.$set(this.form2,['price'+i],res[i].price)
                this.$set(this.form2,['sales'+i],res[i].sales)
                this.$set(this.form2,['cost'+i],res[i].cost)
                this.$set(this.form2,['commission'+i],res[i].commission)
              }
            }
          }).catch(err => {
            this.loading = false
            Message({ message: err, type: 'error' })
            console.log(err)
          })
        }
      })
    },
    submit() {
      this.subForm=true
      this.$refs.form2.validate(ret=>{
        this.subForm=false
        if(!ret){
          Message({ message: '请查看必填项是否填充正确', type: 'error' })
          return
        }else{
          var that = this
          that.submiting = true
          if (!this.checkAttr() || !this.checkGoods()) return
          for (const attr in that.attrs) {
            that.attrs[attr].check = false
          }

          // console.log({items:this.items,attrs:this.attrs})
          this.loading = false
          setAttr(this.form.id, { items: this.items, attrs: this.attrs }).then(res => {
            this.attrs = res
            Message({ message: '操作成功', type: 'success' })
            this.$parent.init()
          }).catch(err => {
            this.loading = false
            this.$refs.form2.resetFields()
            console.log(err.response.data.message)
          })
          this.dialog = false
        }
      })
    },
    clear() {
      this.$confirm(`确定要清空属性数据操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          clearAttr(this.form.id).then(({ data }) => {
            Message({ message: '操作成功', type: 'success' })
            // this.dialog = false
            this.getAttrs(this.form.id)
          })
        })
        .catch(() => { })
    },
  }
}
</script>

<style scoped>

  .demo-upload{
    display: block;
    /*//height: 50px;*/
    text-align: center;
    border: 1px solid transparent;
    border-radius: 4px;
    overflow: hidden;
    background: #fff;
    position: relative;
    box-shadow: 0 1px 1px rgba(0,0,0,.2);
    margin-right: 4px;
  }
  .demo-upload img{
    width: 100%;
    height: 100%;
    display: block;
  }

  .demo-upload-cover{
    display: block;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(0,0,0,.6);
  }
  .demo-upload:hover .demo-upload-cover{
    display: block;
  }
  .demo-upload-cover i{
    color: #fff;
    font-size: 20px;
    cursor: pointer;
    margin: 0 2px;
  }

</style>
