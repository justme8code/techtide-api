package com.justme8code.techtide.filter;

import com.justme8code.techtide.exceptions.JWTException;
import com.justme8code.techtide.services.impls.CustomUserDetailsService;
import com.justme8code.techtide.util.JwtAuthorizer;
import com.justme8code.techtide.util.JwtFilterUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthorizer authorizer;
    private static final Logger JwtFilterLogger = LoggerFactory.getLogger(JWTFilter.class);


    public JWTFilter(CustomUserDetailsService userDetailsService,JwtAuthorizer authorizer) {
        this.customUserDetailsService = userDetailsService;
        this.authorizer = authorizer;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        JwtFilterLogger.debug("JWTFilter doFilter called : {}", request.getRequestURI());
        if(request.getRequestURI().contains("/api/auth") && request.getMethod().equals("DELETE"))
            filterChain.doFilter(request, response);


        String jwtToken = JwtFilterUtil.tryGetJwtTokenFromAuthorizationHeaderOrCookieListsRequest(request);
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            try {
                Claims claims = authorizer.validateToken(jwtToken);
                JwtFilterUtil.isTokenExpired(claims);
                setSecurityContextAfterJwtTokenAuthentication(claims,request);
            } catch (JWTException | UsernameNotFoundException e) {
                JwtFilterUtil.writeMessageToResponse(response, e);
                return;
            }
        }


        filterChain.doFilter(request, response);

    }


    private void setSecurityContextAfterJwtTokenAuthentication(Claims claims,HttpServletRequest request){
        String userId= claims.getSubject();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
