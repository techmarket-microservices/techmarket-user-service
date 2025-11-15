package com.techmarket.user_service.controller;

import com.techmarket.user_service.constants.ApiPaths;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPaths.CUSTOMER_URL)
public class CustomerController {
    @GetMapping
    public String home(){
        return "hello customer";
    }
}
