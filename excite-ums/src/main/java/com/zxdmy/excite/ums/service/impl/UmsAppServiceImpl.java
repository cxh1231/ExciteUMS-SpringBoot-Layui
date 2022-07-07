package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsApp;
import com.zxdmy.excite.ums.mapper.UmsAppMapper;
import com.zxdmy.excite.ums.service.IUmsAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
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
}
