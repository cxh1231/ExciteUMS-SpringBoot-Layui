package com.zxdmy.excite.admin.controller.ums;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.service.IUmsAppService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;

import java.time.LocalDateTime;

/**
 * <p>
 * UMS系统的用户体系 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
@Controller
@AllArgsConstructor
@RequestMapping("/ums/app")
public class UmsAppController extends BaseController {

    private IUmsAppService appService;

    /**
     * 访问页面：用户管理 - 列表页面
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "ums/app/index";
    }

    /**
     * 接口：分页查询
     *
     * @param appName  应用名称
     * @param appId    应用ID
     * @param page 每页条数
     * @param limit  页码
     * @return 分页结果
     */
    public BaseResult getAppPage(String appName, String appId, Integer page, Integer limit) {
        // 获取分页查询结果，输入的数据可以为空，在内部已处理
        Page<UmsApp> appPage = appService.getPage(appName, appId, page, limit);
        // 返回分页查询结果
        return success("查询成功", appPage.getRecords(), appPage.getTotal());
    }

    /**
     * 接口：修改应用状态
     *
     * @param id        应用ID
     * @param newStatus 新状态
     * @return 修改结果
     */
    @RequestMapping(value = "/changeStatus/{newStatus}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult changeAppStatus(String id, @PathVariable String newStatus) {
        int status;
        int idInt;
        // 尝试类型转换
        try {
            status = Integer.parseInt(newStatus);
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return error("参数错误");
        }
        // 更新状态
        UmsApp app = new UmsApp();
        app.setId(idInt);
        app.setStatus(status);
        if (appService.updateById(app)) {
            return success("更新成功");
        } else {
            return error("更新失败");
        }
    }

    /**
     * 接口：获取用户列表
     *
     * @param appid   应用名称
     * @param appName 检索电话或邮箱
     * @param page    当前页面
     * @param limit   每页请求数
     * @return JSON：用户列表 | 错误信息
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getAppList(String appid, String appName, Integer page, Integer limit) {
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<UmsApp> userPage = appService.page(new Page<>(page, limit), wrapper);
        if (null != userPage) {
            return success("查询成功", userPage.getRecords(), (int) userPage.getTotal());
        } else {
            return error("无数据");
        }
    }

    /**
     * 接口：添加应用
     *
     * @param appName 应用名称
     * @return 添加结果
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult addApp(String appName) {
        // 生成 appid，格式：yyyyMMdd + 6位随机数
        String appid = DateUtil.format(DateUtil.date(), "yyyyMMdd") + RandomUtil.randomString("1234567890", 4);
        // 当前生成的 appid，不允许存在
        if (appService.getOne(new QueryWrapper<UmsApp>().eq(UmsApp.APP_ID, appid)) == null) {
            // 生成 app secret，全大写，随机生成
            String appSecret = RandomUtil.randomStringUpper(32);
            // 执行添加
            if (appService.save(new UmsApp().setAppId(appid)
                    .setAppName(appName)
                    .setAppSecret(appSecret)
                    .setAppType("1")
                    .setStatus(1)
                    .setCreateTime(LocalDateTime.now())
            )) {
                return success("应用创建成果！");
            }
            return error("添加失败");
        }
        // 应用id已存在，重新生成
        else {
            return addApp(appName);
        }
    }

    /**
     * 接口：更新秘钥
     *
     * @param id 应用id
     * @return 更新结果
     */
    @RequestMapping(value = "/refreshSecret", method = RequestMethod.POST)
    @ResponseBody
    // 刷新秘钥
    public BaseResult refreshAppSecret(String id) {
        // 尝试类型转换
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return error("参数错误");
        }
        // 生成新的秘钥
        String appSecret = RandomUtil.randomStringUpper(32);
        // 构造新的应用对象
        UmsApp app = new UmsApp().setId(idInt).setAppSecret(appSecret);
        // 更新应用秘钥
        if (appService.updateById(app)) {
            return success("更新成功");
        } else {
            return error("更新失败");
        }
    }

    /**
     * 接口：更新应用名称
     *
     * @param id      应用ID
     * @param newName 新名称
     * @return 更新结果
     */
    @RequestMapping(value = "/updateName", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateAppName(String id, String newName) {
        // 尝试类型转换
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return error("参数错误");
        }
        // 构造新的应用对象
        UmsApp app = new UmsApp().setId(idInt).setAppName(newName);
        // 更新应用名称
        if (appService.updateById(app)) {
            return success("更新成功");
        } else {
            return error("更新失败");
        }
    }

    /**
     * 接口：删除应用
     *
     * @param id 应用ID
     * @return 删除结果
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult deleteApp(String id) {
        int idInt;
        // 尝试类型转换
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return error("参数错误");
        }
        // 执行删除
        if (appService.removeById(idInt)) {
            return success("删除成功");
        } else {
            return error("删除失败");
        }
    }
}
