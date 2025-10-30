package com.LibreriaApi.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configura CORS para todas las rutas que comiencen con /api/
                registry.addMapping("/**") // Aplica a todo el servidor
                        .allowedOrigins("http://localhost:4200")
                        // Permite los métodos HTTP que usará tu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // Permite que se envíen todas las cabeceras
                        .allowedHeaders("*")
                        // Permite el envío de credenciales (necesario para JWT en cookies, etc.)
                        .allowCredentials(true);
            }
        };
    }
}