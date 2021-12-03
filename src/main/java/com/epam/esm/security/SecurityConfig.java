package com.epam.esm.security;

import com.epam.esm.enums.Roles;
import com.epam.esm.exceptions.CustomAccessDeniedHandler;
import com.epam.esm.exceptions.security.CustomAuthenticationEntryPoint;
import com.epam.esm.filter.AuthenticationFilter;
import com.epam.esm.filter.AuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    public SecurityConfig(boolean disableDefaults, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        super(disableDefaults);
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(GET, "/certificates**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/users/refresh").permitAll();
        http.authorizeRequests().antMatchers(POST, "/users").permitAll();

        http.authorizeRequests().antMatchers(GET, "/tags/**", "/certificates" +
                        "/**",
                "/users/**", "/orders/**").hasAnyAuthority(Roles.USER.name(),
                Roles.ADMIN.name());
        http.authorizeRequests().antMatchers(POST, "/orders").hasAnyAuthority(
                Roles.USER.name(), Roles.ADMIN.name());

        http.authorizeRequests().antMatchers(POST, "/tags", "/certificates").hasAuthority(Roles.ADMIN.name());
        http.authorizeRequests().antMatchers(DELETE, "/tags/**",
                "/certificates/**").hasAuthority(Roles.ADMIN.name());
        http.authorizeRequests().antMatchers(PATCH, "/certificates/**").hasAuthority(Roles.ADMIN.name());

        http.authorizeRequests().anyRequest().authenticated();
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        http.addFilter(new AuthenticationFilter(authenticationManager()));
        http.addFilterBefore(authorizationFilter,
                UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }
}
