package com.rzblog.implementspider.blogmove.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jsoup.nodes.Document;

import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.common.file.utilt.FileUtils;

/**
 * @author ricozhou
 * @date Oct 17, 2018 12:51:52 PM
 * @Desc
 */
public class BlogMoveWeChatPublicAccountUtils {

	/**
	 * @date Oct 17, 2018 1:10:19 PM
	 * @Desc 通过微信公众号获取微信公众号搜狗url临时连接
	 * @param doc
	 * @return
	 * @throws IOException
	 */
	public static String getWCPAUrl(String wcpa) throws IOException {
		// 搜狗微信地址
		String searchUrl = "http://weixin.sogou.com/weixin?type=1&ie=utf8&query=" + wcpa;
		Document document = BlogMoveCommonUtils.getDocumentByUrlJsoup(searchUrl);
		return document.select(".tit a").attr("href");
	}

	/**
	 * @date Oct 17, 2018 1:10:19 PM
	 * @Desc 获取标题
	 * @param doc
	 * @return
	 */
	public static String getWeChatPublicAccountArticleTitle(Document doc) {
		// 标题
		return doc.select("#activity-name").text().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:28 PM
	 * @Desc 获取作者
	 * @param doc
	 * @return
	 */
	public static String getWeChatPublicAccountArticleAuthor(Document doc) {
		return doc.select("#js_name").text().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:33 PM
	 * @Desc 获取时间
	 * @param doc
	 * @return
	 */
	public static Date getWeChatPublicAccountArticleTime(Document doc) {
		String s = doc.html();
		s = s.substring(s.indexOf("var ct") + 6, s.indexOf("var ct") + 100);
		s = s.substring(s.indexOf("\"") + 1);
		s = s.substring(0, s.indexOf("\""));
		return new Date(Long.valueOf(s) * 1000L);
	}

	/**
	 * @date Oct 17, 2018 1:10:37 PM
	 * @Desc 获取类型
	 * @param doc
	 * @param moveArticleType
	 * @return
	 */
	public static String getWeChatPublicAccountArticleType(Document doc, String moveArticleType) {
		if ("默认".equals(moveArticleType)) {
			// Element pageMsg2 =
			// doc.select("div.article-detail").first().select("h1.header").first()
			// .select("div.horizontal").first();
			// if ("原".equals(pageMsg2.html())) {
			// return "原创";
			// } else if ("转".equals(pageMsg2.html())) {
			// return "转载";
			// } else if ("译".equals(pageMsg2.html())) {
			// return "翻译";
			// }
			return "原创";
		}
		return moveArticleType;
	}

	/**
	 * @date Oct 17, 2018 1:10:41 PM
	 * @Desc 获取正文
	 * @param doc
	 * @param object
	 * @param blogcontent
	 * @return
	 * @throws IOException
	 */
	public static String getWeChatPublicAccountArticleContent(Document doc, Blogmove blogMove, Blogcontent blogcontent)
			throws IOException {
		String content = doc.select("#js_content").html();
		String images;
		// 注意是否需要替换图片
		if (blogMove.getMoveSaveImg() == 0) {
			// 保存图片到本地
			// 先获取所有图片连接，再按照每个链接下载图片，最后替换原有链接
			// 先创建一个文件夹
			// 先创建一个临时文件夹
			String blogFileName = String.valueOf(UUID.randomUUID());
			FileUtils.createFolder(FilePathConfig.getUploadBlogPath() + File.separator + blogFileName);
			blogcontent.setBlogFileName(blogFileName);
			// 匹配出所有链接
			List<String> imgList = BlogMoveCommonUtils.getArticleImgList(content, "data-src");
			// 下载并返回重新生成的imgurllist
			List<String> newImgList = BlogMoveCommonUtils.getArticleNewImgList(blogMove, imgList, blogFileName);
			// 拼接文章所有链接
			images = BlogMoveCommonUtils.getArticleImages(newImgList);
			blogcontent.setImages(images);
			// 替换所有链接按顺序
			content = BlogMoveCommonUtils.getNewArticleContent(content, imgList, newImgList, "data-src");
		}

		return content;
	}

}
