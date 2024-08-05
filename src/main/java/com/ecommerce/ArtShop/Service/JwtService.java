package com.ecommerce.ArtShop.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim (String token, Function<Claims, T> tClaimsFunction) {
      final Claims claims= extractAllClaims(token);
        return tClaimsFunction.apply(claims);

    }

    public Claims extractAllClaims (String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte [] byteKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(byteKey);
    }

    //we need to generate token; Therefore we need to check if it's valid and not expired
    public String generateToken (UserDetails userDetails)  {
        return  generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken (Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String buildToken (Map<String, Object> extraClaims,
            UserDetails userDetails, long expiration) {
       return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean isTokenValid (String token, UserDetails userDetails) {
        String userName =extractUserEmail(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired (String token) {
        return extractExpiration(token).before(new Date());  //new date - time when object is created
    }

    public Date extractExpiration (String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
