package com.zxdmy.excite.geek.entity;

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
public class GeekUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户微信端的openid
     */
    private String uuid;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String userimg;

    /**
     * 用户积分
     */
    private Integer jifen;

    private Integer num;

    /**
     * 会员到期时间
     */
    private String viptime;

    /**
     * 注册时间
     */
    private String addtime;

    /**
     * 最后登录时间
     */
    private String logintime;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 登录次数
     */
    private Integer count;

    /**
     * 时间什么？估计和签到相关
     */
    private String times;

    /**
     * 签到什么？
     */
    private String qiandao;


}
