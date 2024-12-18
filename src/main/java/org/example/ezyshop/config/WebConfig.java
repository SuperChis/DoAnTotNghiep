package org.example.ezyshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để phục vụ file từ thư mục "uploads"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:src/main/resources/uploads/");
    }
}
