package com.example.demo.hello.controller;

import com.example.demo.hello.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author jiejT
 * @ClassName: MyController
 * @description: TODO
 * @date 2021/10/26
 */
@RestController
@RequestMapping("/mytest")
public class MyController {
    @Autowired
    private MyService service;

    @RequestMapping(value="/getRecord",method = RequestMethod.GET)
    public Map<String,Object> test(){
        return service.getRecordInfo();
    }
}
