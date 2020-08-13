export default [
  {
    path: "/activity"
    // component: () => import("@views/home/index.vue")
  },
  {
    path: "/activity/poster/:id/:type",
    name: "Poster",
    meta: {
      title: "海报",
      keepAlive: false,
      backgroundColor: "#00c17b",
      auth: true
    },
    component: () => import("@views/activity/Poster.vue")
  },
  {
    path: "/activity/dargain_detail/:id/:partake?",
    name: "DargainDetails",
    meta: {
      title: "砍价详情",
      keepAlive: false,
      backgroundColor: "#00c17b",
      auth: true
    },
    component: () => import("@views/activity/DargainDetails.vue")
  },
  {
    path: "/activity/bargain",
    name: "GoodsBargain",
    meta: {
      title: "砍价列表",
      keepAlive: false,
      backgroundColor: "#00c17b"
    },
    component: () => import("@views/activity/GoodsBargain.vue")
  },
  {
    path: "/activity/bargain/record",
    name: "BargainRecord",
    meta: {
      title: "砍价记录",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/activity/BargainRecord.vue")
  },
  {
    path: "/activity/group",
    name: "GoodsGroup",
    meta: {
      title: "拼团列表",
      keepAlive: false,
      backgroundColor: "#00c17b"
    },
    component: () => import("@views/activity/GoodsGroup.vue")
  },
  {
    path: "/activity/group_detail/:id",
    name: "GroupDetails",
    meta: {
      title: "拼团详情",
      keepAlive: false
    },
    component: () => import("@views/activity/GroupDetails.vue")
  },
  {
    path: "/activity/group_rule/:id",
    name: "GroupRule",
    meta: {
      title: "拼团",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/activity/GroupRule.vue")
  },
  {
    path: "/activity/goods_seckill",
    name: "GoodsSeckill",
    meta: {
      title: "限时抢购",
      keepAlive: true,
      backgroundColor: "#ffffff"
    },
    component: () => import("@views/activity/GoodsSeckill.vue")
  },
  {
    path: "/activity/seckill_detail/:id?/:time?/:status?",
    name: "SeckillDetails",
    meta: {
      title: "抢购详情页",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/activity/SeckillDetails.vue")
  }
];
