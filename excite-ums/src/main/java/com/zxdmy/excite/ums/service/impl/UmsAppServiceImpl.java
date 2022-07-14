package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.mapper.UmsAppMapper;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UMS系统的用户体系 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
@Service
@AllArgsConstructor
public class UmsAppServiceImpl extends ServiceImpl<UmsAppMapper, UmsApp> implements IUmsAppService {

    private UmsAppMapper appMapper;

    /**
     * 分页查询
     *
     * @param appName  应用名称
     * @param appId    应用ID
     * @param pageSize 每页条数
     * @param pageNum  页码
     * @return 分页结果
     */
    @Override
    public Page<UmsApp> getPage(String appName, String appId, Integer pageNum, Integer pageSize) {
        // 页码为空时，默认为1
        if (pageNum == null) {
            pageNum = 1;
        }
        // 每页条数为空时，默认为10
        if (pageSize == null) {
            pageSize = 10;
        }
        // 查询条件
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.like(null != appName, UmsApp.APP_NAME, appName)
                .or().like(null != appId, UmsApp.APP_ID, appId)
                // 时间倒序排列
                .orderByDesc(UmsApp.ID);
        // 分页查询并返回
        return appMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 新增或修改应用信息
     *
     * @param app 应用信息
     * @return 保存 | 修改结果
     */
    @Override
    @CachePut(key = "#app.appId", value = "umsApp")
    public UmsApp saveOrUpdateApp(UmsApp app) {
        // 构造查询条件
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsApp.APP_ID, app.getAppId());
        // 更新失败：应用ID不存在，则新增应用
        if (appMapper.update(app, wrapper) == 0) {
            // 返回新增结果：大于0表示新增成功
            appMapper.insert(app);
        }
        // 更新时，缓存注解用的是返回值，所以查询刚修改的应用信息，并返回
        return appMapper.selectOne(wrapper);
    }

    /**
     * 根据应用ID查询应用信息
     *
     * @param appId 应用ID
     * @return 应用信息
     */
    @Override
    @Cacheable(key = "#appId", value = "umsApp")
    public UmsApp getByAppId(String appId) {
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsApp.APP_ID, appId);
        return appMapper.selectOne(wrapper);
    }

    /**
     * 根据应用ID删除应用信息
     *
     * @param appId 应用ID
     * @return 删除结果
     */
    @Override
    @CacheEvict(value = "umsApp", key = "#appId")
    public boolean deleteByAppId(String appId) {
        QueryWrapper<UmsApp> wrapper = new QueryWrapper<>();
        wrapper.eq(UmsApp.APP_ID, appId);
        return appMapper.delete(wrapper) > 0;
    }
}
