package com.fss.util;

import com.fss.controller.vo.PageVO;

import java.util.List;

public class PageUtil {

	public static PageVO getPageList(PageConfig page , List list ){
		PageVO pageVo = new PageVO();
		pageVo.setDataList(list);
		pageVo.setPageNum(page.getPageNum());
		pageVo.setDataCount(page.getRowCount());
		pageVo.setPageCount(page.getPageCount() );
		pageVo.setHasPrePage(page.getPageNum() > 1);
		if(pageVo.getHasPrePage()){
			pageVo.setPrePage(page.getPageNum() - 1);
		}
		pageVo.setHasNextPage(page.getPageNum() < pageVo.getPageCount());
		if(pageVo.getHasNextPage()){
			pageVo.setNextPage(page.getPageNum() + 1);
		}
		return pageVo;
	}
}
