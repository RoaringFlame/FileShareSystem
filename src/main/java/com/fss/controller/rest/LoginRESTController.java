package com.fss.controller.rest;

import com.fss.controller.vo.JsonResultVO;
import com.fss.util.Conts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/validate")
public class LoginRESTController {

    /**
     * 检查验证码是否正确
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public JsonResultVO validateCheck(HttpSession session,
            String code) {
        String validate = (String) session.getAttribute(Conts.VALIDATE_CODE_KEY);
        code = code.toLowerCase();
        validate = validate.toLowerCase();
        if (code.equals(validate)) {
            return new JsonResultVO(JsonResultVO.SUCCESS, "验证码正确!");
        }
        return new JsonResultVO(JsonResultVO.FAILURE, "验证码错误!");
    }

    /**
     * 检查登录状况
     * （spring默认登陆页面登录失败会自动添加后缀，导致页面js请求失效）
     * @return 登录结果
     */
    @RequestMapping(value = "/statue", method = RequestMethod.GET)
    public JsonResultVO loginStatue(
            HttpSession session) {
        String statue = "";
        BadCredentialsException AuthStatue = (BadCredentialsException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (AuthStatue != null) {
            statue = "bad";
        }
        return new JsonResultVO(JsonResultVO.SUCCESS, statue);
    }
}
