package com.fss.controller.rest;

import com.fss.controller.vo.FileHistoryVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 下载，修改版本操作的查询
 */
@RestController
@RequestMapping("/operate")
public class OperateRESTController {

    //预留接口 显示所有操作记录
    @RequestMapping(value = "/uploaded/{userId}",method = RequestMethod.GET)
    public List<FileHistoryVO> showHistory(@PathVariable String userId){
        return null;
    }
}
