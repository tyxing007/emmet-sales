package emmet.sales.shipment;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.hasSize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalesShipmantApplication.class)
@ActiveProfiles("test")

@WebAppConfiguration
public class ShipmentRestAPiIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void uriTest() throws Exception {
		this.mvc.perform(get("/order/shipment/types")).andExpect(status().isOk());
	}
	
	@Test
	public void contentsTest() throws Exception {
		this.mvc.perform(get("/order/shipment/types")).andExpect(jsonPath("$", hasSize(5)));
	}

}
