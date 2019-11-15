package com.rzblog.implementspider.blogmove.utils;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.FileExtensionConstant;
import com.rzblog.common.constant.FileOtherConstant;
import com.rzblog.common.constant.OtherConstant;
import com.rzblog.common.constant.project.BlogConstant;
import com.rzblog.common.utils.ImageUtils;
import com.rzblog.framework.config.FilePathConfig;
import com.rzblog.project.blog.blogcontent.domain.Blogcontent;
import com.rzblog.project.blog.blogcontent.domain.Blogmove;
import com.rzblog.project.common.image.utils.WaterMarkUtils;

/**
 * @author ricozhou
 * @date Oct 29, 2018 5:12:25 PM
 * @Desc
 */
public class BlogMoveCommonUtils {
	/**
	 * @date Oct 17, 2018 1:52:39 PM
	 * @Desc 判断是否重复
	 * @param title
	 * @return
	 */
	public static boolean articleRepeat(List<Blogcontent> bList, String title) {
		if (bList == null || bList.size() < 1) {
			return false;
		}
		for (Blogcontent bc : bList) {
			if (bc.getTitle().equals(title)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @date Oct 17, 2018 1:10:41 PM
	 * @Desc 获取链接
	 * @param doc
	 * @param object
	 * @param blogcontent
	 * @return
	 */
	public static List<String> getArticleImgList(String content) {
		return getArticleImgList(content, "src");
	}

	/**
	 * @date Oct 17, 2018 1:10:41 PM
	 * @Desc 获取链接
	 * @param doc
	 * @param object
	 * @param blogcontent
	 * @return
	 */
	public static List<String> getArticleImgList(String content, String key) {
		return getArticleImgList(content, key, "");
	}

	/**
	 * @date Oct 17, 2018 1:10:41 PM
	 * @Desc 获取链接
	 * @param doc
	 * @param object
	 * @param blogcontent
	 * @return
	 */
	public static List<String> getArticleImgList(String content, String key, String httpString) {
		if (content == null) {
			return null;
		}
		List<String> imgList = new ArrayList<String>();
		Document doc = Jsoup.parse(content);
		Elements imgTags = doc.select("img[" + key + "],IMG[" + key + "]");
		for (Element element : imgTags) {
			// 如果是以这几个开头的话则不再拼接
			String url = element.attr(key);
			if (url.startsWith("http") || url.startsWith("https")) {
				httpString = "";
			} else if (url.startsWith("//")) {
				httpString = "http:";
			}
			imgList.add(httpString + url);
		}
		return imgList;
	}

	/**
	 * @date Oct 17, 2018 1:10:41 PM
	 * @Desc 获取链接
	 * @param doc
	 * @param object
	 * @param blogcontent
	 * @return
	 */
	public static List<String> getArticleImgList2(String content) {
		// 注意，当爬取简书文章时，图片异步加载，有些并没有完全加载出img，所以只能换方法
		if (content == null) {
			return null;
		}
		List<String> imgList = new ArrayList<String>();
		Document doc = Jsoup.parse(content);
		Elements imgTags = doc.select("img[data-original-src]");
		for (Element element : imgTags) {
			imgList.add("https:" + element.attr("data-original-src"));
		}
		return imgList;
	}

	/**
	 * @date Oct 22, 2018 3:31:37 PM
	 * @Desc
	 * @param newImgList
	 * @return
	 */
	public static String getArticleImages(List<String> newImgList) {
		if (newImgList == null || newImgList.size() < 1) {
			return null;
		}
		String images = null;
		for (int i = 0; i < newImgList.size(); i++) {
			if (i == 0) {
				images = newImgList.get(0);
			} else {
				images += CommonSymbolicConstant.COMMA + newImgList.get(i);
			}
		}

		return images;
	}

	/**
	 * @date Oct 29, 2018 2:47:17 PM
	 * @Desc
	 * @param blogFileName
	 * @param fileName
	 * @param blogMove
	 */
	public static void addImgWaterMark(String blogFileName, String fileName, Blogmove blogMove) {
		// 判读图片后缀

		// 查询总设置是否需要打水印
		String srcImgPath = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName + File.separator
				+ fileName; // 源图片地址
		String tarImgPath = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName + File.separator
				+ fileName; // 待存储的地址
		String waterMarkContent = blogMove.getBlogset().getBasicsetWaterMarkMsg(); // 水印内容
		// 第一个是颜色16进制，第二个是字体，第三个是字体样式，第四个是字体大小,第五个是水印方位
		String[] markSetMsg = blogMove.getBlogset().getBasicsetWaterMarkSetMsg().split(CommonSymbolicConstant.COMMA);
		int[] rgb = ImageUtils.hexToRgb(markSetMsg[0]);
		Color color = new Color(rgb[0], rgb[1], rgb[2], 110);
		Font font = new Font(markSetMsg[1], Integer.valueOf(markSetMsg[2]), Integer.valueOf(markSetMsg[3]));
		WaterMarkUtils.addWaterMark(srcImgPath, tarImgPath, waterMarkContent, color, font,
				Integer.valueOf(markSetMsg[4]));
	}

	// 下载图片
	public static String downloadImg(String urlString, String filename, String savePath, Blogmove blogMove) {
		String imgType = null;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		imgType = FileExtensionConstant.FILE_EXTENSION_IMAGE_PNG;

		if (urlString.startsWith(OtherConstant.OTHER_DATAIMAGE)) {
			// base64
			String path = sf.getPath() + File.separator + filename + CommonSymbolicConstant.POINT + imgType;
			ImageUtils.GenerateImageByBase64(urlString, path);
		} else {

			try {
				// 构造URL
				URL url = new URL(urlString);
				// 打开连接
				URLConnection con = url.openConnection();
				// 设置请求超时为5s
				con.setConnectTimeout(5 * 1000);

				// 设置cookie
				BlogMoveCommonUtils.setBlogMoveDownImgCookie(con, blogMove);

				// 输入流
				InputStream is = con.getInputStream();

				// imgType = ImageUtils.getPicType((BufferedInputStream) is);
				if (urlString.toLowerCase().endsWith(".gif")) {
					imgType = FileExtensionConstant.FILE_EXTENSION_IMAGE_GIF;
				}
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;

				OutputStream os = new FileOutputStream(
						sf.getPath() + File.separator + filename + CommonSymbolicConstant.POINT + imgType);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				// 完毕，关闭所有链接
				os.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return filename + CommonSymbolicConstant.POINT + imgType;
	}

	/**
	 * @date Oct 30, 2018 1:39:11 PM
	 * @Desc 下载图片设置cookie
	 * @param con
	 * @param blogMove
	 */
	public static void setBlogMoveDownImgCookie(URLConnection con, Blogmove blogMove) {
		// 这地方注意当单条获取时正则匹配出url中referer
		if (blogMove.getMoveMode() == 0) {
			// 多条
			if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
				con.setRequestProperty("Referer", blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId());
			}

		} else if (blogMove.getMoveMode() == 1) {
			// 一条
			if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
				con.setRequestProperty("Referer",
						blogMove.getMoveWebsiteUrl().substring(0, blogMove.getMoveWebsiteUrl().indexOf("article")));
			} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_SCRIPT_HOUSE.equals(blogMove.getMoveWebsiteId())) {
				System.out.println(blogMove);
				con.setRequestProperty("Referer", blogMove.getMoveWebsiteUrl());
			}
		}

	}

	/**
	 * @date Oct 30, 2018 1:55:34 PM
	 * @Desc 返回列表url
	 * @param blogMove
	 * @return
	 * @throws IOException
	 */
	public static String getBlogMoveArticleListUrl(Blogmove blogMove) throws IOException {
		String oneUrl = null;
		// 网站url拼接
		if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
			oneUrl = blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId() + "/article/list/";
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CNBLOG.equals(blogMove.getMoveWebsiteId())) {
			oneUrl = blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId() + "/default.html?page=";
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_TOUTIAO.equals(blogMove.getMoveWebsiteId())) {

		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_JIANSHU.equals(blogMove.getMoveWebsiteId())) {
			oneUrl = blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId() + "?order_by=shared_at&page=";
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_OSCHINA.equals(blogMove.getMoveWebsiteId())) {
			oneUrl = blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId()
					+ "/widgets/_space_index_newest_blog?catalogId=0&q=&p=%s&type=ajax";
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_WECHAT_PUBLIC_ACCOUNT.equals(blogMove.getMoveWebsiteId())) {
			// 由于微信公众号无法直接浏览，则通过搜狗微信来获取
			oneUrl = BlogMoveWeChatPublicAccountUtils.getWCPAUrl(blogMove.getMoveUserId());
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_51CTO.equals(blogMove.getMoveWebsiteId())) {
			oneUrl = blogMove.getMoveWebsiteUrl() + blogMove.getMoveUserId() + "/p";
		}
		return oneUrl;
	}

	/**
	 * @date Oct 30, 2018 2:20:44 PM
	 * @Desc 计算pagenum
	 * @param blogMove
	 * @return
	 */
	public static int getBlogMoveArticlePageNum(Blogmove blogMove) {
		int pageNum = 0;
		// 网站url拼接
		if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CSDN.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 20 + 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_CNBLOG.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 10 + 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_TOUTIAO.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 20 + 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_JIANSHU.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 9 + 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_OSCHINA.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 20 + 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_WECHAT_PUBLIC_ACCOUNT.equals(blogMove.getMoveWebsiteId())) {
			// 只能获取十条只有一页
			pageNum = 1;
		} else if (BlogConstant.BLOG_BLOGMOVE_WEBSITE_NAME_51CTO.equals(blogMove.getMoveWebsiteId())) {
			pageNum = (blogMove.getMoveNum() - 1) / 20 + 1;
		}
		return pageNum;
	}

	/**
	 * @date Oct 22, 2018 3:31:33 PM
	 * @Desc
	 * @param imgList
	 * @return
	 * @throws IOException
	 */
	public static List<String> getArticleNewImgList(Blogmove blogMove, List<String> imgList, String blogFileName)
			throws IOException {
		// 下载图片
		if (imgList == null || imgList.size() < 1) {
			return null;
		}
		List<String> newImgList = new ArrayList<String>();
		String uuid;
		String filePath = FilePathConfig.getUploadBlogPath() + File.separator + blogFileName;
		String fileName;
		for (String url : imgList) {
			uuid = String.valueOf(UUID.randomUUID());
			fileName = BlogMoveCommonUtils.downloadImg(url, uuid, filePath, blogMove);
			// 打水印
			if (blogMove.getBlogset().getBasicsetAddWaterMark() == 0 && blogMove.getMoveAddWaterMark() == 0
					&& blogMove.getBlogset().getBasicsetWaterMarkMsg() != null
					&& !CommonSymbolicConstant.EMPTY_STRING.equals(blogMove.getBlogset().getBasicsetWaterMarkMsg())) {
				BlogMoveCommonUtils.addImgWaterMark(blogFileName, fileName, blogMove);
			}

			newImgList.add(FileOtherConstant.FILE_JUMP_PATH_PREFIX3 + blogFileName + File.separator + fileName);
		}

		return newImgList;
	}

	/**
	 * 使用jsoup获取页面
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Document getDocumentByUrlJsoup(String url) throws IOException {
		Document document = null;
		document = Jsoup.connect(url).get();
		return document;
	}

	/**
	 * @date Oct 22, 2018 3:31:40 PM
	 * @Desc
	 * @param content
	 * @param imgList
	 * @param newImgList
	 * @return
	 */
	public static String getNewArticleContent(String content, List<String> imgList, List<String> newImgList,
			String key) {
		Document doc = Jsoup.parse(content);
		Elements imgTags = doc.select("img[" + key + "]");
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
