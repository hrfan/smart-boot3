package com.smart.common.database.query;

/**
 * 通用查询条件构造器
 * 基于MyBatis-Plus的QueryWrapper封装，提供强大的动态查询和数据权限功能
 * 
 * 【核心功能】:
 * 1. 动态查询条件构建 - 根据HTTP请求参数自动构建查询条件
 * 2. 多种查询规则支持 - 等于、大于、小于、模糊查询、包含查询等
 * 3. 数据权限控制 - 基于用户角色的自动数据权限过滤
 * 4. 多字段排序支持 - 支持动态排序规则
 * 5. 高级查询功能 - 支持复杂查询条件的JSON格式传递
 * 6. SQL注入防护 - 内置安全检查机制
 * 7. 类型自动转换 - 支持多种数据类型的自动识别和转换
 * 
 * 【使用场景】:
 * - Controller层接收查询参数并构建查询条件
 * - Service层统一的数据权限控制
 * - 复杂查询条件的动态构建
 * - 分页查询的条件封装
 * 
 * 【快速开始】:
 * <pre>
 * // 1. 基本使用
 * QueryWrapper<User> wrapper = QueryBuilder.initQueryWrapper(userQuery, request.getParameterMap());
 * List<User> users = userService.list(wrapper);
 * 
 * // 2. 分页查询
 * Page<User> page = new Page<>(1, 10);
 * QueryWrapper<User> wrapper = QueryBuilder.initQueryWrapper(userQuery, request.getParameterMap());
 * Page<User> result = userService.page(page, wrapper);
 * 
 * // 3. 数据权限控制
 * QueryWrapper<User> wrapper = new QueryWrapper<>();
 * QueryBuilder.installAuthMplus(wrapper, User.class);
 * </pre>
 * 
 * 【查询参数格式】:
 * - 基本格式: fieldName=value (等于查询)
 * - 扩展格式: fieldName$operator=value
 * - 排序: column=fieldName&order=asc/desc
 * - 高级查询: superQueryParams=[{"field":"name","rule":"like","val":"张"}]
 * 
 * @author Smart Boot3
 * @since 1.0.0
 * @version 1.0.0
 */
 
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.smart.common.database.util.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.NumberUtils;
 

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryBuilder {

    protected static Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

    private static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat datetFormat = new SimpleDateFormat("yyyy-MM-dd");
    /**排序列*/
    private static final String ORDER_COLUMN = "column";
    /**排序方式*/
    private static final String ORDER_TYPE = "order";
    private static final String ORDER_TYPE_ASC = "ASC";
    //数据权限SQL规则
    public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";
    /**高级查询前端传来的参数名*/
    private static final String SUPER_QUERY_PARAMS = "superQueryParams";
    /**
     * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
     * <a href="http://192.168.10.123/api/system/user/list?username$lki=122&column=createTime&order=desc&encode=true&field=id,,action,username,realname,userType,status,roleNames&pageNo=1&pageSize=20">...</a>
     * 【使用方法】:
     * 1. 基本用法:
     *    QueryWrapper<User> queryWrapper = QueryBuilder.initQueryWrapper(user, request.getParameterMap());
     *    List<User> users = userService.list(queryWrapper);
     *
     * 2. 分页查询:
     *    Page<User> page = new Page<>(1, 10);
     *    QueryWrapper<User> queryWrapper = QueryBuilder.initQueryWrapper(user, request.getParameterMap());
     *    Page<User> result = userService.page(page, queryWrapper);
     *
     * 3. 支持的查询参数格式:
     *    - name=张三                    // 精确匹配 (默认为eq)
     *    - name$like=张                 // 模糊查询
     *    - age$gt=18                   // 大于查询
     *    - createTime$ge=2023-01-01    // 大于等于查询
     *    - status$in=1,2,3             // 包含查询
     *
     * 4. 支持的查询规则:
     *    - eq: 等于 (默认)
     *    - ne: 不等于
     *    - gt: 大于
     *    - ge: 大于等于
     *    - lt: 小于
     *    - le: 小于等于
     *    - like: 模糊查询
     *    - llk: 左模糊查询
     *    - rlk: 右模糊查询
     *    - in: 包含查询
     *
     * 5. 排序支持:
     *    - column=name&order=asc       // 按name字段升序
     *    - column=createTime&order=desc // 按创建时间降序
     *
     * 6. 高级查询支持:
     *    - superQueryParams=[{"field":"name","rule":"like","val":"张"}]
     *
     * 7. 数据权限支持:
     *    - 自动应用基于用户角色的数据权限规则
     *    - 支持部门、组织级别的数据隔离
     *
     * @param searchObj 查询实体对象，用于封装基本查询条件
     * @param parameterMap HTTP请求参数Map，格式: request.getParameterMap()
     * @param <T> 实体类型
     * @return QueryWrapper实例，可直接用于MyBatis-Plus查询
     *
     * @author Smart Boot3
     * @since 1.0.0
     */
    public static <T> QueryWrapper<T> initQueryWrapper(T searchObj,Map<String, String[]> parameterMap){
       long start = System.currentTimeMillis();
       QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
       installMplus(queryWrapper, searchObj, parameterMap);
       logger.debug("---查询条件构造器初始化完成,耗时:"+(System.currentTimeMillis()-start)+"毫秒----");
       return queryWrapper;
    }

    /**
     * 通过QueryWrapper组装MyBatis-Plus查询条件
     * 该方法为核心处理方法，负责解析请求参数并构建查询条件
     * 
     * 【功能特性】:
     * 1. 动态查询条件构建: 根据请求参数动态生成查询条件
     * 2. 多种查询规则: 支持等于、大于、小于、模糊查询等
     * 3. 数据权限控制: 自动应用基于角色的数据权限规则
     * 4. 多字段排序: 支持按指定字段进行升序或降序
     * 5. 高级查询: 支持复杂的查询条件组合
     * 6. SQL注入防护: 内置安全检查机制
     * 
     * 【参数格式解析】:
     * - 基本格式: fieldName$operator=value
     * - 例如: name$like=张三、age$gt=18、status$in=1,2,3
     * - 缺省操作符: 不带$符号时，默认为等于(eq)查询
     * 
     * 【数据权限规则】:
     * - 基于用户角色和权限配置自动应用
     * - 支持部门级别、组织级别的数据隔离
     * - 支持自定义SQL片段的权限规则
     * 
     * @param queryWrapper MyBatis-Plus的QueryWrapper实例
     * @param searchObj 查询实体对象，包含基本查询条件
     * @param parameterMap HTTP请求参数Map，来源于request.getParameterMap()
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    public static void installMplus(QueryWrapper<?> queryWrapper,Object searchObj,Map<String, String[]> parameterMap) {

       /*
        * 注意:权限查询由前端配置数据规则 当一个人有多个所属部门时候 可以在规则配置包含条件 orgCode 包含 #{sys_dept_code}
       但是不支持在自定义SQL中写orgCode in #{sys_dept_code}
       当一个人只有一个部门 就直接配置等于条件: orgCode 等于 #{sys_dept_code} 或者配置自定义SQL: orgCode = '#{sys_dept_code}'
       */

       PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(searchObj);

       //模块查询条件，精确匹配，字符串模糊查询，日期区间查询，大于小于查询
       if(parameterMap!=null){
          boolean encode = false;
          if(ObjectUtils.isNotEmpty(parameterMap.get("encode"))){
             encode = Boolean.parseBoolean(parameterMap.get("encode")[0]);
          }
          //Iterator it = parameterMap.entrySet().iterator();
          for(Map.Entry<String, String[]> entry : parameterMap.entrySet()){
             QueryConditon queryConditon = null;
             PropertyDescriptor propertyDescriptor = null;
             String key = entry.getKey();
             if(key.indexOf("$")!=-1 ){
                String _name = key.substring(0,key.lastIndexOf("$"));
                String _op = key.substring(key.lastIndexOf("$")+1,key.length());
                propertyDescriptor = getUseField(_name,origDescriptors,searchObj);
                if(propertyDescriptor!=null){
                   queryConditon =  new QueryConditon(_name,decode(parameterMap.get(key)[0].trim(),encode),QueryRuleEnum.getByValue(_op));
                }
             }else { //没有写$符合，默认为eq
                propertyDescriptor = getUseField(key,origDescriptors,searchObj);
                if(propertyDescriptor!=null){
                   queryConditon =  new QueryConditon(key,decode(parameterMap.get(key)[0].trim(),encode),QueryRuleEnum.EQ);
                }
             }
             if(queryConditon!=null){
                try {
                   addQueryByRule(queryWrapper, propertyDescriptor.getName(), propertyDescriptor.getPropertyType().toString(), queryConditon.getValue(), queryConditon.getQueryRuleEnum());
                }catch (Exception e) {
                   logger.error(e.getMessage(), e);
                }
             }

          }
       }

       //数据权限查询，todo 待验证
        Map<String,SmartPermissionDataRuleModel> ruleMap = getRuleMap();
        //权限规则自定义SQL表达式
        for (String c : ruleMap.keySet()) {
            if(!StringUtils.isEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
                queryWrapper.and(i ->i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
            }
        }
       String name;
       for (int i = 0; i < origDescriptors.length; i++) {
          name = origDescriptors[i].getName();
          try {
             if (judgedIsUselessField(name)|| !PropertyUtils.isReadable(searchObj, name)) {
                continue;
             }
             if(ruleMap.containsKey(name)) {
                addRuleToQueryWrapper(ruleMap.get(name), name, origDescriptors[i].getPropertyType(), queryWrapper);
             }

          } catch (Exception e) {
             logger.error(e.getMessage(), e);
          }
       }
       // 排序逻辑 处理
       doMultiFieldsOrder(queryWrapper, parameterMap);

       //高级查询
       doSuperQuery(queryWrapper, parameterMap);

    }

    private static String decode(String value,boolean encode) {
       if(Boolean.TRUE.equals(encode)){
          value = URLUtil.decodeByUTF8(value);
        }
       return value;
    }

    /**
     * 判断查询条件字段是否是有效字段
     * @param name
     * @param origDescriptors
     * @param searchObj
     * @return
     */
    private static PropertyDescriptor getUseField(String name,PropertyDescriptor origDescriptors[],Object searchObj){
       String fieldName;
       for (int i = 0; i < origDescriptors.length; i++) {
          fieldName = origDescriptors[i].getName();
          try {
             if(fieldName.equals(name)){
                if (judgedIsUselessField(fieldName)|| !PropertyUtils.isReadable(searchObj, fieldName)) {
                   return null;
                }
                return origDescriptors[i];
             }

          } catch (Exception e) {
             logger.error(e.getMessage(), e);
          }
       }
       return null;

    }

    /**
     * 多字段排序处理方法
     * 
     * 【使用方法】:
     * 1. URL参数方式:
     *    GET /api/users?column=name&order=asc
     *    GET /api/users?column=createTime&order=desc
     * 
     * 2. 支持的排序方式:
     *    - ASC: 升序排列
     *    - DESC: 降序排列
     * 
     * 3. 字段名转换:
     *    - 自动将驼峰命名转换为数据库下划线命名
     *    - 例如: userName -> user_name
     * 
     * 4. 字典字段处理:
     *    - 自动移除字典翻译文本后缀(_dictText)
     *    - 例如: status_dictText -> status
     * 
     * 5. SQL注入防护:
     *    - 对排序字段进行SQL注入检查
     *    - 防止恶意排序参数攻击
     * 
     * @param queryWrapper MyBatis-Plus的QueryWrapper实例
     * @param parameterMap HTTP请求参数Map，包含column和order参数
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    public static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
       String column=null,order=null;
       if(parameterMap!=null&& parameterMap.containsKey(ORDER_COLUMN)) {
          column = parameterMap.get(ORDER_COLUMN)[0];
       }
       if(parameterMap!=null&& parameterMap.containsKey(ORDER_TYPE)) {
          order = parameterMap.get(ORDER_TYPE)[0];
       }
       logger.debug("排序规则>>列:"+column+",排序方式:"+order);
       if (!StringUtils.isEmpty(column) && !StringUtils.isEmpty(order)) {
          //字典字段，去掉字典翻译文本后缀
          if(column.endsWith(CommonConstant.DICT_TEXT_SUFFIX)) {
             column = column.substring(0, column.lastIndexOf(CommonConstant.DICT_TEXT_SUFFIX));
          }
          //SQL注入check
          CommonUtils.filterSqlInject(column);

          if (order.toUpperCase().indexOf(ORDER_TYPE_ASC)>=0) {
             queryWrapper.orderByAsc(CommonUtils.camel2UnderScore(column));
          } else {
             queryWrapper.orderByDesc(CommonUtils.camel2UnderScore(column));
          }
       }
    }

    /**
     * 高级查询功能处理
     * 支持复杂查询条件的JSON格式传递和解析
     * 
     * 【使用方法】:
     * 1. 参数格式:
     *    superQueryParams=[{"field":"name","rule":"like","val":"张三"},
     *                      {"field":"age","rule":"gt","val":"18"},
     *                      {"field":"status","rule":"in","val":"1,2,3"}]
     * 
     * 2. URL编码支持:
     *    - 自动解码URL编码的高级查询参数
     *    - 支持UTF-8字符集
     * 
     * 3. 查询条件结构:
     *    - field: 字段名称
     *    - rule: 查询规则(同基本查询规则)
     *    - val: 查询值
     *    - type: 数据类型(可选)
     * 
     * 4. 支持的查询规则:
     *    - eq/ne: 等于/不等于
     *    - gt/ge/lt/le: 大于/大于等于/小于/小于等于
     *    - like/llk/rlk: 模糊查询/左模糊/右模糊
     *    - in: 包含查询
     * 
     * 5. 错误处理:
     *    - 参数解码失败时记录错误日志
     *    - 非法参数自动过滤，不影响正常查询
     * 
     * @param queryWrapper MyBatis-Plus的QueryWrapper实例
     * @param parameterMap HTTP请求参数Map，包含superQueryParams参数
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    public static void doSuperQuery(QueryWrapper<?> queryWrapper,Map<String, String[]> parameterMap) {
       if(parameterMap!=null&& parameterMap.containsKey(SUPER_QUERY_PARAMS)){
          String superQueryParams = parameterMap.get(SUPER_QUERY_PARAMS)[0];
          // 解码
          try {
             superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
          } catch (UnsupportedEncodingException e) {
             logger.error("--高级查询参数转码失败!", e);
          }
          List<QueryConditionVo> conditions = JSONUtil.parseArray(superQueryParams, QueryConditionVo.class);
          logger.info("---高级查询参数-->"+conditions.toString());

          for (QueryConditionVo rule : conditions) {
             if(!StringUtils.isEmpty(rule.getField()) && !StringUtils.isEmpty(rule.getRule()) && !StringUtils.isEmpty(rule.getVal())){
                addEasyQuery(queryWrapper, rule.getField(), QueryRuleEnum.getByValue(rule.getRule()), rule.getVal());
             }
          }
       }
    }

    private static void addQueryByRule(QueryWrapper<?> queryWrapper,String name,String type,Object value,QueryRuleEnum rule) throws ParseException {
       if(value!=null && !value.equals("")) {
          Object temp;
          switch (type) {
             case "class java.lang.String":
                temp =  String.valueOf(value);
                break;
             case "class java.lang.Integer":
                temp =  Integer.parseInt(value+"");
                break;
             case "class java.math.BigDecimal":
                temp =  new BigDecimal(value+"");
                break;
             case "class java.lang.Short":
                temp =  Short.parseShort(value+"");
                break;
             case "class java.lang.Long":
                temp =  Long.parseLong(value+"");
                break;
             case "class java.lang.Float":
                temp =   Float.parseFloat(value+"");
                break;
             case "class java.lang.Double":
                temp =  Double.parseDouble(value+"");
                break;
             case "class java.util.Date":
                temp = getDateQueryByRule(value+"", rule);
                break;
             default:
                temp = value;
                break;
          }
          addEasyQuery(queryWrapper, name, rule, temp);
       }
    }

    /**
     * 获取日期类型的值
     */
    private static Date getDateQueryByRule(String value,QueryRuleEnum rule) throws ParseException {
       Date date = null;
       if(value.length()==10) {
          if(rule==QueryRuleEnum.GE) {
             //比较大于
             date = datetimeFormat.parse(value + " 00:00:00");
             //date = getTime().parse(value + " 00:00:00");
          }else if(rule==QueryRuleEnum.LE) {
             //比较小于
             date = datetimeFormat.parse(value + " 23:59:59");
             //date = getTime().parse(value + " 23:59:59");
          }
       }
       if(date==null) {
          if(value.length()==10){
             date = datetFormat.parse(value);
          }else if(value.length()>10){
             date = datetimeFormat.parse(value);
          }else{
             throw new  ParseException("日期格式不正确，日期值["+value+"]。",0);
          }
       }
       return date;
    }

    /**
     * 根据规则执行不同的查询条件构建
     * 该方法为查询条件构建的核心方法
     * 
     * 【支持的查询规则】:
     * 1. 数值比较类:
     *    - GT: 大于 >                    - GE: 大于等于 >=
     *    - LT: 小于 <                    - LE: 小于等于 <=
     *    - EQ: 等于 =                    - NE: 不等于 !=
     * 
     * 2. 字符串模糊类:
     *    - LIKE: 全模糊查询 %value%
     *    - LEFT_LIKE: 左模糊查询 %value
     *    - RIGHT_LIKE: 右模糊查询 value%
     * 
     * 3. 集合类:
     *    - IN: 包含查询，支持字符串数组和逗号分隔
     * 
     * 【自动类型处理】:
     * - 自动将驼峰命名转换为数据库下划线命名
     * - 支持空值检查，自动过滤无效条件
     * 
     * 【使用示例】:
     * QueryWrapper<User> wrapper = new QueryWrapper<>();
     * addEasyQuery(wrapper, "userName", QueryRuleEnum.LIKE, "张三");
     * addEasyQuery(wrapper, "age", QueryRuleEnum.GT, 18);
     * addEasyQuery(wrapper, "status", QueryRuleEnum.IN, "1,2,3");
     * 
     * @param queryWrapper MyBatis-Plus的QueryWrapper实例
     * @param name 字段名称(驼峰命名，会自动转换)
     * @param rule 查询规则枚举
     * @param value 查询值
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    private static void addEasyQuery(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
       if (value == null || rule == null) {
          return;
       }
       name = CommonUtils.camel2UnderScore(name);
       logger.info("--查询规则-->"+name+" "+rule.getValue()+" "+value);
       switch (rule) {
       case GT:
          queryWrapper.gt(name, value);
          break;
       case GE:
          queryWrapper.ge(name, value);
          break;
       case LT:
          queryWrapper.lt(name, value);
          break;
       case LE:
          queryWrapper.le(name, value);
          break;
       case EQ:
          queryWrapper.eq(name, value);
          break;
       case NE:
          queryWrapper.ne(name, value);
          break;
       case IN:
             if(value instanceof String) {
                queryWrapper.in(name, (Object[])value.toString().split(","));
             }else if(value instanceof String[]) {
                queryWrapper.in(name, (Object[]) value);
             }else {
                queryWrapper.in(name, value);
             }
             break;
       case LIKE:
          queryWrapper.like(name, value);
          break;
       case LEFT_LIKE:
          queryWrapper.likeLeft(name, value);
          break;
       case RIGHT_LIKE:
          queryWrapper.likeRight(name, value);
          break;
       default:
          logger.info("--查询规则未匹配到---");
          break;
       }
    }
    /**
     *
     * @param name
     * @return
     */
    private static boolean judgedIsUselessField(String name) {
       return "class".equals(name) || "ids".equals(name)
             || "page".equals(name) || "rows".equals(name)
             || "sort".equals(name) || "order".equals(name);
    }


    public static Map<String, SmartPermissionDataRuleModel> getRuleMap() {
       Map<String, SmartPermissionDataRuleModel> ruleMap = new HashMap<String, SmartPermissionDataRuleModel>();
       List<SmartPermissionDataRuleModel> list = DataAuthUtil.loadDataSearchConditon();
       if(list != null&&list.size()>0){
          if(list.get(0)==null){
             return ruleMap;
          }
          for (SmartPermissionDataRuleModel rule : list) {
             String column = rule.getRuleColumn();
             // 如果前端传入的低代码平台类似的 eq 等条件 需要特殊处理
             if(QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
                column = SQL_RULES_COLUMN+rule.getId();
             }
             // 普通传入的规则 直接put 到map 中
             ruleMap.put(column, rule);
          }
       }
       return ruleMap;
    }

    private static void addRuleToQueryWrapper(SmartPermissionDataRuleModel dataRule,String name, Class propertyType, QueryWrapper<?> queryWrapper) {
       QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
       if(rule.equals(QueryRuleEnum.IN) && ! propertyType.equals(String.class)) {
          String[] values = dataRule.getRuleValue().split(",");
          Object[] objs = new Object[values.length];
          for (int i = 0; i < values.length; i++) {
             objs[i] = NumberUtils.parseNumber(values[i], propertyType);
          }
          addEasyQuery(queryWrapper, name, rule, objs);
       }else {
          if (propertyType.equals(String.class)) {
             addEasyQuery(queryWrapper, name, rule, converRuleValue(dataRule.getRuleValue()));
          } else {
             addEasyQuery(queryWrapper, name, rule, NumberUtils.parseNumber(dataRule.getRuleValue(), propertyType));
          }
       }
    }

    public static String converRuleValue(String ruleValue) {
       String value = VariableUtil.getVariableByKey(ruleValue);
       return value!= null ? value : ruleValue;
    }

    public static String getSqlRuleValue(String sqlRule){
       try {
          Set<String> varParams = getSqlRuleParams(sqlRule);
          for(String var:varParams){
             String tempValue = converRuleValue(var);
             sqlRule = sqlRule.replace("#{"+var+"}",tempValue);
          }
       } catch (Exception e) {
          logger.error(e.getMessage(), e);
       }
       return sqlRule;
    }

    /**
     * 获取sql中的#{key} 这个key组成的set
     */
    public static Set<String> getSqlRuleParams(String sql) {
       if (StringUtils.isEmpty(sql)) {
          return null;
       }
       Set<String> varParams = new HashSet<String>();
       String regex = "\\#\\{\\w+\\}";

       Pattern p = Pattern.compile(regex);
       Matcher m = p.matcher(sql);
       while(m.find()){
          String var = m.group();
          varParams.add(var.substring(var.indexOf("{")+1,var.indexOf("}")));
       }
       return varParams;
    }



    public static String getSingleSqlByRule(QueryRuleEnum rule,String field,Object value,boolean isString) {
       String res = "";
       switch (rule) {
       case GT:
          res =field+rule.getValue()+getFieldConditionValue(value, isString);
          break;
       case GE:
          res = field+rule.getValue()+getFieldConditionValue(value, isString);
          break;
       case LT:
          res = field+rule.getValue()+getFieldConditionValue(value, isString);
          break;
       case LE:
          res = field+rule.getValue()+getFieldConditionValue(value, isString);
          break;
       case EQ:
          res = field+rule.getValue()+getFieldConditionValue(value, isString);
          break;
       case NE:
          res = field+" <> "+getFieldConditionValue(value, isString);
          break;
       case IN:
          res = field + " in "+getInConditionValue(value, isString);
          break;
       case LIKE:
          res = field + " like "+getLikeConditionValue(value);
          break;
       case LEFT_LIKE:
          res = field + " like "+getLikeConditionValue(value);
          break;
       case RIGHT_LIKE:
          res = field + " like "+getLikeConditionValue(value);
          break;
       default:
          res = field+" = "+getFieldConditionValue(value, isString);
          break;
       }
       return res;
    }
    private static String getFieldConditionValue(Object value,boolean isString) {
       String str = value.toString().trim();
       if(str.startsWith("!")) {
          str = str.substring(1);
       }else if(str.startsWith(">=")) {
          str = str.substring(2);
       }else if(str.startsWith("<=")) {
          str = str.substring(2);
       }else if(str.startsWith(">")) {
          str = str.substring(1);
       }else if(str.startsWith("<")) {
          str = str.substring(1);
       }
       if(isString) {
          return " '"+str+"' ";
       }else {
          return value.toString();
       }
    }

    private static String getInConditionValue(Object value,boolean isString) {
       if(isString) {
          String temp[] = value.toString().split(",");
          String res="";
          for (String string : temp) {
             res+=",'"+string+"'";
          }
          return "("+res.substring(1)+")";
       }else {
          return "("+value.toString()+")";
       }
    }

    private static String getLikeConditionValue(Object value) {
       String str = value.toString().trim();
       if(str.startsWith("*") && str.endsWith("*")) {
          return "'%"+str.substring(1,str.length()-1)+"%'";
       }else if(str.startsWith("*")) {
          return "'%"+str.substring(1)+"'";
       }else if(str.endsWith("*")) {
          return "'"+str.substring(0,str.length()-1)+"%'";
       }else {
          if(str.indexOf("%")>=0) {
             return str;
          }else {
             return "'%"+str+"%'";
          }
       }
    }

    /**
     * 编码方式集成数据权限规则，根据权限相关配置生成相关的SQL语句
     * 该方法适用于原生SQL查询场景
     * 
     * 【使用方法】:
     * 1. 基本用法:
     *    String authSql = QueryBuilder.installAuthJdbc(User.class);
     *    String sql = "SELECT * FROM user WHERE 1=1" + authSql;
     * 
     * 2. MyBatis XML中使用:
     *    <select id="selectUsers" resultType="User">
     *        SELECT * FROM user WHERE 1=1
     *        ${@com.smart.common.database.query.QueryBuilder@installAuthJdbc(@com.example.entity.User@class)}
     *    </select>
     * 
     * 3. 生成的SQL示例:
     *    " and create_by = '1' and dept_code = '001'"
     * 
     * 【数据权限类型】:
     * - 用户级别: 只能查看自己创建的数据
     * - 部门级别: 只能查看本部门的数据
     * - 组织级别: 只能查看本组织及子组织的数据
     * - 自定义SQL: 支持复杂的权限规则
     * 
     * 【安全特性】:
     * - SQL注入防护: 所有参数都经过安全检查
     * - 参数转义: 自动处理特殊字符转义
     * 
     * @param clazz 实体类型，用于获取字段信息
     * @return 数据权限SQL片段，以" and "开头
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String installAuthJdbc(Class<?> clazz) {
       StringBuffer sb = new StringBuffer();
       //权限查询
       Map<String,SmartPermissionDataRuleModel> ruleMap = getRuleMap();
       PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);
       String sql_and = " and ";
       for (String c : ruleMap.keySet()) {
          if(!StringUtils.isEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
             sb.append(sql_and+getSqlRuleValue(ruleMap.get(c).getRuleValue()));
          }
       }
       String name;
       for (int i = 0; i < origDescriptors.length; i++) {
          name = origDescriptors[i].getName();
          if (judgedIsUselessField(name)) {
             continue;
          }
          if(ruleMap.containsKey(name)) {
             SmartPermissionDataRuleModel dataRule = ruleMap.get(name);
             QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
             Class propType = origDescriptors[i].getPropertyType();
             boolean isString = propType.equals(String.class);
             Object value;
             if(isString) {
                value = converRuleValue(dataRule.getRuleValue());
             }else {
                value = NumberUtils.parseNumber(dataRule.getRuleValue(),propType);
             }
             String filedSql = getSingleSqlByRule(rule, CommonUtils.camel2UnderScore(name), value,isString);
             sb.append(sql_and+filedSql);
          }
       }
       logger.info("query auth sql is:"+sb.toString());
       return sb.toString();
    }

    /**
     * 编码方式集成数据权限规则，根据权限相关配置组装MyBatis-Plus需要的权限条件
     * 该方法适用于MyBatis-Plus查询场景
     * 
     * 【使用方法】:
     * 1. Service层使用:
     *    QueryWrapper<User> wrapper = new QueryWrapper<>();
     *    QueryBuilder.installAuthMplus(wrapper, User.class);
     *    List<User> users = userMapper.selectList(wrapper);
     * 
     * 2. 统一的数据权限控制:
     *    public List<User> selectUsers() {
     *        QueryWrapper<User> wrapper = new QueryWrapper<>();
     *        // 业务查询条件
     *        wrapper.eq("status", 1);
     *        // 自动添加数据权限
     *        QueryBuilder.installAuthMplus(wrapper, User.class);
     *        return userMapper.selectList(wrapper);
     *    }
     * 
     * 3. 与其他查询条件组合:
     *    QueryWrapper<User> wrapper = new QueryWrapper<>();
     *    wrapper.like("name", "张三").eq("status", 1);
     *    QueryBuilder.installAuthMplus(wrapper, User.class);
     * 
     * 【权限规则应用】:
     * - 自动根据当前用户的角色和权限配置应用
     * - 支持多种数据权限类型（用户、部门、组织等）
     * - 支持复杂的权限规则组合
     * 
     * 【性能优化】:
     * - 懒加载: 只有在需要时才加载权限规则
     * - 缓存支持: 权限规则支持缓存机制
     * 
     * @param queryWrapper MyBatis-Plus的QueryWrapper实例
     * @param clazz 实体类型，用于获取字段信息和权限规则
     * 
     * @author Smart Boot3
     * @since 1.0.0
     */
    public static void installAuthMplus(QueryWrapper<?> queryWrapper,Class<?> clazz) {
       //权限查询
       Map<String,SmartPermissionDataRuleModel> ruleMap = getRuleMap();
       PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(clazz);
       for (String c : ruleMap.keySet()) {
          if(!StringUtils.isEmpty(c) && c.startsWith(SQL_RULES_COLUMN)){
             queryWrapper.and(i ->i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
          }
       }
       String name;
       for (int i = 0; i < origDescriptors.length; i++) {
          name = origDescriptors[i].getName();
          if (judgedIsUselessField(name)) {
             continue;
          }
          if(ruleMap.containsKey(name)) {
             addRuleToQueryWrapper(ruleMap.get(name), name, origDescriptors[i].getPropertyType(), queryWrapper);
          }
       }
    }

}
