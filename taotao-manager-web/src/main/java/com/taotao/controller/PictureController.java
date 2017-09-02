package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_ADDR}")
    private String IMAGE_SERVER_ADDR;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile){
//      接受上传的文件
//      取扩展名
        String originalFilename = uploadFile.getOriginalFilename();
        String exName = originalFilename.substring(originalFilename.indexOf(".") + 1);
//      上传服务器
        Map<String,Object> result = new HashMap<>();
        FastDFSClient fastDFSClient =null;
        try {
            fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            String url =IMAGE_SERVER_ADDR+ fastDFSClient.uploadFile(uploadFile.getBytes(), exName);
            result.put("error",0);
            result.put("url",url);
            return JsonUtils.objectToJson(result);
        }catch (Exception e){
            e.printStackTrace();
            result.put("error",1);
            result.put("message","images upload failed");
            return JsonUtils.objectToJson(result);
        }

//      响应url
    }
}
