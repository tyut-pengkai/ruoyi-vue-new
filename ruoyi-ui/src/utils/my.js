export const year = 12 * 30 * 24 * 60 * 60;
export const month = 30 * 24 * 60 * 60;
export const day = 24 * 60 * 60
export const hour = 60 * 60;
export const minute = 60;
export const second = 1;
export const unitMap = {
  "year": "年",
  "month": "月",
  "day": "天",
  "hour": "小时",
  "minute": "分钟",
  "second": "秒",
}
export function parseSeconds(seconds) {
  if (seconds >= year && seconds % year == 0) {
    return [seconds / year, "year"];
  } else if (seconds >= month && seconds % month == 0) {
    return [seconds / month, "month"];
  } else if (seconds >= day && seconds % day == 0) {
    return [seconds / day, "day"];
  } else if (seconds >= hour && seconds % hour == 0) {
    return [seconds / hour, "hour"];
  } else if (seconds >= minute && seconds % minute == 0) {
    return [seconds / minute, "minute"];
  } else {
    return [seconds, "second"];
  }
}

export function parseUnit(unit) {
  return unitMap[unit];
}

export function parseMoney(val) {
  return Number(val).toFixed(2)
}
