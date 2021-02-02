package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.JwtTokenFilter;
import ar.edu.itba.paw.webapp.auth.TravelUserDetailsService;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@ComponentScan( "ar.edu.itba.paw.webapp.auth" )
public class WebAuthConfig extends WebSecurityConfigurerAdapter
{
    @Value( "classpath:rememberme.key" )
    private Resource key;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( username -> userDetailsService.loadUserByUsername( username ) );
    }


    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.csrf()
            .disable();

        http.exceptionHandling().authenticationEntryPoint( new RestAuthenticationEntryPoint() );

        http.sessionManagement()
            .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
            .and();

        http.authorizeRequests()
            .antMatchers( HttpMethod.OPTIONS, "/**" )
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers( HttpMethod.POST, "/api/auth/**" )
            .anonymous()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated();

        http.addFilterBefore( jwtTokenFilter, UsernamePasswordAuthenticationFilter.class );
    }

    //    private String getRememberMeKey()
    //    {
    //        final StringWriter writer = new StringWriter();
    //        try ( Reader reader = new InputStreamReader( key.getInputStream() ) )
    //        {
    //            char[] data = new char[1024];
    //            int len;
    //            while ( ( len = reader.read( data ) ) != -1 )
    //            {
    //                writer.write( data, 0, len );
    //            }
    //        }
    //        catch ( IOException e )
    //        {
    //            throw new RuntimeException( e );
    //        }
    //        return writer.toString();
    //    }
    //
    @Bean
    public DaoAuthenticationProvider getDaoAuth() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService( userDetailsService );
        ap.setPasswordEncoder( passwordEncoder );
        return ap;
    }

    //
    //    @Override
    //    protected void configure( AuthenticationManagerBuilder auth ) throws Exception
    //    {
    //        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); OTRA FORMA
    //        auth.authenticationProvider( getDaoAuth() );
    //    }
    //
    //    @Override
    //    public void configure( final WebSecurity web ) throws Exception
    //    {
    //        web.ignoring()
    //                .antMatchers( "/css/**", "/js/**", "/img/**", "/favicon.ico", "/icons/**", "/webjars/**" );
    //    }
    //
    @Bean( name = BeanIds.AUTHENTICATION_MANAGER )
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    // Used by spring security if CORS is enabled.
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials( true );
//        config.addAllowedOrigin( "*" );
//        config.addAllowedHeader( "*" );
//        config.addAllowedMethod( "*" );
//        source.registerCorsConfiguration( "/**", config );
//        return new CorsFilter( source );
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



