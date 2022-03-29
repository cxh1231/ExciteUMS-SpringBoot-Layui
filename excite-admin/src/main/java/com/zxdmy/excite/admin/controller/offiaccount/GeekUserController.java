package com.zxdmy.excite.admin.controller.offiaccount;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.common.base.BaseResult;
import com.zxdmy.excite.offiaccount.entity.GeekUser;
import com.zxdmy.excite.offiaccount.service.IGeekUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import com.zxdmy.excite.common.base.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-02-16
 */
@Controller
@AllArgsConstructor
@RequestMapping("/geek/user")
public class GeekUserController extends BaseController {

    private IGeekUserService userService;

    /**
     * 访问页面：用户管理 - 列表页面
     *
     * @return 跳转至列表页面
     */
    @RequestMapping("index")
    public String index() {
        return "geek/user/index";
    }

    /**
     * 接口功能：获取用户列表
     *
     * @param page     当前页面
     * @param limit    每页请求数
     * @param username 检索用户名
     * @param account  检索电话或邮箱
     * @return JSON：用户列表 | 错误信息
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public BaseResult getUserList(Integer page, Integer limit, String username, String account) {
        QueryWrapper<GeekUser> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<GeekUser> userPage = userService.page(new Page<>(page, limit), wrapper);
        if (null != userPage) {
            return success("查询成功", userPage.getRecords(), (int) userPage.getTotal());
        } else {
            return error("查询失败，用户不存在");
        }
    }

    @PostMapping(value = "/sendPoints")
    @ResponseBody
    public BaseResult sendPoints(Integer userId, Integer sendPoints) {
        UpdateWrapper<GeekUser> wrapper = new UpdateWrapper<>();
        wrapper.setSql("points = points + " + sendPoints)
                .eq("id", userId);
        if (userService.update(wrapper)) {
            return success("积分赠送成功！");
        }
        return error("积分赠送失败，请核实！");
    }
}
