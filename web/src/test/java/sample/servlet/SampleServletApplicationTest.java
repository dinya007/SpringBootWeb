package sample.servlet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SampleServletApplicationTest {

    private HelloWorldServlet helloWorldServlet = new HelloWorldServlet();

    @Test
    @Ignore
    public void getGreeter() throws Exception {
        Assert.assertEquals("Hello World", helloWorldServlet.getGreeter(true));
        Assert.assertEquals("There is no greeter for you", helloWorldServlet.getGreeter(false));
    }
}