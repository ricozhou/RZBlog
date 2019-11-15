$(document)
		.ready(
				function() {
					try {
						// 获取客户端信息并存储到后台
						// // 浏览器代码名
						// var clientAppCodeName = navigator.appCodeName;
						// // 浏览器名称
						// var clientAppName = navigator.appName;
						// // 浏览器平台和版本
						// var clientAppVersion = navigator.appVersion;
						// // 是否开启cookie
						// var clientCookieEnabled = navigator.cookieEnabled;
						// 操作系统平台
						var clientPlatform = navigator.platform;
						// User-agent头部值
						var clientUserAgent = navigator.userAgent;
						var clientBrowseMessage = getClientBrowseName(clientUserAgent
								.toLowerCase());
						// 浏览器系统
						var clientBrowsePlatform = clientBrowseMessage[0];
						// 浏览器真名
						var clientBrowseName = clientBrowseMessage[1];
						// 浏览器版本
						var clientBrowseVersion = clientBrowseMessage[2];

						// 浏览器ip
						var clientBrowseIp = '未知';

						// 浏览器地理位置
						var clientBrowseCity = '未知';

						try {
							clientBrowseIp = returnCitySN["cip"];
							clientBrowseCity = returnCitySN["cname"];
						} catch (err) {
							// 处理错误
						}

						var clientBrowseAppAndPc = '未知';
						if ((clientUserAgent
								.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
							clientBrowseAppAndPc = '移动端';
						} else {
							clientBrowseAppAndPc = 'PC端';
						}

						// 浏览地址
						var clientBrowseUrl = window.location.href;
						$.ajax({
							cache : true,
							type : "POST",
							url : "/rzblog/blogbrowe/saveBlogbrowe",
							data : {
								"clientPlatform" : clientPlatform,
								"clientUserAgent" : clientUserAgent,
								"clientBrowsePlatform" : clientBrowsePlatform,
								"clientBrowseName" : clientBrowseName,
								"clientBrowseVersion" : clientBrowseVersion,
								"clientBrowseIp" : clientBrowseIp,
								"clientBrowseCity" : clientBrowseCity,
								"clientBrowseAppAndPc" : clientBrowseAppAndPc,
								"clientBrowseUrl" : clientBrowseUrl
							},
							async : false,
							success : function(data) {
							}
						});

					} catch (err) {
						// 处理错误
					}
				})

// 获取具体浏览器
function getClientBrowseName(agent) {
	var arr = [];
	var system = agent.split(' ')[1].split(' ')[0].split('(')[1];
	arr.push(system);
	var regStr_edge = /edge\/[\d.]+/gi;
	var regStr_ie = /trident\/[\d.]+/gi;
	var regStr_ff = /firefox\/[\d.]+/gi;
	var regStr_chrome = /chrome\/[\d.]+/gi;
	var regStr_saf = /safari\/[\d.]+/gi;
	var regStr_opera = /opr\/[\d.]+/gi;
	// IE
	if (agent.indexOf("trident") > 0) {
		arr.push(agent.match(regStr_ie)[0].split('/')[0]);
		arr.push(agent.match(regStr_ie)[0].split('/')[1]);
		return arr;
	}
	// Edge
	if (agent.indexOf('edge') > 0) {
		arr.push(agent.match(regStr_edge)[0].split('/')[0]);
		arr.push(agent.match(regStr_edge)[0].split('/')[1]);
		return arr;
	}
	// firefox
	if (agent.indexOf("firefox") > 0) {
		arr.push(agent.match(regStr_ff)[0].split('/')[0]);
		arr.push(agent.match(regStr_ff)[0].split('/')[1]);
		return arr;
	}
	// Opera
	if (agent.indexOf("opr") > 0) {
		arr.push(agent.match(regStr_opera)[0].split('/')[0]);
		arr.push(agent.match(regStr_opera)[0].split('/')[1]);
		return arr;
	}
	// Safari
	if (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0) {
		arr.push(agent.match(regStr_saf)[0].split('/')[0]);
		arr.push(agent.match(regStr_saf)[0].split('/')[1]);
		return arr;
	}
	// Chrome
	if (agent.indexOf("chrome") > 0) {
		arr.push(agent.match(regStr_chrome)[0].split('/')[0]);
		arr.push(agent.match(regStr_chrome)[0].split('/')[1]);
		return arr;
	} else {
		arr.push('未知');
		arr.push('未知');
		return arr;
	}
}

var blogRecommendList;
function blogRecommendListData() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/blogRecommendList",
		data : {},
		async : false,
		success : function(data) {
			blogRecommendList = data;
		}
	});
}

// 标签请求
var blogTagsList;
function blogTagsListData() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/tagsMsgList",
		data : {},
		async : false,
		success : function(data) {
			blogTagsList = data;
		}
	});
}
var blogRecommendList2;
function blogRecommendListData2() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/blogRecommendList2",
		data : {},
		async : false,
		success : function(data) {
			blogRecommendList2 = data;
		}
	});
}

// 请求公告
var blogNoticeList;
function blogNoticeListData() {
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/blogNoticeList",
		data : {},
		async : false,
		success : function(data) {
			blogNoticeList = data;
		}
	});
}
// 请求广告
function blogadvertisementListData(data) {
	var blogadvertisementList;
	$.ajax({
		cache : true,
		type : "POST",
		url : "/rzblog/blogadvertisementList",
		data : data,
		async : false,
		success : function(data) {
			blogadvertisementList = data;
		}
	});
	return blogadvertisementList;
}

// 公告滚动方法
function noticeUp(obj, top, time) {
	$(obj).animate({
		marginTop : top
	}, time, function() {
		$(this).css({
			marginTop : "2px"
		}).find(":first").appendTo(this);
	})
}

// 特别推荐封面必须有图片
function getSpecialRecommendedImg(content, blogsetDefaultPic) {
	// 图片不存在将以默认图片显示
	// 先判断设置中是否存在默认图片，不存在则使用系统自带图片
	defaultImg = getOneDefaultImg(0, blogsetDefaultPic);
	var image = '';
	var imgArray;
	// 原则是：特别推荐必须有图片
	// 出错使用默认图片
	if (content.cover != null && (content.cover.indexOf('/blogfiles/') == 0)) {
		image = content.cover;
	} else {
		// 使用内容图片
		imgArray = getContentImgArray(content);
		if (imgArray != null && imgArray.length > 0) {
			// 内容图片第一张
			image = imgArray[0];
		} else {
			image = defaultImg;
		}
	}
	return '<i><img src="' + image + '"></i>';
}
// 获取文章所有图片
function getContentImgArray(data) {
	// 此判断不正确，后续跟进
	if (data.images == null || data.images == '') {
		return null;
	}
	return data.images.split(",");
}

// 获取默认图片
// flag为0表示必须有图片没有默认则使用系统图片，1表示没有默认图片则直接返回
function getOneDefaultImg(flag, blogsetDefaultPic) {
	var arrayDefaultImg = (blogsetDefaultPic == null) ? null
			: (blogsetDefaultPic.split(","));
	if (arrayDefaultImg != null && arrayDefaultImg != ''
			&& arrayDefaultImg.length > 0) {
		// 有图片
		return arrayDefaultImg[getRandomNum(0, arrayDefaultImg.length)]
	}
	if (flag == 0) {
		// 否则从系统中抽取
		return '/img/blog/default' + getRandomNum(0, 10) + '.jpg';
	}
	return '';
}
// 专栏背景图片默认,系统图片
function getSysDefaultBgImg() {
	// 从系统中抽取
	// 默认只有5张
	return '/img/blog/column_bg' + getRandomNum(0, 5) + '.jpg';
}
// 获取随机数
function getRandomNum(n, m) {
	// 前闭后开
	return Math.floor(Math.random() * (m - n) + n);
}

/**
 * 动态加载JS
 * 
 * @param {string}
 *            url 脚本地址
 * @param {function}
 *            callback 回调函数
 */
function dynamicLoadJsFile(url, callback) {
	var head = document.getElementsByTagName('head')[0];
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = url;
	if (typeof (callback) == 'function') {
		script.onload = script.onreadystatechange = function() {
			if (!this.readyState || this.readyState === "loaded"
					|| this.readyState === "complete") {
				callback();
				script.onload = script.onreadystatechange = null;
			}
		};
	}
	head.appendChild(script);
}
/**
 * 动态加载CSS
 * 
 * @param {string}
 *            url 样式地址
 */
function dynamicLoadCssFile(url) {
	var head = document.getElementsByTagName('head')[0];
	var link = document.createElement('link');
	link.type = 'text/css';
	link.rel = 'stylesheet';
	link.href = url;
	head.appendChild(link);
}

const
licenseNameMap = new Map();
licenseNameMap.set(0, 'Affero GPL');
licenseNameMap.set(1, 'Apache v2 License');
licenseNameMap.set(2, 'Artistic License 2.0');
licenseNameMap.set(3, 'BSD (3-Clause) License');
licenseNameMap.set(4, 'BSD 2-Clause License');
licenseNameMap.set(5, 'Eclipse Public License v1.0');
licenseNameMap.set(6, 'GPL v2');
licenseNameMap.set(7, 'GPL v3');
licenseNameMap.set(8, 'LGPL v2.1');
licenseNameMap.set(9, 'LGPL v3');
licenseNameMap.set(10, 'MIT License');
licenseNameMap.set(11, 'Mozilla Public License Version 2.0');
licenseNameMap.set(12, 'OSL 3.0');
licenseNameMap.set(13, 'Public Domain(Unlicense)');
licenseNameMap.set(14, 'WTFPL');
licenseNameMap.set(15, 'Zlib');

const
licenseUrlMap = new Map();
licenseUrlMap.set(0, 'https://opensource.org/licenses/AGPL-3.0');
licenseUrlMap.set(1, 'https://opensource.org/licenses/Apache-2.0');
licenseUrlMap.set(2, 'https://opensource.org/licenses/Artistic-2.0');
licenseUrlMap.set(3, 'https://opensource.org/licenses/BSD-3-Clause');
licenseUrlMap.set(4, 'https://opensource.org/licenses/BSD-2-Clause');
licenseUrlMap.set(5, 'https://opensource.org/licenses/EPL-1.0');
licenseUrlMap.set(6, 'https://opensource.org/licenses/GPL-2.0');
licenseUrlMap.set(7, 'https://opensource.org/licenses/GPL-3.0');
licenseUrlMap.set(8, 'https://opensource.org/licenses/LGPL-2.1');
licenseUrlMap.set(9, 'https://opensource.org/licenses/LGPL-3.0');
licenseUrlMap.set(10, 'https://opensource.org/licenses/MIT');
licenseUrlMap.set(11, 'https://opensource.org/licenses/MPL-2.0');
licenseUrlMap.set(12, 'https://opensource.org/licenses/OSL-3.0');
licenseUrlMap.set(13, 'https://choosealicense.com/licenses/unlicense/');
licenseUrlMap.set(14, 'http://www.wtfpl.net/about/');
licenseUrlMap.set(15, 'https://opensource.org/licenses/Zlib');

function getLicenseNameByIndex(index) {
	return licenseNameMap.get(index);
}
function getLicenseUrlByIndex(index) {
	return licenseUrlMap.get(index);
}

function S4() {
	return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};
// 生成uuid
function guid() {
	return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4()
			+ S4() + S4());
};

// 关闭广告
function closeAd(e) {
	// $(e).parent().css('display', 'none');
	// 滑动关闭
	$(e).parent().fadeToggle();
}
// 主页关闭广告
function closeAd2(e) {
	// $(e).parent().parent().parent().parent().parent().css('display', 'none');
	// 滑动关闭
	$(e).parent().parent().parent().parent().parent().fadeToggle();
}
// 侧边栏关闭广告
function closeAd3(e) {
	// $(e).parent().parent().parent().parent().css('display', 'none');
	$(e).parent().parent().parent().parent().fadeToggle();
}

// checkip
function checkIp(value) {
	if (value == null || value == '') {
		return false;
	}
	var ip = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	return (ip.test(value));
}