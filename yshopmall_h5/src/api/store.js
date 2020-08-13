import request from "@utils/request";

export function getCategory() {
  return request.get("/category", {}, { login: false });
}

export function getProductDetail(id, data) {
  return request.get("/product/detail/" + id, data, { login: true });
}

export function getProductPoster(id) {
  return request.get("/product/poster/" + id, {}, { login: true });
}

export function getProducts(q) {
  return request.get("/products", q, { login: false });
}

export function getCartNum() {
  return request.get("/cart/count");
}

export function toCollect(id, category) {
  return request.get("/collect/add/" + id + "/" + category);
}

export function getHostProducts(page, limit) {
  return request.get(
    "/product/hot",
    { page: page, limit: limit },
    { login: false }
  );
}

export function getGroomList(type) {
  return request.get("/groom/list/" + type, {}, { login: false });
}

export function postCartAdd(data) {
  return request.post("/cart/add", data);
}

export function getCartList() {
  return request.get("/cart/list");
}

export function postCartDel(ids) {
  return request.post("/cart/del", { ids });
}

export function getCartCount(data) {
  return request.get("/cart/count", data);
}

export function changeCartNum(id, number) {
  return request.post("/cart/num", { id, number });
}

export function getSearchKeyword() {
  return request.get("/search/keyword", {}, { login: false });
}

export function getReplyList(id, q) {
  return request.get("/reply/list/" + id, q, { login: true });
}

export function getReplyConfig(id) {
  return request.get("/reply/config/" + id, {}, { login: true });
}

export function postOrderProduct(unique) {
  return request.post("/order/product", { unique }, { login: true });
}

export function postOrderComment(data) {
  return request.post("/order/comment", data, { login: true });
}

export function storeListApi(data) {
  return request.get("store_list", data, { login: false });
}
