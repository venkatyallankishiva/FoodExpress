package registerationlogin.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import registerationlogin.entity.Role;
import registerationlogin.entity.User;
import registerationlogin.repository.UserRepository;
import registerationlogin.security.CustomOidcUser;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository to fetch roles dynamically

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // From Spring6 no need to set user details , it will automatically set user
                                            // details, service and password encoded objects to auntication.
    }

    // configure SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/").permitAll() // For matching http request requests.
                        .requestMatchers("/customer/**").hasRole("CUSTOMER") // Giving acess for admins role users only.
                        .requestMatchers("/restaurant/**").hasRole("RESTAURANT")
                        .requestMatchers("/admin/**").hasRole("ADMIN"))
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                // .defaultSuccessUrl("/users")
                                .successHandler(customSuccessHandler()) // Use the custom success handler
                                .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.oidcUserService()))
                        .successHandler(customSuccessHandler()) // Ensure role-based redirection
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessHandler(customLogoutHandler()) // Use the custom logout handler
                                .permitAll());
        return http.build();
    }

    private OidcUserService oidcUserService() {
        return new OidcUserService() {
            @Override
            public OidcUser loadUser(OidcUserRequest userRequest) {
                OidcUser oidcUser = super.loadUser(userRequest);
                String email = oidcUser.getAttribute("email");

                // Fetch user from database
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    user.setName(oidcUser.getAttribute("name"));
                    Role customerRole = new Role();
                    customerRole.setName("ROLE_CUSTOMER");
                    user.setRoles(Arrays.asList(customerRole)); // Default role for new users
                    userRepository.save(user);
                }

                // Assign roles dynamically
                List<GrantedAuthority> authorities = user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList());

                return new CustomOidcUser(authorities, oidcUser);
            }
        };
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            // Check roles and redirect accordingly
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/dashboard");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_RESTAURANT"))) {
                response.sendRedirect("/restaurant/dashboard");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"))) {
                response.sendRedirect("/customer/home");
            } else {
                response.sendRedirect("/"); // Default redirect if no role matches
            }
        };
    }

    @Bean
    public LogoutSuccessHandler customLogoutHandler() {
        return (request, response, authentication) -> {
            // Check roles and redirect accordingly after logout
            if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/login"); // Redirect after admin logout
            } else if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_RESTAURANT"))) {
                response.sendRedirect("/login"); // Redirect after restaurant logout
            } else if (authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"))) {
                response.sendRedirect("/"); // Redirect after customer logout
            } else {
                response.sendRedirect("/login"); // Default redirect if no role matches
            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder()); // Just passwordEncoder bean and UserDetailsService bean must be
                                                     // there then no need of this configuration setting as sping6 will
                                                     // automatically set user details, service and password encoded
                                                     // objects to auntication.
    }
}
