package com.smart.smartparking.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.entity.Suggestion;
import com.smart.smartparking.service.ISuggestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
* <p>
*  前端控制器
* </p>
*
* @author  
* @since 2023-02-28
*/
@RestController
@RequestMapping("/suggestion")
@Api(tags = "API建议管理")
public class SuggestionController {

    @Resource
    private ISuggestionService suggestionService;

    @ApiOperation(value = "新增建议", notes = "新增建议", response = Car.class)
    @AutoLog("新增建议")
    @PostMapping
    @SaCheckPermission("suggestion.add")
    public Result save(@RequestBody Suggestion suggestion) {
//        User user = SessionUtils.getUser();
//        suggestion.setUser(user.getName());
//        suggestion.setUserid(user.getId());
//        suggestion.setDate(DateUtil.today());
//        suggestion.setTime(DateUtil.now());
        suggestionService.save(suggestion);
        return Result.success("cg");
    }

    @ApiOperation(value = "编辑建议", notes = "编辑建议", response = Car.class)
    @AutoLog("编辑建议")
    @PutMapping
    @SaCheckPermission("suggestion.edit")
    public Result update(@RequestBody Suggestion suggestion) {
        suggestionService.updateById(suggestion);
        return Result.success("cg");
    }

    @ApiOperation(value = "删除建议", notes = "删除建议", response = Car.class)
    @AutoLog("删除建议")
    @DeleteMapping("/{id}")
    @SaCheckPermission("suggestion.delete")
    public Result delete(@PathVariable Integer id) {
        suggestionService.removeById(id);
        return Result.success("cg");
    }

    @ApiOperation(value = "批量删除建议", notes = "批量删除建议", response = Car.class)
    @AutoLog("批量删除建议")
    @PostMapping("/del/batch")
    @SaCheckPermission("suggestion.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        suggestionService.removeByIds(ids);
        return Result.success("cg");
    }

    @ApiOperation(value = "建议列表", notes = "建议列表", response = Car.class)
    @GetMapping
    @SaCheckPermission("suggestion.list")
    public Result findAll() {
        return Result.success(suggestionService.list());
    }

    @ApiOperation(value = "建议列表2", notes = "建议列表2", response = Car.class)
    @GetMapping("/{id}")
    @SaCheckPermission("suggestion.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(suggestionService.getById(id));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", response = Car.class)
    @GetMapping("/page")
    @SaCheckPermission("suggestion.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Suggestion> queryWrapper = new QueryWrapper<Suggestion>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(suggestionService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
    * 导出接口
    */
    @ApiOperation(value = "导出", notes = "导出", response = Car.class)
    @GetMapping("/export")
    @SaCheckPermission("suggestion.export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Suggestion> list = suggestionService.list();
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("Suggestion信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }

    /**
    * excel 导入
    * @param file
    * @throws Exception
    */
    @ApiOperation(value = "导入", notes = "导入", response = Car.class)
    @PostMapping("/import")
    @SaCheckPermission("suggestion.import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<Suggestion> list = reader.readAll(Suggestion.class);

        suggestionService.saveBatch(list);
        return Result.success("cg");
    }

}
