import request from "@utils/request";

export function postOrderConfirm(cartId) {
  return request.post("/order/confirm", { cartId });
}

export function postOrderComputed(key, data) {
  return request.post("/order/computed/" + key, data);
}

export function getOrderCoupon(price) {
  return request.get("/coupons/order/" + (parseFloat(price) || 0));
}

export function createOrder(key, data) {
  return request.post("/order/create/" + key, data || {});
}

export function getOrderData() {
  return request.get("/order/data");
}

export function getOrderList(data) {
  return request.get("/order/list", data);
}

export function cancelOrder(id) {
  return request.post("/order/cancel", { id });
}

export function orderDetail(id) {
  return request.get("/order/detail/" + id);
}

export function getRefundReason() {
  return request.get("/order/refund/reason");
}

export function postOrderRefund(data) {
  return request.post("/order/refund/verify", data);
}

export function takeOrder(uni) {
  return request.post("/order/take", { uni });
}

export function delOrder(uni) {
  return request.post("/order/del", { uni });
}

export function express(params) {
  return request.post("order/express", params);
}

export function payOrder(uni, paytype, from) {
  return request.post("order/pay", { uni, paytype, from });
}

export function orderVerific(verifyCode, isConfirm) {
  return request.post("order/order_verific", { verifyCode, isConfirm });
}
