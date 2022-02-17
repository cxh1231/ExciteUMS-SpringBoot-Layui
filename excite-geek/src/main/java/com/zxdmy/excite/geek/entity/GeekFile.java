package com.zxdmy.excite.geek.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 上传文件记录表
 * </p>
 *
 * @author 拾年之璐
 * @since 2022-02-17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class GeekFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上传用户id
     */
    private Long userId;

    /**
     * 文件名（服务器内存储的）
     */
    private String newName;

    /**
     * 文件原名（上传前）
     */
    private String name;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件路径（相对或绝对）
     */
    private String path;

    /**
     * 文件后缀
     */
    private String type;

    /**
     * 文件类型（mime-type）
     */
    private String mimeType;

    /**
     * 文件分组id
     */
    private Long groupId;

    /**
     * 图片封面
     */
    @TableField(exist = false)
    private String thumb;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    public String getThumb() {
        return path;
    }
}
