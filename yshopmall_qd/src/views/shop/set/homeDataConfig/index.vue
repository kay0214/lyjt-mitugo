<template>
  <div class="app-container">
    <!--工具栏-->
    <div class="head-container">
      <!-- 检索 -->
      <el-select 
        v-model="groupName"
        placeholder="栏目" 
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
      <el-select 
        v-model="status" 
        placeholder="状态" 
        class="filter-item" 
        style="width: 130px"
      >
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" size="mini" type="success" icon="el-icon-search" @click="toQuery">搜索</el-button>
      <!-- 新增 -->
      <div style="display: inline-block;margin: 0px 2px;">
        <el-button
          v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_CREATE']"
          class="filter-item"
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="add"
        >新增</el-button>
      </div>
      <!-- 名称配置 -->
      <el-button
        type="danger"
        class="filter-item"
        size="mini"
        @click="nameConfig"
      >名称配置</el-button>
    </div>
    <!--添加/编辑表单组件-->
    <eForm ref="form" :is-add="isAdd" />
    <!--页面配置表单组件-->
    <nameForm ref="nameForm" />
    <!--表格渲染-->
    <el-table v-loading="loading" :data="data" size="small" style="width: 100%;">
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="map.name" label="标题" />
      <el-table-column ref="table" label="图片">
        <template slot-scope="scope">
          <a :href="scope.row.map.pic" style="color: #42b983" target="_blank"><img :src="scope.row.map.pic" alt="点击打开" class="el-avatar"></a>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" />
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status === 1" style="cursor: pointer" :type="''">显示</el-tag>
            <el-tag v-else style="cursor: pointer" :type=" 'info' ">不显示</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column v-if="checkPermission(['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_EDIT','YXSYSTEMGROUPDATA_DELETE'])" label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <el-button v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_EDIT']" size="mini" type="primary" icon="el-icon-edit" @click="edit(scope.row)" />
          <el-popover
            :ref="scope.row.id"
            v-permission="['admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_DELETE']"
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
        </template>
      </el-table-column>
    </el-table>
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
</template>

<script>
import checkPermission from '@/utils/permission'
import initData from '@/mixins/crud'
import { del } from '@/api/yxSystemGroupData'
//  添加修改弹窗
import eForm from './homeDataConfigForm'
//  页面配置弹窗
import nameForm from './nameConfigForm'
import { getNameConfigData } from '@/api/dataConfig/homeConfig/index'
export default {
  components: { eForm, nameForm },
  mixins: [initData],
  data() {
    return {
      delLoading: false,
      isShow: false,
      groupName: '1',  //  栏目检索全部
      status: '0',   //  默认检索所有
      //  状态
      statusOptions: [
        { value: '0', label: '全部' },
        { value: '1', label: '显示' },
        { value: '2', label: '不显示' }
      ],
      //  栏目
      groupNameOptions: [
        { value: '1', label: '全部' },
        { value: 'local_live_module1', label: '模块1' },
        { value: 'local_live_module2', label: '模块2' },
        { value: 'local_live_module3', label: '模块3_1' },
        { value: 'local_live_module4', label: '模块3_2' },
        { value: 'local_live_module5', label: '模块3_3' }
      ],
    }
  },
  created() {
    this.$nextTick(() => {
      this.init()
    })
  },
  methods: {
    checkPermission,
    beforeInit() {
      this.url = 'api/yxSystemGroupData'
      const sort = 'id,desc'
      this.params = { 
        page: this.page, 
        size: this.size, 
        sort: sort, 
        groupName: this.groupName,
        status: this.status
      }
      return true
    },
    subDelete(id) {
      this.delLoading = true
      del(id).then(res => {
        this.delLoading = false
        this.$refs[id].doClose()
        this.dleChangePage()
        this.init()
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
    //  页面配置
    nameConfig() {
      getNameConfigData().then(res => {
        this.$refs.nameForm.dialog = true
        if (res) {
          this.$refs.nameForm.form = {
            ...res,
            title_1: {
              title: res.title_1.title,
              linkUrl: res.title_1.linkUrl,
              imgUrl: res.title_1.imgUrl,
              imageArr: res.title_1.imgUrl ? res.title_1.imgUrl.split(',') : [],
            },
            title_2: {
              title: res.title_2.title,
              linkUrl: res.title_2.linkUrl,
              imgUrl: res.title_2.imgUrl,
              imageArr: res.title_2.imgUrl ? res.title_2.imgUrl.split(',') : [],
            }
          }
        }
      })
    },
    edit(data) {
      this.isAdd = false
      const _this = this.$refs.form
      _this.form = {
        id: data.id,
        groupName: data.groupName,
        name: data.map.name,
        price: data.map.price,
        crossPrice: data.map.crossPrice,
        url: data.map.url,
        pic: data.map.pic,
        imageArr: data.map.pic ? data.map.pic.split(',') : [],
        sort: data.sort,
        status: data.status,
        showType: data.map.showType,
      }
      _this.dialog = true
    }
  }
}
</script>

<style scoped>

</style>
