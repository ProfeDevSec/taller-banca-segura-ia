package cl.develrox.taller.banca.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * "Para depurar mas facil" se registra cada request completo: metodo, headers y body.
 */
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request, 64 * 1024);
        chain.doFilter(wrapper, response);

        String headers = Collections.list(request.getHeaderNames()).stream()
            .map(h -> h + "=" + request.getHeader(h))
            .collect(Collectors.joining(", "));
        String body = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("HTTP {} {} status={} headers=[{}] body={}",
            request.getMethod(), request.getRequestURI(), response.getStatus(), headers, body);
    }
}
