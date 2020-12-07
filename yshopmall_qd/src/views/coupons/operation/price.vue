
<!--商品属性页面-->
<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="title" width="900px">
     <!--规则编辑-->
    <div style='border:1px solid #e4e7ec;border-radius:5px;padding:20px'>
      <div>
        <el-row :gutter="10">
          <el-col style='width:80px'><span>时间段</span></el-col>
          <el-col
            style="position: relative;margin-right: 6px;width:auto"
          >
            <div>
              <el-date-picker
                v-model="date"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="MM-dd"
                value-format="MMdd"
              >
              </el-date-picker>
            </div>
          </el-col>
          <el-col :span="5"><el-button type="primary" @click="addDate">添加</el-button></el-col>
        </el-row>
      </div>
    </div>
    <!--规则下产品属性设置-->
      <el-form ref='price'  :model='form' :inline='true'>
    <div style='border:1px solid #e4e7ec;border-radius:5px;padding:20px;margin-top:20px;'>
        <div v-if="!prices.length">
          <div style="font-size:12px;text-align: center">
            <span v-if="loading">
              <i class="el-icon-loading" style="font-size:22px;color:#409EFF"></i>
              加载中。。。
              </span>
            <span v-else>无任何数据</span>
          </div>
          <div v-if="isEmpty">
            <el-row :gutter="10">
              <el-col :span="6">
                <el-button type="primary" @click="clear">清空所有属性</el-button>
              </el-col>
            </el-row>
          </div>
        </div>
        <div v-else>
        <template v-for="(price,index) in prices">
          <div :key='index' style='border-bottom:1px dashed #eee;margin-bottom:10px;'>
            <el-row :gutter="24" type="flex" align="middle">
              <el-col :span="4">
                <p style="font-size:12px;text-align: center">
                  {{ price.startDateStr.slice(0,2).concat('-',price.startDateStr.slice(2)) }}
                  ~
                  {{ price.endDateStr.slice(0,2).concat('-',price.endDateStr.slice(2)) }}
                </p>
              </el-col>
              <el-col :span="4">
                <el-form-item :prop='`sellingPrice${index}`' label="销售价:"
                :rules="rules.sellingPrice">
                  <el-input v-model.trim="form['sellingPrice'+index]" style="width: 100%" placeholder="销售价"
                            @input="(val)=>{prices[index].sellingPrice=val}"
                            @change="setCommission($event,index)"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-form-item :prop='`commission${index}`' label="佣金:"
                              :rules='rules.commission'>
                  <el-input v-model.trim="form['commission'+index]" placeholder="佣金" style="width: 100%"
                            @change="changeCommission($event,index)"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item :prop='`scenicPrice${index}`'  label="旅行社价格:"
                :rules='rules.scenicPrice'>
                  <el-input v-model.trim="form['scenicPrice'+index]" placeholder="旅行社价格" style="width: 100%" @input="(val)=>{prices[index].scenicPrice=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item :prop='`travelPrice${index}`' label="景区推广价格:"
                :rules='rules.travelPrice'>
                  <el-input v-model.trim="form['travelPrice'+index]" placeholder="景区推广价格" style="width: 100%" @input="(val)=>{prices[index].travelPrice=val}"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="2">
                <el-button type="primary" @click="priceRemove(index)">删除</el-button>
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
    </div>
      </el-form>
  </el-dialog>
</template>

<script>
import { setPriceConfig as set,getPriceConfigList as get,delAllPriceConfig as del } from '@/api/yxCouponsPriceConfig'
import { Message } from 'element-ui'
import { sub } from "@/utils/math"

export default {
  components: {  },
  props: {
  },
  data() {
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
      loading: false, dialog: false,  title: '价格配置',
      isEmpty:false,
      form: {},
      rules: {
        sellingPrice: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,
            message: '请输入数字--限定6位整数2位小数',
            trigger: 'blur'
          },
          { validator: (rule, value, callback)=>{
            if(value<this.form.settlementPrice*1){
                callback(new Error('金额应大于等于平台结算价格'+this.form.settlementPrice));
              }else{
                callback()
              }
          }, trigger: 'blur'},
        ],
        scenicPrice: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,
            message: '请输入数字--限定6位整数2位小数',
            trigger: 'blur'
          },
        ],
        travelPrice: [
          {required:true,message:'必填项',trigger:'blur'},
          {
            pattern: /^[0-9]{0,6}([.]{1}[0-9]{0,2}){0,1}$/,
            message: '请输入数字--限定6位整数2位小数',
            trigger: 'blur'
          },
        ],
        commission: [
          { required: true, message: '佣金不能为空', trigger: 'blur' }
        ],
      },
      item: {
        startDateStr: "",
        endDateStr: "",
        sellingPrice: "",
        commission: "",
        scenicPrice: "",
        travelPrice: ""
      },
      prices: [],
      date:[],
      submiting: false
    }
  },
  mounted() {
  },
  methods: {
    getPrices(id) {
      this.loading=true
      get(id).then(res => {
        if (Array.isArray(res)) {
          if(res.length){
            for(let i in res){
              this.$set(this.form,['sellingPrice'+i],res[i].sellingPrice)
              this.$set(this.form,['scenicPrice'+i],res[i].scenicPrice)
              this.$set(this.form,['travelPrice'+i],res[i].travelPrice)
              this.$set(this.form,['commission'+i],res[i].commission)
            }
          }
          this.prices=res
          this.loading=false
        }
      }).catch(err=>{
        this.loading=false
      })
    },
    cancel() {
      this.resetForm()
    },
    priceRemove(index) {
      let underArray=this.prices.slice(index+1)
      underArray.forEach((val,idx)=>{
        this.$set(this.form,['sellingPrice'+(index+idx)],this.form['sellingPrice'+(index+idx+1)])
        this.$set(this.form,['scenicPrice'+(index+idx)],this.form['scenicPrice'+(index+idx+1)])
        this.$set(this.form,['travelPrice'+(index+idx)],this.form['travelPrice'+(index+idx+1)])
        this.$set(this.form,['commission'+(index+idx)],this.form['commission'+(index+idx+1)])
      })
      this.prices.splice(index, 1)
      if(!this.prices.length){
        this.isEmpty=true
      }
    },
    resetForm() {
      this.dialog = false
      this.$refs['price'].resetFields()
      this.date=[]
      this.prices=[]
      this.form = {
        id: '',
        sellingPrice: '',
        settlementPrice: ''
      }
    },
    setCommission(sellingPrice,index) {
      this.form['commission'+index]= sub(sellingPrice,this.form.settlementPrice*1)
      this.prices[index].commission=sub(sellingPrice,this.form.settlementPrice*1)
    },
    changeCommission(val,index){
      if(!this.form['sellingPrice'+index]){
        Message({ message: '请先填销售价格', type: 'error' })
        this.form['commission'+index]=' '
      }else if(val>sub(this.form['sellingPrice'+index]*1,this.form.settlementPrice*1)){
        Message({ message: '佣金不能大于 销售价-结算价格='+sub(this.form['sellingPrice'+index]*1,this.form.settlementPrice*1), type: 'error' })
        this.form['commission'+index]=sub(this.form['sellingPrice'+index]*1,this.form.settlementPrice*1)
      }else{
        this.prices[index].commission=val
      }
    },
    submit() {
      this.$refs.price.validate(ret=>{
        if(!ret){
          Message({ message: '请查看必填项是否填充正确', type: 'error' })
          return
        }else{
          var that = this
          that.submiting = true
          this.loading = false
          set({
            couponId:this.form.id,
            data:this.prices
          }).then(res => {
            Message({ message: '操作成功', type: 'success' })
            this.dialog = false
            this.resetForm()
          }).catch(err => {
            // Message({ message: '网络异常，请稍后再试！', type: 'error' })
            // this.loading = false
            // this.$refs.price.resetFields()
            console.log(err.response.data.message)
          })
        }
      })
    },
    clear() {
      this.$confirm(`确定要清空价格配置数据操作?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          del(this.form.id).then(({ data }) => {
            Message({ message: '操作成功', type: 'success' })
            this.isEmpty = false
            this.getPrices(this.form.id)
          })
        })
        .catch(() => { })
    },
    addDate(){
      if(this.date.length<2){
        Message({ message: '请选择时间段', type: 'error' })
        return
      }
      let obj=JSON.parse(JSON.stringify(this.item))
      obj.startDateStr=this.date[0]
      obj.endDateStr=this.date[1]
      this.prices.unshift(obj)
      for(let i in this.prices){
        this.$set(this.form,['sellingPrice'+i],this.prices[i].sellingPrice)
        this.$set(this.form,['scenicPrice'+i],this.prices[i].scenicPrice)
        this.$set(this.form,['travelPrice'+i],this.prices[i].travelPrice)
        this.$set(this.form,['commission'+i],this.prices[i].commission)
      }
    }
  }
}
</script>

