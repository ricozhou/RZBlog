package com.rzblog.framework.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * https配置
 * 
 * @author ricozhou
 */
@Configuration
public class HttpsConfig implements EmbeddedServletContainerCustomizer {
	// 若使用nginx配置https则以下代码可以不再需要，若nginx没有配置https或者不需要nginx作为转发，则要配置http自动转https需要以下代码

	// 以下配置自动转https
	// http端口
	@Value("${server.port}")
	private int port;

	// https端口
	@Value("${https.httpsport}")
	private int httpsport;

	// 是否开启https
	@Value("${https.openhttps}")
	private boolean openhttps;

	// ssl证书位置
	@Value("${https.ssl.key-store}")
	private String keyStore;

	// 证书密码
	@Value("${https.ssl.key-store-password}")
	private String keyStorePassword;

	// 配置https取消以下代码注释，并实现接口：implements EmbeddedServletContainerCustomizer
	// 拦截所有请求
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint constraint = new SecurityConstraint();
				constraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				// 开启则监听所有跳转https
				if (openhttps) {
					collection.addPattern("/*");
				}
				constraint.addCollection(collection);
				context.addConstraint(constraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(httpConnector());
		return tomcat;
	}

	// 配置http转https
	@Bean
	public Connector httpConnector() {
		Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		// Connector监听的http的端口号
		connector.setPort(port);
		connector.setSecure(false);
		// 监听到http的端口号后转向到的https的端口号
		connector.setRedirectPort(httpsport);
		return connector;
	}

	// 这里设置默认端口为443，即https的，如果这里不设置，会https和http争夺80端口
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// 开启则查找证书，否则证书位置无所谓
		if (openhttps) {
			Ssl ssl = new Ssl();
			// 证书
			ssl.setKeyStore(keyStore);
			ssl.setKeyStorePassword(keyStorePassword);
			container.setSsl(ssl);
		}
		container.setPort(httpsport);
	}
}
