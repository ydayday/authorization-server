package com.oauth2.authorization.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth2.authorization.exception.ErrorResponse;
import com.oauth2.authorization.exception.GlobalException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch (GlobalException e){
            setErrorResponse(response, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, GlobalException ex){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(ex.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = ErrorResponse.of(ex);
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
