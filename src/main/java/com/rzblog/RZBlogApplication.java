package com.rzblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动程序
 * 
 * @author ricozhou
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.rzblog.*.*.*.mapper")
public class RZBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(RZBlogApplication.class, args);
		System.out.println(" RZBlog启动成功 \n"
				+ "   .---------.                .-----------.              .----------.                \n"
				+ "  |           \\               | _ _ __    |              |   _ _ _   \\             \n"
				+ "  |    ---     \\                      /  /               |  |      \\  \\           \n"
				+ "  |   ( ' )    |                     /  /                |  |      |  |              \n"
				+ "  |  (_ o _)   /                    /  /                 |  |_ _ _ / /               \n"
				+ "  |   (_,_) . '   __               /  /                  |   _ _ _   \\              \n"
				+ "  |   |  \\  \\    |  |             /  /                   |  |      \\  \\          \n"
				+ "  |   |   \\  \\   /  /            /  /                    |  |       |  |           \n"
				+ "  |   |    \\  \\ /  /            /  /                     |  |       |  |           \n"
				+ "  |   |     \\  `  /            /  /_ _ _ __              |  |_ _ _ /  /             \n"
				+ "  ''-''      `'-'             |_ _ _ _ _ __|             |_ _ _ _ _ _/                ");

	}
}