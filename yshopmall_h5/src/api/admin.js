import request from "@utils/request";

export function getStatisticsInfo() {
  return request.get("/admin/order/statistics", {}, { login: true });
}

export function getStatisticsMonth(where) {
  return request.get("/admin/order/data", where, { login: true });
}

export function getAdminOrderList(where) {
  return request.get("/admin/order/list", where, { login: true });
}

export function setAdminOrderPrice(data) {
  return request.post("/admin/order/price", data, { login: true });
}

export function setAdminOrderRemark(data) {
  return request.post("/admin/order/remark", data, { login: true });
}

export function getAdminOrderDetail(orderId) {
  return request.get("/admin/order/detail/" + orderId, {}, { login: true });
}

export function getAdminOrderDelivery(orderId) {
  return request.get("/admin/order/detail/" + orderId, {}, { login: true });
}

export function setAdminOrderDelivery(data) {
  return request.post("/admin/order/delivery/keep", data, { login: true });
}

export function setOfflinePay(data) {
  return request.post("/admin/order/offline", data, { login: true });
}

export function setOrderRefund(data) {
  return request.post("/admin/order/refund", data, { login: true });
}
