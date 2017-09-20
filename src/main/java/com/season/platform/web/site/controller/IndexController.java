package com.season.platform.web.site.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.season.platform.web.site.entities.JsonResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by yingchun on 2017/9/18.
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private Producer captchaProducerMain;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {
        System.out.println("/");
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            return "redirect:/toLogin";
        }
        return "main/theme";
    }

    @RequestMapping("/toLogin")
    public String toLogin(Model model, HttpServletRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }


    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponseEntity<String> loginDo(HttpServletRequest request, HttpServletResponse response, String loginName, String password, String ifRemember) {
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println("login username " + loginName);
        System.out.println("login password " + password);
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        token.setRememberMe(true);
        try {
            currentUser.login(token);
        } catch (UnknownAccountException e) {
            return new JsonResponseEntity<String>(500, "该账号不存在");
        } catch (DisabledAccountException e) {
            return new JsonResponseEntity<String>(500, "该账号已被冻结");
        } catch (IncorrectCredentialsException e) {
            return new JsonResponseEntity<String>(500, "密码错误");
        } catch (RuntimeException e) {
            log.error("RuntimeException", e);
            return new JsonResponseEntity<String>(500, "未知错误,请联系管理员!");
        }
        return JsonResponseEntity.SUCCESS;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("调用logout");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return JsonResponseEntity.SUCCESS;
    }

    @RequestMapping("/help")
    public String help() {
        return "help";
    }


    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        return "main/home";
    }


    @RequestMapping("/captchaMain-image")
    public ModelAndView getKaptchaImageMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 禁止服务器端缓存
        response.setDateHeader("Expires", 0);

        // 设置标准的 HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // 设置IE扩展 HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // 设置标准 HTTP/1.0 不缓存图片
        response.setHeader("Pragma", "no-cache");

        // 返回一个 jpeg 图片，默认是text/html(输出文档的MIMI类型)
        response.setContentType("image/jpeg");

        // 为图片创建文本
        String capText = captchaProducerMain.createText();

        // 将文本保存在session中，这里就使用包中的静态变量吧
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // 创建带有文本的图片
        BufferedImage bi = captchaProducerMain.createImage(capText);
        ServletOutputStream out = response.getOutputStream();

        // 图片数据输出
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 生成随机数
     *
     * @param count 生成个数
     * @return String
     */
    protected String getRandomNum(int count) {
        Random ra = new Random();
        String random = "";
        for (int i = 0; i < count; i++) {
            random += ra.nextInt(9);
        }
        return random;
    }
}
