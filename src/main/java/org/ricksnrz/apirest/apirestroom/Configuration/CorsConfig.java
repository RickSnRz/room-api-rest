package org.ricksnrz.apirest.apirestroom.Configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**") // Aplica CORS a todos los endpoints
                        .allowedOrigins("http://localhost:3000", "http://localhost:8080") // Dominios permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
                        .allowedHeaders("*") // Encabezados permitidos
                        .allowCredentials(true); // Permite credenciales
            }
        };
    }
}
