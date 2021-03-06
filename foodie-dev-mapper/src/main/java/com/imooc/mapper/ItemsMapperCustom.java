package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentsVO;
import com.imooc.pojo.vo.SearchItemsVO;
import com.imooc.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemCommentsVO> queryItemComments(@Param("paramsMap") Map<String,Object> paramsMap);

    List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String,Object> paramsMap);

    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> paramsMap);

    List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdList);

    Integer decreaseItemSpecStock(@Param("specId") String specId,@Param("pendingCounts") Integer pendingCounts);

}