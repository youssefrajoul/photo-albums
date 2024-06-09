package com.youssefrajoul.photoalbums.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.youssefrajoul.photoalbums.jwt.JwtAuthenticationEntryPoint;
import com.youssefrajoul.photoalbums.jwt.JwtRequestFilter;
import com.youssefrajoul.photoalbums.jwt.JwtTokenUtil;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtTokenUtil jwtUtil;

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        users.setUsersByUsernameQuery("select username, password, enabled from users where username=?");
        users.setAuthoritiesByUsernameQuery("select username, authority from authority where username=?");
        return users;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DataSource dataSource) throws Exception {
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtAuthenticationEntryPoint, jwtUtil,
                userDetailsService(dataSource));
        http.formLogin(login -> login // Configure form-based login
                .loginPage("/login")
                .permitAll()); // Allow access to the login page for everyone
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(new AntPathRequestMatcher("/private")).hasAuthority("admin")
                        .requestMatchers(new AntPathRequestMatcher("/api/album")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/api/picture")).authenticated())
                .httpBasic(withDefaults -> {
                }); // Enable Basic Authentication
        http.exceptionHandling(error -> error.accessDeniedPage("/login"));
        http.exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntryPoint));
        // make sure we use stateless session; session won't be used to store user's
        // state.
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                .requestMatchers(new AntPathRequestMatcher("/api/**"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
