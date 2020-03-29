package com.xp.xx_forum.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.xp.xx_forum.dto.UploadMsgDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author ph
 * @version 1.0
 * @date 2020/3/12 20:43
 */
@Controller
public class FileController {

    @Value("${tengxun.accessKey}")
    private String accessKey;
    @Value("${tengxun.secretId}")
    private String secretKey;
    @Value("${tengxun.region}")
    private String region;
    @Value("${tengxun.bucketName}")
    private String bucketName;
    @Value("${tengxun.path}")
    private String path;
    @Value("${tengxun.qianzui}")
    private String qianzui;

    @ResponseBody
    @PostMapping(value = "/upload")
    public Object upload(@RequestParam(value = "editormd-image-file") MultipartFile file){
        if(file==null){
            return new UploadMsgDTO(0,"上传的文件为空",null);
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.split("\\.")[0];
        String newFileName = UUID.randomUUID().toString()+fileName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = this.bucketName;

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp",null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/"+this.qianzui+"/"+year+"/"+month+"/"+day+"/"+newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
            URL url = cosclient.generatePresignedUrl(bucketName, key, expiration);
            String newUrl = url.toString().split("\\?")[0];
            return new UploadMsgDTO(1,"上传成功",newUrl);
        } catch (IOException e) {
            return new UploadMsgDTO(0,e.getMessage(),null);
        }finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }

    }

}
