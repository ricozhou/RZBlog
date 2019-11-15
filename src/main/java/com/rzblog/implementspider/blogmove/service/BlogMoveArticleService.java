package com.rzblog.implementspider.blogmove.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.WebSocketConstants;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.DateUtils;
import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.framework.websocket.service.WebSocketPushHandler;
import com.rzblog.implementspider.blogmove.utils.BlogMove51CTOUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveALIYQUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveCSDNUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveCnBlogUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveCommonUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveJianShuUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveOsChinaUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveScriptHouseUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveTouTiaoUtils;
import com.rzblog.implementspider.blogmove.utils.BlogMoveWeChatPublicAccountUtils;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;

/**
 * @author ricozhou
 * @date Oct 17, 2018 12:28:21 PM
 * @Desc
 */
public class BlogMoveArticleService {

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void getCSDNArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(oneUrl);
		// System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		Element pageMsg22 = doc.select("div.article-list").first();
		if (pageMsg22 == null) {
			return;
		}
		Elements pageMsg = pageMsg22.select("div.article-item-box");
		Element linkNode;
		for (Element e : pageMsg) {
			linkNode = e.select("h4 a").first();
			// 不知为何，所有的bloglist第一条都是这个：https://blog.csdn.net/yoyo_liyy/article/details/82762601
			if (linkNode.attr("href").contains(blogMove.getMoveUserId())) {
				if (urlList.size() < blogMove.getMoveNum()) {
					urlList.add(linkNode.attr("href"));
				} else {
					break;
				}
			}
		}
		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getCSDNArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.EDGE);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

		Map<String, String> additionalHeaders = new HashMap<String, String>();
		String cookie = blogMove.getMoveCookie();
		if (cookie != null && !"".equals(cookie)) {
			additionalHeaders.put("cookie", cookie);
		}

		System.out.println("---------------cookie:" + cookie);
		WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
		request.setAdditionalHeaders(additionalHeaders);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 正在获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 正在获取文章内容"));
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(request);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));

		Document doc = Jsoup.parse(page.asXml());
		// 获取标题
		String title = BlogMoveCSDNUtils.getCSDNArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		String author = BlogMoveCSDNUtils.getCSDNArticleAuthor(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章作者 -- %s ", author)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", author));
		blogcontent.setAuthor(author);
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveCSDNUtils.getCSDNArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));

		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveCSDNUtils.getCSDNArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveCSDNUtils.getCSDNArticleContent(doc, blogMove, blogcontent));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void getJianShuArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(oneUrl);
		// System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		Element pageMsg22 = doc.select("ul.note-list").first();
		if (pageMsg22 == null) {
			return;
		}
		Elements pageMsg = pageMsg22.select("div.content");
		Element linkNode;
		for (Element e : pageMsg) {
			linkNode = e.select("a.title").first();
			if (linkNode == null) {
				continue;
			}
			if (urlList.size() < blogMove.getMoveNum()) {
				urlList.add(BlogConstant.BLOG_BLOGMOVE_WEBSITE_BASEURL_JIANSHU + linkNode.attr("href"));
			} else {
				break;
			}
		}
		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getJianShuArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		Document doc = Jsoup.parse(page.asXml());
		// 获取标题
		String title = BlogMoveJianShuUtils.getJianShuArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMoveJianShuUtils.getJianShuArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveJianShuUtils.getJianShuArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveJianShuUtils.getJianShuArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveJianShuUtils.getJianShuArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void getOsChinaArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(oneUrl);
		// System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		Element pageMsg22 = doc.select("div.list-container.space-list-container").first();
		if (pageMsg22 == null) {
			return;
		}
		Elements pageMsg = pageMsg22.select("div.content");
		Element linkNode;
		for (Element e : pageMsg) {
			linkNode = e.select("a.header").first();
			if (linkNode == null) {
				continue;
			}
			if (urlList.size() < blogMove.getMoveNum()) {
				urlList.add(linkNode.attr("href"));
			} else {
				break;
			}
		}
		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getOsChinaArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		Document doc = Jsoup.parse(page.asXml());
		// 获取标题
		String title = BlogMoveOsChinaUtils.getOsChinaArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMoveOsChinaUtils.getOsChinaArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveOsChinaUtils.getOsChinaArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveOsChinaUtils.getOsChinaArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveOsChinaUtils.getOsChinaArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void getCnBlogArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(oneUrl);
		// System.out.println(page.asXml());
		Document doc = Jsoup.parse(page.asXml());
		Elements pageMsg = doc.select("div.postTitle");
		Element linkNode;
		for (Element e : pageMsg) {
			linkNode = e.select("a.postTitle2").first();
			if (linkNode == null) {
				continue;
			}
			if (urlList.size() < blogMove.getMoveNum()) {
				urlList.add(linkNode.attr("href"));
			} else {
				break;
			}
		}
		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getCnBlogArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		Document doc = Jsoup.parse(page.asXml());
		// 获取标题
		String title = BlogMoveCnBlogUtils.getCnBlogArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMoveCnBlogUtils.getCnBlogArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveCnBlogUtils.getCnBlogArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveCnBlogUtils.getCnBlogArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveCnBlogUtils.getCnBlogArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public String getTouTiaoArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String max_behot_time = "0";
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

		Map<String, String> additionalHeaders = new HashMap<String, String>();
		additionalHeaders.put("user-agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
		WebRequest request = new WebRequest(new URL(oneUrl), HttpMethod.GET);
		request.setAdditionalHeaders(additionalHeaders);
		UnexpectedPage page2 = webClient.getPage(request);
		String result = IOUtils.toString(page2.getInputStream(), StandardCharsets.UTF_8);

		JSONObject json = JSON.parseObject(result);
		JSONArray jsonarray = (JSONArray) json.get("data");
		JSONObject json2 = (JSONObject) json.get("next");
		// 用于返回留下一页使用
		max_behot_time = json2.getString("max_behot_time");

		// 读取url
		JSONObject jsonObject;
		String url;
		for (Object obj : jsonarray) {
			jsonObject = (JSONObject) obj;
			url = jsonObject.getString("source_url");
			if (url != null) {
				url = url.substring(6, url.length() - 1);
				if (urlList.size() < blogMove.getMoveNum()) {
					urlList.add("https://www.toutiao.com/i" + url);
				} else {
					break;
				}
			} else {
				url = "";
				continue;
			}
		}

		return max_behot_time;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getTouTiaoArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 模拟浏览器操作
		// 创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 关闭css代码功能
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		// 如若有可能找不到文件js则加上这句代码
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// 获取第一级网页html
		HtmlPage page = webClient.getPage(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		Document doc = Jsoup.parse(page.asXml());
		Elements pageMsg22 = doc.body().getElementsByTag("script");

		String msgJsonString = BlogMoveTouTiaoUtils.getTouTiaoArticleMsgJsonString(pageMsg22);

		String content = null;
		String title = null;
		String author = null;
		Date date = null;
		if (msgJsonString != null && !"".equals(msgJsonString)) {
			System.out.println(msgJsonString);
			// 解析
			JSONObject json = JSON.parseObject(msgJsonString);
			json = (JSONObject) json.get("articleInfo");
			content = json.getString("content");
			// 转义
			content = StringEscapeUtils.unescapeHtml3(content);
			title = json.getString("title");
			json = (JSONObject) json.get("subInfo");
			author = json.getString("source");
			date = DateUtils.formatStringDate(json.getString("time"), DateUtils.YYYY_MM_DD_HH_MM_SS);
			date = date == null ? new Date() : date;
		}

		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章标题 -- %s ", blogcontent.getTitle())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", blogcontent.getTitle()));
		// 获取作者
		blogcontent.setAuthor(author);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(date);
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType("原创");
		blogcontent.setType(BlogMoveTouTiaoUtils.getTouTiaoArticleType(msgJsonString, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveTouTiaoUtils.getTouTiaoArticleContent(content, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void getWeChatPublicAccountArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList)
			throws IOException {
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(oneUrl);
		// 截取前10条推送信息的json中的list数据
		String jsonList = doc.html().split("var msgList = ")[1].split("seajs.use")[0].trim();
		jsonList = jsonList.substring(0, jsonList.length() - 1);
		// 转json
		JSONObject json = JSON.parseObject(jsonList);
		JSONArray jsonarray = (JSONArray) json.get("list");
		if (jsonarray.size() < 1) {
			return;
		}
		// 读取url
		JSONObject jsonObject;
		String url;
		for (Object obj : jsonarray) {
			jsonObject = (JSONObject) obj;
			url = jsonObject.getJSONObject("app_msg_ext_info").getString("content_url").replaceAll("amp;", "");
			if (url != null && url.startsWith("/s")) {
				if (urlList.size() < blogMove.getMoveNum()) {
					urlList.add("http://mp.weixin.qq.com" + url);
				} else {
					break;
				}
			} else {
				url = "";
				continue;
			}
		}

		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getWeChatPublicAccountArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList,
			StringBuilder sbmsg) throws IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 使用jsoup直接请求
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		// 获取标题
		String title = BlogMoveWeChatPublicAccountUtils.getWeChatPublicAccountArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取公众号，不再获取单独文章的作者
		blogcontent.setAuthor(BlogMoveWeChatPublicAccountUtils.getWeChatPublicAccountArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveWeChatPublicAccountUtils.getWeChatPublicAccountArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(
				BlogMoveWeChatPublicAccountUtils.getWeChatPublicAccountArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(
				BlogMoveWeChatPublicAccountUtils.getWeChatPublicAccountArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getScriptHouseArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList,
			StringBuilder sbmsg) throws IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 使用jsoup直接请求
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		// 获取标题
		String title = BlogMoveScriptHouseUtils.getScriptHouseArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMoveScriptHouseUtils.getScriptHouseArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveScriptHouseUtils.getScriptHouseArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveScriptHouseUtils.getScriptHouseArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveScriptHouseUtils.getScriptHouseArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:30:46 PM
	 * @Desc
	 * @param blogMove
	 * @param oneUrl
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public void get51CTOArticleUrlList(Blogmove blogMove, String oneUrl, List<String> urlList) throws IOException {
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(oneUrl);
		Elements es = doc.select(".tit");
		String url;
		if (!es.isEmpty()) {
			for (Element e : es) {
				url = e.attr("href").trim();
				if (url != null) {
					if (urlList.size() < blogMove.getMoveNum()) {
						urlList.add(url);
					} else {
						break;
					}
				} else {
					url = "";
					continue;
				}
			}
		}

		return;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent get51CTOArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 使用jsoup直接请求
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		// 获取标题
		String title = BlogMove51CTOUtils.get51CTOArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMove51CTOUtils.get51CTOArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMove51CTOUtils.get51CTOArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMove51CTOUtils.get51CTOArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMove51CTOUtils.get51CTOArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}

	/**
	 * @date Oct 17, 2018 12:46:52 PM
	 * @Desc 获取详细信息
	 * @param blogMove
	 * @param url
	 * @param sbmsg
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws FailingHttpStatusCodeException
	 */
	public Blogcontent getALIYQArticleMsg(Blogmove blogMove, String url, List<Blogcontent> bList, StringBuilder sbmsg)
			throws IOException {
		Blogcontent blogcontent = new Blogcontent();
		blogcontent.setArticleSource(blogMove.getMoveWebsiteId());
		// 使用jsoup直接请求
		Document doc = BlogMoveCommonUtils.getDocumentByUrlJsoup(url);

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章内容")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章内容"));
		// 获取标题
		String title = BlogMoveALIYQUtils.getALIYQArticleTitle(doc);
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章标题 -- %s ", title)));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章标题 -- %s ", title));
		// 是否重复去掉
		if (blogMove.getMoveRemoveRepeat() == 0) {
			// 判断是否重复
			if (BlogMoveCommonUtils.articleRepeat(bList, title)) {
				WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
						ShiroUtils.getLoginName(), new TextMessage(
								String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title)));
				sbmsg.append(CommonSymbolicConstant.LINEBREAK
						+ String.format("-->> 过滤掉文章 -- <a href=\"%s\" target=\"_blank\">%s</a> ", url, title));
				return null;
			}
		}
		blogcontent.setTitle(title);
		// 获取作者
		blogcontent.setAuthor(BlogMoveALIYQUtils.getALIYQArticleAuthor(doc));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章作者 -- %s ", blogcontent.getAuthor()));
		// 获取时间
		if (blogMove.getMoveUseOriginalTime() == 0) {
			blogcontent.setGtmCreate(BlogMoveALIYQUtils.getALIYQArticleTime(doc));
		} else {
			blogcontent.setGtmCreate(new Date());
		}
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate()))));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK
				+ String.format("-->> 已获取文章发布时间 -- %s ", DateUtils.format(blogcontent.getGtmCreate())));
		blogcontent.setGtmModified(new Date());
		// 获取类型
		blogcontent.setType(BlogMoveALIYQUtils.getALIYQArticleType(doc, blogMove.getMoveArticleType()));
		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(),
				new TextMessage(String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType())));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章类型 -- %s ", blogcontent.getType()));
		// 获取正文
		blogcontent.setContent(BlogMoveALIYQUtils.getALIYQArticleContent(doc, blogMove, blogcontent));

		WebSocketPushHandler.sendMessageToUser(WebSocketConstants.WEBSOCKET_PARAMS_BLOGMOVEID,
				ShiroUtils.getLoginName(), new TextMessage(String.format("-->> 已获取文章正文 ")));
		sbmsg.append(CommonSymbolicConstant.LINEBREAK + String.format("-->> 已获取文章正文  "));
		// 设置其他
		blogcontent.setStatus(blogMove.getMoveBlogStatus());
		blogcontent.setBlogColumnName(blogMove.getMoveColumn());
		// 特殊处理
		blogcontent.setArticleEditor(blogMove.getMoveArticleEditor());
		blogcontent.setShowId(DateUtils.format(new Date(), DateUtils.YYYYMMDDHHMMSSSSS));
		blogcontent.setAllowComment(0);
		blogcontent.setAllowPing(0);
		blogcontent.setBlogType(0);
		blogcontent.setAllowDownload(0);
		blogcontent.setShowIntroduction(1);
		blogcontent.setIntroduction("");
		blogcontent.setPrivateArticle(1);
		blogcontent.setArticleTop(1);

		return blogcontent;
	}
}
