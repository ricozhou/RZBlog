$(document).ready(
		function() {
			$(".modal").appendTo("body"), $("[data-toggle=popover]").popover(),
					$(".collapse-link")
							.click(
									function() {
										var div_ibox = $(this).closest(
												"div.ibox"), e = $(this).find(
												"i"), i = div_ibox
												.find("div.ibox-content");
										i.slideToggle(200), e.toggleClass(
												"fa-chevron-up").toggleClass(
												"fa-chevron-down"), div_ibox
												.toggleClass("").toggleClass(
														"border-bottom"),
												setTimeout(function() {
													div_ibox.resize();
												}, 50);
									}), $(".close-link").click(function() {
						var div_ibox = $(this).closest("div.ibox");
						div_ibox.remove()
					});
		});