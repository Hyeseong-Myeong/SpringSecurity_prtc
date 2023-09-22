package com.hyeseong.springsecurity_prct.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
//    private final String accessSecret;
//    private final String refreshSecret;
    private final long tokenValidityInMilliseconds;
    private Key key;
    private Key accessKey;
    private Key refreshKey;

    public TokenProvider(
            @Value("${spring.jwt.secret}") String secret,
//            @Value("${spring.jwt.REFRESH_TOKEN_SECRET_KEY}") String refreshSecret,
//            @Value("${spring.jwt.ACCESS_TOKEN_SECRET_KEY}") String accessSecret,
            @Value("${spring.jwt.token-validity-in-seconds}") long tokenValidityMilliseconds
    ){
        this.secret = secret;
//        this.refreshSecret = refreshSecret;
//        this.accessSecret = accessSecret;
        this.tokenValidityInMilliseconds = tokenValidityMilliseconds * 1000;
    }

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
//        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
//        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));

    }

    //Authentication 객체의 권한 정보를 이용해서 토큰을 생성
    public String createToken(Authentication authentication){
        //authorities 설정
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //토큰 만료시간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    //토큰에 담겨있는 정보를 이용해 Authentication 객체 리턴
    public Authentication getAuthentication(String token) {
        //토큰을 이용햐여 claim 생성
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //claim을 이용하여 authorities 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //claim과 authorites 이용하여 User 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }


    //토큰의 유효성 검증 수행
    public boolean validateToken(String token){
        //토큰 파싱 후 발생하는 예외 캐치하여 false, 정상이면 true 리턴
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            logger.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 토큰입니다.");
        }catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


//    public String createAccessToken(int userIdx){
//        Date now = new Date();
//        return Jwts.builder()
//                .setHeaderParam("type","jwt") // Header 의 type 에다 해당 토큰의 타입을 jwt로 명시
//                .claim("userIdx",userIdx) // claim 에 userIdx 할당
//                .setIssuedAt(now) // 언제 발급되었는지를 현재 시간으로 넣어줌
//                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*30))) // Access Token 만료기간은 30분으로 설정
//                .signWith(accessKey, SignatureAlgorithm.HS256) // 서명(Signature) 를 할떄는 HS256 알고리즘 사용하며,  Secret.JWT_SECRET_KEY 라는 비밀키(Secret key) 를 가지고 Signature 를 생성한다.
//                .compact();                                   //  Secret.JWT_SECRET_KEY 는 비밀키로써 .gitignore 로 절대 노출시키지 말것! 이 비밀키를 통해 내가 밝급한건인지 아닌지를 판별할 수 있으므로
//    }

    // Refresh Token 생성
//    public String createRefreshToken(int userIdx){
//        Date now = new Date();
//        return Jwts.builder()
//                .setHeaderParam("type", "jwt")
//                .claim("userIdx", userIdx)
//                .setIssuedAt(now)
//                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*7))) // Refresh Token 만료기간은 일주일로 설정
//                .signWith(refreshKey, SignatureAlgorithm.HS256) // 1*(1000*60*60*24*30) : 만료기간을 365일(1년)으로 설정하는 경우
//                .compact();
//    }

//    public String ReCreateAccessToken() throws BaseException {
//        // Http Header 로 부터 추출
//        String refreshToken = getRefreshToken();
//        String dbRefreshToken;
//
//        // refresh token 유효성 검증1 : DB조회
//        try {
//            RefreshToken dbRefreshTokenObj = jwtRepository.getRefreshToken(refreshToken);
//            dbRefreshToken = dbRefreshTokenObj.getRefreshToken();
//        } catch(Exception ignored){  // DB에 RefreshToken 이 존재하지 않는경우
//            throw new BaseException(BaseResponseStatus.NOT_DB_CONNECTED_TOKEN);
//        }
//
//        // DB에 RefreshToken이 존재하지 않거나(null), 전달받은 refeshToken 이 DB에 있는 refreshToken 과 일치하지 않는 경우
//        if(dbRefreshToken == null || !dbRefreshToken.equals(refreshToken)){
//            throw new BaseException(BaseResponseStatus.NOT_MATCHING_TOKEN);
//        }
//
//        // Refresh Token 에 대한 DB 조회에 성공한 경우
//        // refresh token 유효성 검증2 : 형태 유효성
//        Jws<Claims> claims;
//        try {
//            claims = Jwts.parserBuilder()
//                    .setSigningKey(Secret.REFRESH_TOKEN_SECRET_KEY)
//                    .build()
//                    .parseClaimsJws(refreshToken);
//        }  catch (io.jsonwebtoken.security.SignatureException signatureException){
//            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
//        }
//        catch (io.jsonwebtoken.ExpiredJwtException expiredJwtException) { // refresh token 이 만료된 경우
//            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_EXPIRED); // 새롭게 로그인읋 시도하라는 Response 를 보낸다.
//        } catch (Exception ignored) { // Refresh Token이 유효하지 않은 경우 (만료여부 외의 예외처리)
//            throw new BaseException(BaseResponseStatus.REFRESH_TOKEN_INVALID);
//        }
//
//        int userIdx = claims.getBody().get("userIdx", Integer.class);
//        return createAccessToken(userIdx);
//    }


}
