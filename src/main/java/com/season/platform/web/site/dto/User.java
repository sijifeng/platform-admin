package com.season.platform.web.site.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yingchun on 2017/9/18.
 */
@Data
@Table(name = "user")
@Entity
public class User implements Serializable{
    private static final long serialVersionUID = -6448279434332110050L;
    @Id
    private String id;
    @Column(name = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String avatar;
    private Date birthday;
    private String email;
    @Column(name = "login_name")
    private String loginName;
    private String name;
    private String mobile;
    private String password;
    private String sex;
    private String state;
}
