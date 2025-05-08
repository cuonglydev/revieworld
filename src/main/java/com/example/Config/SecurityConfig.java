package com.example.Config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.example.Security.AdminDetailsService;
import com.example.Security.CustomOAuth2UserService;
import com.example.Security.UserAuthDetailsService;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private AdminDetailsService adminDetailsService;	
	
	@Autowired
	private UserAuthDetailsService userAuthDetailsService;
	
	
	@Autowired
    private CustomOAuth2UserService customOAuth2UserService;
	
	

	@Bean
	@Order(1)
	public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception{
		http
			.securityMatcher("/admin/**")
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/admin/login", "/Admin/assets/**").permitAll()
					
					.requestMatchers("/admin/admin/**", "/admin/system/**", "/admin/statistic/**").hasRole("ADMIN")
					.requestMatchers("/admin/index", "/admin/user/**", "/admin/rank/**", "/admin/withdraw/**", "/admin/order-type/**"
							,"/admin/order/**", "/admin/appeal-mission/**", "/admin/popups/**")
					.hasAnyRole("ADMIN", "EMPLOYEE")
					
					.anyRequest().hasRole("ADMIN")
					)
			.formLogin(form -> form
					.loginPage("/admin/login")
					.loginProcessingUrl("/admin/login")
					.defaultSuccessUrl("/admin/index", true)
					.failureUrl("/admin/login?error=true")
					.permitAll()
					)
			.logout(logout -> logout
					.logoutUrl("/admin/logout")
					.logoutSuccessUrl("/admin/login?logout=true")
					)
		 .userDetailsService(adminDetailsService)
         .csrf(csrf -> csrf.disable());
		return http.build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain userSecurityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/account/bank/**").authenticated()
				.requestMatchers("/User/Pages/account/**").authenticated()
				.anyRequest().permitAll()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error=true")
				.permitAll()
			)
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/login")
				.authorizationEndpoint(authorization ->
					authorization
						.authorizationRequestResolver(
							customAuthorizationRequestResolver(clientRegistrationRepository)))
				.userInfoEndpoint(userInfo ->
					userInfo.userService(customOAuth2UserService))
				.defaultSuccessUrl("/", true))
			.rememberMe(remember -> remember
				.rememberMeParameter("remember-me")
				.tokenValiditySeconds(7 * 24 * 60 * 60)
				.userDetailsService(userAuthDetailsService)
			)
			.logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout=true")
			)
			.userDetailsService(userAuthDetailsService)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/login", "/register", "/logout")
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			);
		return http.build();
	}
	
	
	 @Bean
	    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
	        return customOAuth2UserService;
	    }

	    private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
	            ClientRegistrationRepository clientRegistrationRepository) {

	        DefaultOAuth2AuthorizationRequestResolver defaultResolver =
	                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

	        return new OAuth2AuthorizationRequestResolver() {
	            @Override
	            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
	                OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request);
	                return customizeAuthRequest(authRequest);
	            }

	            @Override
	            public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
	                OAuth2AuthorizationRequest authRequest = defaultResolver.resolve(request, clientRegistrationId);
	                return customizeAuthRequest(authRequest);
	            }

	            private OAuth2AuthorizationRequest customizeAuthRequest(OAuth2AuthorizationRequest authRequest) {
	                if (authRequest == null) return null;

	                Map<String, Object> additionalParams = new HashMap<>(authRequest.getAdditionalParameters());
	                additionalParams.put("prompt", "select_account"); // Force Google to show account selection

	                return OAuth2AuthorizationRequest.from(authRequest)
	                        .additionalParameters(additionalParams)
	                        .build();
	            }
	        };
	    }

}
