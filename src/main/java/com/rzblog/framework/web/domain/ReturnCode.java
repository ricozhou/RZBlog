package com.rzblog.framework.web.domain;

/**
 * 接口状态码
 *
 */
public enum ReturnCode {

	// ******************0000：返回正常状态码（httpStatus：200）******************

	/**
	 * 操作成功
	 */
	SUCCESS("200", "操作成功"),

	/**
	 * 系统异常
	 */
	SERVER_ERROR("500", "系统异常"),

	/**
	 * 空指针异常
	 */
	NullPointerException("500", "空指针异常"),

	/**
	 * 更新失败
	 */
	UPDATE_FAILED("500", "更新失败"),

	/**
	 * 参数错误
	 */
	PARAM_FAILED("500", "参数错误"),
	// ******************1xxx：权限校验错误码******************

	/**
	 * 身份验证未通过，或者验证信息失效（httpStatus：401）
	 */
	UNAUTHORIZED("401", "身份验证未通过，或者验证信息失效"),

	/**
	 * 没有权限，服务器拒绝请求（httpStatus：403）
	 */
	FORBIDDEN("403", "没有权限，服务器拒绝请求"),

	// ******************4xxx：输入校验错误码（httpStatus：400）******************

	/**
	 * 请求失败
	 */
	BAD_REQUEST("500", "请求失败"),

	/**
	 * 密码错误
	 */
	LOGIN_FAILED("500", "帐号或密码错误"),

	/**
	 * 新密码与确认密码不一致
	 */
	PWD_DISAGREE("500", "新密码与确认密码不一致"),
	/**
	 * 只能修改当前登录用户得密码
	 */
	PWD_FEATED("500", "只能修改当前登录用户得密码"),

	/**
	 * 密码校验失败
	 */
	PWD_FAILED("500", "密码校验失败"),

	/**
	 * 原密码与新密码一致
	 */
	PWD_NEWPWD_AGREES("500", "原密码与新密码一致"),
	/**
	 * 原手机号与新手机号一致
	 */
	PHONE_NEWPHONE_AGREES("500", "原手机号与新手机号一致"),
	/**
	 * 原手机号不正确
	 */
	PHONE_NEWPHONE_NOAGREES("500", "原手机号不正确"),

	/**
	 * 用户名已存在
	 */
	USERNAME_EXIST("500", "用户名已存在"),
	/**
	 * 用户名不存在
	 */
	USERNAME_WETHER("500", "用户名不存在"),

	/**
	 * 用户名可用
	 */
	USERNAME_NOTEXIST("500", "用户名可用"),
	/**
	 * 系统异常
	 */
	NEW_USER_FAILURE("500", "新增用户失败"),

	/**
	 * 手机号码已注册
	 */
	PHONE_NUMBER_EXIST("500", "手机号码已注册"),

	/**
	 * 邮箱已注册
	 */
	EMAIL_ADDRESS_EXIST("500", "邮箱已注册"),

	/**
	 * 用户名不能为空
	 */
	USERNAME_NULL("4101", "用户名不能为空"),

	/**
	 * 实体店地址重复
	 */
	INTERMEDIARY_ADDRESS_REPART("4102", "实体店地址重复"),
	/**
	 * 身份证号已注册
	 */
	INTERMEDIARY_IDCARD_REPART("4103", "身份证号已注册"),
	/**
	 * 文件大小限制
	 */
	MAXUPLOADSIZE_EXCEPTION("4901", "上传的文件大小超出限制"),
	/**
	 * 不存在
	 */
	MAXUPLOAD_EXCEPTION("4902", "文件不存在"),
	/**
	 * 请上传文件
	 */
	MAXUPUPLOAD_EXCEPTION("4903", "请上传文件"),
	/**
	 * 中介不存在
	 */
	INTERMEDIARY_NULL("4104", "中介不存在"),
	/**
	 * 房屋不存在
	 */
	HOUSE_NULL("4104", "房屋不存在"),
	/**
	 * 预约记录不存在
	 */
	APPOINT_NULL("4104", "预约记录不存在"),

	DICTIONARY_EXIST("4106", "字典项已存在"),
	/**
	 * 预约记录推送失败
	 */
	PUSH_APPOINT_NULL("4108", "预约记录推送失败"),

	/**
	 * 解约申请推送失败
	 */
	PUSH_RECISSION_NULL("4109", "解约申请推送失败"),

	/**
	 * 订单记录推送失败
	 */
	PUSH_INDENT_NULL("4110", "订单记录推送失败"),
	/**
	 * 添加房源信息推送失败
	 */
	PUSH_HOUSE_NULL("4112", "添加房源信息推送失败"),
	/**
	 * 签订合同记录消息推送失败
	 */
	PUSH_PACT_NULL("4114", "签订合同记录消息推送失败"),
	/**
	 * 备案证明上传消息推送失败
	 */
	PUSH_REGISTER_NULL("4116", "备案证明上传消息推送失败"),
	/**
	 * 头像上传或修改失败
	 */
	HEADER_RUPLOAD_FAIL("4222", "头像上传或修改失败"),
	/**
	 * 手机验证码验证失败
	 */
	VERIFICATION_MESSAGE_FAIL("4333", "验证码错误"),
	/**
	 * 姓名或者手机号错误
	 */
	FULLNAMEORIDCARD_ERROR("43334", "姓名或者手机号错误"),
	/**
	 * 身份认证失败
	 */
	AUTHENTICATION_FAIL("43334", "身份认证失败"),
	/**
	 * 评论失败
	 */
	COMMENT_FAILED("4333", "评论失败"),
	/**
	 * 小区名已存在
	 */
	COMMITY_EXIST("4334", "小区名已存在"),
	/**
	 * 地址名已存在
	 */
	Address_EXIST("4335", "地址名已存在"),
	/**
	 * 房源信息审核推送失败
	 */
	HOUSEAUDIT_FAILED("4337", "房源信息审核推送失败"),
	/**
	 * 房屋拉入待处理失败
	 */
	HOUSEWAIT_FAILED("4339", "房屋拉入待处理失败"),
	/**
	 * 房屋拉入发布中失败
	 */
	HOUSESEND_FAILED("4340", "房屋拉入发布中失败"),
	/**
	 * 更新合同推送失败
	 */
	UPDATE_PACT_FAILED("4341", "不能删除"),
	/**
	 * 手机号码未注册
	 */
	PHONE_NOT_REGISTER("4342", "手机号码未注册"),
	/**
	 * 手机号码不止一个人注册
	 */
	PHONE_MORE_REGISTER("4343", "手机号码不止一个人注册"),
	/**
	 * 此帐号密码不能修改
	 */
	USER_PASSWORD_NOYUPDATE("4345", "此帐号密码不能修改"),

	// ******************5xxx：业务异常错误码（httpStatus：500）******************

	/**
	 * 新增失败
	 */
	INSERT_FAILED("5100", "新增失败"),

	/**
	 * 删除失败
	 */
	DELETE_FAILED("500", "删除失败"),
	/**
	 * 文件不存在
	 */
	FILE_EXITS("4408", "文件不存在"),

	// ******************9xxx：系统异常错误码（httpStatus：500）******************

	/**
	 * 该接口不存在
	 */
	NOT_FOUND("9404", "该接口不存在");

	private String code;

	private String desc;

	private ReturnCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return this.name() + "[" + this.code + "_" + desc + "]";
	}

}
