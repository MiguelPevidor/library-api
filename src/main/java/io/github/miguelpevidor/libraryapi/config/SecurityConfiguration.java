package io.github.miguelpevidor.libraryapi.config;

import io.github.miguelpevidor.libraryapi.security.CustomUserDetailsService;
import io.github.miguelpevidor.libraryapi.security.LoginSocialSuccessHandler;
import io.github.miguelpevidor.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    // Configura a cadeia de segurança para gerenciar autenticação e autorização.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginSocialSuccessHandler successHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(configurer -> {
                    configurer.loginPage("/login");
                })
                .authorizeHttpRequests(authorize -> { // Define regras de autorização.
                    authorize.requestMatchers("/login").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();
                    authorize.anyRequest().authenticated(); // Exige autenticação para todas as outras requisições.
                })
                .oauth2Login(oauth2 ->
                        oauth2
                        .loginPage("/login")
                        .successHandler(successHandler))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    // Configura o serviço de autenticação para carregar usuários.
    //@Bean
    public UserDetailsService userDetailsService(){
        //Maneira de utilizar usuarios em memoria
//        UserDetails user1 = User.builder()
//                .username("usuario")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("admin")
//                .password(encoder.encode("321"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1,user2);
        return new CustomUserDetailsService();
    }

    // Retira o prefixo "ROLE_" que o spring adiciona para comparar as roles,
    // mas como eu fiz a minha mesma não tem necessidade dessa comparação.
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }
}
