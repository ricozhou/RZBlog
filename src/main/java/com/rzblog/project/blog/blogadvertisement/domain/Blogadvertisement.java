package com.rzblog.project.blog.blogadvertisement.domain;

import com.rzblog.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 广告管理表 blog_blogadvertisement
 * 
 * @author ricozhou
 * @date 2019-02-12
 */
public class Blogadvertisement extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** ID */
	private Integer blogAdId;
	/** 广告名称 */
	private String adName;
	/** 广告内容 */
	private String adContent;
	/** 广告商 */
	private String adCompany;
	/** 广告类型 */
	private String adType;
	/** 广告费用 */
	private Integer adCost;
	/** 广告图片 */
	private String adImage;
	/** 广告链接 */
	private String adUrl;
	/** 投放位置 */
	private Integer adPlacementMain;
	/** 投放位置 */
	private Integer adPlacementConcent;
	/** 投放位置 */
	private Integer adPlacementSider;
	/** 开始时间 */
	private Date adStartIme;
	/** 结束时间 */
	private Date adEndIme;
	/** 广告状态 */
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

	// 传参，最大个数
	private Integer maxShowNum;

	@Override
	public String toString() {
		return "Blogadvertisement [blogAdId=" + blogAdId + ", adName=" + adName + ", adContent=" + adContent
				+ ", adCompany=" + adCompany + ", adType=" + adType + ", adCost=" + adCost + ", adImage=" + adImage
				+ ", adUrl=" + adUrl + ", adPlacementMain=" + adPlacementMain + ", adPlacementConcent="
				+ adPlacementConcent + ", adPlacementSider=" + adPlacementSider + ", adStartIme=" + adStartIme
				+ ", adEndIme=" + adEndIme + ", status=" + status + ", createBy=" + createBy + ", updateBy=" + updateBy
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark=" + remark + ", maxShowNum="
				+ maxShowNum + "]";
	}

	public Integer getMaxShowNum() {
		return maxShowNum;
	}

	public void setMaxShowNum(Integer maxShowNum) {
		this.maxShowNum = maxShowNum;
	}

	/**
	 * 设置：ID
	 */
	public void setBlogAdId(Integer blogAdId) {
		this.blogAdId = blogAdId;
	}

	/**
	 * 获取：ID
	 */
	public Integer getBlogAdId() {
		return blogAdId;
	}

	/**
	 * 设置：广告名称
	 */
	public void setAdName(String adName) {
		this.adName = adName;
	}

	/**
	 * 获取：广告名称
	 */
	public String getAdName() {
		return adName;
	}

	/**
	 * 设置：广告内容
	 */
	public void setAdContent(String adContent) {
		this.adContent = adContent;
	}

	/**
	 * 获取：广告内容
	 */
	public String getAdContent() {
		return adContent;
	}

	/**
	 * 设置：广告商
	 */
	public void setAdCompany(String adCompany) {
		this.adCompany = adCompany;
	}

	/**
	 * 获取：广告商
	 */
	public String getAdCompany() {
		return adCompany;
	}

	/**
	 * 设置：广告类型
	 */
	public void setAdType(String adType) {
		this.adType = adType;
	}

	/**
	 * 获取：广告类型
	 */
	public String getAdType() {
		return adType;
	}

	/**
	 * 设置：广告费用
	 */
	public void setAdCost(Integer adCost) {
		this.adCost = adCost;
	}

	/**
	 * 获取：广告费用
	 */
	public Integer getAdCost() {
		return adCost;
	}

	/**
	 * 设置：广告图片
	 */
	public void setAdImage(String adImage) {
		this.adImage = adImage;
	}

	/**
	 * 获取：广告图片
	 */
	public String getAdImage() {
		return adImage;
	}

	/**
	 * 设置：广告链接
	 */
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	/**
	 * 获取：广告链接
	 */
	public String getAdUrl() {
		return adUrl;
	}

	public Integer getAdPlacementMain() {
		return adPlacementMain;
	}

	public void setAdPlacementMain(Integer adPlacementMain) {
		this.adPlacementMain = adPlacementMain;
	}

	public Integer getAdPlacementConcent() {
		return adPlacementConcent;
	}

	public void setAdPlacementConcent(Integer adPlacementConcent) {
		this.adPlacementConcent = adPlacementConcent;
	}

	public Integer getAdPlacementSider() {
		return adPlacementSider;
	}

	public void setAdPlacementSider(Integer adPlacementSider) {
		this.adPlacementSider = adPlacementSider;
	}

	/**
	 * 设置：开始时间
	 */
	public void setAdStartIme(Date adStartIme) {
		this.adStartIme = adStartIme;
	}

	/**
	 * 获取：开始时间
	 */
	public Date getAdStartIme() {
		return adStartIme;
	}

	/**
	 * 设置：结束时间
	 */
	public void setAdEndIme(Date adEndIme) {
		this.adEndIme = adEndIme;
	}

	/**
	 * 获取：结束时间
	 */
	public Date getAdEndIme() {
		return adEndIme;
	}

	/**
	 * 设置：广告状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取：广告状态
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
