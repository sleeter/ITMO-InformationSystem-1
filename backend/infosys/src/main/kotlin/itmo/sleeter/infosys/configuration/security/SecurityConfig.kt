package itmo.sleeter.infosys.configuration.security

import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@RequiredArgsConstructor
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/**", permitAll)
            }
            httpBasic {}
        }
        return http
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .cors { obj: CorsConfigurer<HttpSecurity> -> obj.disable() }
            .build()
    }
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTION")
            }
        }
    }
}