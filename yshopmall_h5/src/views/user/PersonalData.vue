<template>
  <div class="personal-data">
    <div class="wrapper">
      <div class="title">管理我的账号</div>
      <div class="wrapList">
        <div class="item acea-row row-between-wrapper on">
          <div class="picTxt acea-row row-between-wrapper">
            <div class="pictrue">
              <VueCoreImageUpload
                class="btn btn-primary"
                :crop="false"
                compress="80"
                @imageuploaded="imageuploaded"
                :headers="headers"
                :max-file-size="5242880"
                :credentials="false"
                inputAccept="image/*"
                inputOfFile="file"
                :url="url"
                ref="upImg"
              >
                <div class="pictrue">
                  <img :src="userInfo.avatar" />
                </div>
              </VueCoreImageUpload>
              <img
                src="@assets/images/alter.png"
                class="alter"/>
            </div>
            <div class="text">
              <div class="name line1">{{ userInfo.nickname }}</div>
              <div class="phone">绑定手机号：{{ userInfo.phone }}</div>
            </div>
          </div>
          <div class="currentBnt acea-row row-center-wrapper font-color-red">
            当前账号
          </div>
        </div>
      </div>
    </div>
    <div class="list">
      <div class="item acea-row row-between-wrapper">
        <div>昵称</div>
        <div class="input">
          <input type="text" v-model="userInfo.nickname" />
        </div>
      </div>
      <div class="item acea-row row-between-wrapper">
        <div>ID号</div>
        <div class="input acea-row row-between-wrapper">
          <input type="text" :value="userInfo.uid" disabled class="id" /><span
            class="iconfont icon-suozi"
          ></span>
        </div>
      </div>
      <div v-if="!userInfo.phone">
        <router-link
          :to="'/user/binding'"
          class="item acea-row row-between-wrapper"
        >
          <div>绑定手机号</div>
          <div class="input">
            点击绑定手机号<span class="iconfont icon-xiangyou"></span>
          </div>
        </router-link>
      </div>
      <div class="item acea-row row-between-wrapper" v-else-if="userInfo.phone">
        <div>手机号码</div>
        <div class="input acea-row row-between-wrapper">
          <div class="input acea-row row-between-wrapper">
            <input
              type="text"
              :value="userInfo.phone"
              disabled
              class="id"
            /><span class="iconfont icon-suozi"></span>
          </div>
        </div>
      </div>
    </div>
    <div class="modifyBnt bg-color-red" @click="submit">保存修改</div>
    <div
      class="logOut cart-color acea-row row-center-wrapper"
      @click="logout"
      v-if="!isWeixin"
    >
      退出登录
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { trim, VUE_APP_API_URL, isWeixin } from "@utils";
import VueCoreImageUpload from "vue-core-image-upload";
import { postUserEdit, getLogout, getUser } from "@api/user";
import { clearAuthStatus } from "@libs/wechat";


export default {
  name: "PersonalData",
  components: {
    VueCoreImageUpload
  },
  data: function() {
    return {
      url: `${VUE_APP_API_URL}/api/upload`,
      headers: {
        Authorization: "Bearer " + this.$store.state.app.token
      },
      avatar: "",
      isWeixin: false,
      currentAccounts: 0,
      userIndex: 0
    };
  },
  watch: {
    $route(n) {
      if (n.name === "PersonalData") this.$store.dispatch("USERINFO", true);
    }
  },
  computed: mapGetters(["userInfo"]),
  mounted: function() {
    this.avatar = this.userInfo.avatar;
    this.isWeixin = isWeixin();
  },
  methods: {
    imageuploaded(res) {
      if (res.errno !== 0)
        return this.$dialog.error(res.msg || "上传图片失败");
      if (this.userInfo === undefined) return;
      this.$set(this.userInfo, "avatar", res.link);
    },

    submit: function() {
      let userInfo = this.userInfo;
      postUserEdit({
        nickname: trim(this.userInfo.nickname),
        avatar: userInfo.avatar
      }).then(
        res => {
          this.$store.dispatch("USERINFO", true);
          this.$dialog.success(res.msg);
          this.$router.go(-1);
        },
        error => {
          this.$dialog.error(error.msg);
        }
      );
    },
    logout: function() {
      this.$dialog.confirm({
        mes: "确认退出登录?",
        opts: () => {
          getLogout()
            .then(res => {
              this.$store.commit("LOGOUT");
              clearAuthStatus();
              location.href = location.origin;
              console.log(res);
            })
            .catch(err => {
              console.log(err);
            });
        }
      });
    }
  }
};
</script>
