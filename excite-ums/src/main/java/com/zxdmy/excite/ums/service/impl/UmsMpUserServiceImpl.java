package com.zxdmy.excite.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxdmy.excite.ums.entity.UmsMpUser;
import com.zxdmy.excite.ums.mapper.UmsMpUserMapper;
import com.zxdmy.excite.ums.service.IUmsMpUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信用户 服务实现类
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-06-28
 */
@Service
@AllArgsConstructor
public class UmsMpUserServiceImpl extends ServiceImpl<UmsMpUserMapper, UmsMpUser> implements IUmsMpUserService {

    private UmsMpUserMapper mpUserMapper;

    /**
     * 分页查询
     *
     * @param search          检索关键词：用户昵称、OpenID、备注名、备注信息、订阅场景值。
     * @param subscribeStatus 关注状态
     * @param pageNum         页码
     * @param pageSize        每页数量
     * @return 分页结果
     */
    @Override
    public Page<UmsMpUser> getPage(String search, Integer subscribeStatus, Integer pageNum, Integer pageSize) {
        // 页码设置为空时，默认为1
        if (pageNum == null) {
            pageNum = 1;
        }
        // 每页数量设置为空时，默认为10
        if (pageSize == null) {
            pageSize = 10;
        }
        // 创建查询条件对象
        QueryWrapper<UmsMpUser> queryWrapper = new QueryWrapper<>();
        // 查询条件不为空，则设置查询条件
        if (search != null && !search.isEmpty()) {
            queryWrapper.like(UmsMpUser.NICKNAME, search)
                    .or().like(UmsMpUser.OPEN_ID, search)
                    .or().like(UmsMpUser.REMARK, search)
                    .or().like(UmsMpUser.UMS_REMARK, search)
                    .or().like(UmsMpUser.QR_SCENE, search)
                    .or().like(UmsMpUser.QR_SCENE_STR, search);
        }
        // 关注状态不为空，则检索关注状态
        queryWrapper.eq(subscribeStatus != null, UmsMpUser.SUBSCRIBE, subscribeStatus)
                // 时间倒序排列
                .orderByDesc(UmsMpUser.CREATE_TIME);
        // 分页查询并返回
        return mpUserMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }
}
