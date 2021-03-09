package com.baidu.feign;

import com.baidu.shop.service.SpecificationService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "xxx-server",contextId = "SpecificationService")
public interface SpecificationFeign extends SpecificationService {
}
