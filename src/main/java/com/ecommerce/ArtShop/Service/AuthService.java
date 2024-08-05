package com.ecommerce.ArtShop.Service;


import com.ecommerce.ArtShop.Service.Email.EmailService;
import com.ecommerce.ArtShop.Service.Email.EmailTemplateName;
import com.ecommerce.ArtShop.Model.Token.Token;
import com.ecommerce.ArtShop.Repository.TokenRepository;
import com.ecommerce.ArtShop.Model.Token.TokenType;
import com.ecommerce.ArtShop.DTO.AuthDTO.AuthenticationRequest;
import com.ecommerce.ArtShop.DTO.AuthDTO.AuthenticationResponse;
import com.ecommerce.ArtShop.DTO.AuthDTO.RegisterRequest;
import com.ecommerce.ArtShop.Model.User;
import com.ecommerce.ArtShop.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public void register (RegisterRequest request) throws MessagingException {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    public void checkForExistingUser(RegisterRequest userDto, BindingResult result) {
        Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent() ) {
            result.addError(new ObjectError("user",  "There is already an account registered with the same email"));
        }
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken =generateAndSendActivationToken(user);
         final String activationUrl="http://localhost:8080/artshop/auth/activation?token="+ newToken;
        //send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"

        );

    }

    private String generateAndSendActivationToken(User user) {
        //generate token
        String generatedToken=generateActivationCode(6);
        var token=Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        tokenRepository.save(token);
        return  generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();


    }

    public AuthenticationResponse authenticate (AuthenticationRequest authenticationRequest) {
        userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()->new RuntimeException("User not found"));
       var auth= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
       var claims=new HashMap<String,Object>();
       var  user =((User)auth.getPrincipal());

       claims.put("fullName", user.fullName());
        if (!user.getEnabled()) {
            throw new DisabledException("User account is not enabled");
        }
        var jwtToken=jwtService.generateToken(claims, user);
        revokeAllOtherTokens(user);
        saveUserToken(jwtToken, user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();

    }

    public void checkForExistingUser(AuthenticationRequest request, BindingResult result) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (!existingUser.isPresent()){
            result.addError(new ObjectError("user", "user does not excist"));
        }
    }

    private void revokeAllOtherTokens(User user) {
        var validTokens =tokenRepository.findAllValidTokens(user.getId());
        if (validTokens.isEmpty()) {
            return;
        } validTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });
        tokenRepository.saveAll(validTokens);

    }


    private void saveUserToken(String jwtToken, User user){
        var token= Token.builder()
                .token(jwtToken)
                .user(user)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken =tokenRepository.findByToken(token)
                .orElseThrow((() -> new RuntimeException("Invalid token")));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw  new RuntimeException("Activation token has expired");
        }
        var user=userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()->new RuntimeException("User not found") );
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
