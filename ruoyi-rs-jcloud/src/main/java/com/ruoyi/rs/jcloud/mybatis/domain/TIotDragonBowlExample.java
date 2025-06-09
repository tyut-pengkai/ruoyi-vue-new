/**
 * @Copyright: lyj  All rights reserved.
 */
package com.ruoyi.rs.jcloud.mybatis.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;

public class TIotDragonBowlExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TIotDragonBowlExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }
        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("`id` =", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("`id` <>", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("`id` >", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`id` >=", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("`id` <", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("`id` <=", value, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("`id` in", values, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("`id` not in", values, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("`id` between", value1, value2, "`id`");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`id` not between", value1, value2, "`id`");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("`user_id` is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("`user_id` is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Integer value) {
            addCriterion("`user_id` =", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Integer value) {
            addCriterion("`user_id` <>", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Integer value) {
            addCriterion("`user_id` >", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("`user_id` >=", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Integer value) {
            addCriterion("`user_id` <", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Integer value) {
            addCriterion("`user_id` <=", value, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Integer> values) {
            addCriterion("`user_id` in", values, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Integer> values) {
            addCriterion("`user_id` not in", values, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Integer value1, Integer value2) {
            addCriterion("`user_id` between", value1, value2, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Integer value1, Integer value2) {
            addCriterion("`user_id` not between", value1, value2, "`user_id`");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("`status` like", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("`status` not like", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("`status` =", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("`status` <>", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("`status` >", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("`status` >=", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("`status` <", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("`status` <=", value, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("`status` in", values, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("`status` not in", values, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("`status` between", value1, value2, "`status`");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("`status` not between", value1, value2, "`status`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1IsNull() {
            addCriterion("`fixed_time_open_1` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1IsNotNull() {
            addCriterion("`fixed_time_open_1` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1Like(String value) {
            addCriterion("`fixed_time_open_1` like", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1NotLike(String value) {
            addCriterion("`fixed_time_open_1` not like", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1EqualTo(String value) {
            addCriterion("`fixed_time_open_1` =", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1NotEqualTo(String value) {
            addCriterion("`fixed_time_open_1` <>", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1GreaterThan(String value) {
            addCriterion("`fixed_time_open_1` >", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_1` >=", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1LessThan(String value) {
            addCriterion("`fixed_time_open_1` <", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_1` <=", value, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1In(List<String> values) {
            addCriterion("`fixed_time_open_1` in", values, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1NotIn(List<String> values) {
            addCriterion("`fixed_time_open_1` not in", values, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1Between(String value1, String value2) {
            addCriterion("`fixed_time_open_1` between", value1, value2, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen1NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_open_1` not between", value1, value2, "`fixed_time_open_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2IsNull() {
            addCriterion("`fixed_time_open_2` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2IsNotNull() {
            addCriterion("`fixed_time_open_2` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2Like(String value) {
            addCriterion("`fixed_time_open_2` like", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2NotLike(String value) {
            addCriterion("`fixed_time_open_2` not like", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2EqualTo(String value) {
            addCriterion("`fixed_time_open_2` =", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2NotEqualTo(String value) {
            addCriterion("`fixed_time_open_2` <>", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2GreaterThan(String value) {
            addCriterion("`fixed_time_open_2` >", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_2` >=", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2LessThan(String value) {
            addCriterion("`fixed_time_open_2` <", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_2` <=", value, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2In(List<String> values) {
            addCriterion("`fixed_time_open_2` in", values, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2NotIn(List<String> values) {
            addCriterion("`fixed_time_open_2` not in", values, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2Between(String value1, String value2) {
            addCriterion("`fixed_time_open_2` between", value1, value2, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen2NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_open_2` not between", value1, value2, "`fixed_time_open_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3IsNull() {
            addCriterion("`fixed_time_open_3` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3IsNotNull() {
            addCriterion("`fixed_time_open_3` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3Like(String value) {
            addCriterion("`fixed_time_open_3` like", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3NotLike(String value) {
            addCriterion("`fixed_time_open_3` not like", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3EqualTo(String value) {
            addCriterion("`fixed_time_open_3` =", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3NotEqualTo(String value) {
            addCriterion("`fixed_time_open_3` <>", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3GreaterThan(String value) {
            addCriterion("`fixed_time_open_3` >", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_3` >=", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3LessThan(String value) {
            addCriterion("`fixed_time_open_3` <", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_open_3` <=", value, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3In(List<String> values) {
            addCriterion("`fixed_time_open_3` in", values, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3NotIn(List<String> values) {
            addCriterion("`fixed_time_open_3` not in", values, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3Between(String value1, String value2) {
            addCriterion("`fixed_time_open_3` between", value1, value2, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeOpen3NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_open_3` not between", value1, value2, "`fixed_time_open_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1IsNull() {
            addCriterion("`fixed_time_close_1` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1IsNotNull() {
            addCriterion("`fixed_time_close_1` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1Like(String value) {
            addCriterion("`fixed_time_close_1` like", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1NotLike(String value) {
            addCriterion("`fixed_time_close_1` not like", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1EqualTo(String value) {
            addCriterion("`fixed_time_close_1` =", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1NotEqualTo(String value) {
            addCriterion("`fixed_time_close_1` <>", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1GreaterThan(String value) {
            addCriterion("`fixed_time_close_1` >", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_1` >=", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1LessThan(String value) {
            addCriterion("`fixed_time_close_1` <", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_1` <=", value, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1In(List<String> values) {
            addCriterion("`fixed_time_close_1` in", values, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1NotIn(List<String> values) {
            addCriterion("`fixed_time_close_1` not in", values, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1Between(String value1, String value2) {
            addCriterion("`fixed_time_close_1` between", value1, value2, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose1NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_close_1` not between", value1, value2, "`fixed_time_close_1`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2IsNull() {
            addCriterion("`fixed_time_close_2` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2IsNotNull() {
            addCriterion("`fixed_time_close_2` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2Like(String value) {
            addCriterion("`fixed_time_close_2` like", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2NotLike(String value) {
            addCriterion("`fixed_time_close_2` not like", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2EqualTo(String value) {
            addCriterion("`fixed_time_close_2` =", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2NotEqualTo(String value) {
            addCriterion("`fixed_time_close_2` <>", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2GreaterThan(String value) {
            addCriterion("`fixed_time_close_2` >", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_2` >=", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2LessThan(String value) {
            addCriterion("`fixed_time_close_2` <", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_2` <=", value, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2In(List<String> values) {
            addCriterion("`fixed_time_close_2` in", values, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2NotIn(List<String> values) {
            addCriterion("`fixed_time_close_2` not in", values, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2Between(String value1, String value2) {
            addCriterion("`fixed_time_close_2` between", value1, value2, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose2NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_close_2` not between", value1, value2, "`fixed_time_close_2`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3IsNull() {
            addCriterion("`fixed_time_close_3` is null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3IsNotNull() {
            addCriterion("`fixed_time_close_3` is not null");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3Like(String value) {
            addCriterion("`fixed_time_close_3` like", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3NotLike(String value) {
            addCriterion("`fixed_time_close_3` not like", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3EqualTo(String value) {
            addCriterion("`fixed_time_close_3` =", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3NotEqualTo(String value) {
            addCriterion("`fixed_time_close_3` <>", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3GreaterThan(String value) {
            addCriterion("`fixed_time_close_3` >", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3GreaterThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_3` >=", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3LessThan(String value) {
            addCriterion("`fixed_time_close_3` <", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3LessThanOrEqualTo(String value) {
            addCriterion("`fixed_time_close_3` <=", value, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3In(List<String> values) {
            addCriterion("`fixed_time_close_3` in", values, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3NotIn(List<String> values) {
            addCriterion("`fixed_time_close_3` not in", values, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3Between(String value1, String value2) {
            addCriterion("`fixed_time_close_3` between", value1, value2, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andFixedTimeClose3NotBetween(String value1, String value2) {
            addCriterion("`fixed_time_close_3` not between", value1, value2, "`fixed_time_close_3`");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("`remark` is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("`remark` is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("`remark` like", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("`remark` not like", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("`remark` =", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("`remark` <>", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("`remark` >", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("`remark` >=", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("`remark` <", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("`remark` <=", value, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("`remark` in", values, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("`remark` not in", values, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("`remark` between", value1, value2, "`remark`");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("`remark` not between", value1, value2, "`remark`");
            return (Criteria) this;
        }

    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue; 
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;  
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
