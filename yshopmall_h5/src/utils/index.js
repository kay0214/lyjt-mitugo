export function dataFormat(time, option) {
  time = +time * 1000;
  const d = new Date(time);
  const now = Date.now();

  const diff = (now - d) / 1000;

  if (diff < 30) {
    return "刚刚";
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + "分钟前";
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + "小时前";
  } else if (diff < 3600 * 24 * 2) {
    return "1天前";
  }
  if (option) {
    //return parseTime(time, option);
  } else {
    return (
      d.getFullYear() +
      "年" +
        (d.getMonth() + 1) +
      "月" +
      d.getDate() +
      "日" +
      d.getHours() +
      "时" +
      d.getMinutes() +
      "分"
    );
  }
}

export function dataFormatT(time) {
  time = +time * 1000;
  const d = new Date(time);

  return (
      d.getFullYear() +
      "/" +
      (d.getMonth() + parseInt(1)) +
      "/" +
      d.getDate()
  );

}

export function dataFormatF(time) {
  time = +time * 1000;
  const d = new Date(time);
  return (
      d.getFullYear() +
      "年" +
      (d.getMonth() + 1) +
      "月" +
      d.getDate() +
      "日" +
      d.getHours() +
      "时" +
      d.getMinutes() +
      "分"
  );
}

export function dataFormatS(time) {
  time = +time * 1000;
  const d = new Date(time);
  return (
      d.getFullYear() +
      "-" +
      (d.getMonth() + 1) +
      "-" +
      d.getDate() +
      " " +
      d.getHours() +
      ":" +
      d.getMinutes()
  );
}


export function trim(str) {
  return String.prototype.trim.call(str);
}

export function isType(arg, type) {
  return Object.prototype.toString.call(arg) === "[object " + type + "]";
}

export function isWeixin() {
  return navigator.userAgent.toLowerCase().indexOf("micromessenger") !== -1;
}


export function parseQuery() {
  const res = {};

  const query = (location.href.split("?")[1] || "")
    .trim()
    .replace(/^(\?|#|&)/, "");

  if (!query) {
    return res;
  }

  query.split("&").forEach(param => {
    const parts = param.replace(/\+/g, " ").split("=");
    const key = decodeURIComponent(parts.shift());
    const val = parts.length > 0 ? decodeURIComponent(parts.join("=")) : null;

    if (res[key] === undefined) {
      res[key] = val;
    } else if (Array.isArray(res[key])) {
      res[key].push(val);
    } else {
      res[key] = [res[key], val];
    }
  });

  return res;
}

//const VUE_APP_API_URL = 'http://127.0.0.1:8009/api';
const VUE_APP_API_URL = process.env.VUE_APP_API_URL;
const VUE_APP_WS_URL =
  process.env.VUE_APP_WS_URL || `ws:${location.hostname}:20003`;

export { VUE_APP_API_URL, VUE_APP_WS_URL };
