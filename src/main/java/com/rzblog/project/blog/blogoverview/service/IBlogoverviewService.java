package com.rzblog.project.blog.blogoverview.service;

import com.rzblog.project.blog.blogoverview.domain.Blogoverview;

/**
 * 文章内容 服务层
 * 
 * @author ricozhou
 * @date 2018-06-12
 */
public interface IBlogoverviewService {

	/**
	 * @param blogoverview 
	 * @date Oct 25, 2018 10:01:48 AM
	 * @Desc
	 * @return
	 */
	Blogoverview selectBlogoverview(Blogoverview blogoverview);

}
