package webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import webapp.services.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	 @Override
    protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin();		
    	//http.csrf().disable().cors();
        http
            .authorizeRequests()
                .antMatchers("/login","/adduser", "/js/**", "/css/**", "/images/**").permitAll()
                .antMatchers("/", "/home", "/addsubject","/upload/db/**", "/downloadFile/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))            
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)        // set invalidation state when logout
                .deleteCookies("JSESSIONID")        
                .and()
        	.exceptionHandling().accessDeniedPage("/error");
    }
	 
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
      throws Exception {
     	auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
    } 

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
