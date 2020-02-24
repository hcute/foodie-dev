package com.imooc.controller.center;

import com.imooc.controller.BasicController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "用户信息接口",tags = {"用户信息相关接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserController extends BasicController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "修改用户信息",notes = "修改用户信息",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (result.hasErrors()) {
            Map<String, String> errorsMsg = getErrorsMsg(result);
            return IMOOCJSONResult.errorMap(errorsMsg);
        }
        Users users = centerUserService.updateUserInfo(userId,centerUserBO);
        setNullProperty(users);

        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);

        // TODO 增加令牌token，会整合redis，分布式会话

        return IMOOCJSONResult.ok(users);
    }

    @ApiOperation(value = "用户上传图像",notes = "用户上传图像",httpMethod = "POST")
    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(
            @ApiParam(name = "userId",value = "用户id",required = true)
            String userId,
            @ApiParam(name = "file",value = "用户图像",required = true)
            MultipartFile file,
            HttpServletRequest request,HttpServletResponse response){

        // 图像的保存地址

//        String fileSpace = IMAGE_USER_FACE_LOCATION;
        String fileSpace = fileUpload.getImageUserFaceLocation();

        // 为每个用户创建自己的文件夹
        String uploadPathPrefix = File.separator + userId;

        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                String originalFilename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(originalFilename)){
                    // face-{userId}.png

                    // 文件重命名，比如上传文件名为imooc-face.png -> [imooc,png]
                    String[] fileNameArr = originalFilename.split("\\.");
                    // 获取文件的后缀名
                    String suffix = fileNameArr[fileNameArr.length-1];
                    // 判断图片格式
                    if (!(suffix.equalsIgnoreCase("png") || suffix.equalsIgnoreCase("jpg")
                            || suffix.equalsIgnoreCase("jpeg"))){
                        return IMOOCJSONResult.errorMsg("图片格式不正确");
                    }
                    // 文件名重组 覆盖式上传，如果需要增量的话可以带上当前的时间戳
                    String newFileName = "face-" +userId + "." + suffix;
                    // 最终上传的图像保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator +  newFileName;

                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);

                    if (outFile.getParentFile() !=null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 进行文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream !=null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            return IMOOCJSONResult.errorMsg("文件不能为空！");
        }

        // 更新用户图像到数据库
        String imageServerUrl = fileUpload.getImageServerUrl();
        // 前端浏览器存在缓存，加上时间戳可以解决
        String finalUserFaceUrl =
                imageServerUrl + uploadPathPrefix + "?t="+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN);
        Users users = centerUserService.updateUserFaces(userId, finalUserFaceUrl);
        setNullProperty(users);

        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);

        // TODO 增加令牌token，会整合redis，分布式会话
        return IMOOCJSONResult.ok(users);

    }

    private void setNullProperty(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
    }

    private Map<String,String> getErrorsMsg(BindingResult result){
        Map<String,String> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            // 错误的属性
            String field = fieldError.getField();
            // 错误的原因
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field,defaultMessage);

        }
        return errorMap;
    }
}
