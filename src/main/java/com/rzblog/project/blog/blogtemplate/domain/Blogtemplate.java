package com.rzblog.project.blog.blogtemplate.domain;

import com.rzblog.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 模板管理表 blog_blogtemplate
 * 
 * @author ricozhou
 * @date 2019-02-18
 */
public class Blogtemplate extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Integer blogTemplateId;
	/** 模板名称 */
	private String templateName;
	/** 模板存储文件夹 */
	private String blogFileName;
	/** 模板类型，文章，软件，影视 */
	private Integer blogType;
	/** 编辑器类型 */
	private Integer articleEditor;
	/** 作者 */
	private String author;
	/** 模板内容 */
	private String content;
	/** Markdown模板内容 */
	private String contentMd;
	/** 状态 */
	private Integer status;
	/** 创建者 */
	private String createBy;
	/** 更新者 */
	private String updateBy;
	/** 创建时间 */
	private Date createTime;
	/** 更新时间 */
	private Date updateTime;
	/** 备注 */
	private String remark;

	@Override
	public String toString() {
		return "Blogtemplate [blogTemplateId=" + blogTemplateId + ", templateName=" + templateName + ", blogFileName="
				+ blogFileName + ", blogType=" + blogType + ", articleEditor=" + articleEditor + ", author=" + author
				+ ", content=" + content + ", contentMd=" + contentMd + ", status=" + status + ", createBy=" + createBy
				+ ", updateBy=" + updateBy + ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark="
				+ remark + "]";
	}

	/**
	 * 设置：ID
	 */
	public void setBlogTemplateId(Integer blogTemplateId) {
		this.blogTemplateId = blogTemplateId;
	}

	/**
	 * 获取：ID
	 */
	public Integer getBlogTemplateId() {
		return blogTemplateId;
	}

	/**
	 * 设置：模板名称
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * 获取：模板名称
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * 设置：模板存储文件夹
	 */
	public void setBlogFileName(String blogFileName) {
		this.blogFileName = blogFileName;
	}

	/**
	 * 获取：模板存储文件夹
	 */
	public String getBlogFileName() {
		return blogFileName;
	}

	/**
	 * 设置：模板类型，文章，软件，影视
	 */
	public void setBlogType(Integer blogType) {
		this.blogType = blogType;
	}

	/**
	 * 获取：模板类型，文章，软件，影视
	 */
	public Integer getBlogType() {
		return blogType;
	}

	/**
	 * 设置：编辑器类型
	 */
	public void setArticleEditor(Integer articleEditor) {
		this.articleEditor = articleEditor;
	}

	/**
	 * 获取：编辑器类型
	 */
	public Integer getArticleEditor() {
		return articleEditor;
	}

	/**
	 * 设置：作者
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 获取：作者
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * 设置：模板内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取：模板内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置：Markdown模板内容
	 */
	public void setContentMd(String contentMd) {
		this.contentMd = contentMd;
	}

	/**
	 * 获取：Markdown模板内容
	 */
	public String getContentMd() {
		return contentMd;
	}

	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置：创建者
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取：创建者
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * 设置：更新者
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 获取：更新者
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

}
