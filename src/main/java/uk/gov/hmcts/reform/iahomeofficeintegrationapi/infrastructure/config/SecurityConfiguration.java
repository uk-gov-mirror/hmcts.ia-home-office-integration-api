package uk.gov.hmcts.reform.iahomeofficeintegrationapi.infrastructure.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.reform.auth.checker.core.service.ServiceRequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.spring.serviceonly.AuthCheckerServiceOnlyFilter;

@Configuration
@ConfigurationProperties(prefix = "security")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final List<String> anonymousPaths = new ArrayList<>();
    private final AuthenticationManager authenticationManager;
    private final ServiceRequestAuthorizer serviceRequestAuthorizer;

    public SecurityConfiguration(
        AuthenticationManager authenticationManager,
        ServiceRequestAuthorizer serviceRequestAuthorizer
    ) {
        this.authenticationManager = authenticationManager;
        this.serviceRequestAuthorizer = serviceRequestAuthorizer;
    }

    public List<String> getAnonymousPaths() {
        return anonymousPaths;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers(
            anonymousPaths
                .stream()
                .toArray(String[]::new)
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        AuthCheckerServiceOnlyFilter serviceOnlyFilter = new AuthCheckerServiceOnlyFilter(serviceRequestAuthorizer);

        //TO reinstate when Idam is introduced
        //AuthCheckerServiceAndUserFilter authCheckerServiceAndUserFilter =
        //    new AuthCheckerServiceAndUserFilter(serviceRequestAuthorizer, userRequestAuthorizer);
        serviceOnlyFilter.setAuthenticationManager(authenticationManager);

        http
            .addFilter(serviceOnlyFilter)
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .authorizeRequests().anyRequest().authenticated()
        ;
    }
}
