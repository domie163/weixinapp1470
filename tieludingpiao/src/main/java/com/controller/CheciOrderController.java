
package com.controller;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;

import com.service.TokenService;
import com.utils.*;

import java.lang.reflect.InvocationTargetException;

import com.service.DictionaryService;
import org.apache.commons.lang3.StringUtils;
import com.annotation.IgnoreAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.*;
import com.service.*;
import com.utils.PageUtils;
import com.utils.R;
import com.alibaba.fastjson.*;

/**
 * 购票订单
 * 后端接口
 *
 * @author
 * @email
 */
@RestController
@Controller
@RequestMapping("/checiOrder")
public class CheciOrderController {
    private static final Logger logger = LoggerFactory.getLogger(CheciOrderController.class);

    private static final String TABLE_NAME = "checiOrder";

    @Autowired
    private CheciOrderService checiOrderService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private CheciService checiService;//车次信息
    @Autowired
    private CheciCollectionService checiCollectionService;//车次收藏
    @Autowired
    private CheciCommentbackService checiCommentbackService;//车次评价
    @Autowired
    private DictionaryService dictionaryService;//字典表
    @Autowired
    private ForumService forumService;//论坛
    @Autowired
    private NewsService newsService;//通知公告
    @Autowired
    private YonghuService yonghuService;//用户
    @Autowired
    private UsersService usersService;//管理员


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if (false)
            return R.error(511, "永不会进入");
        else if ("用户".equals(role))
            params.put("yonghuId", request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = checiOrderService.queryPage(params);

        //字典表数据转换
        List<CheciOrderView> list = (List<CheciOrderView>) page.getList();
        for (CheciOrderView c : list) {
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        CheciOrderEntity checiOrder = checiOrderService.selectById(id);
        if (checiOrder != null) {
            //entity转view
            CheciOrderView view = new CheciOrderView();
            BeanUtils.copyProperties(checiOrder, view);//把实体数据重构到view中
            //级联表 车次信息
            //级联表
            CheciEntity checi = checiService.selectById(checiOrder.getCheciId());
            if (checi != null) {
                BeanUtils.copyProperties(checi, view, new String[]{"id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
                view.setCheciId(checi.getId());
            }
            //级联表 用户
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(checiOrder.getYonghuId());
            if (yonghu != null) {
                BeanUtils.copyProperties(yonghu, view, new String[]{"id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
                view.setYonghuId(yonghu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        } else {
            return R.error(511, "查不到数据");
        }

    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CheciOrderEntity checiOrder, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,checiOrder:{}", this.getClass().getName(), checiOrder.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if (false)
            return R.error(511, "永远不会进入");
        else if ("用户".equals(role))
            checiOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        checiOrder.setCreateTime(new Date());
        checiOrder.setInsertTime(new Date());
        checiOrderService.insert(checiOrder);

        return R.ok();
    }

    /**
     * 后端修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CheciOrderEntity checiOrder, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,checiOrder:{}", this.getClass().getName(), checiOrder.toString());
        CheciOrderEntity oldCheciOrderEntity = checiOrderService.selectById(checiOrder.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
//        else if("用户".equals(role))
//            checiOrder.setYonghuId(Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        checiOrderService.updateById(checiOrder);//根据id更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), ids.toString());
        List<CheciOrderEntity> oldCheciOrderList = checiOrderService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        checiOrderService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save(String fileName, HttpServletRequest request) {
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}", this.getClass().getName(), fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<CheciOrderEntity> checiOrderList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields = new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf == -1) {
                return R.error(511, "该文件没有后缀");
            } else {
                String suffix = fileName.substring(lastIndexOf);
                if (!".xls".equals(suffix)) {
                    return R.error(511, "只支持后缀为xls的excel文件");
                } else {
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if (!file.exists()) {
                        return R.error(511, "找不到上传文件，请联系管理员");
                    } else {
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for (List<String> data : dataList) {
                            //循环
                            CheciOrderEntity checiOrderEntity = new CheciOrderEntity();
//                            checiOrderEntity.setCheciOrderUuidNumber(data.get(0));                    //订单号 要改的
//                            checiOrderEntity.setCheciId(Integer.valueOf(data.get(0)));   //车次 要改的
//                            checiOrderEntity.setYonghuId(Integer.valueOf(data.get(0)));   //用户 要改的
//                            checiOrderEntity.setCheciOrderTruePrice(data.get(0));                    //实付价格 要改的
//                            checiOrderEntity.setCheciOrderTypes(Integer.valueOf(data.get(0)));   //订单类型 要改的
//                            checiOrderEntity.setBuySectionNumber(Integer.valueOf(data.get(0)));   //车厢 要改的
//                            checiOrderEntity.setBuyZuoweiNumber(data.get(0));                    //购买的座位 要改的
//                            checiOrderEntity.setBuyZuoweiTime(sdf.parse(data.get(0)));          //订购日期 要改的
//                            checiOrderEntity.setInsertTime(date);//时间
//                            checiOrderEntity.setCreateTime(date);//时间
                            checiOrderList.add(checiOrderEntity);


                            //把要查询是否重复的字段放入map中
                            //订单号
                            if (seachFields.containsKey("checiOrderUuidNumber")) {
                                List<String> checiOrderUuidNumber = seachFields.get("checiOrderUuidNumber");
                                checiOrderUuidNumber.add(data.get(0));//要改的
                            } else {
                                List<String> checiOrderUuidNumber = new ArrayList<>();
                                checiOrderUuidNumber.add(data.get(0));//要改的
                                seachFields.put("checiOrderUuidNumber", checiOrderUuidNumber);
                            }
                        }

                        //查询是否重复
                        //订单号
                        List<CheciOrderEntity> checiOrderEntities_checiOrderUuidNumber = checiOrderService.selectList(new EntityWrapper<CheciOrderEntity>().in("checi_order_uuid_number", seachFields.get("checiOrderUuidNumber")));
                        if (checiOrderEntities_checiOrderUuidNumber.size() > 0) {
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for (CheciOrderEntity s : checiOrderEntities_checiOrderUuidNumber) {
                                repeatFields.add(s.getCheciOrderUuidNumber());
                            }
                            return R.error(511, "数据库的该表中的 [订单号] 字段已经存在 存在数据为:" + repeatFields.toString());
                        }
                        checiOrderService.insertBatch(checiOrderList);
                        return R.ok();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(511, "批量插入数据异常，请联系管理员");
        }
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("list方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = checiOrderService.queryPage(params);

        //字典表数据转换
        List<CheciOrderView> list = (List<CheciOrderView>) page.getList();
        for (CheciOrderView c : list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("detail方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        CheciOrderEntity checiOrder = checiOrderService.selectById(id);
        if (checiOrder != null) {


            //entity转view
            CheciOrderView view = new CheciOrderView();
            BeanUtils.copyProperties(checiOrder, view);//把实体数据重构到view中

            //级联表
            CheciEntity checi = checiService.selectById(checiOrder.getCheciId());
            if (checi != null) {
                BeanUtils.copyProperties(checi, view, new String[]{"id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setCheciId(checi.getId());
            }
            //级联表
            YonghuEntity yonghu = yonghuService.selectById(checiOrder.getYonghuId());
            if (yonghu != null) {
                BeanUtils.copyProperties(yonghu, view, new String[]{"id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setYonghuId(yonghu.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        } else {
            return R.error(511, "查不到数据");
        }
    }


    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CheciOrderEntity checiOrder, HttpServletRequest request) {
        logger.debug("add方法:,,Controller:{},,checiOrder:{}", this.getClass().getName(), checiOrder.toString());
        CheciEntity checiEntity = checiService.selectById(checiOrder.getCheciId());
        if (checiEntity == null) {
            return R.error(511, "查不到该车次信息");
        }
        // Double checiNewMoney = checiEntity.getCheciNewMoney();

        if (false) {
        } else if (checiEntity.getCheciNewMoney() == null) {
            return R.error(511, "现价不能为空");
        }
        List<String> buyZuoweiNumberList = new ArrayList<>(Arrays.asList(checiOrder.getBuyZuoweiNumber().split(",")));//这次购买的座位
        List<String> beforeBuyZuoweiNumberList = new ArrayList<>();//之前已经购买的座位

        //某天日期的某个分段
        List<CheciOrderEntity> checiOrderEntityList =
                checiOrderService.selectList(new EntityWrapper<CheciOrderEntity>()
                        .eq("checi_id", checiOrder.getCheciId())
                        .eq("buy_zuowei_time", checiOrder.getBuyZuoweiTime())
                        .eq("buy_section_number", checiOrder.getBuySectionNumber())
                        .notIn("checi_order_types", 102)//退款的订单

                );
        for (CheciOrderEntity d : checiOrderEntityList) {
            beforeBuyZuoweiNumberList.addAll(Arrays.asList(d.getBuyZuoweiNumber().split(",")));
        }
        buyZuoweiNumberList.retainAll(beforeBuyZuoweiNumberList);//判断当前购买list包含已经被购买的list中是否有重复的  有的话 就保留
        if (buyZuoweiNumberList != null && buyZuoweiNumberList.size() > 0) {
            return R.error(511, buyZuoweiNumberList.toString() + " 的座位已经被使用");
        }

        //计算所获得积分
        Double buyJifen = 0.0;
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);
        if (yonghuEntity == null)
            return R.error(511, "用户不能为空");
        if (yonghuEntity.getNewMoney() == null)
            return R.error(511, "用户金额不能为空");
        double balance = yonghuEntity.getNewMoney() - checiEntity.getCheciNewMoney() * (checiOrder.getBuyZuoweiNumber().split(",").length);//余额
        if (balance < 0)
            return R.error(511, "余额不够支付");
        checiOrder.setCheciOrderTypes(101); //设置订单状态为已支付
        checiOrder.setCheciOrderTruePrice(checiEntity.getCheciNewMoney() * (checiOrder.getBuyZuoweiNumber().split(",").length)); //设置实付价格
        checiOrder.setYonghuId(userId); //设置订单支付人id
        checiOrder.setCheciOrderUuidNumber(String.valueOf(new Date().getTime()));
        checiOrder.setInsertTime(new Date());
        checiOrder.setCreateTime(new Date());
        checiOrderService.insert(checiOrder);//新增订单
        //更新第一注册表
        yonghuEntity.setNewMoney(balance);//设置金额
        yonghuService.updateById(yonghuEntity);


        return R.ok();
    }

    /**
     * 添加订单
     */
    @RequestMapping("/order")
    public R add(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("order方法:,,Controller:{},,params:{}", this.getClass().getName(), params.toString());
        String checiOrderUuidNumber = String.valueOf(new Date().getTime());

        //获取当前登录用户的id
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        Integer buySectionNumber = Integer.valueOf(String.valueOf(params.get("buySectionNumber")));//车厢
        String buyZuoweiNumber = String.valueOf(params.get("buyZuoweiNumber"));//购买的座位

        String data = String.valueOf(params.get("checis"));
        JSONArray jsonArray = JSON.parseArray(data);
        List<Map> checis = JSON.parseObject(jsonArray.toString(), List.class);

        //获取当前登录用户的个人信息
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);

        //当前订单表
        List<CheciOrderEntity> checiOrderList = new ArrayList<>();
        //商品表
        List<CheciEntity> checiList = new ArrayList<>();

        BigDecimal zhekou = new BigDecimal(1.0);

        //循环取出需要的数据
        for (Map<String, Object> map : checis) {
            //取值
            Integer checiId = Integer.valueOf(String.valueOf(map.get("checiId")));//商品id
            Integer buyNumber = Integer.valueOf(String.valueOf(map.get("buyNumber")));//购买数量
            CheciEntity checiEntity = checiService.selectById(checiId);//购买的商品
            String id = String.valueOf(map.get("id"));


            //订单信息表增加数据
            CheciOrderEntity checiOrderEntity = new CheciOrderEntity<>();

            //赋值订单信息
            checiOrderEntity.setCheciOrderUuidNumber(checiOrderUuidNumber);//订单号
            checiOrderEntity.setCheciId(checiId);//车次
            checiOrderEntity.setYonghuId(userId);//用户
            checiOrderEntity.setCheciOrderTypes(101);//订单类型
            checiOrderEntity.setBuySectionNumber(buySectionNumber);//车厢 ？？？？？？
            checiOrderEntity.setBuyZuoweiNumber(buyZuoweiNumber);//购买的座位 ？？？？？？
            checiOrderEntity.setInsertTime(new Date());//订单创建时间
            checiOrderEntity.setCreateTime(new Date());//创建时间

            //判断是什么支付方式 1代表余额 2代表积分
            //计算金额
            Double money = new BigDecimal(checiEntity.getCheciNewMoney()).multiply(new BigDecimal(buyNumber)).multiply(zhekou).doubleValue();

            if (yonghuEntity.getNewMoney() - money < 0) {
                return R.error("余额不足,请充值！！！");
            } else {
                //计算所获得积分
                Double buyJifen = 0.0;
                yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() - money); //设置金额


                checiOrderEntity.setCheciOrderTruePrice(money);

            }
            checiOrderList.add(checiOrderEntity);
            checiList.add(checiEntity);

        }
        checiOrderService.insertBatch(checiOrderList);
        checiService.updateBatchById(checiList);
        yonghuService.updateById(yonghuEntity);

        return R.ok();
    }


    /**
     * 退款
     */
    @RequestMapping("/refund")
    public R refund(Integer id, HttpServletRequest request) {
        logger.debug("refund方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        String role = String.valueOf(request.getSession().getAttribute("role"));

        CheciOrderEntity checiOrder = checiOrderService.selectById(id);//当前表service
        Integer buyNumber = checiOrder.getBuyZuoweiNumber().split(",").length;
        Integer checiId = checiOrder.getCheciId();
        if (checiId == null)
            return R.error(511, "查不到该车次信息");
        CheciEntity checiEntity = checiService.selectById(checiId);
        if (checiEntity == null)
            return R.error(511, "查不到该车次信息");
        Double checiNewMoney = checiEntity.getCheciNewMoney();
        if (checiNewMoney == null)
            return R.error(511, "车次信息价格不能为空");

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        YonghuEntity yonghuEntity = yonghuService.selectById(userId);
        if (yonghuEntity == null)
            return R.error(511, "用户不能为空");
        if (yonghuEntity.getNewMoney() == null)
            return R.error(511, "用户金额不能为空");
        Double zhekou = 1.0;

        //计算金额
        Double money = checiEntity.getCheciNewMoney() * buyNumber * zhekou;
        //计算所获得积分
        Double buyJifen = 0.0;
        yonghuEntity.setNewMoney(yonghuEntity.getNewMoney() + money); //设置金额


        checiOrder.setCheciOrderTypes(102);//设置订单状态为退款
        checiOrderService.updateAllColumnById(checiOrder);//根据id更新
        yonghuService.updateById(yonghuEntity);//更新用户信息
        checiService.updateById(checiEntity);//更新订单中车次信息的信息

        return R.ok();
    }

    /**
     * 评论
     */
    @RequestMapping("/commentback")
    public R commentback(Integer id, String commentbackText, Integer checiCommentbackPingfenNumber, HttpServletRequest request) {
        logger.debug("commentback方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        CheciOrderEntity checiOrder = checiOrderService.selectById(id);
        if (checiOrder == null)
            return R.error(511, "查不到该订单");
        Integer checiId = checiOrder.getCheciId();
        if (checiId == null)
            return R.error(511, "查不到该车次信息");

        CheciCommentbackEntity checiCommentbackEntity = new CheciCommentbackEntity();
        checiCommentbackEntity.setId(id);
        checiCommentbackEntity.setCheciId(checiId);
        checiCommentbackEntity.setYonghuId((Integer) request.getSession().getAttribute("userId"));
        checiCommentbackEntity.setCheciCommentbackText(commentbackText);
        checiCommentbackEntity.setInsertTime(new Date());
        checiCommentbackEntity.setReplyText(null);
        checiCommentbackEntity.setUpdateTime(null);
        checiCommentbackEntity.setCreateTime(new Date());
        checiCommentbackService.insert(checiCommentbackEntity);

        checiOrder.setCheciOrderTypes(105);//设置订单状态为已评论
        checiOrderService.updateById(checiOrder);//根据id更新
        return R.ok();
    }

    /**
     * 可取票
     */
    @RequestMapping("/deliver")
    public R deliver(Integer id, HttpServletRequest request) {
        logger.debug("refund:,,Controller:{},,ids:{}", this.getClass().getName(), id.toString());
        CheciOrderEntity checiOrderEntity = checiOrderService.selectById(id);
        checiOrderEntity.setCheciOrderTypes(103);//设置订单状态为可取票
        checiOrderService.updateById(checiOrderEntity);

        return R.ok();
    }


    /**
     * 取票
     */
    @RequestMapping("/receiving")
    public R receiving(Integer id, HttpServletRequest request) {
        logger.debug("refund:,,Controller:{},,ids:{}", this.getClass().getName(), id.toString());
        CheciOrderEntity checiOrderEntity = checiOrderService.selectById(id);
        checiOrderEntity.setCheciOrderTypes(104);//设置订单状态为取票
        checiOrderService.updateById(checiOrderEntity);
        return R.ok();
    }

}

