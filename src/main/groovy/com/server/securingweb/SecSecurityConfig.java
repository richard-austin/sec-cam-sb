package com.server.securingweb;

import com.server.security.MyUserDetailsService;
import com.server.security.TwoFactorAuthenticationDetailsSource;
import com.server.security.TwoFactorAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
public class SecSecurityConfig {
    @Autowired
    private MyUserDetailsService userDetailsService;
//    @Autowired
//    UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // @TODO Makes Restful API calls available to any role, or no role
                 .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/login/authenticate").permitAll()
  //                     .requestMatchers("/setupWifi2").hasAnyAuthority("ROLE_CLIENT")
                        .requestMatchers("/hello").hasRole("CLIENT")
                        .anyRequest().authenticated()
                )
                .rememberMe(rememberMe -> rememberMe.key("uniqueAndSecret"))
                .formLogin((form) -> form
                        .authenticationDetailsSource(authenticationDetailsSource())
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public TwoFactorAuthenticationProvider twoFactorAuthenticationProvider() {
        final TwoFactorAuthenticationProvider authProvider = new TwoFactorAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        //      authProvider.setPostAuthenticationChecks(differentLocationChecker());
        return authProvider;

    }

    @Bean
    public TwoFactorAuthenticationDetailsSource authenticationDetailsSource() {
        return new TwoFactorAuthenticationDetailsSource();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("supersecret", userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }

//    @Bean
//    RememberMeAuthenticationFilter rememberMeFilter() {
//        return new RememberMeAuthenticationFilter(theAuthenticationManager, rememberMeServices());
//    }
//
//    @Bean
//    TokenBasedRememberMeServices rememberMeServices() {
//        return new TokenBasedRememberMeServices("supersecret", userDetailsService);
//    }
//
//    @Bean
//    RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
//        return new RememberMeAuthenticationProvider("springRocks");
//    }
}
