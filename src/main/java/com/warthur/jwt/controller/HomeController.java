package com.warthur.jwt.controller;

import com.warthur.jwt.common.util.Response;
import com.warthur.jwt.common.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    public Response hello() {

        return ResponseUtil.success("ok");
    }
}
