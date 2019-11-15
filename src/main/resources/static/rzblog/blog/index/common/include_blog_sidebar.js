// 一开始便加载
$(document)
		.ready(
				function() {
					// 先判断屏幕大小，小屏不再显示侧边栏后续操作直接屏蔽
					if ($(window).width() < 974) {
						// 不再渲染侧边栏
						return;
					}

					if (blogset.stylesetShowRightSiderbar == 0) {
						// 请求推荐信息(不包括关于我关于本站)
						blogRecommendListData();
						// 请求标签信息
						blogTagsListData();
					} else {
						return;
					}

					// tab分割
					setTabSet();
					// 初始化界面推荐
					if (blogRecommendList != null && blogRecommendList != '') {
						// 初始化侧边栏
						// 特别推荐
						if (blogRecommendList.specialRecommendedBlogcontentList != null
								&& blogRecommendList.specialRecommendedBlogcontentList != ''
								&& blogRecommendList.specialRecommendedBlogcontentList.length > 0) {
							// 显示
							// $("#specialRecommended").css('display', 'block');
							// 生成
							var html = '';
							var imgHtml = '';
							for (var i = 0; i < blogRecommendList.specialRecommendedBlogcontentList.length; i++) {
								imgHtml = getSpecialRecommendedImg(
										blogRecommendList.specialRecommendedBlogcontentList[i],
										blogset.blogsetDefaultPic)
								html = html
										+ '<li>'
										+ imgHtml
										+ '<p>'
										+ blogRecommendList.specialRecommendedBlogcontentList[i].title
										+ '<span><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.specialRecommendedBlogcontentList[i].showId
										+ '" target="_blank">阅读</a></span></p></li>';
							}
							// 添加
							document.getElementById('showSpecialRecommended')
									.insertAdjacentHTML('afterBegin', html)
						}

						// 列表推荐
						if (blogRecommendList.recommendedBlogcontentList != null
								&& blogRecommendList.recommendedBlogcontentList != ''
								&& blogRecommendList.recommendedBlogcontentList.length > 0) {
							// 生成
							var html = '';
							var imgHtml = '';
							for (var i = 0; i < blogRecommendList.recommendedBlogcontentList.length; i++) {
								imgHtml = getRecommendedImg(blogRecommendList.recommendedBlogcontentList[i])
								html = html
										+ '<li><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.recommendedBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.recommendedBlogcontentList[i].title
										+ '">'
										+ imgHtml
										+ '</a><p class="ellipsisp'
										+ (imgHtml == '' ? '0' : '1')
										+ '"><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.recommendedBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.recommendedBlogcontentList[i].title
										+ '">'
										+ blogRecommendList.recommendedBlogcontentList[i].title
										+ '</a></p>'
										+ (imgHtml == '' ? ''
												: '<span class="span1">'
														+ blogRecommendList.recommendedBlogcontentList[i].gtmCreate
														+ '</span>') + '</li>'
							}
							// 添加
							document.getElementById('recommended')
									.insertAdjacentHTML('afterBegin', html)
						}

						// 点赞排行
						if (blogRecommendList.likeBlogcontentList != null
								&& blogRecommendList.likeBlogcontentList != ''
								&& blogRecommendList.likeBlogcontentList.length > 0) {
							// 生成
							var html = '';
							var imgHtml = '';
							for (var i = 0; i < blogRecommendList.likeBlogcontentList.length; i++) {
								imgHtml = getRecommendedImg(blogRecommendList.likeBlogcontentList[i])
								html = html
										+ '<li><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.likeBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.likeBlogcontentList[i].title
										+ '">'
										+ imgHtml
										+ '</a><p class="ellipsisp'
										+ (imgHtml == '' ? '0' : '1')
										+ '"><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.likeBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.likeBlogcontentList[i].title
										+ '">'
										+ blogRecommendList.likeBlogcontentList[i].title
										+ '</a></p>'
										+ (imgHtml == '' ? ''
												: '<span class="span1">'
														+ blogRecommendList.likeBlogcontentList[i].gtmCreate
														+ '</span>') + '</li>'
							}
							// 添加
							document.getElementById('likeBlogcontent')
									.insertAdjacentHTML('afterBegin', html)
						}
						// 点击排行
						if (blogRecommendList.browseBlogcontentList != null
								&& blogRecommendList.browseBlogcontentList != ''
								&& blogRecommendList.browseBlogcontentList.length > 0) {
							// 生成
							var html = '';
							var imgHtml = '';
							for (var i = 0; i < blogRecommendList.browseBlogcontentList.length; i++) {
								imgHtml = getRecommendedImg(blogRecommendList.browseBlogcontentList[i])
								html = html
										+ '<li><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.browseBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.browseBlogcontentList[i].title
										+ '">'
										+ imgHtml
										+ '</a><p class="ellipsisp'
										+ (imgHtml == '' ? '0' : '1')
										+ '"><a href="/rzblog/blogcontent/details/'
										+ blogRecommendList.browseBlogcontentList[i].showId
										+ '" target="_blank" title="'
										+ blogRecommendList.browseBlogcontentList[i].title
										+ '">'
										+ blogRecommendList.browseBlogcontentList[i].title
										+ '</a></p>'
										+ (imgHtml == '' ? ''
												: '<span class="span1">'
														+ blogRecommendList.browseBlogcontentList[i].gtmCreate
														+ '</span>') + '</li>'
							}
							// 添加
							document.getElementById('browseBlogcontent')
									.insertAdjacentHTML('afterBegin', html)
						}

						$(".ellipsisp0").css("-webkit-line-clamp", "1")
						$(".ellipsisp1").css("-webkit-line-clamp", "2")
					}

					// 初始化博主简介
					// $("#aboutme").css('display', 'block');
					if (blogset.bloggersetBloggerProfile != null
							&& blogset.bloggersetBloggerProfile != '') {
						// 显示头像
						$("#showProfile").css('display', 'block');
						document.getElementById('showProfileImg').src = blogset.bloggersetBloggerProfile;
					}

					// 初始化广告
					// 请求广告信息
					if (blogset.basicsetShowAdvertisement == 0
							&& blogset.blogsetAdvertisementMaxShowNum > 0) {
						var param = {
							adPlacementSider : '0',
							maxShowNum : blogset.blogsetAdvertisementMaxShowNum
						};
						var blogadvertisementList = blogadvertisementListData(param);

						var blogadvertisementHtml = '';
						if (blogadvertisementList != null
								&& blogadvertisementList != ''
								&& blogadvertisementList.length > 0) {
							// 拼接
							for (var i = 0; i < blogadvertisementList.length; i++) {
								blogadvertisementHtml = blogadvertisementHtml
										+ '<li class="slide" style=" "><a href="'
										+ blogadvertisementList[i].adUrl
										+ '" target="_blank"><img src="'
										+ blogadvertisementList[i].adImage
										+ '" alt="'
										+ blogadvertisementList[i].adName
										+ '"></a> </li> ';
							}
							// 添加
							document.getElementById('banner3')
									.insertAdjacentHTML('afterBegin',
											blogadvertisementHtml);
							if (blogadvertisementList.length > 1) {
								// 轮播
								$('#banner3').easyFader();
							} else {
								// 只有一个广告
								// 去除一个class让其显示，并重新设置高度，即重新给个class
								$('#banner3').removeClass('fader3');
								$('#banner3').addClass('fader33');
								// 左右按钮隐藏
								$('.fader3_controls').css('display', 'none');
							}

							// 设置是否显示广告标志
							if (blogset.basicsetShowAdvertisementFlag == 0) {
								// 显示广告标志
								$(".ad.whitebg .abgc").css('display', 'block');
								$(".ad.whitebg .cbb").css('display', 'block');
							}
						} else {
							// 清除原有html
							var d1 = document.getElementById("siderad");
							d1.parentNode.removeChild(d1);
						}
					} else {
						// 清除原有html
						var d1 = document.getElementById("siderad");
						d1.parentNode.removeChild(d1);
					}

					// 初始化云标签
					if (blogTagsList != null && blogTagsList.length > 0) {
						// 显示
						// $("#cloud").css('display', 'block');
						// 生成
						var html = '';
						for (var i = 0; i < blogTagsList.length; i++) {
							html = html + '<a href="/rzblog/blogtags/?tags='
									+ blogTagsList[i].blogTagsName
									+ '" target="_blank">'
									+ blogTagsList[i].blogTagsName + '</a>';
						}
						// 添加
						document.getElementById('showTags').insertAdjacentHTML(
								'afterBegin', html)
					}

					// 初始化显示参数
					// 解析json友链
					var obj = "{}";
					if (blogsetFriendLinks.replace(/(^s*)|(s*$)/g, "").length != 0) {
						obj = JSON.parse(blogsetFriendLinks);
						if (obj != null) {
							if (obj.length != 0) {
								// 显示
								// $("#links").css('display', 'block');
								// 遍历添加已存在的参数
								for ( var index in obj) {
									key = obj[index].key;
									value = obj[index].value;
									// 写到网页中
									dynamicAddBlogsetFriendLinks(key, value)
								}
							}
						}
					}

					// 初始化其他
					var obj2 = "{}";
					if (blogsetSidebarOtherMessage.replace(/(^s*)|(s*$)/g, "").length != 0) {
						obj = JSON.parse(blogsetSidebarOtherMessage);
						if (obj != null) {
							if (obj.length != 0) {
								// 遍历添加已存在的参数
								for ( var index in obj) {
									key = obj[index].key;
									value = obj[index].value;

									// 写到网页中
									dynamicAddBlogsetSidebarOtherMessage(key,
											value)
								}
							}
						}
					}

				})

// 动态添加
function dynamicAddBlogsetFriendLinks(key, value) {
	html = '<li><a href="' + value + '" target="_blank">' + key + '</a></li>';
	// 添加到增加参数按钮之前
	document.getElementById('linksLi').insertAdjacentHTML('afterbegin', html)
}
// 动态添加
function dynamicAddBlogsetSidebarOtherMessage(key, value) {
	html = '<div class="divside links" id="other-' + key
			+ '" style="display: none"><h2 class="hometitle">' + key
			+ '</h2><p>' + value + '</p></div>'

	// 添加到增加参数按钮之前
	document.getElementById('follow-us')
			.insertAdjacentHTML('beforebegin', html)
}

// 列表推荐点赞点击排行图片没有就算，有封面用封面没有封面用内容图
function getRecommendedImg(content) {
	var image = '';
	var imgArray;
	if (content.cover != null && (content.cover.indexOf('/blogfiles/') == 0)) {
		image = content.cover;
	} else if (blogset.blogsetNoCoverpicUseContentpic == 0) {
		// 使用内容图片
		imgArray = getContentImgArray(content);
		if (imgArray != null && imgArray.length > 0) {
			// 内容图片第一张
			image = imgArray[0];
		} else {
			return image;
		}
	} else {
		return image;
	}
	return '<i><img src="' + image + '"></i>';
}
function setTabSet() {
	// 列表推荐点击排行点赞排行分割
	// tab
	var oLi = document.getElementById("tab").getElementsByTagName("a");
	var oUls = document.getElementsByName("sidenews");
	for (var i = 0; i < oLi.length; i++) {
		if (i == 0) {
			oLi[i].style.padding = "0 10px 0 0px";
		} else {
			oLi[i].style.padding = "0 10px";
		}
		oLi[i].index = i;
		oLi[i].onmouseover = function() {
			for (var n = 0; n < oLi.length; n++)
				oLi[n].className = "";
			this.className = "current";
			for (var n = 0; n < oUls.length; n++)
				oUls[n].style.display = "none";
			oUls[this.index].style.display = "block"
		}
	}
}