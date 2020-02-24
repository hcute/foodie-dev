package com.imooc.controller.center;


import com.imooc.controller.BasicController;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.service.center.MyCommentService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "用户中心评价模块",tags = {"用户中心评价模块相关的api"})
@RestController
@RequestMapping("/mycomments")
public class MyCommentController extends BasicController {

    @Autowired
    private MyCommentService myCommentService;


    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @PostMapping("/pending")
    public IMOOCJSONResult pending(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam  String userId,
            @ApiParam(name = "orderId",value = "用户id",required = true)
            @RequestParam String orderId) {


        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (!imoocjsonResult.getStatus().equals(HttpStatus.OK.value())){
            return imoocjsonResult;
        }

        Orders myOrder = (Orders) imoocjsonResult.getData();
        if (myOrder.getIsComment()== YesOrNo.YES.type){
            return IMOOCJSONResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> list = myCommentService.queryPendingComment(orderId);

        return IMOOCJSONResult.ok(list);
    }




    @ApiOperation(value = "保存评论列表",notes = "保存评论列表",httpMethod = "POST")
    @PostMapping("/saveList")
    public IMOOCJSONResult saveList(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam  String userId,
            @ApiParam(name = "orderId",value = "用户id",required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {


        System.out.println(commentList);
        IMOOCJSONResult imoocjsonResult = checkUserOrder(userId, orderId);
        if (!imoocjsonResult.getStatus().equals(HttpStatus.OK.value())){
            return imoocjsonResult;
        }

        // 判断评论内容不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }

        myCommentService.saveComments(userId,orderId,commentList);

        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "查询用户评价",notes = "查询用户评价",httpMethod = "POST")
    @PostMapping("/query")
    public IMOOCJSONResult query(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam String userId,
            @ApiParam(name = "page",value = "查询下一页的第几页",required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize",value = "每页显示的条数",required = false)
            @RequestParam Integer pageSize
    ){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult pagedGridResult = myCommentService.queryMyComments(userId, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
    }

}
