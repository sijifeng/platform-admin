package com.season.platform.web.site.controller;

import com.season.platform.web.site.dto.User;
import com.season.platform.web.site.entities.JsonResponseEntity;
import com.season.platform.web.site.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yingchun on 2017/9/18.
 */
@Controller
@RequestMapping(value = "/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/init")
    private String index() {
        return "system/user/list";
    }


    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length, String userHandler) {
        return userService.pageList(start, length, userHandler);
    }


    @RequestMapping("/user")
    public String user(Model model, String userId) {
        try {
            User user = userService.findOne(userId);
            model.addAttribute("user", user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "system/user/add";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonResponseEntity<String> save(User user) {
        System.out.println("进入save 方法" + user);
        userService.save(user);
        return JsonResponseEntity.SUCCESS;
    }


    @RequestMapping("/view")
    @ResponseBody
    public JsonResponseEntity<User> view(String userId) {
        User user = userService.findOne(userId);
        return new JsonResponseEntity(user);
    }


    @RequestMapping("/remove")
    @ResponseBody
    public JsonResponseEntity<String> remove(String userId) {
        userService.delete(userId);
        return JsonResponseEntity.SUCCESS;
    }


    @RequestMapping(value = "/ajaxlist", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajaxList(@RequestBody Map<String, Object> param) {//
        Map<String, Object> maps = new HashMap<>();
        try {
            log.info("param = " + param);

            int start = MapUtils.getIntValue(param, "start", 1);
            int size = MapUtils.getIntValue(param, "length", 10);
            log.info("param start :" + start);
            log.info("param size :" + size);
            List<User> list = userService.findAll();// 分页列表
            long count = userService.count();

            maps.put("recordsTotal", count);        // 总记录数
            maps.put("recordsFiltered", count);    // 过滤后的总记录数
            maps.put("draw", param.get("draw"));
            maps.put("data", list);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return maps;
    }


    /**
     * 用户编辑
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/edit")
    private String edit(String id, HttpServletRequest request) {

        request.setAttribute("id", id);
        return "base/test/user_edit";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseBody
    private User getUser(String id) {
        return userService.findOne(id);
    }
}
