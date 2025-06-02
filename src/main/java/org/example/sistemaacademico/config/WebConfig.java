package org.example.sistemaacademico.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de Spring MVC para habilitar CORS (Cross-Origin Resource Sharing).
 * Permite que los clientes frontend accedan a los recursos de la API desde orígenes específicos,
 * manteniendo compatibilidad con la configuración anterior.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las políticas CORS para permitir solicitudes desde orígenes específicos.
     *
     * @param registry El registro de configuraciones CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mantiene todas las rutas como antes
                .allowedOrigins("http://localhost:5173", "*") // Restaura el acceso a todos los orígenes
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Incluye OPTIONS
                .allowedHeaders("*") // Restaura todos los encabezados para compatibilidad
                .allowCredentials(false) // Desactiva credenciales para alinearse con el comportamiento original
                .maxAge(3600); // Añade cache de preflight por 1 hora
    }
}
