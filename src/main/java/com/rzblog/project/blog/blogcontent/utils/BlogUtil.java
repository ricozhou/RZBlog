package com.rzblog.project.blog.blogcontent.utils;

import java.io.PrintWriter;
import java.text.MessageFormat;

import com.rzblog.common.constant.ThirdPartyCallConstant;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.softcontent.domain.Softcontent;

/**
 * @author ricozhou
 * @date Oct 17, 2018 5:46:16 PM
 * @Desc
 */
public class BlogUtil {
	public static void writerToHtml(PrintWriter writer, String... msgs) {
		if (null == writer) {
			return;
		}
		for (String msg : msgs) {
			writer.print("<script>printMessage('" + msg + "');</script>");
			// writer.print( msg);
			System.out.println(writer);
			writer.flush();
		}
	}

	public static void shutdownWriter(PrintWriter writer) {
		writerToHtml(writer, "爬取结束...", "shutdown");
		if (null != writer) {
			writer.close();
		}
	}

	// 对象转换
	public static void turnSoftcontentFromBlogcontent(Blogcontent blogcontent, Softcontent softcontent) {
		if (softcontent == null) {
			softcontent = new Softcontent();
		}
		if (blogcontent == null) {
			return;
		}
		softcontent.setCid(blogcontent.getCid());
		softcontent.setSoftName(blogcontent.getSoftName());
		softcontent.setSoftType(blogcontent.getSoftType());
		softcontent.setSoftWebsite(blogcontent.getSoftWebsite());
		softcontent.setSoftDownUrl(blogcontent.getSoftDownUrl());
		softcontent.setSoftUpdateUrl(blogcontent.getSoftUpdateUrl());
		softcontent.setSoftDocUrl(blogcontent.getSoftDocUrl());
		softcontent.setSoftLicense(blogcontent.getSoftLicense());
		softcontent.setSoftLanguage(blogcontent.getSoftLanguage());
		softcontent.setSoftOperateSystem(blogcontent.getSoftOperateSystem());
		softcontent.setSoftAuthor(blogcontent.getSoftAuthor());
		softcontent.setCreateBy(ShiroUtils.getLoginName());
		softcontent.setUpdateBy(ShiroUtils.getLoginName());

		blogcontent.setSoftcontent(softcontent);
	}

	/**
	 * 提交链接到百度的接口地址
	 *
	 * @param type
	 *            urls: 推送, update: 更新, del: 删除
	 * @param site
	 *            待提交的站点
	 * @param baiduPushToken
	 *            百度推送的token，百度站长平台获取
	 * @return
	 */
	public static String getBaiduPushUrl(Integer type, String site, String baiduPushToken) {
		String pushType = ThirdPartyCallConstant.THIRD_PARTY_CALL_BAIDU_PUSH_TYPE_PUSH;
		if (type == 1) {
			pushType = ThirdPartyCallConstant.THIRD_PARTY_CALL_BAIDU_PUSH_TYPE_UPDATE;
		} else if (type == 2) {
			pushType = ThirdPartyCallConstant.THIRD_PARTY_CALL_BAIDU_PUSH_TYPE_DELETE;
		}

		return MessageFormat.format(ThirdPartyCallConstant.THIRD_PARTY_CALL_BAIDU_PUSH_URL_PATTERN, pushType, site,
				baiduPushToken);
	}

	// 获取请求的域名拼接
	public static String getRequestDomain(String networkProtocol, String ip, int port) {
		String domain = null;
		if ("http".equals(networkProtocol)) {
			if (port == 80) {
				domain = networkProtocol + "://" + ip;
			} else {
				domain = networkProtocol + "://" + ip + ":" + port;
			}
		} else if ("https".equals(networkProtocol)) {
			if (port == 443) {
				domain = networkProtocol + "://" + ip;
			} else {
				domain = networkProtocol + "://" + ip + ":" + port;
			}
		}
		return domain;
	}
}
