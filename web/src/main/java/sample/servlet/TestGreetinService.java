package sample.servlet;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

//@Component
public class TestGreetinService implements GreetingService {

    private Logger logger = Logger.getLogger(TestGreetinService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final String consulUrl;
    private final DockerClient dockerClient;
    private final String NETWORK_NAME = "my-net";

    public TestGreetinService() {

        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://192.168.99.103:2376")
                .withDockerCertPath("/Users/denis/.docker/machine/machines/default/").build();

        dockerClient = DockerClientBuilder.getInstance(config).build();
        Info info = dockerClient.infoCmd().exec();

        consulUrl = info.getClusterStore().replace("consul", "http");

        logger.debug("Consul address: " + consulUrl);
        logger.debug("Info: " + info);
    }

    public String getGreeting() {
        String helloUrl = "http://127.0.0.1:8082/hello";
        String worldUrl = "http://127.0.0.1:8083/world";

        logger.debug("Hello url: " + helloUrl);

        String hello = restTemplate.getForObject(helloUrl, String.class);
        String world = restTemplate.getForObject(worldUrl, String.class);

        return hello + " " + world + "!";
    }

}
