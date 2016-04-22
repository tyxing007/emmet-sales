package emmet.sales.order;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalesOrderApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Sql({ "/sql/testdata.sql" })
public class SalesOrderIntegrationTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	String dateInString;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		DateFormat df = new SimpleDateFormat("YYMMdd");
		Date today = Calendar.getInstance().getTime();
		dateInString = df.format(today);
	}

	@Test
	public void savePiTest() throws Exception {
		String id = "OD" + dateInString + "0001";
		String versionId = id+"-1";


	}

	@Test
	public void createOrderFromPi() throws Exception {
		String requestBody ="{\"piVersionId\":\"\"}";
		
		this.mvc.perform(post("/createFromPI"))//
				.andDo(print()).andExpect(status().isNotFound());

	}
	
	
	

}
