package com.osen.osenshop.auth.application.services;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.repository.UserRepository;
import com.osen.osenshop.auth.domain.services.TokenService;

import com.osen.osenshop.common.handler_exception.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private int jwtExpiration;

    @Value("${application.security.jwt.refresh-expiration}")
    private int jwtRefreshExpiration;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);


    public TokenServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        User currenUser = (User) authentication.getPrincipal();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(currenUser.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(jwtExpiration, ChronoUnit.MINUTES)) // el ChronoUnit convierte mi valor a minutos, sin eso seria solo milisegundos x defecto
                .claim("roles", roles)
                .build();

        var jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    @Override
    public String getUserFromToken(String token) {
        Jwt jwtToken = jwtDecoder.decode(token);
        return jwtToken.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            jwtDecoder.decode(token);
            return true;

        } catch (JwtException exception) {
            throw new TokenExpiredException("Error while trying to validate token");
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getEmail())
                .issuedAt(now) // fecha de creacion
                .expiresAt(now.plus(jwtRefreshExpiration, ChronoUnit.MINUTES)) // el ChronoUnit convierte mi valor a minutos, sin eso seria solo milisegundos x defecto
                .claim("type", "refresh")
                .build();

        var jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    // para la rotacion de tokens
    @Override
    public Map<String, String> refreshToken(String refreshToken) {
        try {
            // Decodificar (valida firma y expiración automáticamente)
            Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);

            // Validar tipo
            String type = jwtRefreshToken.getClaim("type");
            if (!"refresh".equals(type)) {
                throw new JwtException("Invalid token type");
            }

            // Extraer usuario
            String email = jwtRefreshToken.getSubject();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Usuario no encontrado"));

            // Genero nuevos tokens (rotación lógica)
            String newAccessToken = generateToken(
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    )
            );

            String newRefreshToken = generateRefreshToken(user);

            return Map.of(
                    "access", newAccessToken,
                    "refresh", newRefreshToken
            );

        } catch (JwtException e) {
            throw new TokenExpiredException("Invalid or expired refresh token");
        }
    }


}

