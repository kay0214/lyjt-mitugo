import request from "@utils/request";

export function login(data) {
  return request.post("/login", data, { login: false });
}

export function loginMobile(data) {
  return request.post("/login/mobile", data, { login: false });
}

export function registerVerify(data) {
  return request.post("/register/verify", data, { login: false });
}

export function register(data) {
  return request.post("/register", data, { login: false });
}


export function getCoupon(q) {
  return request.get("/coupons", q, { login: true });
}

export function getCouponReceive(id) {
  return request.post("/coupon/receive", { couponId: id }, { login: true });
}

export function couponReceiveBatch(couponId) {
  return request.post("/coupon/receive/batch", { couponId });
}

export function getCouponsUser(type) {
  return request.get("/coupons/user/" + type);
}

export function getUser() {
  return request.get("/user");
}

export function getUserInfo() {
  return request.get("/userinfo", { login: true });
}

export function getMenuUser() {
  return request.get("/menu/user");
}

export function getAddressList(data) {
  return request.get("/address/list", data || {});
}

export function getAddressRemove(id) {
  return request.post("/address/del", { id: id });
}

export function getAddressDefaultSet(id) {
  return request.post("/address/default/set", { id: id });
}

export function getAddressDefault() {
  return request.get("/address/default");
}

export function getAddress(id) {
  return request.get("/address/detail/" + id);
}

export function postAddress(data) {
  return request.post("/address/edit", data);
}

export function getCollectUser(page, limit) {
  return request.get("/collect/user", { page: page, limit: limit });
}

export function getCollectDel(id, category) {
  return request.post("/collect/del", { id: id, category: category });
}


export function getCollectAdd(id, category) {
  return request.post("collect/add", { id: id, category: category });
}

export function getSignConfig() {
  return request.get("/sign/config");
}

export function getSignList(page, limit) {
  return request.get("/sign/list", { page: page, limit: limit });
}

export function getSignMonth(page, limit) {
  return request.get("/sign/month", { page: page, limit: limit });
}

export function postSignUser(sign) {
  return request.post("/sign/user", sign);
}

export function postSignIntegral(sign) {
  return request.post("/sign/integral", sign);
}

export function getSpreadInfo() {
  return request.get("/commission");
}

export function getSpreadUser(screen) {
  return request.post("/spread/people", screen);
}

export function getSpreadOrder(where) {
  return request.post("/spread/order", where);
}

export function getCommissionInfo(q, types) {
  return request.get("/spread/commission/" + types, q);
}

export function getIntegralList(q) {
  return request.get("/integral/list", q);
}

export function getBank() {
  return request.get("/extract/bank");
}

export function postCashInfo(cash) {
  return request.post("/extract/cash", cash);
}

export function getVipInfo() {
  return request.get("/user/level/grade");
}

export function getVipTask(id) {
  return request.get("/user/level/task/" + id);
}

export function getBalance() {
  return request.get("/user/balance");
}


export function getSpreadImg() {
  return request.get("/spread/banner");
}

export function postUserEdit(data) {
  return request.post("/user/edit", data);
}



export function rechargeWechat(data) {
  return request.post("/recharge/wechat", data);
}

export function getLogout() {
  return request.post("/auth/logout");
}

export function bindingPhone(data) {
  return request.post("binding", data);
}


export function setDetection() {
  return request.get("user/level/detection");
}

export function getRechargeApi() {
  return request.get("recharge/index");
}
