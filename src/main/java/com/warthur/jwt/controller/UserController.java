package com.warthur.jwt.controller;

import com.warthur.jwt.common.util.Response;
import com.warthur.jwt.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private List<String> users;

    @Autowired
    private HttpServletRequest request;

    @PostConstruct
    public void init() {
        this.users = new ArrayList<String>() {{
            add("张三");
            add("李四");
            add("王五");
        }};
    }

    @GetMapping("")
    public Response userList() {
        return ResponseUtil.success(users);
    }

    @GetMapping("/{userId}")
    public Response userInfo(@PathVariable Integer userId) {
        return ResponseUtil.success(users.get(userId));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('role_admin') and hasAuthority('auth_write')")
    public Response addUser(String userName) {
        users.add(userName);
        return ResponseUtil.success();
    }

    @PutMapping("/{userId}")
    public Response editUser(@PathVariable Integer userId, String userName) {
        users.set(userId, userName);
        return ResponseUtil.success();
    }

    @DeleteMapping("/{userId}")
    public Response deleteUser(@PathVariable int userId) {
        users.remove(userId);
        return ResponseUtil.success();
    }
}
