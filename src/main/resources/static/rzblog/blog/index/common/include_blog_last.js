$(document)
		.ready(
				function() {
					// 是否开启纪念日
					if (blogset.basicsetOpenAnniversary == 0) {
						// 首先判断今日的纪念日有几个，没有则返回，有的话则遍历判断是否存在置灰操作，然后进行显示操作，最后进行置灰操作
						var bloganniversaryList = blogset.bloganniversaryList;
						var setGrayFlag = 1;
						var topShowHtml = '';
						var sideShowHtml = '';
						var aname = '';
						var adesc = '';
						if (bloganniversaryList != null
								&& bloganniversaryList != ''
								&& bloganniversaryList.length > 0) {
							for (var i = 0; i < bloganniversaryList.length; i++) {
								if (setGrayFlag == 1) {
									if (bloganniversaryList[i].anniversaryWebsiteOperate == 1) {
										setGrayFlag = 0;
									}
								}
								aname = bloganniversaryList[i].anniversaryName;
								adesc = (bloganniversaryList[i].anniversaryDesc != '' && bloganniversaryList[i].anniversaryDesc != null) ? bloganniversaryList[i].anniversaryDesc
										: aname;
								// 判断显示类型
								if (bloganniversaryList[i].anniversaryWebsiteShow == 1) {
									// 弹窗显示
									showLayuiMsg(aname, adesc);

								} else if (bloganniversaryList[i].anniversaryWebsiteShow == 2) {
									// 顶部显示
									if ($('#anniversaryShow').length > 0) {
										// 暂时只能主页显示顶部显示，需要判断一下是否是主页
										topShowHtml = topShowHtml
												+ '<li class="scrolltext-title">'
												+ aname
												+ ((bloganniversaryList[i].anniversaryDesc != '' && bloganniversaryList[i].anniversaryDesc != null) ? ' ： '
														+ bloganniversaryList[i].anniversaryDesc
														: '') + '</li>';
									}

								} else if (bloganniversaryList[i].anniversaryWebsiteShow == 3) {
									// 侧边显示(暂时显示在右侧，日历后面)
									sideShowHtml = sideShowHtml
											+ '<div class="divside links" id="anniversarySide-'
											+ aname
											+ '"><h2 class="hometitle"><i class="fa fa-heart"></i>&nbsp;'
											+ aname + '</h2><p>' + adesc
											+ '</p></div>';
								}

							}
							// 顶部显示
							topShowAnniversary(topShowHtml);
							// 侧边显示
							sideShowAnniversary(sideShowHtml);
							// 置灰耗时，只置灰一次
							if (setGrayFlag == 0) {
								// 置灰
								setWebSiteGray();
							}
						}
					}
					// 先判断屏幕大小，小屏不再显示侧边栏后续操作直接屏蔽
					if ($(window).width() < 974) {
						// 设置导航栏变化
						setHeaderChange();
						// 不再渲染侧边栏
						return;
					} else {
						// 不使用js直接改变css，这样会永久改变导致无法适应屏幕，更改为使用js加载css文件，这样既可生效
						// 最后加载
						if (blogset.stylesetShowLeftSiderbar == 0) {
							setSiderbarLMsg();
							// 设置样式，因为默认样式是右侧显示
							if (blogset.stylesetShowRightSiderbar == 0) {
								setSiderbarMsg();
								// 加载双侧css
								dynamicLoadCssFile("/css/blog/siderbarD.css");
								// 加载控制屏幕css
								dynamicLoadCssFile("/css/blog/siderbarmD.css");
							} else {
								$("#sidebar").css('display', 'none');
								// 加载左侧css
								dynamicLoadCssFile("/css/blog/siderbarL.css");
								// 加载控制屏幕css
								dynamicLoadCssFile("/css/blog/siderbarmL.css");
							}

						} else {
							$("#sidebarL").css('display', 'none');
							if (blogset.stylesetShowRightSiderbar != 0) {
								// 右侧也不显示
								// 都不显示则加载新文件
								$("#sidebar").css('display', 'none');
								dynamicLoadCssFile("/css/blog/siderbarN.css");
							} else {
								setSiderbarMsg();
							}
						}
						// 判断是否显示天气插件
						// 对天气插件进行处理
						if (blogset.basicsetShowWeather == 0) {
							// 插件代码
							(function(T, h, i, n, k, P, a, g, e) {
								g = function() {
									P = h.createElement(i);
									a = h.getElementsByTagName(i)[0];
									P.src = k;
									P.charset = "utf-8";
									P.async = 1;
									a.parentNode.insertBefore(P, a)
								};
								T["ThinkPageWeatherWidgetObject"] = n;
								T[n] || (T[n] = function() {
									(T[n].q = T[n].q || []).push(arguments)
								});
								T[n].l = +new Date();
								if (T.attachEvent) {
									T.attachEvent("onload", g)
								} else {
									T.addEventListener("load", g, true)
								}
							}
									(window, document, "script", "tpwidget",
											"//widget.seniverse.com/widget/chameleon.js"))
							tpwidget("init", {
								"flavor" : "bubble",
								"location" : "WTTDPCGXTWUS",
								"geolocation" : "enabled",
								"position" : "bottom-left",
								"margin" : "10px 10px",
								"language" : "zh-chs",
								"unit" : "c",
								"theme" : "chameleon",
								"uid" : "U0769246DF",
								"hash" : "596ab54cd8d65aa6e315371349b6e375"
							});
							tpwidget("show");
							// 插件代码
						}
						// 设置导航栏变化
						setHeaderChange();
						// 设置关注我
						setFollowUs();
					}

				});

// 右侧
function setSiderbarMsg() {
	// 设置左右侧显示
	// 判断右侧边栏显示哪些
	var data = blogset.blogsiderbarList;
	for (var i = 0; i < data.length; i++) {
		if (data[i].blogSiderbrName == '博主简介') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#aboutme").css('display', 'block');
			} else {
				$("#aboutme").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '广告') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#siderad").css('display', 'block');
			} else {
				$("#siderad").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '日历') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#calendarside").css('display', 'block');
			} else {
				$("#calendarside").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '特别推荐') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#specialRecommended").css('display', 'block');
			} else {
				$("#specialRecommended").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '列表推荐') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#tuijian").css('display', 'block');
			} else {
				$("#tuijian").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '文章标签') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#cloud").css('display', 'block');
			} else {
				$("#cloud").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '友情链接') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#links").css('display', 'block');
			} else {
				$("#links").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '关注博主') {
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#follow-us").css('display', 'block');
			} else {
				$("#follow-us").css('display', 'none');
			}
		} else {
			// 其他显示
			// 此处其他信息由于动态生成html无法通过id改变显示，更改为在主文件中设置
			if (data[i].blogShowRightSiderbr == 0) {
				// 显示
				$("#other-" + data[i].blogSiderbrName + "").css('display',
						'block');
			} else {
				$("#other-" + data[i].blogSiderbrName + "").css('display',
						'none');
			}
		}

	}
}

// 左侧
function setSiderbarLMsg() {
	// 设置左右侧显示

	// 判断右侧边栏显示哪些
	var data = blogset.blogsiderbarList;
	for (var i = 0; i < data.length; i++) {
		if (data[i].blogSiderbrName == '博主简介') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#aboutmeL").css('display', 'block');
			} else {
				$("#aboutmeL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '广告') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#sideradL").css('display', 'block');
			} else {
				$("#sideradL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '日历') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#calendarsideL").css('display', 'block');
			} else {
				$("#calendarsideL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '特别推荐') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#specialRecommendedL").css('display', 'block');
			} else {
				$("#specialRecommendedL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '列表推荐') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#tuijianL").css('display', 'block');
			} else {
				$("#tuijianL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '文章标签') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#cloudL").css('display', 'block');
			} else {
				$("#cloudL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '友情链接') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#linksL").css('display', 'block');
			} else {
				$("#linksL").css('display', 'none');
			}
		} else if (data[i].blogSiderbrName == '关注博主') {
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#follow-usL").css('display', 'block');
			} else {
				$("#follow-usL").css('display', 'none');
			}
		} else {
			// 其他显示
			if (data[i].blogShowLeftSiderbr == 0) {
				// 显示
				$("#other-" + data[i].blogSiderbrName + "L").css('display',
						'block');
			} else {
				$("#other-" + data[i].blogSiderbrName + "L").css('display',
						'none');
			}
		}

	}
}

// 设置关注
function setFollowUs() {
	// 需要在所有动态加载完设置此，否则位移不准确
	// 设置固定关注我们
	if ($('#follow-us')) {
		var followUsPosition = $('#follow-us').offset().top;
		var followUsPositionL = $('#follow-usL').offset().top;
		// 跟换方法，使用$(window).scroll(()=>{}此为jquery封装好方法，可实现多个滚动监听，由于天气插件已存在监听，导致内部监听失效，故改用此方法
		$(window).scroll(function() {
			var nowPosition = document.documentElement.scrollTop;
			if (blogset.stylesetShowRightSiderbar == 0) {
				if (nowPosition - followUsPosition > 0) {
					setTimeout(function() {
						$('#follow-us').attr('class', 'divside guanzhu gd');
					}, 150);
				} else {
					$('#follow-us').attr('class', 'divside guanzhu');
				}
			}

			if (blogset.stylesetShowLeftSiderbar == 0) {
				if (nowPosition - followUsPositionL > 0) {
					setTimeout(function() {
						$('#follow-usL').attr('class', 'divside guanzhu gd');
					}, 150);
				} else {
					$('#follow-usL').attr('class', 'divside guanzhu');
				}
			}

		});
	}
}

// 设置导航栏变化
function setHeaderChange() {
	var new_scroll_position = 0;
	var last_scroll_position;
	var header = document.getElementsByClassName("headerSlide");

	window.addEventListener('scroll', function(e) {
		last_scroll_position = window.scrollY;

		if (new_scroll_position < last_scroll_position
				&& last_scroll_position > 80) {
			header[0].classList.remove("slideDown");
			header[1].classList.remove("slideDown");
			header[0].classList.add("slideUp");
			header[1].classList.add("slideUp");
			setMnavHidden();

		} else if (new_scroll_position > last_scroll_position) {
			header[0].classList.remove("slideUp");
			header[1].classList.remove("slideUp");
			header[0].classList.add("slideDown");
			header[1].classList.add("slideDown");
		}

		new_scroll_position = last_scroll_position;
	});

}

function setMnavHidden() {
	document.getElementsByTagName('dl')[0].style.display = 'none';
	document.getElementsByTagName('h2')[0].className = '';
}

// 顶部显示
function topShowAnniversary(topShowHtml) {
	if (topShowHtml != null && topShowHtml != ''
			&& $('#anniversaryShow').length > 0) {
		$("#anniversaryShow").css('display', 'block');
		// 添加
		document.getElementById('anniversary-box').insertAdjacentHTML(
				'afterBegin', topShowHtml)
		if ($('#anniversary-box li').length > 1) {
			// 设置滚动
			// 调用 公告滚动函数
			setInterval("noticeUp('#ascrolltext ul','-20px',1000)", 5000);
		}
	}

}
// 侧边显示
function sideShowAnniversary(sideShowHtml) {
	if (sideShowHtml != null && sideShowHtml != '') {
		// 添加
		document.getElementById('calendarside').insertAdjacentHTML('afterEnd',
				sideShowHtml)
	}

}
// 弹窗显示
function showLayuiMsg(title, content) {
	if (Cookies.get("flag") != 0) {
		layer.alert(content, {
			skin : 'layui-layer-molv',
			title : title,
			btn : [ '不再显示', '关闭' ],
			btnclass : [ 'btn btn-primary', 'btn btn-danger' ],
			time : 50000,
			yes : function() {
				// 添加到cookie中
				Cookies.set("flag", 0);
				layer.closeAll();
			},
			btn2 : function() {
			}
		});
	}
}

// 整站置灰
function setWebSiteGray() {
	// ie10,11以上方法不兼容
	var navStr = navigator.userAgent.toLowerCase();
	if (navStr.indexOf("msie 10.0") !== -1 || navStr.indexOf("rv:11.0") !== -1) { // 判断是IE10或者IE11
		// 加载置灰文件
		dynamicLoadJsFile('/js/grayscale.js', function() {
			// 去掉导航栏的渐变色
			$('.menu').addClass("change-menu");
			$('.mnav').addClass("change-menu");

			// 置灰
			grayscale(document.body);
		});
	} else {
		// 除ie10,11以外使用此方法速度快
		// 更新上述方法，上述插件太慢
		dynamicLoadCssFile("/css/setgray.css");
	}
}