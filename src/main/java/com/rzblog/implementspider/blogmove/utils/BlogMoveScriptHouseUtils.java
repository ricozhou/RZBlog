package com.rzblog.implementspider.blogmove.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.RegularExpressionConstant;
import com.rzblog.common.utils.DateUtils;
import com.rzblog.common.utils.StringUtils;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.common.file.utilt.FileUtils;

/**
 * @author ricozhou
 * @date Oct 17, 2018 12:51:52 PM
 * @Desc
 */
public class BlogMoveScriptHouseUtils {

	/**
	 * @date Oct 17, 2018 1:10:19 PM
	 * @Desc 获取标题
	 * @param doc
	 * @return
	 */
	public static String getScriptHouseArticleTitle(Document doc) {
		// 标题
		Element pageMsg2 = doc.select("#article .title").first();
		System.out.println(pageMsg2.ownText().trim());
		return pageMsg2.ownText().trim();
	}

	/**
	 * @date Oct 17, 2018 1:10:28 PM
	 * @Desc 获取作者
	 * @param doc
	 * @return
	 */
	public static String getScriptHouseArticleAuthor(Document doc) {
		Element pageMsg2 = doc.select("#article .info").first();
		String s = pageMsg2.ownText().trim();
		System.out.println(s);
		String k = s.contains("作者") ? "作者" : "投稿";
		s = s.substring(s.indexOf(k) + 3).trim();
		s = CommonSymbolicConstant.EMPTY_STRING.equals(s.trim()) ? "未知" : s.trim();
		return s;
	}

	/**
	 * @date Oct 17, 2018 1:10:33 PM
	 * @Desc 获取时间
	 * @param doc
	 * @return
	 */
	public static Date getScriptHouseArticleTime(Document doc) {
		Element pageMsg2 = doc.select("#article .info").first();
		String s = pageMsg2.ownText().trim();
		List<String> result = StringUtils.GetRegResultToList(RegularExpressionConstant.REGULAR_EXPRESSION_DATETIME_2,
				s);
		if (result != null && result.size() > 0) {
			return DateUtils.formatStringDate(result.get(0), DateUtils.YYYY_MM_DD_HH_MM_SS5);
		} else {
			result = StringUtils.GetRegResultToList(RegularExpressionConstant.REGULAR_EXPRESSION_DATETIME_1, s);
			if (result != null && result.size() > 0) {
				return DateUtils.formatStringDate(result.get(0), DateUtils.YYYY_MM_DD_HH_MM_SS);
			}

		}
		return new Date();
	}

	/**
	 * @date Oct 17, 2018 1:10:37 PM
	 * @Desc 获取类型
	 * @param doc
	 * @param moveArticleType
	 * @return
	 */
	public static String getScriptHouseArticleType(Document doc, String moveArticleType) {
		if ("默认".equals(moveArticleType)) {
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
	public static String getScriptHouseArticleContent(Document doc, Blogmove blogMove, Blogcontent blogcontent)
			throws IOException {
		String content = doc.select("#content").html();
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
			List<String> imgList = BlogMoveCommonUtils.getArticleImgList(content, "src", "https:");
			System.out.println(imgList);
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
