package com.zxdmy.excite.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsMpUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 微信用户 服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-28
 */
public interface IUmsMpUserService extends IService<UmsMpUser> {

    /**
     * 分页查询
     *
     * @param search          检索关键词：用户昵称、OpenID、备注名、备注信息、订阅场景值。
     * @param subscribeStatus 关注状态
     * @param pageNum         页码
     * @param pageSize        每页数量
     * @return 分页结果
     */
    Page<UmsMpUser> getPage(String search, Integer subscribeStatus, Integer pageNum, Integer pageSize);

}
