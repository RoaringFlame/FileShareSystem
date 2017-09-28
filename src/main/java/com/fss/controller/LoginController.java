package com.fss.controller;

import com.eshore.fss.util.RandomValidateCodeGenerator;
import com.eshore.fss.vo.Conts;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Map;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public String login() {
        return "view/login";
    }

    @RequestMapping("/")
    public String index() {
        return "view/index";
    }

    @RequestMapping("/validate")
    public void validate(HttpSession session ,HttpServletRequest request, HttpServletResponse response) throws
            Exception {

        String key = RandomValidateCodeGenerator.randKey(4);

        try {
            Map.Entry<String, BufferedImage> randCode = new RandomValidateCodeGenerator().getRandCode(key);
            session.setAttribute(Conts.VALIDATE_CODE_KEY, randCode.getKey());

            ImageIO.write(randCode.getValue(), "JPEG", response.getOutputStream());

        } catch (Exception e) {
            System.out.println("生成验证码图片失败了!");
            e.printStackTrace();
        }
    }
}