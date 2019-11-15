package com.rzblog.project.blog.softcontent.domain;

import com.rzblog.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 博客模块软件管理表 blog_softcontent
 * 
 * @author ricozhou
 * @date 2019-02-10
 */
public class Softcontent extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 博客id */
	private Long cid;
	/** 软件名 */
	private String softName;
	/** 软件类型 */
	private String softType;
	/** 软件网址 */
	private String softWebsite;
	/** 下载地址 */
	private String softDownUrl;
	/** 更新地址 */
	private String softUpdateUrl;
	/** 文档地址 */
	private String softDocUrl;
	/** 开源许可 */
	private Integer softLicense;
	/** 开发语言 */
	private String softLanguage;
	/** 操作系统 */
	private String softOperateSystem;
	/** 软件作者 */
	private String softAuthor;
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
		return "Softcontent [cid=" + cid + ", softName=" + softName + ", softType=" + softType + ", softWebsite="
				+ softWebsite + ", softDownUrl=" + softDownUrl + ", softUpdateUrl=" + softUpdateUrl + ", softDocUrl="
				+ softDocUrl + ", softLicense=" + softLicense + ", softLanguage=" + softLanguage
				+ ", softOperateSystem=" + softOperateSystem + ", softAuthor=" + softAuthor + ", createBy=" + createBy
				+ ", updateBy=" + updateBy + ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark="
				+ remark + "]";
	}

	public String getSoftUpdateUrl() {
		return softUpdateUrl;
	}

	public void setSoftUpdateUrl(String softUpdateUrl) {
		this.softUpdateUrl = softUpdateUrl;
	}

	/**
	 * 设置：博客id
	 */
	public void setCid(Long cid) {
		this.cid = cid;
	}

	/**
	 * 获取：博客id
	 */
	public Long getCid() {
		return cid;
	}

	/**
	 * 设置：软件名
	 */
	public void setSoftName(String softName) {
		this.softName = softName;
	}

	/**
	 * 获取：软件名
	 */
	public String getSoftName() {
		return softName;
	}

	/**
	 * 设置：软件类型
	 */
	public void setSoftType(String softType) {
		this.softType = softType;
	}

	/**
	 * 获取：软件类型
	 */
	public String getSoftType() {
		return softType;
	}

	/**
	 * 设置：软件网址
	 */
	public void setSoftWebsite(String softWebsite) {
		this.softWebsite = softWebsite;
	}

	/**
	 * 获取：软件网址
	 */
	public String getSoftWebsite() {
		return softWebsite;
	}

	/**
	 * 设置：下载地址
	 */
	public void setSoftDownUrl(String softDownUrl) {
		this.softDownUrl = softDownUrl;
	}

	/**
	 * 获取：下载地址
	 */
	public String getSoftDownUrl() {
		return softDownUrl;
	}

	/**
	 * 设置：文档地址
	 */
	public void setSoftDocUrl(String softDocUrl) {
		this.softDocUrl = softDocUrl;
	}

	/**
	 * 获取：文档地址
	 */
	public String getSoftDocUrl() {
		return softDocUrl;
	}

	/**
	 * 设置：开源许可
	 */
	public void setSoftLicense(Integer softLicense) {
		this.softLicense = softLicense;
	}

	/**
	 * 获取：开源许可
	 */
	public Integer getSoftLicense() {
		return softLicense;
	}

	/**
	 * 设置：开发语言
	 */
	public void setSoftLanguage(String softLanguage) {
		this.softLanguage = softLanguage;
	}

	/**
	 * 获取：开发语言
	 */
	public String getSoftLanguage() {
		return softLanguage;
	}

	/**
	 * 设置：操作系统
	 */
	public void setSoftOperateSystem(String softOperateSystem) {
		this.softOperateSystem = softOperateSystem;
	}

	/**
	 * 获取：操作系统
	 */
	public String getSoftOperateSystem() {
		return softOperateSystem;
	}

	/**
	 * 设置：软件作者
	 */
	public void setSoftAuthor(String softAuthor) {
		this.softAuthor = softAuthor;
	}

	/**
	 * 获取：软件作者
	 */
	public String getSoftAuthor() {
		return softAuthor;
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
