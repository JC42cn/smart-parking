package com.smart.smartparking.controller;

//import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.File;
import com.smart.smartparking.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
* <p>
*  前端控制器
* </p>
*
* @author 程序员青戈
* @since 2023-02-04
*/
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    private static final String FILES_DIR = "/files/";

    @Value("${server.port:8080}")
    private String port;
    @Value("${file.download.ip:localhost}")
    private String downloadIp;

    @Resource
    private IFileService fileService;

    /**
     * 文件上传
     *
     * @param file MultipartFile
     * @return Result
     */
    @PostMapping("/upload")
    @AutoLog("上传文件")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 文件完整的名称
        String extName = FileUtil.extName(originalFilename);  // 文件后缀名
        String uniFileFlag = IdUtil.fastSimpleUUID();
        String fileFullName = uniFileFlag + StrUtil.DOT + extName;
        // 封装完整的文件路径获取方法
        String fileUploadPath = getFileUploadPath(fileFullName);
        //  完整的上传文件名
        long size = file.getSize();  // 单位是 byte, size / 1024 -> kb
//        byte[] bytes = file.getBytes();
        String name = file.getName();
        log.info("{}, {}, {}", originalFilename, size, name);
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询看看是否存在相同md5的文件
        List<File> existFiles = fileService.list(new QueryWrapper<File>().eq("md5", md5));
        if (existFiles.size() > 0) {
            File existFile = existFiles.get(0);
            String location = existFile.getLocation();
            if (new java.io.File(location).exists()) {
                saveFile(originalFilename, size, md5, extName, existFile.getLocation(), existFile.getUrl());
                // 如果文件存在, 就使用该文件
                return Result.success(existFile.getUrl());
            }
        }
        try {
            java.io.File uploadFile = new java.io.File(fileUploadPath);
            java.io.File parentFile = uploadFile.getParentFile();
            if (!parentFile.exists()) {  // 如果父级不存在，也就是说files目录不存在，那么我要创建出来
                parentFile.mkdirs();
            }
            file.transferTo(uploadFile);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }

        String url = "http://" + downloadIp + ":" + port + "/file/download/" + fileFullName;
        saveFile(originalFilename, size, md5, extName, fileUploadPath, url);
        // 看看数据库是否存在错误的文件路径，修复下
        if (existFiles.size() > 0) {
            for (File existFile : existFiles) {
                existFile.setUrl(url);
                existFile.setLocation(fileUploadPath);
                fileService.updateById(existFile);
            }
        }

        return Result.success(url);
    }

    @AutoLog("上传图片")
    @PostMapping("/uploadImg")
    public Dict uploadImg(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 文件完整的名称
        String extName = FileUtil.extName(originalFilename);  // 文件后缀名
        String uniFileFlag = IdUtil.fastSimpleUUID();
        String fileFullName = uniFileFlag + StrUtil.DOT + extName;
        // 封装完整的文件路径获取方法
        String fileUploadPath = getFileUploadPath(fileFullName);
        long size = file.getSize();  // 单位是 byte, size / 1024 -> kb
        String name = file.getName();
        log.info("{}, {}, {}", originalFilename, size, name);
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询看看是否存在相同md5的文件
        List<File> existFiles = fileService.list(new QueryWrapper<File>().eq("md5", md5));
        String url = "";
        if (existFiles.size() > 0) {
            File existFile = existFiles.get(0);
            String location = existFile.getLocation();
            if (new java.io.File(location).exists()) {
                saveFile(originalFilename, size, md5, extName, existFile.getLocation(), existFile.getUrl());
                // 如果文件存在, 就使用该文件
                url = existFile.getUrl();
            }
        } else {
            try {
                java.io.File uploadFile = new java.io.File(fileUploadPath);
                java.io.File parentFile = uploadFile.getParentFile();
                if (!parentFile.exists()) {  // 如果父级不存在，也就是说files目录不存在，那么我要创建出来
                    parentFile.mkdirs();
                }
                file.transferTo(uploadFile);
            } catch (Exception e) {
                log.error("文件上传失败", e);
                return Dict.create().set("errno", 1);
            }

            url = "http://" + downloadIp + ":" + port + "/file/download/" + fileFullName;
            saveFile(originalFilename, size, md5, extName, fileUploadPath, url);
            // 看看数据库是否存在错误的文件路径，修复下
            if (existFiles.size() > 0) {
                for (File existFile : existFiles) {
                    existFile.setUrl(url);
                    existFile.setLocation(fileUploadPath);
                    fileService.updateById(existFile);
                }
            }
        }
        Dict dict = Dict.create().set("errno", 0).set("data", Dict.create().set("url", url));
        return dict;
    }

    // 保存文件记录到数据库
    private void saveFile(String name, long size, String md5, String type, String fileUploadPath, String url) {
        File savedFile = new File();
        savedFile.setName(name);
        savedFile.setSize(size);
        savedFile.setMd5(md5);
        savedFile.setType(type);
        savedFile.setLocation(fileUploadPath);
        savedFile.setUrl(url);
        fileService.save(savedFile);
    }

    /**
     * 文件下载
     * @param fileFullName
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/{fileFullName}")
    @AutoLog("下载文件")
    public void downloadFile(@PathVariable String fileFullName,
                             HttpServletResponse response) throws IOException {
        String fileUploadPath = getFileUploadPath(fileFullName);
        byte[] bytes = FileUtil.readBytes(fileUploadPath);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileFullName, "UTF-8"));  // 附件下载
        OutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }

    /**
     * 获取文件的完整路径
     *
     * @param fileFullName
     * @return
     */
    private String getFileUploadPath(String fileFullName) {
        String uploadPath = System.getProperty("user.dir");
        return uploadPath + FILES_DIR + fileFullName;
    }

    @AutoLog("新增文件记录")
    @PostMapping
    //file.add
    public Result save(@RequestBody File file) {
        fileService.save(file);
        return Result.success();
    }

    @AutoLog("编辑文件记录")
    @PutMapping
   // file.edit
    public Result update(@RequestBody File file) {
        fileService.updateById(file);
        return Result.success();
    }

    @AutoLog("删除文件记录")
    @DeleteMapping("/{id}")
    //file.delete
    public Result delete(@PathVariable Integer id) {
        fileService.removeById(id);
        return Result.success();
    }

    @AutoLog("批量删除文件记录")
    @PostMapping("/del/batch")
    //file.deleteBatch
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        fileService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(fileService.list());
    }

    @GetMapping("/{id}")
    //file.list
    public Result findOne(@PathVariable Integer id) {
        return Result.success(fileService.getById(id));
    }

    @GetMapping("/page")
    //file.list
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<File> queryWrapper = new QueryWrapper<File>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(fileService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
