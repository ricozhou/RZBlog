$(document).ready(function() {
	// 回到顶部
	// browser window scroll (in pixels) after which the "back to top"
	// link is shown
	var offset = 300,
	// browser window scroll (in pixels) after which the "back to top"
	// link opacity is reduced
	offset_opacity = 1200,
	// duration of the top scrolling animation (in ms)
	scroll_top_duration = 700,
	// grab the "back to top" link
	$back_to_top = $('#cd-top');
	$back_to_share = $('#cd-share');
	$back_to_cservice = $('#cd-cservice');
	$back_to_kf = $('#cservice');
	document.getElementById("cd-top").style.zIndex = "999";
	document.getElementById("cd-share").style.zIndex = "999";
	document.getElementById("cd-cservice").style.zIndex = "999";

	// hide or show the "back to top" link
	$(window).scroll(function() {
		if ($(this).scrollTop() > offset) {
			$back_to_top.addClass('cd-is-visible')
			document.getElementById("cd-share").style.bottom = "90px";
			document.getElementById("cd-cservice").style.bottom = "140px";
			document.getElementById("cservice").style.bottom = "195px";
		} else {
			$back_to_top.removeClass('cd-is-visible cd-fade-out')
			document.getElementById("cd-share").style.bottom = "40px";
			document.getElementById("cd-cservice").style.bottom = "90px";
			document.getElementById("cservice").style.bottom = "145px";
		}
		if ($(this).scrollTop() > offset_opacity) {
			$back_to_top.addClass('cd-fade-out');
		} else {
		}
	});
	// smooth scroll to top
	$back_to_top.on('click', function(event) {
		event.preventDefault();
		$('body,html').animate({
			scrollTop : 0,
		}, scroll_top_duration);
	});

	// 分享
	$back_to_cservice.on('click', function(event) {
		$(".cservice").fadeToggle();
	});
	// kf
	$("#closed").click(function() {
		// $back_to_kf.css('display', 'none');
		$(".cservice").fadeToggle();
	});

});

// 一开始便加载
$(document)
		.ready(
				function() {
					// 请求专栏信息
					blogcolumnList();
					// 显示专栏
					// 先从后台获取专栏数据
					if (blogcolumnList != null && blogcolumnList.length > 0) {
						// 渲染两个，一个是正常尺寸显示，一个是缩小尺寸显示
						var children;
						// var html =
						// getAboutMe(blogRecommendList.aboutmeBlogcontent);
						var html = '';
						// var html2 =
						// getAboutMe2(blogRecommendList.aboutmeBlogcontent);
						var html2 = '';
						var arrowClass = '';
						for (var i = 0; i < (blogset.blogsetNavBarShowNum < blogcolumnList.length ? blogset.blogsetNavBarShowNum
								: blogcolumnList.length); i++) {
							children = blogcolumnList[i].children;
							if (children != null) {
								if (children.length > 0) {
									arrowClass = 'arrowEffect';
								}
								html = html
										+ '<li class="'
										+ arrowClass
										+ '"><a href="/rzblog/column'
										+ blogcolumnList[i].url
										+ '">'
										+ getColumnIconHtml(blogcolumnList[i].icon)
										+ blogcolumnList[i].blogColumnName
										+ '</a><ul class="sub-nav">';
								// 小屏显示时如果主专栏可以点击则手风琴效果会失效直接跳转主专栏页面，在此取消主专栏的链接，都改用次专栏链接显示
								html2 = html2
										+ '<dt class="list_dt"><a href="#">'
										+ getColumnIconHtml(blogcolumnList[i].icon)
										+ blogcolumnList[i].blogColumnName
										+ '</a></dt><dd class="list_dd"><ul>';

								for (var j = 0; j < children.length; j++) {
									html = html
											+ '<li><a href="/rzblog/column'
											+ blogcolumnList[i].url
											+ children[j].url
											+ '">'
											+ getColumnIconHtml(children[j].icon)
											+ children[j].blogColumnName
											+ '</a></li>';
									html2 = html2
											+ '<li><a href="/rzblog/column'
											+ blogcolumnList[i].url
											+ children[j].url
											+ '">'
											+ getColumnIconHtml(children[j].icon)
											+ children[j].blogColumnName
											+ '</a></li>';
								}
								html = html + '</ul></li>';
								html2 = html2 + '</ul></dd>';
								// target="_blank"
							}
						}
						// 添加到增加参数按钮之前
						document.getElementById('show2').insertAdjacentHTML(
								'afterEnd', html)
						document.getElementById('show3').insertAdjacentHTML(
								'afterEnd', html2)
						// 重新渲染
						reNavCssStyle();
					}

					// 设置底部信息
					// 版权信息
					if (blogset.basicsetFooterCopyrightInfo != null
							&& blogset.basicsetFooterCopyrightInfo != '') {
						document.getElementById('copyrightmsg')
								.insertAdjacentHTML('afterBegin',
										blogset.basicsetFooterCopyrightInfo)
					}
					// 站点声明
					if (blogset.basicsetFooterSiteStatement != null
							&& blogset.basicsetFooterSiteStatement != '') {
						var siteStatement = '<p><b>站点声明：</b></p>'
								+ blogset.basicsetFooterSiteStatement;
						document.getElementById('endnav').insertAdjacentHTML(
								'afterBegin', siteStatement)
					}
					// 设置底部二维码信息
					// 原则，顺序：QQ，微信
					if (blogset.bloggersetBloggerWechatOrcode != null
							&& blogset.bloggersetBloggerWechatOrcode != '') {
						$("#mywechatqr").css('display', 'block');
					}

					// 右侧客服
					if (blogset.basicsetOpenCservice == 0) {
						$("#cd-cservice").css('display', 'block');
					}

					// 设置首页左侧图标
					if (blogset.basicsetWebsiteIco != null
							&& blogset.basicsetWebsiteIco != '') {
						$("#weblogo").css("padding-left", '50px');
						$("#weblogo")
								.css(
										"background",
										'url('
												+ blogset.basicsetWebsiteIco
														.replace('\\', '/')
												+ ') no-repeat left center / 40px 40px');

						// 移动端导航栏
						$("#weblogoL").css("padding-left", '20px');
						$("#mnav h2 .navicon").css("margin-right", '35px');
						$("#weblogoL").css(
								"background",
								'url('
										+ blogset.basicsetWebsiteIco.replace(
												'\\', '/')
										+ ') no-repeat 4px center / 30px 30px');

					}

				})

// 获取专栏图标HTML
function getColumnIconHtml(icon) {
	if (icon == null || icon == '') {
		return '';
	}
	return '<i class="' + icon + '"></i> ';
}
// 请求专栏列表
// 专栏
var blogcolumnList;
function blogcolumnList() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/columnMsgList",
		data : {},
		async : false,
		success : function(data) {
			blogcolumnList = data;
		}
	});
}

// 重新渲染导航栏
function reNavCssStyle() {
	/* nav show or hide */
	$('.nav>li').hover(function() {
		$(this).children('ul').stop(true, true).show(400);
	}, function() {
		$(this).children('ul').stop(true, true).hide(400);
	});
	/* search */
	$('.search_ico').click(function() {
		if ($('.search_bar').is('.search_open')) {
			if ($("#keyboard").val() == null || $("#keyboard").val() == '') {
				// 没有输入则收回
				$('.search_bar').toggleClass('search_open');
				return;
			}
			// 如果包含class则搜索功能开始
			startSearch();
		} else {
			// 不包含则打开搜索
			$('.search_bar').toggleClass('search_open');
		}
	});
	/* search2 */
	$('.search_ico2').click(function() {
		$('.search_bar2').toggleClass('search_open');
		if ($('#keyboard').val().length > 2) {
			$('#keyboard').val('');
			$('#searchform').submit();
		} else {
			return false;
		}
	});

	// 为了将点击的标签蓝色显示
	/* topnav select */
	var obj = null;
	var allMenu = document.getElementById('topnav').getElementsByTagName('a');
	obj = allMenu[0];
	for (i = 1; i < allMenu.length; i++) {
		if (window.location.href.indexOf(allMenu[i].href) >= 0) {
			obj = allMenu[i];
		}
	}
	obj.id = 'topnav_current';

	setMnavCss();
	// 菜单点击效果
	$('.list_dt').on(
			'click',
			function() {
				$('.list_dd').stop();
				$(this).siblings('dt').removeAttr('id');
				if ($(this).attr('id') == 'open') {
					$(this).removeAttr('id').siblings('dd').slideUp();
				} else {
					$(this).attr('id', 'open').next().slideDown()
							.siblings('dd').slideUp();
				}
			});
}
function setMnavCss() {
	/* mnav dl open */
	var oH2 = document.getElementsByTagName('h2')[0];
	var oUl = document.getElementsByTagName('dl')[0];
	oH2.onclick = function() {
		var style = oUl.style;
		style.display = style.display == 'block' ? 'none' : 'block';
		oH2.className = style.display == 'block' ? 'open' : '';
	};
}
// 动态添加style
function addCssByStyle(cssString) {
	var doc = document;
	var style = doc.createElement("style");
	style.setAttribute("type", "text/css");

	if (style.styleSheet) {// IE
		style.styleSheet.cssText = cssString;
	} else {// w3c
		var cssText = doc.createTextNode(cssString);
		style.appendChild(cssText);
	}

	var heads = doc.getElementsByTagName("head");
	if (heads.length)
		heads[0].appendChild(style);
	else
		doc.documentElement.appendChild(style);
}

// 关于我
function getAboutMe(content) {
	var html = '';
	if (content != null && content != '') {
		html = '<li><a href="/rzblog/blogcontent/details/' + content.showId
				+ '" target="_blank">' + "关于我"
				+ '</a><ul class="sub-nav"></ul></li>'
	}
	return html;
}
// 关于我
// 小屏显示
function getAboutMe2(content) {
	var html = '';
	if (content != null && content != '') {
		html = '<dt class="list_dt"><a href="/rzblog/blogcontent/details/'
				+ content.showId + '" target="_blank">' + "关于我"
				+ '</a></dt><dd class="list_dd"><ul></ul></dd>'
	}
	return html;
}

// 计算平滑度
function getBorderRadius(width, param) {
	if (param == null || param == '') {
		return "0";
	}
	return width * param / 100;
}

// 监听搜索框回车
$('#keyboard').bind('keydown', function(event) {
	if (event.keyCode == "13") {
		startSearch();
	}
});
// 搜索
function startSearch() {
	var keyboard = $("#keyboard").val();
	// 校验合法性,只能中文英文数字
	var reg = /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/;
	if (keyboard == null || keyboard == '') {
		// $.modalMsg("请输入关键字！", "warning");
		return;
	}
	if (!reg.test(keyboard)) {
		$.modalMsg("只能是中文、英文和数字！", "warning");
		return;
	}
	if (keyboard.length > 30) {
		$.modalMsg("最多30个字符！", "warning");
		return;
	}

	// 开始后台查询
	window.location.href = "/rzblog/search/?keyword=" + keyboard;

}
// 消息窗体
$.modalMsg = function(content, type) {
	if (type != undefined) {
		var icon = "";
		if (type == modal_status.WARNING) {
			icon = 0;
		} else if (type == modal_status.SUCCESS) {
			icon = 1;
		} else if (type == modal_status.FAIL) {
			icon = 2;
		}
		layer.msg(content, {
			icon : icon,
			time : 2000,
			shift : 0
		});
		$(".layui-layer-msg").find('i.' + icon).parents('.layui-layer-msg')
				.addClass('layui-layer-msg-' + type);
	} else {
		layer.msg(content);
	}
}
/** 弹窗状态码 */
modal_status = {
	SUCCESS : "success",
	FAIL : "error",
	WARNING : "warning"
};