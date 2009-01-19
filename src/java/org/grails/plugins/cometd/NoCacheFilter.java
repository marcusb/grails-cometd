package org.grails.plugins.cometd;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoCacheFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException { }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ((HttpServletResponse) servletResponse).setHeader("Expires", "0"); // 0 is invalid, and browsers should interpret it as expired
        ((HttpServletResponse) servletResponse).setHeader("Cache-Control", "no-cache");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {}
}
