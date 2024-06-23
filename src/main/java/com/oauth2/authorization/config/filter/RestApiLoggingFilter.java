package com.oauth2.authorization.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.UUID;

@Slf4j
public class RestApiLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            final UUID uuid = UUID.randomUUID();
            MDC.put("traceId", uuid.toString());
            filterChain.doFilter(requestWrapper, responseWrapper);
            MDC.clear();
        } finally {
            logRequest(requestWrapper);
            logResponse(responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder requestMessage = new StringBuilder();
        requestMessage.append("\n >>>>>>>>>> Request [Method] [URI] : ");
        requestMessage.append(request.getMethod()).append(" ");
        requestMessage.append(request.getRequestURI());
        requestMessage.append("\n >>>>>>>>>> Request [Params] : ").append(request.getQueryString());
        requestMessage.append("\n >>>>>>>>>> Request [Client IP] : ").append(request.getRemoteAddr());

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            requestMessage.append("\n >>>>>>>>>> Request Header [").append(headerName).append("] : ").append(headerValue).append(", ");
        }

        /* 마지막 쉼표 제거 */
        if (requestMessage.lastIndexOf(", ") != -1) {
            requestMessage.setLength(requestMessage.length() - 2);
        }

        log.info(requestMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append(" >>>>>>>>>> Response: ");
        responseMessage.append(response.getStatus());
        responseMessage.append(" Body: \n").append(getResponseBody(response));
        log.info(responseMessage.toString());
    }

    private String getResponseBody(ContentCachingResponseWrapper response) throws UnsupportedEncodingException {
        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            return new String(buf, 0, buf.length, response.getCharacterEncoding()) + "\n";
        }
        return "";
    }
}
