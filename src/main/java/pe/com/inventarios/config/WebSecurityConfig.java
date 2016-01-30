package pe.com.inventarios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Created by sandovlu on 21/11/2015.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] UNSECURED_RESOURCE_LIST = new String[] { "/resources/**",
            "/css/**", "/js/**", "/webjars/**", "/image/**", "/reportes/**", "/dandelion-assets/**" };

    private static final String[] UNAUTHORIZED_RESOURCE_LIST = new String[] { "/",
            "/error/unauthorized", "/error/*", "/*/util/**" };

    @Value("${rememberMeToken}")
    private String rememberMeToken;

    @Value("${rememberMeParameter}")
    private String rememberMeParameter;

    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
            .antMatchers(UNSECURED_RESOURCE_LIST);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
    	http
    	.csrf().disable()
    	.authorizeRequests()
        	.antMatchers(UNAUTHORIZED_RESOURCE_LIST).permitAll()
//    		.antMatchers("/", "/template/**").permitAll()
//        .and()
//        	.authorizeRequests()
//        	.anyRequest().authenticated()
//        	.antMatchers("/activo/**").hasAuthority("ADMIN")
//        	.anyRequest().fullyAuthenticated()
        .and()
	        .formLogin()
	        .loginPage("/login")
		        .passwordParameter("j_password")
		        .usernameParameter("j_username") 
		     .successHandler(savedRequestAwareAuthenticationSuccessHandler())
		     .permitAll()
	        .failureUrl("/login?error")
		        .permitAll();                
        // @formatter:on
    	
    	http
        	.authorizeRequests()
        	.anyRequest().authenticated()
            .antMatchers("/activo/**", "activo/*/**", "report/**").hasRole("ADMIN")
            .anyRequest().fullyAuthenticated();
    	
    	http
	        .logout()
	        .invalidateHttpSession(true)
	        .logoutUrl("/logout")
	        .deleteCookies("remember-me")
	        .deleteCookies("JSESSIONID")
	        .invalidateHttpSession(true)
	        .logoutSuccessUrl("/login")
	        .permitAll();
    	
    	http
	        .rememberMe()
	        .tokenValiditySeconds(1209600);
    	
    	http
	        .headers()
		        .frameOptions()
		        .disable();
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
    
    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler 
                savedRequestAwareAuthenticationSuccessHandler() {

               SavedRequestAwareAuthenticationSuccessHandler auth 
                    = new SavedRequestAwareAuthenticationSuccessHandler();
        auth.setTargetUrlParameter("targetUrl");
        auth.setDefaultTargetUrl("/activo");

        return auth;
    }
    

}


