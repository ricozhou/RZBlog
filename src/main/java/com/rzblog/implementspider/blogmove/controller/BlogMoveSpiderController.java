package com.rzblog.implementspider.blogmove.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.socket.TextMessage;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.WebSocketConstants;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.DateUtils;
import com.rzblog.common.utils.MapUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.common.utils.spring.SpringUtils;
import com.rzblog.framework.websocket.service.WebSocketPushHandler;
import com.rzblog.implementspider.blogmove.service.BlogMoveArticleService;
import com.rzblog.implementspider.blogmove.service.BlogMoveOfficeArticleService;
import com.rzblog.implementspider.blogmove.utils.BlogMoveCommonUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveTouTiaoUtils;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.blog.blogcontent.service.BlogmoveServiceImpl;
import com.rzblog.project.blog.blogcontent.service.IBlogcontentService;
import com.rzblog.project.blog.blogcontent.service.IBlogmoveService;
import com.rzblog.project.blog.blogset.domain.Blogset;
import com.rzblog.project.blog.blogset.service.IBlogsetService;

/**
 * @author ricozhou
 * @date Oct 17, 2018 12:10:48 PM
 * @Desc
 */
public class BlogMoveSpiderController {
	BlogMoveArticleService blogMoveArticleService = new BlogMoveArticleService();
	BlogMoveOfficeArticleService blogMoveWordArticleService = new BlogMoveOfficeArticleService();
	IBlogcontentService blogcontentService = (IBlogcontentService) SpringUtils.getBean(IBlogcontentService.class);
	IBlogmoveService blogmoveService = (IBlogmoveService) SpringUtils.getBean(IBlogmoveService.class);
	IBlogsetService blogsetService = (IBlogsetService) SpringUtils.getBean(IBlogsetService.class);
	public int num = 0;
	public StringBuilder sbmsg = new StringBuilder();

	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 * @date Oct 17, 2018 12:11:53 PM
	 * @Desc 博客搬家启动方法
	 */
	public boolean blogMoveController(Blogmove blogMove) {
		String oneUrl;

		// 校验参数
		if (blogMove == null) {
			return false;
		}

		// 设置信息如水印信息
		Blogset blogset = blogsetService.selectBlogsetWaterMarkMsgById(1);
		blogMove.setBlogset(blogset);

		if (blogMove.getMoveMode() == 0 || blogMove.getMoveMode() == 1) {
			blogMoveArticleController(blogMove);

		} else if (blogMove.getMoveMode() == 2 || blogMove.getMoveMode() == 3) {
			// 读取word,pdf
			blogMoveOfficeController(blogMove);

		} else {

		}
		return false;
	}

	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 * @throws InterruptedException
	 * @date Oct 17, 2018 12:11:53 PM
	 * @Desc CSDN博客搬家启动方法
	 */
	public boolean blogMoveArticleController(Blogmove blogMove) {
		String loginName = ShiroUtils.getLoginName();
		String statusMsg;
		try {
			String oneUrl = BlogMoveCommonUtils.getBlogMoveArticleListUrl(blogMove);

			// 校验参数
			if (blogMove == null) {
				return false;
			}
			// 首先获取文章列表url list
			List<String> urlList = new ArrayList<String>();
			int pageNum = BlogMoveCommonUtils.getBlogMoveArticlePageNum(blogMove);
			String webName = blogMove.getMoveWebsiteId();
			statusMsg = "-->> 正在获取" + webName + "文章列表URL...";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(blogMove.getMoveMessage() + CommonSymbolicConstant.LINEBREAK + statusMsg);

			if (blogMove.getMoveMode() == 0) {
				String max_behot_time = "0";
				for (int i = 1; i < pageNum + 1; i++) {
					if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.getCSDNArticleUrlList(blogMove, oneUrl + i, urlList);
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CNBLOG.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.getCnBlogArticleUrlList(blogMove, oneUrl + i, urlList);
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_TOUTIAO.equals(blogMove.getMoveWebsiteId())) {
						// 今日头条比较难爬取
						// 首先去获取三个参数as，cp，_signature这三个参数很重要，爬取首页则无所谓
						oneUrl = BlogMoveTouTiaoUtils.getTouTiaoListUrl(blogMove, i, max_behot_time);
						max_behot_time = blogMoveArticleService.getTouTiaoArticleUrlList(blogMove, oneUrl, urlList);
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_JIANSHU.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.getJianShuArticleUrlList(blogMove, oneUrl + i, urlList);
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_OSCHINA.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.getOsChinaArticleUrlList(blogMove, String.format(oneUrl, i), urlList);
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_WECHAT_PUBLIC_ACCOUNT
							.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.getWeChatPublicAccountArticleUrlList(blogMove, oneUrl, urlList);
						// 只有一页只有10条最多
						break;
					} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_51CTO.equals(blogMove.getMoveWebsiteId())) {
						blogMoveArticleService.get51CTOArticleUrlList(blogMove, oneUrl + i, urlList);
					}

				}
			} else if (blogMove.getMoveMode() == 1) {
				urlList.clear();
				urlList.add(blogMove.getMoveWebsiteUrl());
			}

			statusMsg = "-->> " + webName + "文章列表URL获取完成...";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			// 根据url获取所有文章并插入数据库
			if (urlList == null || urlList.size() < 1) {
				statusMsg = "-->> 未发现" + webName + "文章...";
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
						new TextMessage(statusMsg));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
				statusMsg = "over";
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
						new TextMessage(statusMsg));
				blogMove.setMoveMessage(sbmsg.toString());
				blogMove.setMoveSuccess(0);
				blogMove.setMoveStopMode(0);
				blogMove.setMoveSuccessNum(num);
				blogmoveService.updateBlogmove(blogMove);
				// 从map中去除线程
				MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadMap, loginName);
				MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadBMCMap, loginName);
				return true;
			}
			// 获取原有的文章
			List<Blogcontent> bList = null;
			if (blogMove.getMoveRemoveRepeat() == 0) {
				bList = blogcontentService.selectBlogcontentListWithoutContent(null);
			}

			// 开始爬取
			Blogcontent blogcontent = new Blogcontent();

			for (String url : urlList) {
				if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getCSDNArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CNBLOG.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getCnBlogArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_TOUTIAO.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getTouTiaoArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_JIANSHU.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getJianShuArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_OSCHINA.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getOsChinaArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_WECHAT_PUBLIC_ACCOUNT
						.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getWeChatPublicAccountArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_SCRIPT_HOUSE.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getScriptHouseArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_51CTO.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.get51CTOArticleMsg(blogMove, url, bList, sbmsg);
				} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_ALIYQ.equals(blogMove.getMoveWebsiteId())) {
					blogcontent = blogMoveArticleService.getALIYQArticleMsg(blogMove, url, bList, sbmsg);
				}

				// 插入数据库
				if (blogcontent != null) {
					statusMsg = String.format(
							"-->> 正在爬取" + webName
									+ "文章 -- <a style=\"color:blue;\" href=\"%s\" target=\"_blank\">%s</a> -- %s -- %s",
							url, blogcontent.getTitle(),
							DateUtils.format(blogcontent.getGtmCreate(), DateUtils.YYYY_MM_DD_HH_MM_SS),
							blogcontent.getAuthor());
					WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
							new TextMessage(statusMsg));
					sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);

					blogcontentService.insertBlogcontent(blogcontent);
					statusMsg = String.format(
							"-->> 正在存入数据库 -- <a style=\"color:blue;\" href=\"%s\" target=\"_blank\">%s</a> -- %s -- %s",
							url, blogcontent.getTitle(),
							DateUtils.format(blogcontent.getGtmCreate(), DateUtils.YYYY_MM_DD_HH_MM_SS),
							blogcontent.getAuthor());
					WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
							new TextMessage(statusMsg));
					sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
					num++;
				}
				// 延迟一秒
				Thread.sleep(1000);
			}
			statusMsg = "博客搬家完成";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			blogMove.setMoveSuccess(0);
			blogMove.setMoveStopMode(0);
			blogMove.setMoveSuccessNum(num);
		} catch (Exception e) {
			// 获取具体的栈打印信息
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			statusMsg = "爬取文章出错,报错信息：\n\r" + sw.toString();
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			blogMove.setMoveSuccess(1);
			blogMove.setMoveStopMode(0);
			blogMove.setMoveSuccessNum(num);
		}
		statusMsg = "over";
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
				new TextMessage(statusMsg));
		blogMove.setMoveMessage(blogMove.getMoveMessage() + CommonSymbolicConstant.LINEBREAK + sbmsg.toString());
		// 最后操作
		blogmoveService.updateBlogmove(blogMove);
		// 从map中去除线程
		MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadMap, loginName);
		MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadBMCMap, loginName);
		return true;
	}

	/**
	 * @date Oct 29, 2018 2:34:48 PM
	 * @Desc 读取word
	 * @param blogMove
	 */
	private boolean blogMoveOfficeController(Blogmove blogMove) {
		String loginName = ShiroUtils.getLoginName();
		String statusMsg;
		String modeName = CommonSymbolicConstant.EMPTY_STRING;
		if (blogMove.getMoveMode() == 2) {
			modeName = BlogConstant.BLOG_BLOGMOVE_MODE_NAME_WORD;
		} else if (blogMove.getMoveMode() == 3) {
			modeName = BlogConstant.BLOG_BLOGMOVE_MODE_NAME_PDF;
		}
		// 校验参数
		if (blogMove == null) {
			return false;
		}
		statusMsg = "-->> 正在读取" + modeName + "文档...";
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
				new TextMessage(statusMsg));
		sbmsg.append(blogMove.getMoveMessage() + CommonSymbolicConstant.LINEBREAK + statusMsg);

		if (blogMove.getMoveFileNames() == null
				|| CommonSymbolicConstant.EMPTY_STRING.equals(blogMove.getMoveFileNames())
				|| blogMove.getMoveFileNames().split(CommonSymbolicConstant.COMMA).length < 1) {
			statusMsg = "-->> 未发现" + modeName + "文档...";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			statusMsg = "over";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			blogMove.setMoveMessage(sbmsg.toString());
			blogMove.setMoveSuccess(0);
			blogMove.setMoveStopMode(0);
			blogMove.setMoveSuccessNum(num);
			blogmoveService.updateBlogmove(blogMove);
			// 从map中去除线程
			MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadMap, loginName);
			MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadBMCMap, loginName);
			return true;
		}

		// 获取原有的文章
		List<Blogcontent> bList = null;
		if (blogMove.getMoveRemoveRepeat() == 0) {
			bList = blogcontentService.selectBlogcontentListWithoutContent(null);
		}
		try {
			String[] fileNames = blogMove.getMoveFileNames().split(CommonSymbolicConstant.COMMA);
			String[] fileONames = blogMove.getMoveFileONames().split(CommonSymbolicConstant.COMMA);
			Blogcontent blogcontent = null;
			for (int i = 0; i < fileNames.length; i++) {
				// 读取word并返回html字符串相关信息
				if (blogMove.getMoveMode() == 2) {
					blogcontent = BlogMoveOfficeArticleService.getWordArticleMsg(blogMove, fileNames[i], fileONames[i],
							bList, sbmsg);
				} else if (blogMove.getMoveMode() == 3) {
					blogcontent = BlogMoveOfficeArticleService.getPdfArticleMsg(blogMove, fileNames[i], fileONames[i],
							bList, sbmsg);
				}

				// 插入数据库
				if (blogcontent != null) {
					statusMsg = String.format("-->> 正在读取" + modeName + "文档  --" + fileONames[i] + " ");
					WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
							new TextMessage(statusMsg));
					sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
					blogcontentService.insertBlogcontent(blogcontent);
					statusMsg = String.format("-->> 正在存入数据库  --" + fileONames[i] + " ");
					WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
							new TextMessage(statusMsg));
					sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
					num++;
				}
				// 延迟一秒
				Thread.sleep(1000);
			}
			statusMsg = "博客搬家完成";
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			blogMove.setMoveSuccess(0);
			blogMove.setMoveStopMode(0);
			blogMove.setMoveSuccessNum(num);
		} catch (Exception e) {
			// 获取具体的栈打印信息
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			statusMsg = "读取" + modeName + "出错,报错信息：" + CommonSymbolicConstant.LINEBREAK + sw.toString();
			WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
					new TextMessage(statusMsg));
			sbmsg.append(CommonSymbolicConstant.LINEBREAK + statusMsg);
			blogMove.setMoveSuccess(1);
			blogMove.setMoveStopMode(0);
			blogMove.setMoveSuccessNum(num);
		}
		statusMsg = "over";
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID, loginName,
				new TextMessage(statusMsg));
		blogMove.setMoveMessage(blogMove.getMoveMessage() + CommonSymbolicConstant.LINEBREAK + sbmsg.toString());
		// 最后操作
		blogmoveService.updateBlogmove(blogMove);
		// 从map中去除线程
		MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadMap, loginName);
		MapUtils.removeObjectFromMap(BlogmoveServiceImpl.blogMoveThreadBMCMap, loginName);
		return true;

	}

	public int getNum() {
		return num;
	}

	public String getSbmsg() {
		return sbmsg.toString();
	}

}
