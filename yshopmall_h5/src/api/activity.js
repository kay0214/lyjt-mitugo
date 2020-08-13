import request from "@utils/request";

export function getCombinationList(data) {
  return request.get("/combination/list", data, { login: false });
}

export function getCombinationDetail(id) {
  return request.get("/combination/detail/" + id, {}, { login: true });
}

export function getCombinationPink(id) {
  return request.get("/combination/pink/" + id);
}

export function getCombinationRemove(data) {
  return request.post("/combination/remove", data);
}

export function getCombinationPoster(data) {
  return request.post("/combination/poster", data);
}

export function getSeckillConfig() {
  return request.get("/seckill/index", {}, { login: false });
}

export function getSeckillList(time, data) {
  return request.get("/seckill/list/" + time, data, { login: false });
}

export function getSeckillDetail(id) {
  return request.get("/seckill/detail/" + id, {}, { login: false });
}

export function getBargainList(data) {
  return request.get("/bargain/list", data, { login: false });
}

export function getBargainDetail(id) {
  return request.get("/bargain/detail/" + id);
}

export function getBargainShare(data) {
  return request.post("/bargain/share", data);
}

export function getBargainStart(data) {
  return request.post("/bargain/start", data);
}

export function getBargainHelp(data) {
  return request.post("/bargain/help", data);
}

export function getBargainHelpPrice(data) {
  return request.post("/bargain/help/price", data);
}

export function getBargainHelpCount(data) {
  return request.post("/bargain/help/count", data);
}

export function getBargainStartUser(data) {
  return request.post("/bargain/start/user", data);
}

export function getBargainHelpList(data) {
  return request.post("/bargain/help/list", data);
}

export function getBargainPoster(data) {
  return request.post("/bargain/poster", data);
}

export function getBargainUserList(data) {
  return request.get("/bargain/user/list", data);
}

export function getBargainUserCancel(data) {
  return request.post("/bargain/user/cancel", data);
}
