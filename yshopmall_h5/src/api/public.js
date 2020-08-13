import request from "@utils/request";

export function getHomeData() {
  return request.get("index", {}, { login: false });
}

export function getArticleList(q) {
  return request.get("/article/list/", q, { login: false });
}

export function getShare() {
  return request.get("/share", {}, { login: false });
}

export function getArticleDetails(id) {
  return request.get("/article/details/" + id, {}, { login: false });
}

export function getWechatConfig() {
  return request.get(
    "/wechat/config",
    { url: document.location.href },
    { login: false }
  );
}

export function wechatAuth(code, spread, login_type) {
  return request.get(
    "/wechat/auth",
    { code, spread, login_type },
    { login: false }
  );
}

export function getLogistics() {
  return request.get("/logistics", {}, { login: false });
}


