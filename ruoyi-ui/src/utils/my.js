export const year = 365 * 24 * 60 * 60;
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

// 判断两个对象是否完全相等
export function cmp(x, y) {
  // If both x and y are null or undefined and exactly the same 
  if (x === y) {
    return true;
  }
  // If they are not strictly equal, they both need to be Objects 
  if (!(x instanceof Object) || !(y instanceof Object)) {
    return false;
  }
  //They must have the exact same prototype chain,the closest we can do is
  //test the constructor. 
  if (x.constructor !== y.constructor) {
    return false;
  }
  for (var p in x) {
    //Inherited properties were tested using x.constructor === y.constructor
    if (x.hasOwnProperty(p)) {
      // Allows comparing x[ p ] and y[ p ] when set to undefined 
      if (!y.hasOwnProperty(p)) {
        return false;
      }
      // If they have the same strict value or identity then they are equal 
      if (x[p] === y[p]) {
        continue;
      }
      // Numbers, Strings, Functions, Booleans must be strictly equal 
      if (typeof (x[p]) !== "object") {
        return false;
      }
      // Objects and Arrays must be tested recursively 
      if (!Object.equals(x[p], y[p])) {
        return false;
      }
    }
  }
  for (p in y) {
    // allows x[ p ] to be set to undefined 
    if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) {
      return false;
    }
  }
  return true;
};
