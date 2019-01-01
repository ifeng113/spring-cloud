package com.eairlv.service.demo1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "demo2", fallback = Demo2Fallback.class)
@FeignClient(name = "demo2", fallbackFactory = Demo2FeignFactory.class)
public interface Demo2Feign {

    @GetMapping("/demo2")
    String demo2(@RequestParam(value = "parameter") String parameter, @RequestParam(value = "parameter2") String parameter2);

    @GetMapping("/demo2o")
    String demo2o(PObj pObj);

}
