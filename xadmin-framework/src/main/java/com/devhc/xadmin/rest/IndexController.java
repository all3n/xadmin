package com.devhc.xadmin.rest;

import com.devhc.xadmin.annotation.rest.AnonymousGetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

    @AnonymousGetMapping("/index")
    public String index(){
        return "index";
    }
}
