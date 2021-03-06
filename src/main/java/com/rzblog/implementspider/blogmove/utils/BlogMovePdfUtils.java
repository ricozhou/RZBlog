package com.rzblog.implementspider.blogmove.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.rzblog.common.constant.FileOtherConstant;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.common.file.utilt.FileUtils;

/**
 * @author ricozhou
 * @date Oct 29, 2018 5:22:42 PM
 * @Desc
 */
public class BlogMovePdfUtils {

	/**
	 * @date Oct 29, 2018 5:22:53 PM
	 * @Desc
	 * @param fileName
	 * @param blogMove
	 * @param blogcontent
	 * @return
	 * @throws SAXException
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static String getPdfArticleContent(String fileName, Blogmove blogMove, Blogcontent blogcontent)
			throws IOException, ParserConfigurationException, TransformerException, SAXException {
		// 注意是否需要替换图片
		// 保存图片到本地
		// 先获取所有图片连接，再按照每个链接下载图片，最后替换原有链接
		// 先创建一个文件夹
		String blogFileName = String.valueOf(UUID.randomUUID());
		FileUtils.createFolder(FilePathConfig.getUploadBlogPath() + File.separator + blogFileName);
		blogcontent.setBlogFileName(blogFileName);
		// 核心方法读取pdf并缓存到此文件夹
		String content = BlogMovePdfToHtml.pdfToHtml(FilePathConfig.getUploadCachePath() + File.separator + fileName,
				FilePathConfig.getUploadBlogPath() + File.separator + blogFileName,
				FilePathConfig.getUploadBlogPath() + File.separator + blogFileName,
				FileOtherConstant.FILE_JUMP_PATH_PREFIX3 + blogFileName);
		// 由于此方法转的pdf都是成图片，所以暂时不要将其图片显示出来

		// // 匹配出所有链接
		// List<String> imgList =
		// BlogMoveCommonUtils.getArticleImgList(content);
		// // 拼接文章所有链接
		// String images = BlogMoveCommonUtils.getArticleImages(imgList);
		// blogcontent.setImages(images);
		return content;
	}

	/**
	 * @date Oct 17, 2018 1:10:37 PM
	 * @Desc 获取类型
	 * @param doc
	 * @return
	 */
	public static String getPdfArticleType(String moveArticleType) {
		if ("默认".equals(moveArticleType)) {
			return "原创";
		}

		return moveArticleType;
	}

}
