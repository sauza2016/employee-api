package com.invex.employee.infrastructure.adapter.in.rest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        try {
            log.info(
                    "Request method={} uri={} contentType={} userAgent={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getContentType(),
                    request.getHeader("User-Agent")
            );

            filterChain.doFilter(request, response);

        } finally {

            long duration = System.currentTimeMillis() - start;

            log.info(
                    "Response method={} uri={} status={} duration={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration
            );
        }
    }
}