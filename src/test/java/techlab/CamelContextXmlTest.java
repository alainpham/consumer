package techlab;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CamelContextXmlTest extends CamelSpringTestSupport {

    // Templates to send to input endpoints
    @Produce(uri = "activemq:queue:app.events")
    protected ProducerTemplate inputEndpoint;

    @EndpointInject(uri = "mock:output")
    protected MockEndpoint outputEndpoint;

    @Test
    public void testMessageReception() throws Exception{

        // Create routes from the endpoints to our mock endpoints so we can assert expectations
        AdviceWithRouteBuilder interceptorForMock = new AdviceWithRouteBuilder() {

            @Override
            public void configure() throws Exception {
                // mock the for testing
                interceptFrom("activemq:queue:app.events").to(outputEndpoint);
            }
        };

        context.getRouteDefinition("consume-events").adviceWith(context, interceptorForMock);

        // Ensure expectations make sense for the route(s) we're testing
        outputEndpoint.expectedMinimumMessageCount(1);

        for (int i = 0; i < 10; i++) {
            inputEndpoint.sendBody("Hello");
        }

        // Validate our expectations
        assertMockEndpointsSatisfied();

    }

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext() {


        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");

    }
}
