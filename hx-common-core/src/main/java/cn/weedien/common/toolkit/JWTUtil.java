package cn.weedien.common.toolkit;

import cn.weedien.common.auth.UserInfoDTO;
import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.weedien.common.constant.UserConstant.*;

@Slf4j
public final class JWTUtil {

    private static final long EXPIRATION = 86400L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ISS = "weedien";
    public static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("SecretKey039245678901232039487623456783092349288901402967890140939827".getBytes());

    /**
     * 生成用户 Token
     *
     * @param userInfo 用户信息
     * @return 用户访问 Token
     */
    public static String generateAccessToken(UserInfoDTO userInfo) {
        Map<String, Object> customerUserMap = new HashMap<>();
        customerUserMap.put(USER_ID_KEY, userInfo.getUserId());
        customerUserMap.put(USER_NAME_KEY, userInfo.getUsername());
        customerUserMap.put(REAL_NAME_KEY, userInfo.getRealName());

        String jwtToken = Jwts.builder()
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .issuedAt(new Date())
                .issuer(ISS)
                .subject(JSON.toJSONString(customerUserMap))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .compact();
        return TOKEN_PREFIX + jwtToken;
    }

    /**
     * 解析用户 Token
     *
     * @param jwtToken 用户访问 Token
     * @return 用户信息
     */
    public static UserInfoDTO parseJwtToken(String jwtToken) {
        if (StringUtil.hasText(jwtToken)) {
            String actualJwtToken = jwtToken.replace(TOKEN_PREFIX, "");
            try {
                Claims claims = Jwts.parser()
                        .decryptWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(actualJwtToken)
                        .getPayload();
                Date expiration = claims.getExpiration();
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    return JSON.parseObject(subject, UserInfoDTO.class);
                }
            } catch (ExpiredJwtException ignored) {
            } catch (Exception ex) {
                log.error("JWT Token解析失败，请检查", ex);
            }
        }
        return null;
    }
}