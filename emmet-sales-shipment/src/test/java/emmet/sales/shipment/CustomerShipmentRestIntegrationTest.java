package emmet.sales.shipment;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalesShipmantApplication.class)
@ActiveProfiles("test")
//@Sql({ "/sql/delete_data.sql", "/sql/insert_data.sql" })

@WebAppConfiguration
public class CustomerShipmentRestIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void consigneeUriTest() throws Exception {
		this.mvc.perform(get("/order/shipment/shippingCompany")).andExpect(status().isOk());
	}

	@Test
	public void consigneeContentTest() throws Exception {
		ResultActions resultActions = this.mvc.perform(
				MockMvcRequestBuilders.get("/order/shipment/shippingCompany").accept(MediaType.APPLICATION_JSON));
		resultActions.andDo(MockMvcResultHandlers.print());
		
		this.mvc.perform(get("/order/shipment/shippingCompany")).andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", equalTo(1))).andExpect(jsonPath("$[0].name", equalTo("DHL")))
				.andExpect(jsonPath("$[2].name", equalTo("TNT")));

	}

}
