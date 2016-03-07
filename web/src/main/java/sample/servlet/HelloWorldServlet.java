/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@RestController
@ComponentScan
public class HelloWorldServlet extends SpringBootServletInitializer {

    @Autowired
    private GreetingService greetingService;

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorldServlet.class, args);
    }

    @Bean
    @SuppressWarnings("serial")
    @RequestMapping("/greeting")
    public Servlet dispatcherServlet() {

        return new GenericServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res)
                    throws ServletException, IOException {
                res.setContentType("text/plain");

                res.getWriter().append(greetingService.getGreeting() + " Now: " + new Date());

                for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                    res.getWriter().append("\n" + entry.getKey() + " : " + entry.getValue());
                }
            }
        };
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HelloWorldServlet.class);
    }

    public String getGreeter(boolean isGreat) {
        String hello = restTemplate.getForObject("http://127.0.0.1:8082/hello", String.class);
        String world = restTemplate.getForObject("http://127.0.0.1:8083/world", String.class);
        return (isGreat) ? hello + " " + world + "!" : "There is no greeter for you";
    }

}