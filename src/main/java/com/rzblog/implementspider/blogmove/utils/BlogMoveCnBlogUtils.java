package com.rzblog.implementspider.blogmove.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.rzblog.common.utils.DateUtils;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.common.file.utilt.FileUtils;

/**
 * @author ricozhou
 * @date Oct 17, 2018 12:51:52 PM
 * @Desc
 */
public class BlogMoveCnBlogUtils {

	/**
	 * @date Oct 17, 2018 1:10:19 PM
	 * @Desc 获取标题
	 * @param doc
	 * @return
	 */
	public static String getCnBlogArticleTitle(Document doc) {
		// 标题
		Element pageMsg2 = doc.select("div#post_detail #cb_post_title_url").first();
		return pageMsg2.ownText().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:28 PM
	 * @Desc 获取作者
	 * @param doc
	 * @return
	 */
	public static String getCnBlogArticleAuthor(Document doc) {
		Element pageMsg2 = doc.select("p.postfoot a").first();
		if (pageMsg2 == null) {
			pageMsg2 = doc.select("div.postDesc a").first();
		}
		return pageMsg2.ownText().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:33 PM
	 * @Desc 获取时间
	 * @param doc
	 * @return
	 */
	public static Date getCnBlogArticleTime(Document doc) {
		Element pageMsg2 = doc.select("#post-date").first();
		String date = pageMsg2.ownText().trim();
		// 这地方时间格式变化太多暂时不实现
		Date d = DateUtils.formatStringDate(date, DateUtils.YYYY_MM_DD_HH_MM_SS4);
		// 注意有些格式不正确
		return d == null ? new Date() : d;
	}

	/**
	 * @date Oct 17, 2018 1:10:37 PM
	 * @Desc 获取类型
	 * @param doc
	 * @param moveArticleType
	 * @return
	 */
	public static String getCnBlogArticleType(Document doc, String moveArticleType) {
		if ("默认".equals(moveArticleType)) {
			// Element pageMsg2 =
			// doc.select("div.article-title-box").first().select("span.article-type").first();
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
	public static String getCnBlogArticleContent(Document doc, Blogmove blogMove, Blogcontent blogcontent)
			throws IOException {
		Element pageMsg2 = doc.select("div#cnblogs_post_body").first();
		String content = pageMsg2.toString();
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
			List<String> imgList = BlogMoveCommonUtils.getArticleImgList(content);
			// 下载并返回重新生成的imgurllist
			List<String> newImgList = BlogMoveCommonUtils.getArticleNewImgList(blogMove, imgList, blogFileName);
			// 拼接文章所有链接
			images = BlogMoveCommonUtils.getArticleImages(newImgList);
			blogcontent.setImages(images);
			// 替换所有链接按顺序
			content = getCnBlogNewArticleContent(content, imgList, newImgList);

		}

		return content;
	}

	/**
	 * @date Oct 22, 2018 3:31:40 PM
	 * @Desc
	 * @param content
	 * @param imgList
	 * @param newImgList
	 * @return
	 */
	private static String getCnBlogNewArticleContent(String content, List<String> imgList, List<String> newImgList) {
		Document doc = Jsoup.parse(content);
		Elements imgTags = doc.select("img[src]");
		if (imgList == null || imgList.size() < 1 || newImgList == null || newImgList.size() < 1 || imgTags == null
				|| "".equals(imgTags)) {
			return content;
		}
		for (int i = 0; i < imgTags.size(); i++) {
			imgTags.get(i).attr("src", newImgList.get(i));
		}
		return doc.body().toString();
	}

}
