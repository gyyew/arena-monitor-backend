package com.example.post.feign;

import com.example.post.feign.config.UserServiceFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Feign Client Import Selector
 * Programmatically registers Feign client interfaces
 */
public class FeignClientImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // Return the Feign client interface to be imported
        return new String[]{
            UserServiceClient.class.getName()
        };
    }
}
