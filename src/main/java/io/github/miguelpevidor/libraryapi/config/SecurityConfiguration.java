package io.github.miguelpevidor.libraryapi.config;

import io.github.miguelpevidor.libraryapi.security.CustomUserDetailsService;
import io.github.miguelpevidor.libraryapi.security.JwtCustomAuthenticationFilter;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    // Configura a cadeia de segurança para gerenciar autenticação e autorização.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   LoginSocialSuccessHandler successHandler,
                                                   JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception {
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
                .oauth2ResourceServer(oauth2RS ->{
                    oauth2RS.jwt(Customizer.withDefaults());
                })
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }

    // Retira o prefixo "ROLE_" que o spring adiciona para comparar as roles,
    // mas como eu fiz a minha mesma não tem necessidade dessa comparação.
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("");
    }

    // CONFIGURA, NO TOKEN JWT, O PREFIXO SCOPE
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}
