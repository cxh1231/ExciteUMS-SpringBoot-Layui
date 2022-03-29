package com.zxdmy.excite.admin.controller.advanced;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.zxdmy.excite.common.base.BaseController;
import com.zxdmy.excite.common.base.BaseResult;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author 拾年之璐
 * @since 2021/12/29 18:50
 */
@Controller
@AllArgsConstructor
@RequestMapping("/system/develop")
public class GeneratorController extends BaseController {

    private DataSourceProperties dataSourceProperties;

    /**
     * 访问[代码生成]页面
     *
     * @return 页面跳转
     */
    @RequestMapping("generator")
    public String generatorIndex() {
        return "system/advanced/generator";
    }

    @GetMapping("/generator/list")
    @ResponseBody
    public BaseResult getTableList() {

//        System.out.println(dataSourceProperties.getUrl());

        // 获取链接数据库的基本信息，并填入配置类
        DataSourceConfig dsConfig = new DataSourceConfig
                .Builder(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                .build();

        // 策略配置(数据库表配置)
        StrategyConfig strategyConfig = new StrategyConfig.Builder().build();

        // 获取所有的表
        ConfigBuilder configBuilder = new ConfigBuilder(null, dsConfig, strategyConfig, null, null, null);

        List<TableInfo> tableInfos = configBuilder.getTableInfoList();
        return success(200, tableInfos);
    }

    /**
     * 生成代码
     *
     * @param projectPack 项目路径
     * @param moduleName  模块名
     * @param parentPack  父包名
     * @param author      作者
     * @param tableName   表名
     * @return 结果
     */
    @PostMapping("/generator/create")
    @ResponseBody
    public BaseResult CodeGenerator(String projectPack, String moduleName, String parentPack, String author, String tableName) {
        //【1】基础信息配置
        String projectPath = System.getProperty("user.dir");

        // 六个文件的路径。多模块项目下，一般来说每个文件的路径都是不同的（根据项目实际，可能在不同的模块下）。
        String entityPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/entity/";
        String servicePath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/service/";
        String serviceImplPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/service/impl/";
        String mapperPath = projectPath + "/excite-" + moduleName + projectPack + moduleName + "/mapper";
        String mapperXmlPath = projectPath + "/excite-" + moduleName + "/src/main/resources/mapper/";
        String controllerPath = projectPath + "/excite-admin/src/main/java/com/zxdmy/excite/admin/controller/" + moduleName + "/";

        // 【2】开始执行代码生成
        FastAutoGenerator.create(dataSourceProperties.getUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword())
                // 1. 全局配置
                .globalConfig(builder -> builder
                        // 作者名称
                        .author(author)
                        // 开启覆盖已生成的文件。注释掉则关闭覆盖。请谨慎开启此选项！
                        // .fileOverride()
                        // 禁止打开输出目录。注释掉则生成完毕后，自动打开生成的文件目录。
                        .disableOpenDir()
                        // 指定输出目录。多模块下，每个类型的文件输出目录不一致，在包配置阶段配置。
                        // .outputDir(packagePath)
                        // 开启swagger2。注释掉则默认关闭。
                        // .enableSwagger()
                        // 开启 kotlin 模式。注释掉则关闭此模式
                        // .enableKotlin()
                        // 指定时间策略。
                        .dateType(DateType.TIME_PACK)
                        // 注释时间策略。
                        .commentDate("yyyy-MM-dd")
                )

                // 2. 包配置
                .packageConfig((scanner, builder) -> builder
                        // 阶段1：各个文件的包名设置，用来拼接每个java文件的第一句：package com.XXX.XXX.XXX.xxx;
                        // 父包名配置
                        .parent(parentPack)
                        // 输入模块名。此模块名会在下面的几个包名前加。多模块项目，请根据实际选择是否添加。
                        // .moduleName(moduleName)
                        .entity(moduleName + ".entity")
                        .mapper(moduleName + ".mapper")
                        .service(moduleName + ".service")
                        .serviceImpl(moduleName + ".service.impl")
                        .controller("admin.controller." + moduleName)
                        .other("other")
                        // 阶段2：所有文件的生成路径配置
                        .pathInfo(
                                new HashMap<OutputFile, String>() {{
                                    // 实体类的保存路径
                                    put(OutputFile.entity, entityPath);
                                    // mapper接口的保存路径
                                    put(OutputFile.mapper, mapperPath);
                                    // mapper.xml文件的保存路径
                                    put(OutputFile.xml, mapperXmlPath);
                                    // service层口的保存路径
                                    put(OutputFile.service, servicePath);
                                    // service层接口实现类的保存路径
                                    put(OutputFile.serviceImpl, serviceImplPath);
                                    // 控制类的保存路径
                                    put(OutputFile.controller, controllerPath);
                                }}
                        )
                )

                // 3. 策略配置【请仔细阅读每一行，根据项目实际项目需求，修改、增删！！！】
                .strategyConfig((scanner, builder) -> builder.addInclude(tableName)
                        // 阶段1：Entity实体类策略配置
                        .entityBuilder()
                        // 设置父类。会在生成的实体类名后：extends BaseEntity
                        // .superClass(BaseEntity.class)
                        // 禁用生成 serialVersionUID。（不推荐禁用）
                        // .disableSerialVersionUID()
                        // 开启生成字段常量。
                        // 会在实体类末尾生成一系列 [public static final String NICKNAME = "nickname";] 的语句。（一般在写wapper时，会用到）
                        .enableColumnConstant()
                        // 开启链式模型。
                        // 会在实体类前添加 [@Accessors(chain = true)] 注解。用法如 [User user=new User().setAge(31).setName("snzl");]（这是Lombok的注解，需要添加Lombok依赖）
                         .enableChainModel()
                        // 开启 lombok 模型。
                        // 会在实体类前添加 [@Getter] 和 [@Setter] 注解。（这是Lombok的注解，需要添加Lombok依赖）
                        .enableLombok()
                        // 开启 Boolean 类型字段移除 is 前缀。
                        // .enableRemoveIsPrefix()
                        // 开启生成实体时生成字段注解。
                        // 会在实体类的属性前，添加[@TableField("nickname")]
                        // .enableTableFieldAnnotation()
                        // 逻辑删除字段名(数据库)。
                        // .logicDeleteColumnName("is_delete")
                        // 逻辑删除属性名(实体)。
                        // 会在实体类的该字段属性前加注解[@TableLogic]
                        // .logicDeletePropertyName("isDelete")
                        // 数据库表映射到实体的命名策略(默认下划线转驼峰)。一般不用设置
                        // .naming(NamingStrategy.underline_to_camel)
                        // 数据库表字段映射到实体的命名策略(默认为 null，未指定按照 naming 执行)。一般不用设置
                        // .columnNaming(NamingStrategy.underline_to_camel)
                        // 添加父类公共字段。
                        // 这些字段不会出现在新增的实体类中。
                        // .addSuperEntityColumns("id", "delete_time")
                        // 添加忽略字段。
                        // 这些字段不会出现在新增的实体类中。
                        // .addIgnoreColumns("password")
                        // 添加表字段填充
                        // 会在实体类的该字段上追加注解[@TableField(value = "create_time", fill = FieldFill.INSERT)]
                        .addTableFills(new Column("create_time", FieldFill.INSERT))
                        // 会在实体类的该字段上追加注解[@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)]
                        .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                        // 全局主键类型。如果MySQL主键设置为自增，则不需要设置此项。
                        // .idType(IdType.AUTO)
                        // 格式化文件名称。
                        // 如果不设置，如表[sys_user]的实体类名是[SysUser]。设置成下面这样，将是[SysUserEntity]
                        // .formatFileName("%sEntity")

                        // 阶段2：Mapper策略配置
                        .mapperBuilder()
                        // 设置父类
                        // 会在mapper接口方法集成[extends BaseMapper<XXXEntity>]
                        // .superClass(BaseMapper.class)
                        // 开启 @Mapper 注解。
                        // 会在mapper接口上添加注解[@Mapper]
                        .enableMapperAnnotation()
                        // 启用 BaseResultMap 生成。
                        // 会在mapper.xml文件生成[通用查询映射结果]配置。
                        .enableBaseResultMap()
                        // 启用 BaseColumnList。
                        // 会在mapper.xml文件生成[通用查询结果列 ]配置
                        .enableBaseColumnList()
                        // 设置缓存实现类
                        // .cache(MyMapperCache.class)
                        // 格式化 mapper 文件名称。
                        // 如果不设置，如表[sys_user]，默认的文件名是[SysUserMapper]。写成下面这种形式后，将变成[SysUserDao]。
                        // .formatMapperFileName("%sDao")
                        // 格式化 xml 实现类文件名称。
                        // 如果不设置，如表[sys_user]，默认的文件名是[SysUserMapper.xml]，写成下面这种形式后，将变成[SysUserXml.xml]。
                        // .formatXmlFileName("%sXml")

                        // 阶段3：Service策略配置
                        // .serviceBuilder()
                        // 设置 service 接口父类
                        // .superServiceClass(BaseService.class)
                        // 设置 service 实现类父类
                        // .superServiceImplClass(BaseServiceImpl.class)
                        // 格式化 service 接口文件名称
                        // 如果不设置，如表[sys_user]，默认的是[ISysUserService]。写成下面这种形式后，将变成[SysUserService]。
                        // .formatServiceFileName("%sService")
                        // 格式化 service 实现类文件名称
                        // 如果不设置，如表[sys_user]，默认的是[SysUserServiceImpl]。
                        // .formatServiceImplFileName("%sServiceImpl")

                        // 阶段4：Controller策略配置
                        .controllerBuilder()
                        // 设置父类。
                        // 会集成此父类。
                         .superClass(BaseController.class)
                        // 开启生成 @RestController 控制器
                        // 会在控制类中加[@RestController]注解。
                        // .enableRestStyle()
                        // 开启驼峰转连字符
                        .enableHyphenStyle()

                        // 最后：构建
                        .build()
                )

                //模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                //.templateEngine(new BeetlTemplateEngine())
                .templateEngine(new FreemarkerTemplateEngine())

                // 执行
                .execute();
        return success("表" + tableName + "的代码生成成功！");
    }

}
