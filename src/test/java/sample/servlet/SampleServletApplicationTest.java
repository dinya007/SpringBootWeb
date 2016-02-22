package sample.servlet;

import org.junit.Assert;
import org.junit.Test;

public class SampleServletApplicationTest {

    private SampleServletApplication sampleServletApplication = new SampleServletApplication();

    @Test
    public void getGreeter() throws Exception {
        Assert.assertEquals("Hello World", sampleServletApplication.getGreeter(true));
        Assert.assertEquals("There is no greeter for you", sampleServletApplication.getGreeter(false));
    }
}