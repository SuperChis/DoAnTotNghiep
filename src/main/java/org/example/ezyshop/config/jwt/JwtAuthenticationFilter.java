package org.example.ezyshop.config.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.config.service.UserDetailsServiceImpl;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

//Check user request
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if(jwt != null ){
                // Kiểm tra xem token có hết hạn không
                if (jwtUtils.isTokenExpired(jwt)) {
                    response.setContentType("application/json");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setStatus(419);
                    response.getWriter().write("{\"code\": 419, \"message\": \"Token expired. Please refresh.\"}");
                    return;
                }
                if (!jwtUtils.validateJwtToken(jwt)) {
                    return;
                }
                String userMail = jwtUtils.getUsernameFromJwtToken(jwt);
                Optional<User> user = userService.findByEmail(userMail);
                UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e){
            log.error("failed on set account authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.split(" ")[1].trim();
        }


        return null;
    }
}
