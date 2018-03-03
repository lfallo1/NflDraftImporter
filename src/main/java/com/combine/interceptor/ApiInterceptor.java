package com.combine.interceptor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor designed to allow only logged in users to pass through
 *
 * @author lfallon
 */
@Component
public class ApiInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(ApiInterceptor.class);

//    @Value("${application.rest.token}")
//    private String applicationToken;

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex)
            throws Exception {
        //stub
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView ex)
            throws Exception {
        // stub
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {

        try {

//            UUID uuid = UUID.fromString(req.getHeader("X-Auth-Token"));
//            return applicationToken.equals(uuid.toString());
            return true;

        } catch (NullPointerException | IllegalArgumentException e) {
            return false;
        }
    }

}
