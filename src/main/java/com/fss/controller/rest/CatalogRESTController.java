package com.fss.controller.rest;


import com.fss.controller.vo.CatalogVO;
import com.fss.controller.vo.JsonResultVO;
import com.fss.dao.domain.User;
import com.fss.dao.enums.UserRole;
import com.fss.service.ICatalogService;
import com.fss.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
public class CatalogRESTController {

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private IUserService userService;

    /**
     * 添加或修改目录，注：通过是否有传递catalogVo.id判断
     * @param catalogVO 目录数据类
     * @return json信息
     */
    // TODO: 2017/9/4 权限配置，最高级才可以发送这些请求，nowUser加cache！
    @RequestMapping(value = "/addOrUpdate",method = RequestMethod.POST)
    public JsonResultVO addOrUpdateCatalog(CatalogVO catalogVO){
    	User user = userService.getNowUser();
    	if(user.getRole()== UserRole.MANAGER)
    		return this.catalogService.addOrUpdate(catalogVO);
    	else
    		return new JsonResultVO(JsonResultVO.FAILURE, "权限不足");
    }

    /**
     * 删除目录
     * @param catalogId 目录id
     * @return json信息
     */
    @RequestMapping(value = "/delete/{catalogId}", method = RequestMethod.DELETE)
    public JsonResultVO deleteCatalog(@PathVariable String catalogId){
    	User user = userService.getNowUser();
    	if(user.getRole()==UserRole.MANAGER)
    		return this.catalogService.deleteCatalogById(catalogId);
    	else
    		return new JsonResultVO(JsonResultVO.FAILURE, "权限不足");
    }

    /**
     * 得到单个目录信息,注：此处为保留接口，防止查询描述需求
     * @param catalogId
     * @return
     */
    @RequestMapping(value = "/showInfo/{catalogId}",method = RequestMethod.GET)
    public CatalogVO showCatalogInfo(@PathVariable String catalogId){
        return this.catalogService.getCatalogInfo(catalogId);
    }

    /**
     * 得到所有目录信息,注：此处为保留接口，防止查询描述需求
     * @param
     * @return
     */
    @RequestMapping(value = "/showCatalogList",method = RequestMethod.GET)
    public List<CatalogVO> showCatalogList(){
        return this.catalogService.getCatalogList();
    }
    
    /**
     * 放入容器中，通过算法返回Map<key1,Map<key2,value>>数据结构
     * 其中:key1为父目录id，Map<key2,value>为目录信息，key2:目录id，value目录名
     * 返回数据结构表示所有父目录（通过id对应）下的子目录信息
     */
    @RequestMapping(value = "/showAll",method = RequestMethod.GET)
    public Map<String,Map<String,String>> showAllCatalog(){
        return this.catalogService.getAllCatalog();
    }
}
