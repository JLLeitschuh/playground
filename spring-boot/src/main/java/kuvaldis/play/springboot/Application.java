package kuvaldis.play.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.actuate.system.EmbeddedServerPortFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties(ImportantConfig.class)
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
//        ApplicationContext ctx = SpringApplication.run(Application.class, args);
//        final PersonServiceImpl<Person> service = ctx.getBean(PersonServiceImpl.class);
//        service.save(new Person());
        final SpringApplication app = new SpringApplicationBuilder()
                .main(Application.class)
                .sources(Application.class)
                .showBanner(false)
                .listeners(new ApplicationPidFileWriter(), new EmbeddedServerPortFileWriter(),
                        event -> log.info("Application event is thrown {}", event))
                .build();
        final Environment environment = app.run(args).getEnvironment();
        final String serverPort = environment.getProperty("server.port", "8080");
        log.info("The application is started on localhost:{}. Access url is http://localhost:{}", serverPort, serverPort);

    }

    // binds to url in case of not found error
    @Bean
    public EmbeddedServletContainerCustomizer error404Customize() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
    }

}
