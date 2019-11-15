$(document)
		.ready(
				function() {
					title = basicsetTitle + "-" + blogcolumn.blogColumnName;
					// 设置专栏信息
					if (blogcolumn != null && blogcolumn != '') {
						// 设置专栏背景
						var bgImg = getSysDefaultBgImg();
						$(".sh")
								.css(
										"background",
										'url('
												+ ((blogcolumn.backImg == null || blogcolumn.backImg == '') ? bgImg
														: blogcolumn.backImg)
												+ ') no-repeat');
						// 动态设置css实现拉伸效果
						$(".sh").css("background-size", "cover")
						$(".sh").css("background-position", "top center")

						// 设置专栏信息
						$("#n1").css('display', 'block');
						$("#n2").css('display', 'block');
						if (blogcolumn.columnType == 'M') {
							// 设置title
							title = basicsetTitle + "-"
									+ blogcolumn.blogColumnName;

							// 主专栏
							// 首页，主专栏
							document.getElementById("n1").href = "/rzblog";
							document.getElementById("n1").innerText = "首页";

							document.getElementById("n2").href = '/rzblog/column'
									+ blogcolumn.url;
							document.getElementById("n2").innerText = blogcolumn.blogColumnName;
						} else if (blogcolumn.columnType == 'C') {
							// 设置title
							title = basicsetTitle + "-"
									+ blogcolumn.parent.blogColumnName + "（"
									+ blogcolumn.blogColumnName + "）";

							// 主专栏，次专栏
							document.getElementById("n1").href = '/rzblog/column'
									+ blogcolumn.parent.url;
							document.getElementById("n1").innerText = blogcolumn.parent.blogColumnName;

							document.getElementById("n2").href = '/rzblog/column'
									+ blogcolumn.parent.url + blogcolumn.url;
							document.getElementById("n2").innerText = blogcolumn.blogColumnName;
						}
					}
					document.title = title;

					// 设置寄语
					document.getElementById("columnMsg").innerHTML = blogcolumn.columnMessage;
				})

var pageSize = blogset.blogsetPerpageShowNum;
var pageNum = 1;
var total;
var backColor = (blogset.stylesetBackColor == null || blogset.stylesetBackColor == '') ? '#FFF'
		: blogset.stylesetBackColor;
// 平滑度
var blogsRadius = getBorderRadius(100, blogset.stylesetSmoothStyle);
function resetScroll() {
	// 检测ie 6789
	if (!(/msie [6|7|8|9]/i.test(navigator.userAgent))) {
		new scrollReveal({
			reset : true
		});
	}
}

// 是否手动自动加载
var autoLoading = blogset.basicsetAutoLoading;
// 是否显示文章来源
var basicsetShowArticleSource = blogset.basicsetShowArticleSource;
// 滚动事件
// 到底部自动加载
if (autoLoading == 0) {
	$(document).scroll(
			function() {
				if ($(document).scrollTop() + window.innerHeight == $(document)
						.height()) {
					if (total > (pageNum - 1) * pageSize) {
						nextPage();
					}
				}
			});
}
var blogadvertisementList;
$(function() {
	bindList(pageNum);
	// resetScroll();

	// 请求广告信息
	if (blogset.basicsetShowAdvertisement == 0
			&& blogset.blogsetAdvertisementMaxShowNum > 0) {
		var param = {
			adPlacementMain : '0',
			maxShowNum : blogset.blogsetAdvertisementMaxShowNum
		};
		blogadvertisementList = blogadvertisementListData(param);
	}
});
// 下一页
function nextPage() {
	bindList(pageNum)
}
// 绑定查询列表
function bindList() {
	var flag = 0;
	if (blogcolumn != null && blogcolumn != '') {
		flag = blogcolumn.columnType == 'C' ? 1 : 0;
	}
	$
			.ajax({
				url : '/rzblog/columnList',
				method : 'post',
				data : {
					status : 1,
					blogColumnName : blogcolumn.blogColumnName,
					flag : flag,
					pageSize : pageSize,
					pageNum : pageNum,
					orderByColumn : "gtm_create",
					isAsc : "desc"
				},
				dataType : 'json',
				success : function(data) {
					var rows = data.rows;
					if (rows == null || rows == '') {
						var nodataHtml = '<p class="news_title">暂无文章，请浏览其它专栏！</p>';
						$('#blogsbox').append(nodataHtml);
						return;
					}

					// 手动加载删除
					if (autoLoading == 1) {
						var removeObj = document
								.getElementsByClassName('clearfix')[0];
						if (removeObj) {
							removeObj.parentNode.removeChild(removeObj);
						}
					}

					total = data.total;

					var htmlText = "";
					var blogTypeHtml = '';
					var uuid;
					for (i = 0; i < rows.length; i++) {
						// 封面
						coverHtml = getCoverHtml(rows[i]);
						// 简介
						htmlIntroduction = getHtmlIntroduction(rows[i]);
						// 来源
						articleSourceHtml = getHtmlArticleSource(rows[i],
								basicsetShowArticleSource);
						// 专栏
						// 默认的不显示，其他的显示
						column = getHtmlColumn(rows[i]);
						if (rows[i].blogType == 0) {
							blogTypeHtml = '<span class="lm f_l"><a href="/rzblog/blogarticletype/?blogtype=文章" title="文章" target="_blank" class="blogType0">文章</a></span>';
						} else if (rows[i].blogType == 1) {
							blogTypeHtml = '<span class="lm f_l"><a href="/rzblog/blogarticletype/?blogtype=软件" title="软件" target="_blank" class="blogType1">软件</a></span>';
						} else if (rows[i].blogType == 2) {
							blogTypeHtml = '<span class="lm f_l"><a href="/rzblog/blogarticletype/?blogtype=影视" title="影视" target="_blank" class="blogType2">影视</a></span>';
						}
						htmlText = '<div class="blogs" style="background-color:'
								+ backColor
								+ ';border-radius:'
								+ blogsRadius
								+ 'px;" data-scroll-reveal="enter bottom over 1s"><h3 class="blogtitle">'
								+ '<a href="/rzblog/blogtype/?type='
								+ rows[i].type
								+ '" title="'
								+ rows[i].type
								+ '" target="_blank">'
								+ '<span class="article-type type-'
								+ (rows[i].type == '原创' ? '1'
										: (rows[i].type == '转载' ? '2' : '3'))
								+ '">'
								+ (rows[i].type == '原创' ? '原'
										: (rows[i].type == '转载' ? '转' : '译'))
								+ '</span>'
								+ '</a>'
								+ '<a href="/rzblog/blogcontent/details/'
								+ rows[i].showId
								+ '" target="_blank">'
								+ rows[i].title
								+ '</a></h3>'
								+ coverHtml
								+ htmlIntroduction
								+ '<div class="bloginfo"><ul>'
								+ '<li class="blogTypeHtml"> '
								+ blogTypeHtml
								+ '</li>'
								+ '<li class="author"><i class="fa fa-user"></i> '
								+ '<a href="/rzblog/blogauthor/?author='
								+ rows[i].author
								+ '" title="'
								+ rows[i].author
								+ '" target="_blank">'
								+ rows[i].author
								+ '</a>'
								+ '</li>'
								+ column
								+ '<li class="timer"><i class="fa fa-calendar-o"></i> '
								+ rows[i].gtmCreate.substring(0, 10)
								+ '</li>'
								+ '<li class="view"><span><i class="fa fa-eye"></i> '
								+ rows[i].browseNum
								+ '</span></li>'
								+ '<li class="like"><i class="fa fa-thumbs-up"></i> '
								+ rows[i].likeNum
								+ '</li>'
								+ '<li class="commentNum"><i class="fa fa-comments"></i> '
								+ (rows[i].commentsNum < 0 ? '0'
										: rows[i].commentsNum)
								+ '</li>'
								+ articleSourceHtml + '</ul></div></div>';
						// 设置广告位置
						if (i == 3) {
							uuid = guid();
							// 获取广告标签内的html
							var firstHtml = '<div class="blogs ad" style="background-color:'
									+ backColor
									+ ';border-radius:'
									+ blogsRadius
									+ 'px;" data-scroll-reveal="enter bottom over 1s">'
									+ '<div class="ad whitebg">';
							blogadvertisementHtml = getBlogadvertisementHtml(
									firstHtml, uuid);
							htmlText = htmlText + blogadvertisementHtml;
						}
						$('#blogsbox').append(htmlText);

						if (i == 3 && blogadvertisementList.length > 0) {
							if (blogadvertisementList.length > 1) {
								if ($('#' + uuid)) {
									$('#' + uuid).easyFader();
								}
							} else if (blogadvertisementList.length == 1) {
								// 只有一个广告
								// 去除一个class让其显示，并重新设置高度，即重新给个class
								$('#' + uuid).removeClass('fader2');
								$('#' + uuid).addClass('faderNew2');
								// 左右按钮隐藏
								$('.fader2_controls').css('display', 'none');
							}
							// 设置是否显示广告标志
							if (blogset.basicsetShowAdvertisementFlag == 0) {
								// 显示广告标志
								$(".abgc").css('display', 'block');
								$(".cbb").css('display', 'block');
							}
						}
					}
					// 设置手动加载
					var clearfixHtml;
					if (total < pageNum * pageSize + 1) {
						clearfixHtml = '<div class="clearfix"><p id="flagLoaded" >已全部加载</p></div>';
						$('#blogsbox').append(clearfixHtml);
					} else {
						if (autoLoading == 1) {
							clearfixHtml = '<div class="clearfix"><a id="flagLoad" href="javascript:void(0)" onclick="nextPage()">加载更多</a></div>';
							$('#blogsbox').append(clearfixHtml);
						}
					}

					// 异步加载html每次需要重新滚动设置
					resetScroll();
					pageNum++;
				}
			});
}

// 获取广告区html
function getBlogadvertisementHtml(firstHtml, uuid) {
	var blogadvertisementHtml = '';
	if (blogadvertisementList == null || blogadvertisementList == ''
			|| blogadvertisementList.length < 1) {
		return blogadvertisementHtml;
	}

	// 拼接
	blogadvertisementHtml = firstHtml
			+ '<div class=""><div class="banner2"><div id="' + uuid
			+ '" class="fader2" >';
	for (var i = 0; i < blogadvertisementList.length; i++) {
		blogadvertisementHtml = blogadvertisementHtml
				+ '<li class="slide" style=" "><a href="'
				+ blogadvertisementList[i].adUrl
				+ '" target="_blank"><img src="'
				+ blogadvertisementList[i].adImage + '" alt="'
				+ blogadvertisementList[i].adName + '"></a> </li> ';
	}
	return blogadvertisementHtml
			+ '<div class="fader2_controls"><div class="page prev" data-target="prev">&lsaquo;</div><div class="page next" data-target="next">&rsaquo;</div><ul class="pager_list"></ul></div>'
			+ '<div id="abgc" class="abgc" dir="ltr" aria-hidden="true" style="display: none"> <div id="abgb" class="abgb" style="display: block;"> <div class="il-wrap"> <div class="il-icon"> <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 24 16"> <path d="M10.90 4.69L10.90 5.57L3.17 5.57L3.17 7.22L3.17 7.22Q3.17 9.15 3.06 10.11L3.06 10.11L3.06 10.11Q2.95 11.07 2.58 11.92L2.58 11.92L2.58 11.92Q2.21 12.77 1.27 13.56L1.27 13.56L0.59 12.93L0.59 12.93Q2.29 11.82 2.29 8.66L2.29 8.66L2.29 4.69L6.11 4.69L6.11 2.95L7.00 2.95L7.00 4.69L10.90 4.69ZM23.00 7.34L23.00 8.22L12.80 8.22L12.80 7.34L17.55 7.34L17.55 5.53L15.12 5.53L15.12 5.53Q14.55 6.53 13.86 7.07L13.86 7.07L13.10 6.46L13.10 6.46Q14.44 5.46 14.95 3.33L14.95 3.33L15.84 3.55L15.84 3.55Q15.77 3.86 15.49 4.65L15.49 4.65L17.55 4.65L17.55 2.84L18.47 2.84L18.47 4.65L21.86 4.65L21.86 5.53L18.47 5.53L18.47 7.34L23.00 7.34ZM21.45 8.88L21.45 13.63L20.53 13.63L20.53 12.78L15.32 12.78L15.32 13.63L14.41 13.63L14.41 8.88L21.45 8.88ZM15.32 11.90L20.53 11.90L20.53 9.76L15.32 9.76L15.32 11.90Z"></path></svg></div></div></div></div><div id="cbb" class="cbb" aria-hidden="true" style="display: none" onclick="closeAd2(this);"><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 15 15"><path d="M3.25,3.25l8.5,8.5M11.75,3.25l-8.5,8.5"></path></svg></div>'
			+ '</div></div></div></div></div>';
}

// 拼接封面
function getCoverHtml(data) {
	// 图片不存在将以默认图片显示
	defaultImg = getOneDefaultImg(1);
	var image;
	var imgArray;
	var coverHtml = '';
	// 原则是：如果有封面图片则使用封面图片，没有封面图片根据设置是否使用文章内容图片，文章没有图片的根据设置判断是否使用默认图片，不使用默认图片的则直接文字显示
	// 出错使用默认图片
	if (data.cover != null && (data.cover.indexOf('/blogfiles/') == 0)) {
		image = data.cover;
	} else if (blogset.blogsetNoCoverpicUseContentpic == 0) {
		// 使用内容图片
		imgArray = getContentImgArray(data);
		if (imgArray != null && imgArray.length > 0) {
			// 内容图片
			if (imgArray.length == 1) {
				image = imgArray[0];
			} else {
				coverHtml = '<span class="bplist"><a href="/rzblog/blogcontent/details/'
						+ data.showId
						+ '" target="_blank" title="'
						+ data.title + '">';
				// 不止一张
				for (var i = 0; i < (imgArray.length > 3 ? 3 : imgArray.length); i++) {
					coverHtml = coverHtml + '<li><img src="' + imgArray[i]
							+ '" alt="' + data.title + '"></li>';
				}

				return coverHtml + '</a></span>';
			}
		} else {
			if (blogset.blogsetNoPicUseDefault == 0) {
				// 是否使用默认图片
				image = defaultImg;
			} else {
				// 不使用图片
				return coverHtml;
			}
		}
	} else {
		if (blogset.blogsetNoPicUseDefault == 0) {
			// 是否使用默认图片
			image = defaultImg;
		} else {
			// 不使用图片
			return coverHtml;
		}
	}

	coverHtml = '<span class="blogpic"><a href="/rzblog/blogcontent/details/'
			+ data.showId + '" target="_blank" title="' + data.title
			+ '"><img src="' + image + '" alt="' + data.title
			+ '" onerror="this.src=' + defaultImg + '"></a></span>';
	return coverHtml;
}

// 获取文章所有图片
function getContentImgArray(data) {
	// 此判断不正确，后续跟进
	if (data.images == null || data.images == '') {
		return null;
	}
	return data.images.split(",");

}

// 简介
function getHtmlIntroduction(data) {
	var htmlIntroduction = '';
	if (data.showIntroduction == 0) {
		htmlIntroduction = '<a href="/rzblog/blogcontent/details/'
				+ data.showId + '" target="_blank"><p class="blogtext">'
				+ data.introduction + '......</p></a>';
	}
	return htmlIntroduction;
}
// 专栏
function getHtmlColumn(data) {
	var htmlColumn = '';
	if (data.blogColumnName != '默认') {
		htmlColumn = '<li class="lmname"><i class="fa fa-list"></i> '
				+ data.blogColumnName + '</li>';
	}
	return htmlColumn;
}

// 来源
function getHtmlArticleSource(data, basicsetShowArticleSource) {
	var html = '';
	if (basicsetShowArticleSource == 0 && data.articleSource != null
			&& data.articleSource != '' && data.articleSource != '本站') {
		html = '<li class="articleSource"><i class="fa fa-thumb-tack"></i> '
				+ '<a href="/rzblog/blogsource/?source=' + data.articleSource
				+ '" title="' + data.articleSource + '" target="_blank">'
				+ data.articleSource + '</a>' + '</li>';
	}
	return html;
}