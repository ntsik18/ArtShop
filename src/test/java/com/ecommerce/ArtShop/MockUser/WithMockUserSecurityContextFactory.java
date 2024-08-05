package com.ecommerce.ArtShop.MockUser;


import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithMockUser annotation) {

        var principal = User.builder()
                .id(annotation.userId())
                .email("test@test.com")
                .password("12345678")
                .firstName("test")
                .lastName("test")
                .addresses(null)
                .build();
        userRepository.save(principal);

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UserPrincipalAuthenticationToken(principal));
        return context;
    }
}
