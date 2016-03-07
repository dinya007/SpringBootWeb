package sample.servlet;

import com.spotify.docker.client.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Enumeration;

import static java.net.NetworkInterface.getNetworkInterfaces;

//@Component
public class SpotifyGreetingService implements GreetingService {


    String ip = null;

    private Logger logger = Logger.getLogger(SpotifyGreetingService.class);


    public SpotifyGreetingService() throws DockerCertificateException, SocketException, DockerException, InterruptedException {

        Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                String hostAddress = inetAddresses.nextElement().getHostAddress();
                if (hostAddress.startsWith("172")) ip = hostAddress;
            }
        }


        final DockerClient docker = DefaultDockerClient.builder()
//                .uri(URI.create("https://" + ip + ":2376"))
                .uri(URI.create(System.getenv("DOCKER_HOST")))
                .dockerCertificates(new DockerCertificates(Paths.get("/etc/ssl/certs/")))
                .build();

        System.out.println(docker.info());
        logger.error(docker.info());

    }

    @Override
    public String getGreeting() throws IOException {
        return null;
    }
}
