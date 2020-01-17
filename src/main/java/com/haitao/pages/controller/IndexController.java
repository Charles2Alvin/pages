package com.haitao.pages.controller;

import com.haitao.pages.service.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@Controller // 加了Controller注解才能使这个类被自动调用
public class IndexController {
//    @Autowired
//    MsgService msgService;

    @RequestMapping(path = {"/"}) // 指定这个网页的访问地址
    @ResponseBody // 加这个注解的时候直接返回你设定的字符串;不加注解的时候会自动地去template包里面找对应的模板文件

    public String root(HttpSession httpSession) {
        // 从session中读取消息
        return "Hello NowCoder" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path = {"index"})
    public String index() {
        return "index";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "nowcoder") String key) {
        return String.format("Profile Page of %s / %d, t: %d k: %s", groupId, userId, type, key);
    }

    @RequestMapping(value = "/home") // 输入这个网址时会调用的方法，注意这个方法没有responseBody标注！
    public String template(Model model) {
        // 通过model来向后端传参
        model.addAttribute("key", "value");
        return "home"; // 返回HTML页面
    }

    @RequestMapping(value = "/request") // 输入这个网址时会调用的方法
    @ResponseBody
    public String t(Model model,
                    HttpServletResponse response,
                    HttpServletRequest request,
                    HttpSession session) {
        // 读取request和response的各种参数
        StringBuffer sb = new StringBuffer();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("header头部", "header内容");
        response.addCookie(new Cookie("username", "nowCoder"));
        return sb.toString();
    }

    @GetMapping(path = {"/redirect/{code}"})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        // 在重定向的过程中通过session来传递消息
        httpSession.setAttribute("msg", " jump from redirect");
        RedirectView redirectView = new RedirectView("/", true );
        if (code == 301) {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @GetMapping(path = {"/admin"})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        // 这个request parameter是写在URL里?后面的东西，比如?key=admin
        if ("admin".equals(key)) {
            return "Hello admin";
        }
        throw new IllegalArgumentException("Wrong parameters");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}
