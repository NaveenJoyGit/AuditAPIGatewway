package com.cognizant.zuulapigateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cognizant.zuulapigateway.model.UserPrincipal;
import com.cognizant.zuulapigateway.service.UserAuthDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
//	@Value("${jwt.secret}")
//	private String jwtSecret;
	
	private String jwtSecret = "SomeTextInRandom";

//	@Autowired
//	private JWTTokenProvider tokenProvider;

	@Autowired
	private UserAuthDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String headerParam  =   request.getHeaders("Authorization").nextElement();
//		response.setHeader("Authorization", headerParam);
	    log.info("---------header in zuul---------");
        log.info("headerParam: "+headerParam);
		
		log.info("Validating Token!!!!!");
		try {
			String jwt = getJwtFromRequest(request);
			log.info("----------Recieved jwt--------");
			log.info(jwt);
			if (StringUtils.hasText(jwt) && validateToken(jwt)) {
				log.info("Token is Valid ");
				String userNameFromToken = getUserNameFromToken(jwt);
				UserPrincipal userDetails = userDetailsService.loadUserByUsername(userNameFromToken);
				log.info("printing parsed dredentials");
				log.info(userNameFromToken);
				log.info(userDetails.getUsername());
				System.out.println(userDetails.getAuthorities());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		} catch (Exception ex) {
			log.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
	public boolean validateToken(String jwt) {
		try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
	}

	public String getUserNameFromToken(String token) {
		return  Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
}
