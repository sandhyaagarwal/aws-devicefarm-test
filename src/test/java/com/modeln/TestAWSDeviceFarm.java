package com.modeln;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.devicefarm.DeviceFarmClient;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlRequest;
import software.amazon.awssdk.services.devicefarm.model.CreateTestGridUrlResponse;

import java.net.URL;

public class TestAWSDeviceFarm {
    // ... When you set up your test suite
    private static RemoteWebDriver driver;

    @Before
    public void setUp() {
        String myProjectARN = "arn:aws:devicefarm:us-west-2:423254791125:testgrid-project:495c7e0e-650e-4f13-a628-470352c0af7d";
        DeviceFarmClient client  = DeviceFarmClient.builder().region(Region.US_WEST_2).build();
        CreateTestGridUrlRequest request = CreateTestGridUrlRequest.builder()
        .expiresInSeconds(300)
        .projectArn(myProjectARN)
        .build();
        CreateTestGridUrlResponse response = client.createTestGridUrl(request);
        try {
            URL testGridUrl = new URL(response.url());
            System.out.println("testGridUrl = " + testGridUrl);
            // You can now pass this URL into RemoteWebDriver.
            driver = new RemoteWebDriver(testGridUrl, DesiredCapabilities.internetExplorer());
            System.out.println("driver  = " + driver);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBasic() {
        System.out.println("driver = " + driver);
        assert driver != null;
    }

    @After
    public void tearDown() {
        // make sure to close your WebDriver session and avoid being over-billed:
        driver.quit();
    }
}
