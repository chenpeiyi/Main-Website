package org.dcsc.config.security;

import org.dcsc.core.authentication.user.UserDetailsFactory;
import org.dcsc.core.authentication.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@ComponentScan({"org.dcsc"})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2ClientContextFilter oAuth2ClientContextFilter;
    @Autowired
    private UserDetailsFactory userDetailsFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private Environment environment;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .addFilterAfter(oAuth2ClientContextFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
                .addFilterAfter(new XsrfHeaderFilter(), CsrfFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .and().formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/admin/")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                .and().csrf().csrfTokenRepository(csrfTokenRepository());
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        if (environment.acceptsProfiles("dev")) {
            authenticationManagerBuilder.authenticationProvider(devAuthenticationProvider());
        }
    }

    @Bean
    @Profile("dev")
    public DevAuthenticationProvider devAuthenticationProvider() {
        return new DevAuthenticationProvider();
    }

    @Bean
    public OpenIdConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
        return new OpenIdConnectAuthenticationFilter("/login-oidc");
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login");
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
}
