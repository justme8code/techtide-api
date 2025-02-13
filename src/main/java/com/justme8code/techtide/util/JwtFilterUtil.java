package com.justme8code.techtide.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justme8code.techtide.exceptions.ErrorObject;
import com.justme8code.techtide.exceptions.ErrorObjectCreator;
import com.justme8code.techtide.exceptions.JWTException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class JwtFilterUtil {

    private JwtFilterUtil() {}
    private static final String BEARER_TOKEN = "Bearer ";

    public static String getJwtTokenFromAuthorizationHeader(HttpServletRequest request) {
        final String unModifiedJwtToken = request.getHeader("Authorization");
        return unModifiedJwtToken!=null && !unModifiedJwtToken.contains(BEARER_TOKEN)
                ?  BEARER_TOKEN.concat(unModifiedJwtToken):unModifiedJwtToken;
    }

    public static String getJwtTokenFromCookieListRequest(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken == null && request.getCookies() != null) {
            jwtToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "tech-tide-auth-cookie".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .map(token -> BEARER_TOKEN + token)
                    .orElse(null);
        }

        return jwtToken;
    }

    public static void isTokenExpired(io.jsonwebtoken.Claims claims) {
        Date expirationDate = claims.getExpiration();
        if(expirationDate.before(new Date())){
            throw new JWTException(HttpStatus.NOT_ACCEPTABLE, "JWT token is expired");
        }
    }


    public static void writeMessageToResponse(HttpServletResponse response, Exception e) throws IOException {
        ErrorObject errorObject = ErrorObjectCreator.createErrorObject(
                e instanceof JWTException jwtException ? jwtException.getErrorCode() : HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        response.setContentType("application/json");
        response.setStatus(errorObject.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorObject));
    }



    public static String tryGetJwtTokenFromAuthorizationHeaderOrCookieListsRequest(HttpServletRequest request) {
        String token = getJwtTokenFromAuthorizationHeader(request);
        return token == null || token.isEmpty() ? getJwtTokenFromCookieListRequest(request) : token;
    }
}
