package com.staffing.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
 * @author JngKang
 * @date 2022-02-22 08:47
 * @description Mybatis-plus代码生成器
 */
public class CodeGenerator {
    public static void main(String[] args) {
        Generator();
    }

    private static void Generator() {
        String url = "jdbc:mysql://localhost:3306/staffing?useSSL=true&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
        FastAutoGenerator.create(url, "root", "123456")
                .globalConfig(builder -> {
//                    builder.author("JngKang") // 设置作者
                    builder
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\Code\\GraduationProject\\Staffing\\src\\main\\java\\"); // 指定输出目录

                })
                .packageConfig(builder -> {
                    builder.parent("com.staffing") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\Code\\GraduationProject\\Staffing\\src\\main\\resources\\mapper\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok()// 开启entity试用lombok
                            .enableRemoveIsPrefix();// 	开启 Boolean 类型字段移除 is 前缀
                    builder.mapperBuilder().enableMapperAnnotation().build();// 开启entityMapper中生成@Mapper
                    builder.controllerBuilder().enableHyphenStyle()// 开启驼峰转连字符
                            .enableRestStyle();// 开启生成@RestController控制器
                    builder.addInclude("") // 设置需要生成的表名
                            .addTablePrefix("sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}