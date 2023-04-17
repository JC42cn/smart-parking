package com.smart.smartparking.common;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;
@Slf4j
public class JwtUtil {

    private static long time = 60*1000*60*24 ;//60*1000*30
    private static String secret = "q1w2^&*&*%";

    public static String createToken(){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", "admin")
                .claim("role", "admin")
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        return jwtToken;
    }

    public static boolean checkToken(String token){
        log.info("使用JWTUtil.....");
        if(token == null){
            log.info("token为空");
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);//解析token信息赋值给claimsJws
        } catch (Exception e) {
            log.info("解析异常");
            log.info(e.toString());
            return false;
        }
        log.info("解析成功,token有效");
        return true;
    }
}
