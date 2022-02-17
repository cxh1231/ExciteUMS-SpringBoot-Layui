package com.zxdmy.excite.admin.controller.component;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qiniu.storage.model.FileInfo;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.component.bo.QiniuOssBO;
import com.zxdmy.excite.component.qiniu.QiniuOssService;
import com.zxdmy.excite.component.vo.QiniuFileVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 七牛云文件存储服务控制类
 *
 * @author 拾年之璐
 * @since 2022/1/24 21:49
 */
@Controller
@AllArgsConstructor
@RequestMapping("/component/qiniuOss")
public class QiniuOssController extends BaseController {

    QiniuOssService qiniuOssService;

    @RequestMapping("index")
    public String index() {
        return "component/qiniuOss/index";
    }

    /**
     * 设置七牛云OSS的配置信息
     *
     * @return 结果
     */
    @PostMapping(value = "setKey")
    @ResponseBody
    public BaseResult setKey(@Validated QiniuOssBO qiniuOssBO) {
        System.out.println(qiniuOssBO);
        if (qiniuOssService.saveQiniuConfig(qiniuOssBO))
            return success("保存成功！");
        return error("保存失败，请重试！");
    }


    @GetMapping(value = "fileList")
    @ResponseBody
    public BaseResult getList(Integer page, Integer limit) {
        Page<FileInfo> fileInfoList = qiniuOssService.getQiniuFileListPage(page, limit, "", "");
        return success("获取成功！", fileInfoList.getRecords(), (int) fileInfoList.getTotal());
    }


}
