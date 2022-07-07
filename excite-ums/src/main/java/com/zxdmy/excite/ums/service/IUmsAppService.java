package com.zxdmy.excite.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * UMS系统的用户体系 服务类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
public interface IUmsAppService extends IService<UmsApp> {

    /**
     * 分页查询
     *
     * @param appName  应用名称
     * @param appId    应用ID
     * @param pageSize 每页条数
     * @param pageNum  页码
     * @return 分页结果
     */
    Page<UmsApp> getPage(String appName, String appId, Integer pageNum,Integer pageSize);


}
