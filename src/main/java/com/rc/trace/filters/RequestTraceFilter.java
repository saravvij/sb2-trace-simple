package com.rc.trace.filters;

import com.rc.trace.common.IdGenerator;
import com.rc.trace.common.TraceHeaders;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(1)
public class RequestTraceFilter implements Filter {
    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("REQUEST FILTER.....");
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String traceId = Optional.ofNullable(httpRequest.getHeader(TraceHeaders.TRACE_ID))
                .orElse(IdGenerator.generateId());
        String spanId = Optional.ofNullable(httpRequest.getHeader(TraceHeaders.SPAN_ID))
                .orElse(IdGenerator.generateId());

        // Append current service to TracePath
        String tracePath = Optional.ofNullable(httpRequest.getHeader(TraceHeaders.TRACE_PATH))
                .map(path -> path + " > " + serviceName)
                .orElse(serviceName);

        // Todo: Read from user token
        String userDomain = "unknown";
        String domain = Optional.ofNullable(httpRequest.getHeader(TraceHeaders.DOMAIN))
                .orElse(userDomain);

        MDC.put(TraceHeaders.SERVICE_NAME, serviceName);
        MDC.put(TraceHeaders.TRACE_ID, traceId);
        MDC.put(TraceHeaders.SPAN_ID, spanId);
        MDC.put(TraceHeaders.CLIENT_REQUEST_ID, httpRequest.getHeader(TraceHeaders.CLIENT_REQUEST_ID));
        MDC.put(TraceHeaders.DOMAIN, domain);
        MDC.put(TraceHeaders.TRACE_PATH, tracePath);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
