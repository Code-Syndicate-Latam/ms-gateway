package com.urbe.microservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                // Rutas públicas críticas (login, oauth2, errores, assets)
                .pathMatchers("/login/**", "/oauth2/**", "/error", "/assets/**", "/favicon.ico").permitAll()
                .pathMatchers("/api/v1/auth/register").permitAll()
                
                // Rutas de desarrollo de Angular/Vite
                .pathMatchers("/@vite/**", "/@fs/**", "/@ng/**", "/node_modules/**").permitAll()
                
                // Archivos estáticos (JS, CSS, etc.) son públicos para que la app pueda cargar
                .pathMatchers("/*.js", "/*.css", "/*.png", "/*.jpg", "/*.svg", "/*.woff", "/*.woff2", "/*.ttf", "/*.eot", "/*.json").permitAll()
                
                // IMPORTANTE: La ruta raíz ("/") y "index.html" NO están en permitAll().
                // Esto fuerza a que cualquier petición a http://localhost:8000/ requiera autenticación.
                // Al no estar autenticado, oauth2Login() interceptará y redirigirá al Auth Server.
                
                // Todo lo demás requiere autenticación
                .anyExchange().authenticated()
            )
            .oauth2Login(Customizer.withDefaults())
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8000", "http://127.0.0.1:8000", "http://localhost:4200", "http://127.0.0.1:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
