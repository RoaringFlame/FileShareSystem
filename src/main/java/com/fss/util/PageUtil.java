package com.fss.util;

import com.fss.controller.vo.PageVO;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageUtil {

    public static PageVO getPageList(PageConfig page, List list) {
        PageVO pageVo = new PageVO();
        pageVo.setDataList(list);
        pageVo.setPageNum(page.getPageNum());
        pageVo.setDataCount(page.getRowCount());
        pageVo.setPageCount(page.getPageCount());
        pageVo.setHasPrePage(page.getPageNum() > 1);
        if (pageVo.getHasPrePage()) {
            pageVo.setPrePage(page.getPageNum() - 1);
        }
        pageVo.setHasNextPage(page.getPageNum() < pageVo.getPageCount());
        if (pageVo.getHasNextPage()) {
            pageVo.setNextPage(page.getPageNum() + 1);
        }
        return pageVo;
    }

    public static PageVO generateBy(Page page, List list) {
        PageVO pageVO = new PageVO();
        pageVO.setDataList(list);
        pageVO.setPageNum(page.getNumber() + 1);
        Long totalData = page.getTotalElements();
        pageVO.setDataCount(totalData.intValue());
        pageVO.setPageCount(page.getTotalPages());
        pageVO.setHasPrePage(page.hasPrevious());
        if (pageVO.getHasPrePage()) {
            pageVO.setPrePage(pageVO.getPageNum() - 1);
        }else{
            pageVO.setPrePage(pageVO.getPageNum());
        }
        pageVO.setHasNextPage(page.hasNext());
        if (pageVO.getHasNextPage()) {
            pageVO.setNextPage(pageVO.getPageNum() + 1);
        }else{
            pageVO.setPrePage(pageVO.getPageNum());
        }
        return pageVO;
    }
}
