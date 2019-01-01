package com.eairlv.service.demo1;

import com.eairlv.service.demo1.utils.HystrixErrorFactory;
import org.springframework.stereotype.Component;

@Component
public class Demo2FeignFactory extends HystrixErrorFactory<Demo2Feign> {

}
