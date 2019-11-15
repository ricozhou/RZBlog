package com.rzblog.framework.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
@Order(Integer.MIN_VALUE)
public class CrossFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 开发环境与测试环境的切换
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;
		res.setContentType("textml;charset=UTF-8");
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers",
				" Client,Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie, token,Authorization, RequestPerm,language");

		res.setHeader("XDomainRequestAllowed", "1");

		if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
			((HttpServletResponse) res).setStatus(HttpStatus.OK.value());
			return;
		} else {
			chain.doFilter(req, res);
			return;
		}
	}

	@Override
	public void destroy() {
	}

}
