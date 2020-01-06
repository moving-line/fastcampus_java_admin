package com.example.study.controller;

import com.example.study.model.SearchParam;
import com.example.study.model.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GetController {
    @RequestMapping(method = RequestMethod.GET, path = "/getMethod")
    public String getRequest() {
        return "Hi getMethod";
    }

    @GetMapping("/getParameter")
    public String getParameter(@RequestParam String id, @RequestParam(name="password") String pwd) {
        System.out.println("id : " + id);
        System.out.println("password : " + pwd);
        return id + pwd;
    }

    // localhost:8080/api/getMultiParameter/account=kkk&email=study@gmail.com&page=10
    @GetMapping("/getMultiParameter")
    public String getMultiParameter(SearchParam searchParam) {
        // (@RequestParam String account, @RequestParam String email, @RequestParam int page)
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());
        return "OK";
    }

    // 잭슨을 통해 json 자동 변환
    @GetMapping("/getMultiParameter2")
    public SearchParam getMultiParameter2(SearchParam searchParam) {
        System.out.println(searchParam.getAccount());
        System.out.println(searchParam.getEmail());
        System.out.println(searchParam.getPage());
        return searchParam;
    }

    @GetMapping("/header")
    public Header getHeader() {

        return Header.builder().resultCode("OK").description("OK!!").build();
    }
}
