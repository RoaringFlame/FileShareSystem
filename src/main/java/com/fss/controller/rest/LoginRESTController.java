package com.fss.controller.rest;

import com.fss.controller.vo.JsonResultVo;
import com.fss.util.Conts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/validate")
public class LoginRESTController {

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public JsonResultVo validateCheck(HttpSession session,
            String code) {
        String validate = (String) session.getAttribute(Conts.VALIDATE_CODE_KEY);
        code = code.toLowerCase();
        validate = validate.toLowerCase();
        if (code.equals(validate)) {
            return new JsonResultVo(JsonResultVo.SUCCESS, "验证码正确!");
        }
        return new JsonResultVo(JsonResultVo.FAILURE, "验证码错误!");
    }

    @RequestMapping(value = "/statue", method = RequestMethod.GET)
    public JsonResultVo loginStatue(
            HttpSession session) {
        String statue = "";
        BadCredentialsException AuthStatue = (BadCredentialsException) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (AuthStatue != null) {
            statue = "bad";
        }
        return new JsonResultVo(JsonResultVo.SUCCESS, statue);
    }
}
