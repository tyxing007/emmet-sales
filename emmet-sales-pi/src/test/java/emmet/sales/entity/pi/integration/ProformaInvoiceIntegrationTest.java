package emmet.sales.entity.pi.integration;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.pi.SalesProformaInvoiceApplication;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalesProformaInvoiceApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
@Sql({ "/sql/testdata.sql" })
public class ProformaInvoiceIntegrationTest {

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
		String id = "PI" + dateInString + "0001";
		String requestBody = "{" + //
				"\"extraCharges\":[{\"itemName\":\"Foo\",\"price\":100.5,\"tax\":5}]," + //
				"\"productItems\":[{\"product\":{\"id\":\"0000-0001\"}, " + //
				"\"unit\":\"PCS\", \"currency\":{\"id\":\"USD\"}, \"quantity\":1, \"unitPrice\":100.5 , \"note1\":\"123\", \"note2\":\"456\" }],"
				+ //
				"\"info\":{" + //
				"\"sales\":{\"id\":\"EM001\"}," + //
				"\"customerDocumentId\":\"8888\"," + //
				"\"proformaInvoiceDate\":\"2016-1-1\"," + //
				"\"customer\":{\"id\":\"AU00000001\"}," + //
				"\"corporation\":{\"id\":10}," + //
				"\"contact\":{\"id\":1,\"firstName\":\"Jonh\",\"lastName\":\"Doe\"}," + //
				"\"shippingDate\":\"2016-3-1\"" + //
				"}," + //
				"\"shipping\":{\"fare\":10.5,\"tax\": 0.1,\"info\":\"TNT, ASAP\"}" + //
				"}";

		this.mvc.perform(post("/proformaInvoices").contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody))//
				.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("id", equalTo(id)));//

		this.mvc.perform(get("/proformaInvoices"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("content[0].id", equalTo(id)));

		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(id)))//
				.andExpect(jsonPath("status", equalTo("PROCESSING")))//
				.andExpect(jsonPath("finalVersion.id", equalTo(id + "-1")))//
				.andExpect(jsonPath("finalVersion.info.customerDocumentId", equalTo("8888")))//
				.andExpect(jsonPath("finalVersion.info.proformaInvoiceDate", equalTo("2016-01-01")))//
				.andExpect(jsonPath("finalVersion.info.sales.id", equalTo("EM001")))//
				.andExpect(jsonPath("finalVersion.info.contact.id", equalTo(1)))//
				.andExpect(jsonPath("finalVersion.info.contact.firstName", equalTo("John")))//
				.andExpect(jsonPath("finalVersion.info.shippingDate", equalTo("2016-03-01")))//
				.andExpect(jsonPath("finalVersion.info.customer.id", equalTo("AU00000001")))//
				.andExpect(jsonPath("finalVersion.info.corporation.id", equalTo(10)))//
				.andExpect(jsonPath("finalVersion.info.customerDocumentId", equalTo("8888")))//
				//
				.andExpect(jsonPath("finalVersion.shipping.fare", equalTo(10.5)))//
				.andExpect(jsonPath("finalVersion.shipping.tax", equalTo(0.1)))//
				.andExpect(jsonPath("finalVersion.shipping.info", equalTo("TNT, ASAP")))//
				//
				.andExpect(jsonPath("finalVersion.extraCharges[0].itemName", equalTo("Foo")))//
				.andExpect(jsonPath("finalVersion.extraCharges[0].price", equalTo(100.5)))//
				.andExpect(jsonPath("finalVersion.extraCharges[0].tax", equalTo(5.0)))//
				//
				.andExpect(jsonPath("finalVersion.productItems[0].product.id", equalTo("0000-0001")))//
				.andExpect(jsonPath("finalVersion.productItems[0].product.name", equalTo("Bar")))//
				.andExpect(jsonPath("finalVersion.productItems[0].unit", equalTo("PCS")))//
				.andExpect(jsonPath("finalVersion.productItems[0].quantity", equalTo(1)))//
				.andExpect(jsonPath("finalVersion.productItems[0].unitPrice", equalTo(100.5)))//
				.andExpect(jsonPath("finalVersion.productItems[0].currency.id", equalTo("USD")));//

		// Modify
		String requestBody2 = "{" + //
				"\"productItems\":[{\"product\":{\"id\":\"0000-0002\"}, " + //
				"\"unit\":\"PCS\", \"currency\":{\"id\":\"USD\"}, \"quantity\":1, \"unitPrice\":100.5 , \"note1\":\"123\", \"note2\":\"456\" }],"
				+ //
				"\"info\":{" + //
				"\"sales\":{\"id\":\"EM001\"}," + //
				"\"customer\":{\"id\":\"AU00000001\"}" + //
				"}}";

		this.mvc.perform(put("/proformaInvoices/" + id).contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody2))//
				.andDo(print()).andExpect(status().isCreated());//

		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(id)))//

				// version change to 2
				.andExpect(jsonPath("finalVersion.id", equalTo(id + "-2")))
				.andExpect(jsonPath("finalVersion.productItems", hasSize(1)))//
				.andExpect(jsonPath("finalVersion.productItems[0].product.id", equalTo("0000-0002")));//

		this.mvc.perform(get("/proformaInvoices/" + id + "/versions"))//
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));//

		// Get specified version
		this.mvc.perform(get("/proformaInvoices/" + id + "/versions/" + id + "-2"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(id + "-2")));

		// Modify
		String requestBody3 = "{" + //
				"\"info\":{" + //
				"\"customer\":{\"id\":\"AU00000002\"}" + //
				"}"//
				+ "}";

		this.mvc.perform(put("/proformaInvoices/" + id).contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody3))//
				.andDo(print()).andExpect(status().isConflict());//

		// Set final version
		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andExpect(jsonPath("id", equalTo(id)))//
				.andExpect(jsonPath("finalVersion.id", equalTo(id + "-2")));//

		this.mvc.perform(put("/proformaInvoices/" + id + "/setFinalVersion?version=" + id + "-1"))//
				.andDo(print()).andExpect(status().isOk());//

		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andExpect(jsonPath("id", equalTo(id)))//
				.andExpect(jsonPath("finalVersion.id", equalTo(id + "-1")));//

		// Search by sales
		this.mvc.perform(get("/proformaInvoices/search/findBySales/?id=EM001"))//
				.andDo(print()).andExpect(jsonPath("content", hasSize(1)))//
				.andExpect(jsonPath("content[0].id", equalTo(id)));//

		// Set confirmed
		// Search by sales and status is COMFIRMED, it would not be found now.
		this.mvc.perform(get("/proformaInvoices/search/findBySales/?id=EM001&status=CONFIRMED"))//
				.andExpect(jsonPath("content", hasSize(0)));//

		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andExpect(jsonPath("id", equalTo(id)))//
				.andExpect(jsonPath("status", equalTo("PROCESSING")));//

		this.mvc.perform(put("/proformaInvoices/" + id + "/setConfirmed"))//
				.andDo(print()).andExpect(status().isOk());//

		this.mvc.perform(get("/proformaInvoices/" + id))//
				.andExpect(jsonPath("id", equalTo(id)))//
				.andExpect(jsonPath("status", equalTo("CONFIRMED")));//

		// Search by sales and status is COMFIRMED
		this.mvc.perform(get("/proformaInvoices/search/findBySales/?id=EM001&status=CONFIRMED"))//
				.andDo(print()).andExpect(jsonPath("content", hasSize(1)))//
				.andExpect(jsonPath("content[0].id", equalTo(id)));//

	}

	@Test
	public void savePiErrorHandlingTest() throws Exception {
		this.mvc.perform(get("/proformaInvoices/NOT_EXIST"))//
				.andDo(print()).andExpect(status().isNotFound());

	}

}
