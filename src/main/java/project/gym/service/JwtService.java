package project.gym.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.Member;
import project.gym.repo.MemberRepo;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.token.access.expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.token.refresh.expiration}")
    private long refreshTokenExpiration;

    private final MemberRepo memberRepo;

    public JwtService(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extactAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extactAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(Member member, long expirationTime) {
        return Jwts
                .builder()
                .subject(member.getUsername())
                .claim("role", member.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    public String generateAccessToken(Member member) {
        return generateToken(member, accessTokenExpiration);
    }

    public String generateRefreshToken(Member member) {
        return generateToken(member, refreshTokenExpiration);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Member getMember(String token) {
        String email = extractUsername(token.substring(7));
        return memberRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);
    }
}
