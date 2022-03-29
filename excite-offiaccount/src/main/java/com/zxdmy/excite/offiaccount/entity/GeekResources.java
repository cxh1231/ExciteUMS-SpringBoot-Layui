package com.zxdmy.excite.offiaccount.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GeekResources implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类ID
     */
    private Integer catId;

    /**
     * 资料标题
     */
    private String title;

    /**
     * 资料封面
     */
    private String cover;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取方式：1.广告解锁 2.广告全文 3.vip 4.积分兑换 5.免费
     */
    private Integer type;

    /**
     * 价格
     */
    private String price;

    /**
     * 获取网站1
     */
    private String website1;

    /**
     * 获取网站1
     */
    private String website2;

    /**
     * 是否首页显示：1-显示 | 0 - 不显示
     */
    private Integer showIndex;

    /**
     * 浏览次数
     */
    private Integer count;

    /**
     * 状态：1-显示 0-隐藏
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 更新时间
     */
    private Integer updateTime;

    /**
     * 删除时间
     */
    private Integer deleteTime;


}
