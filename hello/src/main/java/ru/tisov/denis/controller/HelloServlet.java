package ru.tisov.denis.controller;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.*;
import java.io.IOException;

@Configuration
@EnableAutoConfiguration
@RestController
public class HelloServlet extends SpringBootServletInitializer {

    private Logger logger = Logger.getLogger(HelloServlet.class);


    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloServlet.class, args);
    }

    @Bean
    @SuppressWarnings("serial")
    @RequestMapping("/hello")
    public Servlet dispatcherServlet() {

        return new GenericServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res)
                    throws ServletException, IOException {
                res.setContentType("text/plain");
                logger.debug("Request was received");
                res.getWriter().append(getHello(true));
            }
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HelloServlet.class);
    }

    public String getHello(boolean isGreat) {
        return (isGreat) ? "Hello" : "There is no greeter for you";
    }

}
