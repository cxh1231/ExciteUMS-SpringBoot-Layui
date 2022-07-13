package com.zxdmy.excite.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

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
    Page<UmsApp> getPage(String appName, String appId, Integer pageNum, Integer pageSize);

    /**
     * 新增或修改应用信息
     *
     * @param app 应用信息
     * @return 保存 | 修改结果
     */
    UmsApp saveOrUpdateApp(UmsApp app);

    /**
     * 根据应用ID查询应用信息
     *
     * @param appId 应用ID
     * @return 应用信息
     */
    UmsApp getByAppId(String appId);

    /**
     * 根据应用ID删除应用信息
     *
     * @param appId 应用ID
     * @return 删除结果
     */
    boolean deleteByAppId(String appId);
}
