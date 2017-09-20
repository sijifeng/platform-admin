package com.season.platform.web.site.service;

import com.season.platform.web.site.dao.UserRepository;
import com.season.platform.web.site.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yingchun on 2017/9/18.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByLoginName(String loginName) {
        return userRepository.findByLoginName(loginName);
    }

    public Map<String, Object> pageList(int start, int length, String userHandler) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", start);
        params.put("pagesize", length);
        params.put("userHandler", userHandler);

        int page = start / length;
        Pageable pageable = new PageRequest(page, length);

        // page list
        Page<User> list = userRepository.findAll(pageable);
        long count = userRepository.count();

        // package result
        Map<String, Object> maps = new HashMap<>();
        maps.put("recordsTotal", count);        // 总记录数
        maps.put("recordsFiltered", count);    // 过滤后的总记录数
        maps.put("data", list.getContent());                // 分页列表
        return maps;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }


    public long count() {
        return userRepository.count();
    }


    public User findOne(String id) {
        return userRepository.findOne(id);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(String userId) {
        userRepository.delete(userId);
    }
}
