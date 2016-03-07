package sample.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsulRegistryRequest {

    @JsonProperty("Name")
    private final String name;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Port")
    private String port;

    public ConsulRegistryRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
