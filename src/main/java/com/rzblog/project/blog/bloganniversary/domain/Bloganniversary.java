package com.rzblog.project.blog.bloganniversary.domain;

import com.rzblog.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 纪念日管理表 blog_bloganniversary
 * 
 * @author ricozhou
 * @date 2018-12-04
 */
public class Bloganniversary extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Integer blogAnniversaryId;
	/** 纪念日日期 */
	private String anniversaryDate;
	/** 纪念日名 */
	private String anniversaryName;
	/** 纪念日描述 */
	private String anniversaryDesc;
	/** 纪念日博客操作（0无操作，1置灰） */
	private Integer anniversaryWebsiteOperate;
	/** 纪念日显示（0不显示，1弹窗显示，2顶部显示，3侧边栏显示） */
	private Integer anniversaryWebsiteShow;
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
		return "Bloganniversary [blogAnniversaryId=" + blogAnniversaryId + ", anniversaryDate=" + anniversaryDate
				+ ", anniversaryName=" + anniversaryName + ", anniversaryDesc=" + anniversaryDesc
				+ ", anniversaryWebsiteOperate=" + anniversaryWebsiteOperate + ", anniversaryWebsiteShow="
				+ anniversaryWebsiteShow + ", status=" + status + ", createBy=" + createBy + ", updateBy=" + updateBy
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark=" + remark + "]";
	}

	/**
	 * 设置：ID
	 */
	public void setBlogAnniversaryId(Integer blogAnniversaryId) {
		this.blogAnniversaryId = blogAnniversaryId;
	}

	/**
	 * 获取：ID
	 */
	public Integer getBlogAnniversaryId() {
		return blogAnniversaryId;
	}

	/**
	 * 设置：纪念日日期
	 */
	public void setAnniversaryDate(String anniversaryDate) {
		this.anniversaryDate = anniversaryDate;
	}

	/**
	 * 获取：纪念日日期
	 */
	public String getAnniversaryDate() {
		return anniversaryDate;
	}

	/**
	 * 设置：纪念日名
	 */
	public void setAnniversaryName(String anniversaryName) {
		this.anniversaryName = anniversaryName;
	}

	/**
	 * 获取：纪念日名
	 */
	public String getAnniversaryName() {
		return anniversaryName;
	}

	/**
	 * 设置：纪念日描述
	 */
	public void setAnniversaryDesc(String anniversaryDesc) {
		this.anniversaryDesc = anniversaryDesc;
	}

	/**
	 * 获取：纪念日描述
	 */
	public String getAnniversaryDesc() {
		return anniversaryDesc;
	}

	/**
	 * 设置：纪念日博客操作（0无操作，1置灰）
	 */
	public void setAnniversaryWebsiteOperate(Integer anniversaryWebsiteOperate) {
		this.anniversaryWebsiteOperate = anniversaryWebsiteOperate;
	}

	/**
	 * 获取：纪念日博客操作（0无操作，1置灰）
	 */
	public Integer getAnniversaryWebsiteOperate() {
		return anniversaryWebsiteOperate;
	}

	/**
	 * 设置：纪念日显示（0不显示，1弹窗显示，2顶部显示，3侧边栏显示）
	 */
	public void setAnniversaryWebsiteShow(Integer anniversaryWebsiteShow) {
		this.anniversaryWebsiteShow = anniversaryWebsiteShow;
	}

	/**
	 * 获取：纪念日显示（0不显示，1弹窗显示，2顶部显示，3侧边栏显示）
	 */
	public Integer getAnniversaryWebsiteShow() {
		return anniversaryWebsiteShow;
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
