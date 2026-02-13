package com.yourapp.config;

import com.yourapp.security.JwtAuthenticationFilter;
import com.yourapp.security.JwtTokenProvider;
import com.yourapp.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private JwtAuthenticationFilter jwtRequestFilter;
    private JwtTokenProvider tokenProvider;
    private  CustomUserDetailsService myUserDetailsService;
    
    public SecurityConfig(JwtAuthenticationFilter jwtRequestFilter, 
            JwtTokenProvider tokenProvider, 
            CustomUserDetailsService myUserDetailsService) {
    	this.jwtRequestFilter = jwtRequestFilter;
    	this.tokenProvider = tokenProvider;
    	this.myUserDetailsService = myUserDetailsService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.cors().and()
        	.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/users/register", "/api/users/login","/api/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // register our authentication provider
        http.authenticationProvider(authenticationProvider(myUserDetailsService));

        // add JWT filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService myUserDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }}
//
//    public SecurityConfig(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
//        this.tokenProvider = tokenProvider;
//        this.userDetailsService = userDetailsService;
//    }
//
//    // ✅ 1. Password encoder bean
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    // ✅ 2. Authentication provider
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
//
//    // ✅ 3. JWT filter bean
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(tokenProvider, userDetailsService);
//    }
//
//    // ✅ 4. Authentication manager (needed for AuthController)
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    // ✅ 5. Custom entry point (handles 401)
//    @Bean
//    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
//        return (request, response, authException) -> {
//            System.err.println("Unauthorized error. Message - " + authException.getMessage());
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
//        };
//    }
//
//    // ✅ 6. Main security filter chain
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors().and()
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint()))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers("/api/users/signup").permitAll()
//                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
//                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/categories/**").permitAll()
//                        .requestMatchers("/api/users/**").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                );
//
//        // ✅ Attach authentication provider & JWT filter
//        http.authenticationProvider(authenticationProvider());
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}
