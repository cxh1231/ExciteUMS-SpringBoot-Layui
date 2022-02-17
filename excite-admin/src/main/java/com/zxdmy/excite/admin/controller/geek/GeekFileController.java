package com.zxdmy.excite.admin.controller.geek;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.component.bo.QiniuOssBO;
import com.zxdmy.excite.component.qiniu.QiniuOssService;
import com.zxdmy.excite.component.vo.QiniuFileVO;
import com.zxdmy.excite.geek.entity.GeekFile;
import com.zxdmy.excite.geek.service.IGeekFileService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 上传文件记录表 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-02-17
 */
@Controller
@AllArgsConstructor
@RequestMapping("/geek/file")
public class GeekFileController extends BaseController {

    QiniuOssService qiniuOssService;

    IGeekFileService geekFileService;

    @RequestMapping("index")
    public String index() {
        return "geek/file/index";
    }

    /**
     * 设置七牛云OSS的配置信息
     *
     * @return 结果
     */
    @PostMapping(value = "qiniu/config")
    @ResponseBody
    public BaseResult setKey(@Validated QiniuOssBO qiniuOssBO) {
        System.out.println(qiniuOssBO);
        if (qiniuOssService.saveQiniuConfig(qiniuOssBO))
            return success("保存成功！");
        return error("保存失败，请重试！");
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 结果
     */
    @PostMapping("upload")
    @ResponseBody
    public BaseResult uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return error(400, "上传的文件为空，请检查！");
        }
        // 构造文件名
        String fileName = file.getOriginalFilename();
        String fileType = StrUtil.subAfter(fileName, ".", true);
        String newFileName = System.currentTimeMillis() + "_" + IdUtil.simpleUUID() + "." + fileType;
        System.out.println(newFileName);
        // 临时文件保存的路径和文件名，这个路径要存在！
        String tempPath = "D:/temp/" + newFileName;
        // 生成临时文件
        File newFile = new File(tempPath);
        try {
            // 复制一份文件
            file.transferTo(newFile);
            // 将新生成文件上传至七牛云
            QiniuFileVO qiniuFile = qiniuOssService.uploadFile(newFile);
            System.out.println(qiniuFile);
            // 删除复制后的文件
            FileUtil.del(newFile);
            if (qiniuFile != null) {
                GeekFile geekFile = new GeekFile()
                        .setUserId(10000L)
                        .setNewName(newFileName)
                        .setName(fileName)
                        .setSize(file.getSize())
                        .setPath(qiniuFile.getProtocol() + "://" + qiniuFile.getDomain() + "/" + newFileName)
                        .setType(fileType)
                        .setMimeType(qiniuFile.getMimeType())
                        .setCreateTime(LocalDateTime.now());
                // 保存至数据库
                geekFileService.save(geekFile);
                // 返回结果
                return success(200, "上传成功！", geekFile);
            }
            // 返回结果
            return error(400, "上传失败，请重试！");
        } catch (Exception e) {
            return error(400, e.getMessage());
        }
    }

    /**
     * 获取文件列表
     *
     * @return 结果
     */
    @GetMapping("list")
    @ResponseBody
    public BaseResult list(Integer page, Integer limit) {
        if (page == null) page = 1;
        if (limit == null) limit = 24;
        List<GeekFile> geekFileList = geekFileService.list();
        Page<GeekFile> geekFilePage = geekFileService.page(new Page<>(page, limit));
        return success(200, "获取列表成功", geekFilePage.getRecords(), (int) geekFilePage.getTotal());
    }
}
