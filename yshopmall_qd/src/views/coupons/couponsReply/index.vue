<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->

<!--      <el-input v-model.trim="query.nickName" clearable placeholder="商户昵称(完全匹配)" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />-->
      <el-input v-model.trim="query.couponName" clearable placeholder="商品名称(完全匹配)" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-input v-model.trim="query.username" clearable placeholder="商户用户名(完全匹配)" style="width: 200px;" class="filter-item" @keyup.enter.native="toQuery" />
      <el-select v-model="query.isReply" clearable placeholder="请选择"
                 style="width: 200px;" class="filter-item">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>

      <!--表单组件-->
      <!--表格渲染-->
      <el-table ref="table" v-loading="loading" :data="data" size="small" style="width: 100%;">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="username" label="用户" />
        <el-table-column prop="merUsername" label="所在商户" />
        <el-table-column prop="couponName" label="商品信息" />
        <el-table-column prop="generalScore" label="分数" />
        <el-table-column prop="" label="评论回复" >
          <template slot-scope="scope">
            <div>{{scope.row.merchantReplyContent}}</div>
            <div v-if="scope.row.isReply" style="text-align: center;color:#909399;">{{ formatTime(scope.row.merchantReplyTime) }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="comment" label="评论内容" />
        <el-table-column prop="" label="评论图片">
          <template slot-scope="scope">
            <div v-if="scope.row.pics">
              <a v-for="pic in handlePic(scope.row.pics)" :href="pic" style="color: #42b983" target="_blank">
                <img :src="pic" alt="点击打开" class="el-avatar">
              </a>
            </div>
            <div v-else>无图</div>
          </template>
        </el-table-column>
        <el-table-column prop="addTime" label="评论时间">
          <template slot-scope="scope">
            <span>{{ formatTime(scope.row.addTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <el-popover
              :ref="scope.row.id"
              v-permission="permission.del"
              placement="top"
              width="180"
            >
              <p>确定删除本条数据吗？</p>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="$refs[scope.row.id].doClose()">取消</el-button>
                <el-button :loading="delLoading" type="primary" size="mini" @click="subDelete(scope.row.id)">确定</el-button>
              </div>
              <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini" />
            </el-popover>
            <el-button v-if="(!scope.row.isReply) && checkPermission(['yxCouponsReply:reply'])" @click="reply(scope.row)">评论回复</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
        title="评论回复"
        :visible.sync="replyShow"
        width="30%"
        center>

        <div>
          <p>评论内容</p>
          <div style="font-size: small; padding:0 20px 20px;">{{replyComment}}</div>
        </div>
        <div>
          <p>评论回复</p>
          <el-input type="textarea" :rows="5" v-model.trim="merchantReplyContent"
                    placeholder="请输入回复内容" maxlength="200"
                    show-word-limit></el-input>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="replyShow = false">取 消</el-button>
        <el-button type="primary" :loading="subLoading" @click="replySubmit()">确 定</el-button>
      </span>
      </el-dialog>
      <!--分页组件-->
      <el-pagination
        :total="total"
        :current-page="page + 1"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>
  </div>
</template>

<script>
import { del,reply } from '@/api/yxCouponsReply'
import pagination from '@crud/Pagination'
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { formatTime } from '@/utils/index'

export default {
  name: 'YxCouponsReply',
  components: { pagination},
  mixins: [initData],
  data() {
    return {

      statusList:[
        {value:0,label:'未回复'},
        {value:1,label:'已回复'}
      ],
      subLoading: false,
      delLoading: false,
      replyShow: false,
      replyComment: '',
      merchantReplyContent: '',
      replyId: '',
      permission: {
        reply: ['admin', 'yxCouponsReply:reply'],
        list: ['admin', 'yxCouponsReply:list'],
        del: ['admin', 'yxCouponsReply:del']
      },
      rules: {
        uid: [
          { required: true, message: '用户ID不能为空', trigger: 'blur' }
        ],
        oid: [
          { required: true, message: '订单ID不能为空', trigger: 'blur' }
        ],
        couponId: [
          { required: true, message: '卡券id不能为空', trigger: 'blur' }
        ],
        generalScore: [
          { required: true, message: '总体感觉不能为空', trigger: 'blur' }
        ],
        comment: [
          { required: true, message: '评论内容不能为空', trigger: 'blur' }
        ],
        addTime: [
          { required: true, message: '评论时间不能为空', trigger: 'blur' }
        ],
        isReply: [
          { required: true, message: '0：未回复，1：已回复不能为空', trigger: 'blur' }
        ],
        merId: [
          { required: true, message: '商户id不能为空', trigger: 'blur' }
        ],
        delFlag: [
          { required: true, message: '是否删除（0：未删除，1：已删除）不能为空', trigger: 'blur' }
        ],
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        updateTime: [
          { required: true, message: '更新时间不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  computed:{
    transferLabel:function(){
      return function(value,list){
        if(list.length){
          let i= list.filter(function(item){
            return new RegExp(item.value, 'i').test(value)
          })
          if(i.length){
            return i[0].label
          }else{
            return ""
          }
        }else{
          return ""
        }
      }
    }
  },
  methods: {
    formatTime,
    checkPermission,
    beforeInit() {
      this.url = 'api/yxCouponsReply'
      const sort = 'id,desc'
      this.params = { page: this.page, size: this.size, sort: sort }
      return true
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        that.init()
        this.$notify({
          title: '删除成功',
          type: 'success',
          duration: 2500
        })
      }).catch(err => {
        this.delLoading = false
        this.$refs[id].doClose()
        console.log(err.response.data.message)
      })
    },
    add() {
      this.isAdd = true
      this.$refs.form.dialog = true
    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        id: data.id,
        uid: data.uid,
        oid: data.oid,
        unique: data.unique,
        productId: data.productId,
        replyType: data.replyType,
        productScore: data.productScore,
        serviceScore: data.serviceScore,
        comment: data.comment,
        pics: data.pics,
        addTime: data.addTime,
        merchantReplyContent: data.merchantReplyContent,
        merchantReplyTime: data.merchantReplyTime,
        isDel: data.isDel,
        isReply: data.isReply
      }
      _this.dialog = true
    },
    reply(data){
      this.replyComment=data.comment
      this.replyId=data.id
      this.merchantReplyContent=''
      this.replyShow=true
    },
    replySubmit(){
      if(this.subLoading){
        this.$notify({
          title: '请勿重复提交',
          type: 'error',
          duration: 2500
        })
        return
      }
      if(!this.merchantReplyContent.length){
        this.$notify({
          title: '请输入回复内容',
          type: 'error',
          duration: 2500
        })
        return
      }
      this.subLoading=true
      let that=this
      reply({
        id:this.replyId,
        merchantReplyContent:this.merchantReplyContent
      }).then(res=>{
        that.$notify({
          title: '回复成功',
          type: 'success',
          duration: 2500
        })
        that.init()
        this.subLoading=false
        this.replyShow=false
      }).catch(err=>{
        this.$notify({
          title: '提交异常，请稍后再试',
          type: 'error',
          duration: 2500
        })
        this.subLoading=false
        this.replyShow=false
      })
    }
  }
}



</script>

<style scoped>
  .table-img {
    display: inline-block;
    text-align: center;
    background: #ccc;
    color: #fff;
    white-space: nowrap;
    position: relative;
    overflow: hidden;
    vertical-align: middle;
    width: 32px;
    height: 32px;
    line-height: 32px;
  }
</style>
