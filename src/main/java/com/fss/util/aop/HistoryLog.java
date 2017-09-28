package com.fss.util.aop;

import org.aspectj.lang.annotation.Aspect;

@Aspect public class HistoryLog {

    //    @Autowired
    //    private GoodsTypeRepository goodsTypeRepository;
    //
    //    @AfterReturning("execution(* com.fss.service.GoodsService.goodsSearch(Long,String,..))" +
    //            "&& args(goodsTypeId,title,..)")
    //    public void changeGoodsState(Long goodsTypeId, String title) {
    //        String typeName = "";
    //        if (goodsTypeId == null) {
    //            typeName = "所有";
    //        } else {
    //            GoodsType goodsType = this.goodsTypeRepository.findOne(goodsTypeId);
    //            typeName = goodsType.getTypeName();
    //        }
    //        if (title == null || "".equals(title)) title = "空白";
    //        System.out.println("---------------typeName = " + typeName +
    //                "-------------title = " + title + "---------------------");
    //    }
}