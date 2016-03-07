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
public class WorldServlet extends SpringBootServletInitializer {

    private Logger logger = Logger.getLogger(WorldServlet.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WorldServlet.class, args);
    }

    @Bean
    @SuppressWarnings("serial")
    @RequestMapping("/world")
    public Servlet dispatcherServlet() {

        return new GenericServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res)
                    throws ServletException, IOException {
                logger.debug("Request was received");
                res.setContentType("text/plain");
                res.getWriter().append(getWorld(true));
            }
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WorldServlet.class);
    }

    public String getWorld(boolean isGreat) {
        return (isGreat) ? "World" : "There is no greeter for you";
    }

}
