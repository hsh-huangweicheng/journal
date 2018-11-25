package com.hwc.wos.controller;

import com.hwc.wos.repository.WosArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(path = "/wos")
public class WosController {

    @Autowired
    private WosArticleRepository articleRepository;

    @GetMapping(path = "/import")
    public @ResponseBody
    String addNewUser() {
        return "" + new Date();
    }
}
