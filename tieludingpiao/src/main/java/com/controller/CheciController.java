
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
 * 车次信息
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/checi")
public class CheciController {
    private static final Logger logger = LoggerFactory.getLogger(CheciController.class);

    private static final String TABLE_NAME = "checi";

    @Autowired
    private CheciService checiService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private CheciCollectionService checiCollectionService;//车次收藏
    @Autowired
    private CheciCommentbackService checiCommentbackService;//车次评价
    @Autowired
    private CheciOrderService checiOrderService;//购票订单
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
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("用户".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        params.put("checiDeleteStart",1);params.put("checiDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = checiService.queryPage(params);

        //字典表数据转换
        List<CheciView> list =(List<CheciView>)page.getList();
        for(CheciView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        CheciEntity checi = checiService.selectById(id);
        if(checi !=null){
            //entity转view
            CheciView view = new CheciView();
            BeanUtils.copyProperties( checi , view );//把实体数据重构到view中
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CheciEntity checi, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,checi:{}",this.getClass().getName(),checi.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<CheciEntity> queryWrapper = new EntityWrapper<CheciEntity>()
            .eq("checi_name", checi.getCheciName())
            .eq("checi_types", checi.getCheciTypes())
            .eq("checi_chufadi", checi.getCheciChufadi())
            .eq("checi_mudidi", checi.getCheciMudidi())
            .eq("section_number", checi.getSectionNumber())
            .eq("zuowei_number", checi.getZuoweiNumber())
            .eq("shangxia_types", checi.getShangxiaTypes())
            .eq("checi_delete", 1)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        CheciEntity checiEntity = checiService.selectOne(queryWrapper);
        if(checiEntity==null){
            checi.setShangxiaTypes(1);
            checi.setCheciDelete(1);
            checi.setCreateTime(new Date());
            checiService.insert(checi);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody CheciEntity checi, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,checi:{}",this.getClass().getName(),checi.toString());
        CheciEntity oldCheciEntity = checiService.selectById(checi.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));
//        if(false)
//            return R.error(511,"永远不会进入");
        if("".equals(checi.getCheciPhoto()) || "null".equals(checi.getCheciPhoto())){
                checi.setCheciPhoto(null);
        }

            checiService.updateById(checi);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<CheciEntity> oldCheciList =checiService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<CheciEntity> list = new ArrayList<>();
        for(Integer id:ids){
            CheciEntity checiEntity = new CheciEntity();
            checiEntity.setId(id);
            checiEntity.setCheciDelete(2);
            list.add(checiEntity);
        }
        if(list != null && list.size() >0){
            checiService.updateBatchById(list);
        }

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<CheciEntity> checiList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            CheciEntity checiEntity = new CheciEntity();
//                            checiEntity.setCheciName(data.get(0));                    //车次标题 要改的
//                            checiEntity.setCheciPhoto("");//详情和图片
//                            checiEntity.setCheciTypes(Integer.valueOf(data.get(0)));   //火车类型 要改的
//                            checiEntity.setCheciNewMoney(data.get(0));                    //现价 要改的
//                            checiEntity.setCheciChufadi(data.get(0));                    //出发地 要改的
//                            checiEntity.setCheciMudidi(data.get(0));                    //目的地 要改的
//                            checiEntity.setCheciTime(sdf.parse(data.get(0)));          //出发时间 要改的
//                            checiEntity.setSectionNumber(Integer.valueOf(data.get(0)));   //车厢 要改的
//                            checiEntity.setZuoweiNumber(Integer.valueOf(data.get(0)));   //座位 要改的
//                            checiEntity.setShangxiaTypes(Integer.valueOf(data.get(0)));   //是否上架 要改的
//                            checiEntity.setCheciDelete(1);//逻辑删除字段
//                            checiEntity.setCheciContent("");//详情和图片
//                            checiEntity.setCreateTime(date);//时间
                            checiList.add(checiEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        checiService.insertBatch(checiList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }



    /**
    * 个性推荐
    */
    @IgnoreAuth
    @RequestMapping("/gexingtuijian")
    public R gexingtuijian(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("gexingtuijian方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        CommonUtil.checkMap(params);
        List<CheciView> returnCheciViewList = new ArrayList<>();

        //查询订单
        Map<String, Object> params1 = new HashMap<>(params);params1.put("sort","id");params1.put("yonghuId",request.getSession().getAttribute("userId"));
        PageUtils pageUtils = checiOrderService.queryPage(params1);
        List<CheciOrderView> orderViewsList =(List<CheciOrderView>)pageUtils.getList();
        Map<Integer,Integer> typeMap=new HashMap<>();//购买的类型list
        for(CheciOrderView orderView:orderViewsList){
            Integer checiTypes = orderView.getCheciTypes();
            if(typeMap.containsKey(checiTypes)){
                typeMap.put(checiTypes,typeMap.get(checiTypes)+1);
            }else{
                typeMap.put(checiTypes,1);
            }
        }
        List<Integer> typeList = new ArrayList<>();//排序后的有序的类型 按最多到最少
        typeMap.entrySet().stream().sorted((o1, o2) -> o2.getValue() - o1.getValue()).forEach(e -> typeList.add(e.getKey()));//排序
        Integer limit = Integer.valueOf(String.valueOf(params.get("limit")));
        for(Integer type:typeList){
            Map<String, Object> params2 = new HashMap<>(params);params2.put("checiTypes",type);
            PageUtils pageUtils1 = checiService.queryPage(params2);
            List<CheciView> checiViewList =(List<CheciView>)pageUtils1.getList();
            returnCheciViewList.addAll(checiViewList);
            if(returnCheciViewList.size()>= limit) break;//返回的推荐数量大于要的数量 跳出循环
        }
        //正常查询出来商品,用于补全推荐缺少的数据
        PageUtils page = checiService.queryPage(params);
        if(returnCheciViewList.size()<limit){//返回数量还是小于要求数量
            int toAddNum = limit - returnCheciViewList.size();//要添加的数量
            List<CheciView> checiViewList =(List<CheciView>)page.getList();
            for(CheciView checiView:checiViewList){
                Boolean addFlag = true;
                for(CheciView returnCheciView:returnCheciViewList){
                    if(returnCheciView.getId().intValue() ==checiView.getId().intValue()) addFlag=false;//返回的数据中已存在此商品
                }
                if(addFlag){
                    toAddNum=toAddNum-1;
                    returnCheciViewList.add(checiView);
                    if(toAddNum==0) break;//够数量了
                }
            }
        }else {
            returnCheciViewList = returnCheciViewList.subList(0, limit);
        }

        for(CheciView c:returnCheciViewList)
            dictionaryService.dictionaryConvert(c, request);
        page.setList(returnCheciViewList);
        return R.ok().put("data", page);
    }

    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = checiService.queryPage(params);

        //字典表数据转换
        List<CheciView> list =(List<CheciView>)page.getList();
        for(CheciView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        CheciEntity checi = checiService.selectById(id);
            if(checi !=null){


                //entity转view
                CheciView view = new CheciView();
                BeanUtils.copyProperties( checi , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody CheciEntity checi, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,checi:{}",this.getClass().getName(),checi.toString());
        Wrapper<CheciEntity> queryWrapper = new EntityWrapper<CheciEntity>()
            .eq("checi_name", checi.getCheciName())
            .eq("checi_types", checi.getCheciTypes())
            .eq("checi_chufadi", checi.getCheciChufadi())
            .eq("checi_mudidi", checi.getCheciMudidi())
            .eq("section_number", checi.getSectionNumber())
            .eq("zuowei_number", checi.getZuoweiNumber())
            .eq("shangxia_types", checi.getShangxiaTypes())
            .eq("checi_delete", checi.getCheciDelete())
//            .notIn("checi_types", new Integer[]{102})
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        CheciEntity checiEntity = checiService.selectOne(queryWrapper);
        if(checiEntity==null){
            checi.setCheciDelete(1);
            checi.setCreateTime(new Date());
        checiService.insert(checi);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

