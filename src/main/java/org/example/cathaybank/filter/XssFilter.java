package org.example.cathaybank.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
/**
 * @Author: Lichi
 * @Date: 2024/02/03/上午 05:47
 * @Description:
 */

public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        XssHttpServletRequestWrapper wrappedRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(wrappedRequest, response);
    }

    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return cleanXss(value);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }
            int length = values.length;
            String[] sanitizedValues = new String[length];
            for (int i = 0; i < length; i++) {
                sanitizedValues[i] = cleanXss(values[i]);
            }
            return sanitizedValues;
        }

        private String cleanXss(String value) {
            return value != null ? Jsoup.clean(value, Safelist.none()) : null;
        }
    }
}