package com.ongoni.onlineshop.config

import com.ongoni.onlineshop.service.ShopUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var shopUserDetailsService: ShopUserDetailsService

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll().anyRequest().authenticated()
                .antMatchers("/h2", "/h2/**").permitAll()
                .and()
                .formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/hello").permitAll()
                .and()
                .logout().permitAll()

        http.csrf().disable()
        http.headers().frameOptions().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth
                ?.userDetailsService(shopUserDetailsService)
                ?.passwordEncoder(BCryptPasswordEncoder())
    }

}