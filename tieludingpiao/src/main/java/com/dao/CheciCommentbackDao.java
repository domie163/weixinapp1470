package com.dao;

import com.entity.CheciCommentbackEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.view.CheciCommentbackView;

/**
 * 车次评价 Dao 接口
 *
 * @author 
 */
public interface CheciCommentbackDao extends BaseMapper<CheciCommentbackEntity> {

   List<CheciCommentbackView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
