package net.a.g.jbpm.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Order(1)
class DefaultWebSecurityConfig : WebSecurityConfigurerAdapter() {
    
    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/rest/*").authenticated().and()
                .httpBasic().and()
                .headers().frameOptions().disable()
    }


    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("kie-server")
        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("admin")
        auth.inMemoryAuthentication().withUser("kieserver").password("{noop}kieserver123_").roles("kie-server")
    }
}

