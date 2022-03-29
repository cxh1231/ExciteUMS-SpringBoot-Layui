package com.zxdmy.excite.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目启动入口
 *
 * @author 拾年之璐
 */
@RestController
@SpringBootApplication(scanBasePackages = {
        "com.zxdmy.excite",
        "com.zxdmy.excite.system.service",
        "com.zxdmy.excite.common.service",
        "com.zxdmy.excite.offiaccount.service",
})
@MapperScan(basePackages = {
        "com.zxdmy.excite.system.mapper",
        "com.zxdmy.excite.common.mapper",
        "com.zxdmy.excite.offiaccount.mapper",
})
public class ExciteAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExciteAdminApplication.class, args);
        System.out.println("系统启动成功，访问 http://localhost:9001");
    }

}
