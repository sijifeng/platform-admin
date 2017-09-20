package com.season.platform.web.site.dao;

import com.season.platform.web.site.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yingchun on 2017/9/18.
 */
public interface UserRepository extends JpaRepository<User, String> {
    User findByLoginName(String loginName);
}
