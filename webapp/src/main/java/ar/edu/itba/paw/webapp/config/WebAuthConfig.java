package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtAuthenticationService;
import ar.edu.itba.paw.webapp.filter.JwtTokenFilter;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
import ar.edu.itba.paw.webapp.filter.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStream;

@EnableWebSecurity
@Configuration
@ComponentScan( "ar.edu.itba.paw.webapp.auth" )
public class WebAuthConfig extends WebSecurityConfigurerAdapter
{
    @Value( "classpath:private_key.der" )
    private Resource secretKey;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;

    @Autowired
    private ObjectMapper objectMapper;


    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( username -> userDetailsService.loadUserByUsername( username ) );
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.csrf().disable();

        http.exceptionHandling().authenticationEntryPoint( new RestAuthenticationEntryPoint() );

        http.sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS );

        http.authorizeRequests()
            .antMatchers( HttpMethod.OPTIONS, "/**" )
            .permitAll()
            .antMatchers( HttpMethod.POST, "/api/auth/**" )
            .anonymous()
            .antMatchers( HttpMethod.POST, "/api/login" )
            .anonymous()
            .antMatchers( HttpMethod.GET, "/api/**/picture" )
            .anonymous()
            .anyRequest()
            .authenticated();

        http.addFilterBefore( jwtTokenFilter, UsernamePasswordAuthenticationFilter.class );
        http.addFilterBefore(
                new LoginFilter( "/api/login", jwtAuthenticationService, userDetailsService, objectMapper ),
                UsernamePasswordAuthenticationFilter.class );
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuth() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService( userDetailsService );
        ap.setPasswordEncoder( passwordEncoder );
        return ap;
    }

    @Bean( name = BeanIds.AUTHENTICATION_MANAGER )
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InputStream secretKey() {
        try {
            return this.secretKey.getInputStream();
        }
        catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void configure( WebSecurity web ) {
        web.ignoring().antMatchers( "/css/**", "/js/**", "/img/**", "/favicon.ico" );
    }
}



