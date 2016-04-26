package emmet.sales.entity.pi.integration;

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

import emmet.sales.pi.SalesProformaInvoiceApplication;

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
		String versionId = id+"-1";
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
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(versionId)))
				.andExpect(jsonPath("proformaInvoice.id", equalTo(id)));//

		this.mvc.perform(get("/proformaInvoices"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("content[0].id", equalTo(id)));

		this.mvc.perform(get("/proformaInvoices/"+id+"/versions/" + versionId))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(versionId)))
				.andExpect(jsonPath("status", equalTo("PROCESSING")))
				.andExpect(jsonPath("versionSequence", equalTo(1)))
				.andExpect(jsonPath("info.customerDocumentId", equalTo("8888")))
				.andExpect(jsonPath("info.proformaInvoiceDate", equalTo("2016-01-01")))
				.andExpect(jsonPath("info.contact.id", equalTo(1)))
				.andExpect(jsonPath("info.shippingDate", equalTo("2016-03-01")))
				.andExpect(jsonPath("info.sales.id", equalTo("EM001")))
				.andExpect(jsonPath("info.corporation.id", equalTo(10)))
				.andExpect(jsonPath("extraCharges[0].itemName", equalTo("Foo")))
				.andExpect(jsonPath("shipping.info", equalTo("TNT, ASAP")))
				.andExpect(jsonPath("productItems[0].quantity", equalTo(1)))
				.andExpect(jsonPath("productItems[0].note1", equalTo("123")))
				.andExpect(jsonPath("productItems[0].currency.id", equalTo("USD")))
				;//


		// Modify
		String requestBody2 = "{" + //
				"\"extraCharges\":[{\"itemName\":\"Foo123\",\"price\":100.5,\"tax\":5}]," + //
				"\"productItems\":[{\"product\":{\"id\":\"0000-0002\"}, " + //
				"\"unit\":\"PCS\", \"currency\":{\"id\":\"USD\"}, \"quantity\":1, \"unitPrice\":100.5 , \"note1\":\"123\", \"note2\":\"456\" }],"
				+ //
				"\"info\":{" + //
				"\"sales\":{\"id\":\"EM001\"}," + //
				"\"customerDocumentId\":\"8888-11\"," + //
				"\"proformaInvoiceDate\":\"2016-1-1\"," + //
				"\"customer\":{\"id\":\"AU00000001\"}," + //
				"\"corporation\":{\"id\":10}," + //
				"\"contact\":{\"id\":1,\"firstName\":\"Jonh\",\"lastName\":\"Doe\"}," + //
				"\"shippingDate\":\"2016-5-1\"" + //
				"}," + //
				"\"shipping\":{\"fare\":20.5,\"tax\": 0.2,\"info\":\"TNT, ASAP56789\"}" + //
				"}";

		this.mvc.perform(
				put("/proformaInvoices/versions/" + versionId)
				.contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody2)
				)//
				.andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("id", equalTo(versionId)))//
				.andExpect(jsonPath("extraCharges[0].itemName", equalTo("Foo123")))//
				.andExpect(jsonPath("productItems[0].product.id", equalTo("0000-0002")))//
				.andExpect(jsonPath("info.customerDocumentId", equalTo("8888-11")))//
				.andExpect(jsonPath("info.shippingDate", equalTo("2016-05-01")))//
				.andExpect(jsonPath("shipping.fare", equalTo(20.5)))//
				.andExpect(jsonPath("shipping.tax", equalTo(0.2)))//
				.andExpect(jsonPath("shipping.info", equalTo("TNT, ASAP56789")))//
				;//

		this.mvc.perform(get("/proformaInvoices/"+id+"/versions/" + versionId))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(versionId)))//
				.andExpect(jsonPath("extraCharges[0].itemName", equalTo("Foo123")))//
				.andExpect(jsonPath("productItems[0].product.id", equalTo("0000-0002")))//
				.andExpect(jsonPath("info.customerDocumentId", equalTo("8888-11")))//
				.andExpect(jsonPath("info.shippingDate", equalTo("2016-05-01")))//
				.andExpect(jsonPath("shipping.fare", equalTo(20.5)))//
				.andExpect(jsonPath("shipping.tax", equalTo(0.2)))//
				.andExpect(jsonPath("shipping.info", equalTo("TNT, ASAP56789")))//
				;//
				

		//generate new version from other version
		this.mvc.perform(post("/proformaInvoices/versions/"+versionId+"/copyFrom"))//
		.andDo(print())
		.andExpect(jsonPath("id", equalTo(id+"-2")))
		.andExpect(jsonPath("extraCharges[0].itemName", equalTo("Foo123")))//
		.andExpect(jsonPath("productItems[0].product.id", equalTo("0000-0002")))//
		.andExpect(jsonPath("info.customerDocumentId", equalTo("8888-11")))//
		.andExpect(jsonPath("info.shippingDate", equalTo("2016-05-01")))//
		.andExpect(jsonPath("shipping.fare", equalTo(20.5)))//
		.andExpect(jsonPath("shipping.tax", equalTo(0.2)))//
		.andExpect(jsonPath("shipping.info", equalTo("TNT, ASAP56789")))//
		;//
		
		this.mvc.perform(get("/proformaInvoices/" + id + "/versions"))//
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));//

		// Get specified version
		this.mvc.perform(get("/proformaInvoices/" + id + "/versions/" + id + "-2"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("id", equalTo(id + "-2")));



		// Search by sales
		this.mvc.perform(get("/proformaInvoices/list/filterBySalesAndPiLike?piId=&salesId=EM001"))//
				.andDo(print()).andExpect(jsonPath("content", hasSize(1)))//
				.andExpect(jsonPath("content[0].finalVersion.versionSequence", equalTo(2)));//

		// Set confirmed

		String requestBody3="{\"status\":\"CONFIRMED\"}";
		
		this.mvc.perform(put("/proformaInvoices/versions/" + versionId + "/setStatus")
				.contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody3)
				)//
				.andDo(print()).andExpect(status().isOk());//

		this.mvc.perform(get("/proformaInvoices/" + id + "/versions/" + versionId))//
				.andExpect(jsonPath("id", equalTo(versionId)))//
				.andExpect(jsonPath("status", equalTo("CONFIRMED")));//


		// Set PurchaseNumber;
		String requestBody4="{\"number\":\"PO888888\"}";
		
		this.mvc.perform(put("/proformaInvoices/versions/" + versionId + "/setPurchaseNumber")
				.contentType(MediaType.APPLICATION_JSON_VALUE)//
				.content(requestBody4)
				)//
				.andDo(print()).andExpect(status().isOk());//

		this.mvc.perform(get("/proformaInvoices/" + id + "/versions/" + versionId))//
				.andExpect(jsonPath("id", equalTo(versionId)))//
				.andExpect(jsonPath("info.customerDocumentId", equalTo("PO888888")));//
	}

	@Test
	public void savePiErrorHandlingTest() throws Exception {
		this.mvc.perform(get("/proformaInvoices/NOT_EXIST"))//
				.andDo(print()).andExpect(status().isNotFound());

	}

}
