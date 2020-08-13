package com.cakefactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ControllerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(ControllerInterceptor.class);
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
            throws Exception {
        if (request.getRequestURI().contains("."))
            return;

//        log.info("[postHandle][" + request.getRequestURI() + "], ");

        ResponseEntity<String> responseEntity =
                restTemplate.getForEntity("http://169.254.169.254/latest/meta-data/local-ipv4/", String.class);
        String ip = responseEntity.getBody();

        responseEntity = restTemplate.getForEntity("http://169.254.169.254/latest/meta-data/placement/availability-zone/", String.class);
        String az = responseEntity.getBody();

        if (modelAndView != null) {
            modelAndView.getModelMap().addAttribute("ip", ip);
            modelAndView.getModelMap().addAttribute("az", az);
        }
    }
}
