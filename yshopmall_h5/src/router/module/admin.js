export default [
  {
    path: "/customer/index",
    name: "OrderIndex",
    meta: {
      title: "订单首页",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/orderAdmin/OrderIndex.vue")
  },
  {
    path: "/customer/orders/:types?",
    name: "AdminOrderList",
    meta: {
      title: "订单列表",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/orderAdmin/AdminOrderList.vue")
  },
  {
    path: "/customer/delivery/:oid?",
    name: "GoodsDeliver",
    meta: {
      title: "订单发货",
      keepAlive: true,
      auth: true
    },
    component: () => import("@views/orderAdmin/GoodsDeliver.vue")
  },
  {
    path: "/customer/orderdetail/:oid?",
    name: "AdminOrder",
    meta: {
      title: "订单详情",
      keepAlive: false,
      auth: true
    },
    component: () => import("@views/orderAdmin/AdminOrder.vue")
  },
  {
    path: "/order/order_cancellation",
    name: "OrderCancellation",
    meta: {
      title: "订单核销",
      keepAlive: true,
      auth: true,
      backgroundColor: "#fff"
    },
    component: () => import("@views/orderAdmin/OrderCancellation.vue")
  }
];
