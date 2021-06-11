package com.optimagrowth.license.filter;

import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        var httpServletRequest = (HttpServletRequest) request;

        UserContextHolder.getContext().setCorrelationId(
            httpServletRequest.getHeader(UserContext.CORRELATION_ID));
        UserContextHolder.getContext().setAuthToken(
            httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrganizationId(
            httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

        log.info("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        chain.doFilter(httpServletRequest, response);
    }
}
