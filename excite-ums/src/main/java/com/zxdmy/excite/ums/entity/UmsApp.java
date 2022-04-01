package com.zxdmy.excite.ums.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * UMS系统的用户体系
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-04-01
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("ums_app")
public class UmsApp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用的对外主键
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用私钥
     */
    private String appSecret;

    /**
     * 应用类型（预留字段）
     */
    private String appType;

    /**
     * 备注
     */
    private String appRemark;

    /**
     * 用户状态：1-正常 0-禁用
     */
    private Integer status;

    /**
     * 应用创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;


    public static final String ID = "id";

    public static final String APP_ID = "app_id";

    public static final String APP_NAME = "app_name";

    public static final String APP_SECRET = "app_secret";

    public static final String APP_TYPE = "app_type";

    public static final String APP_REMARK = "app_remark";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DELETE_TIME = "delete_time";

}
