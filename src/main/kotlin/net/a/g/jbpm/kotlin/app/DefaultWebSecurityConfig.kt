package net.a.g.jbpm.kotlin.app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@EnableWebSecurity
@Order(1)
class DefaultWebSecurityConfig() : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/rest/*").authenticated().and()
            .httpBasic().and()
            .headers().frameOptions().disable()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("admin")
        auth.inMemoryAuthentication().withUser("kieserver").password("kieserver123_").roles("kie-server")
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.allowedOrigins = Arrays.asList("*")
        corsConfiguration.allowCredentials = true
        corsConfiguration.allowedMethods = Arrays.asList(HttpMethod.GET.name, HttpMethod.HEAD.name,
                HttpMethod.POST.name, HttpMethod.DELETE.name, HttpMethod.PUT.name)
        corsConfiguration.applyPermitDefaultValues()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return NoOpPasswordEncoder.getInstance()
    }
}

