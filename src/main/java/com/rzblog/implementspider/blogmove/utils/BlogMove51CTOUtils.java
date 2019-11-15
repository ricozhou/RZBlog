package com.rzblog.implementspider.blogmove.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
public class BlogMove51CTOUtils {

	/**
	 * @date Oct 17, 2018 1:10:19 PM
	 * @Desc 获取标题
	 * @param doc
	 * @return
	 */
	public static String get51CTOArticleTitle(Document doc) {
		// 标题
		return doc.select(".artical-title").text().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:28 PM
	 * @Desc 获取作者
	 * @param doc
	 * @return
	 */
	public static String get51CTOArticleAuthor(Document doc) {
		String s = doc.select(".name").first().text().trim();
		return s;
	}

	/**
	 * @date Oct 17, 2018 1:10:33 PM
	 * @Desc 获取时间
	 * @param doc
	 * @return
	 */
	public static Date get51CTOArticleTime(Document doc) {
		String date = doc.select(".time").text().trim();
		return date == null ? new Date() : DateUtils.formatStringDate(date, DateUtils.YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * @date Oct 17, 2018 1:10:37 PM
	 * @Desc 获取类型
	 * @param doc
	 * @param moveArticleType
	 * @return
	 */
	public static String get51CTOArticleType(Document doc, String moveArticleType) {
		if ("默认".equals(moveArticleType)) {
			String pageMsg2 = doc.select(".status .tab_name").first().text().trim();
			if ("原创".equals(pageMsg2)) {
				return "原创";
			} else if ("转载".equals(pageMsg2)) {
				return "转载";
			} else if ("翻译".equals(pageMsg2)) {
				return "翻译";
			}
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
	public static String get51CTOArticleContent(Document doc, Blogmove blogMove, Blogcontent blogcontent)
			throws IOException {
		Element e = doc.select(".editor-preview-side").first();
		if (e == null) {
			e = doc.select(".main-content").first();
		}
		String content = e.html();
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
			List<String> imgList = BlogMoveCommonUtils.getArticleImgList(content, "src");
			// 下载并返回重新生成的imgurllist
			List<String> newImgList = BlogMoveCommonUtils.getArticleNewImgList(blogMove, imgList, blogFileName);
			// 拼接文章所有链接
			images = BlogMoveCommonUtils.getArticleImages(newImgList);
			blogcontent.setImages(images);
			// 替换所有链接按顺序
			content = BlogMoveCommonUtils.getNewArticleContent(content, imgList, newImgList, "src");
		}

		return content;
	}

}
