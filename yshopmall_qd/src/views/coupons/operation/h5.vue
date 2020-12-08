<template>
  <el-dialog :append-to-body="true" :close-on-click-modal="false" :before-close="cancel" :visible.sync="dialog" :title="'预览'" width="" style="text-align: center">

    <iframe :src="$store.state.app.h5Url+'/live/pages/CardsCon/index?id='+id" style="width:414px;height: 736px"></iframe>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="cancel">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  data(){
    return{
      dialog: false,
      id:''
    }
  },
  methods: {
    cancel() {
      this.dialog = false
    }
  }
}
</script>
