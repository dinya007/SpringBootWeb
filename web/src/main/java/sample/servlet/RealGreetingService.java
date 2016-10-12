package sample.servlet;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Component
public class RealGreetingService implements GreetingService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String consulUrl;
    private final DockerClient dockerClient;
    private final String NETWORK_NAME = "my-net";
    private Logger logger = Logger.getLogger(RealGreetingService.class);

    public RealGreetingService() throws UnknownHostException, SocketException {

        String ip = null;
//
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()){
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                String hostAddress = inetAddresses.nextElement().getHostAddress();
                if (hostAddress.startsWith("172")) ip = hostAddress;
            }
        }

//        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
//                .withDockerHost(System.getenv("DOCKER_HOST"))
//                .withDockerCertPath(System.getenv("DOCKER_CERT_PATH")).build();

        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://192.168.99.105:3376")
                .withDockerCertPath("/Users/denis/.docker/machine/machines/web-node").build();

        dockerClient = DockerClientBuilder.getInstance(config).build();
        Info info = dockerClient.infoCmd().exec();

        consulUrl = info.getClusterStore().replace("consul", "http");

        logger.debug("Consul address: " + consulUrl);
        logger.debug("Info: " + info);

    }

//    public static void main(String[] args) throws DockerCertificateException, DockerException, InterruptedException, IOException {
//
//        new RealGreetingService().getGreeting();
//
//    }

    public void registerService(){
        ConsulRegistryRequest request = new ConsulRegistryRequest(System.getenv("HOSTNAME"));
        request.setAddress("");
        request.setPort("8080");
//        restTemplate.postForObject(consulUrl + "/v1/agent/service/register", )
    }

    @Override
    public String getGreeting() throws IOException {

        Network myNetwork = getMyNetwork();

        InspectContainerResponse helloContainer = null;
        InspectContainerResponse worldContainer = null;

        for (String id : myNetwork.getContainers().keySet()) {
            InspectContainerResponse container = dockerClient.inspectContainerCmd(id).exec();
            if (container.getName().contains("_hello_")) helloContainer = container;
            else if (container.getName().contains("_world_")) worldContainer = container;
        }
        String helloUrl = "http://" + helloContainer.getConfig().getHostName() + ":8080/";
        String worldUrl = "http://" + worldContainer.getConfig().getHostName() + ":8080/";

        logger.debug("Hello url: " + helloUrl);
        logger.debug("World url: " + worldUrl);

        String hello = restTemplate.getForObject(helloUrl, String.class);
        String world = restTemplate.getForObject(worldUrl, String.class);

        return hello + " " + world + "!";
    }

    private Network getMyNetwork() {
        final Network[] networks = {null};
        dockerClient.listNetworksCmd().exec().forEach(net -> {
//            if (net.getName().endsWith("_" + NETWORK_NAME)) {
//                networks[0] = net;
//            }

            if (net.getName().equals(NETWORK_NAME)) {
                networks[0] = net;
            }
        });
        Network network = networks[0];

        return dockerClient.inspectNetworkCmd().withNetworkId(network.getId()).exec();
    }

}
