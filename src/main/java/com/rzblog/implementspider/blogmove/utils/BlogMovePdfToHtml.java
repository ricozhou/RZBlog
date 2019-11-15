package com.rzblog.implementspider.blogmove.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.xml.sax.SAXException;

import com.rzblog.common.constant.FileExtensionConstant;

/**
 * @author ricozhou
 * @date Oct 29, 2018 5:27:08 PM
 * @Desc
 */
public class BlogMovePdfToHtml {

	// pdf全路径（包括文件名），博客缓存路径，图片保存路径，html中图片显示路径
	public static String pdfToHtml(String docAllPath, String path, String imagePath, String htmlImgPath)
			throws IOException, ParserConfigurationException, TransformerException, SAXException {
		StringBuffer buffer = new StringBuffer();
		if (docAllPath.toLowerCase().endsWith(FileExtensionConstant.FILE_EXTENSION_POINT_FILE_PDF)) {
			// pdf
			BufferedImage image;
			FileOutputStream out;

			PDDocument document;
			File pdfFile;
			int size;
			// PDF转换成HTML保存的文件夹
			// 遍历处理pdf附件
			buffer.append("<body>\r\n");
			document = new PDDocument();
			// 获取pdf
			pdfFile = new File(docAllPath);
			// 加载pdf
			document = PDDocument.load(pdfFile, (String) null);
			size = document.getNumberOfPages();
			// 开始读取
			PDFRenderer reader = new PDFRenderer(document);
			String imageName;
			// 直接转图片
			for (int i = 0; i < size; i++) {
				imageName = UUID.randomUUID() + ".png";
				image = reader.renderImage(i, 1.5f);
				// 生成图片保存到博客对应目录
				out = new FileOutputStream(path + File.separator + imageName);
				ImageIO.write(image, "png", out);
				// html中的显示设置
				buffer.append(
						"<img style=\"background-color:#fff; text-align:center; width:100%; max-width:100%;margin-top:6px;\" src=\""
								+ htmlImgPath + File.separator + imageName + "\"/>\r\n");
				image = null;
				out.flush();
				out.close();
			}
			reader = null;
			document.close();
			buffer.append("</body>\r\n");
			return buffer.toString();
		} else {
			return null;
		}
	}

}
