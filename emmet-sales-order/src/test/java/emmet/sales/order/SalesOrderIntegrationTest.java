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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
		DateFormat df = new SimpleDateFormat("YYYYMMdd");
		Date today = Calendar.getInstance().getTime();
		dateInString = df.format(today);
	}

	@Test
	public void createOrderFromPi() throws Exception {
		String requestBody1 = "{\"piVersionId\":\"PI1604090030-1\"}";
		String requestBody11 = "{\"piVersionId\":\"PI1604090030-2\"}";

		String requestBody2 = "{\"piVersionId\":\"PI1604200001-1\"}";

		String id1 = "OD" + dateInString + "01";
		String id2 = "OD" + dateInString + "02";

		// orders have no data
		this.mvc.perform(get("/orders/"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("content", hasSize(0)));

		// create first order
		this.mvc.perform(
				post("/orders/createFromPI").contentType(
						MediaType.APPLICATION_JSON_VALUE)//
						.content(requestBody1))//
				.andDo(print()).andExpect(status().isOk());

		this.mvc.perform(get("/orders/"))
				//
				.andDo(print())
				.andExpect(status().isOk())
				//
				.andExpect(jsonPath("content", hasSize(1)))
				.andExpect(jsonPath("content[0].id", equalTo(id1)))
				.andExpect(
						jsonPath("content[0].productItems[0].id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].productItems[0].quantity",
								equalTo(5)))
				.andExpect(
						jsonPath("content[0].productItems[0].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[0].productItems[0].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[0].productItems[0].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[0].productItems[0].product.id",
								equalTo("0000-0001")))
				.andExpect(jsonPath("content[0].productItems[0].status",equalTo("NORMAL")))
				.andExpect(jsonPath("content[0].productItems[0].soldQty",equalTo(0)))					
				.andExpect(
						jsonPath("content[0].productItems[1].id", equalTo(2)))
				.andExpect(
						jsonPath("content[0].productItems[1].quantity",
								equalTo(10)))
				.andExpect(
						jsonPath("content[0].productItems[1].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[0].productItems[1].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[0].productItems[1].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[0].productItems[1].product.id",
								equalTo("0000-0002")))
				.andExpect(jsonPath("content[0].info.id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].info.custPo.poNo",
								equalTo("PO881")))
				.andExpect(
						jsonPath("content[0].info.sales.id", equalTo("EM001")))
				.andExpect(
						jsonPath("content[0].info.dataEntryClerk.id",
								equalTo("EM001")))
				.andExpect(
						jsonPath("content[0].info.customer.id",
								equalTo("AU00000001")))
				.andExpect(
						jsonPath("content[0].info.warranty",
								equalTo("1 years, and bala...1")))
				.andExpect(
						jsonPath("content[0].extraCharges[0].id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].extraCharges[0].itemName",
								equalTo("q11 yyhhj")))
				.andExpect(
						jsonPath("content[0].extraCharges[0].price",
								equalTo(24.0)))
				.andExpect(
						jsonPath("content[0].extraCharges[0].tax", equalTo(5.0)))
				.andExpect(jsonPath("content[0].shipping.id", equalTo(1)))
				.andExpect(jsonPath("content[0].shipping.info", equalTo("aaa")))
				.andExpect(jsonPath("content[0].shipping.fare", equalTo(11.0)))
				.andExpect(jsonPath("content[0].shipping.tax", equalTo(5.0)))
				.andExpect(jsonPath("content[0].status", equalTo("PROCESSING")))

		;

		// create one order and pi has been use
		this.mvc.perform(
				post("/orders/createFromPI").contentType(
						MediaType.APPLICATION_JSON_VALUE)//
						.content(requestBody11))//
				.andDo(print()).andExpect(status().isBadRequest());

		this.mvc.perform(get("/orders/"))//
				.andDo(print()).andExpect(status().isOk())//
				.andExpect(jsonPath("content", hasSize(1)));

		// create second order
		this.mvc.perform(
				post("/orders/createFromPI").contentType(
						MediaType.APPLICATION_JSON_VALUE)//
						.content(requestBody2))//
				.andDo(print()).andExpect(status().isOk());

		this.mvc.perform(get("/orders/"))
				//
				.andDo(print())
				.andExpect(status().isOk())
				//
				.andExpect(jsonPath("content", hasSize(2)))
				.andExpect(jsonPath("content[1].id", equalTo(id2)))
				.andExpect(
						jsonPath("content[1].productItems[0].id", equalTo(3)))
				.andExpect(
						jsonPath("content[1].productItems[0].quantity",
								equalTo(12)))
				.andExpect(
						jsonPath("content[1].productItems[0].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[1].productItems[0].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[1].productItems[0].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[1].productItems[0].product.id",
								equalTo("0000-0001")))
				.andExpect(jsonPath("content[1].productItems[0].status",equalTo("NORMAL")))
				.andExpect(jsonPath("content[1].productItems[0].soldQty",equalTo(0)))					
				.andExpect(
						jsonPath("content[1].productItems[1].id", equalTo(4)))
				.andExpect(
						jsonPath("content[1].productItems[1].quantity",
								equalTo(36)))
				.andExpect(
						jsonPath("content[1].productItems[1].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[1].productItems[1].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[1].productItems[1].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[1].productItems[1].product.id",
								equalTo("0000-0002")))
				.andExpect(jsonPath("content[1].info.id", equalTo(2)))
				.andExpect(
						jsonPath("content[1].info.custPo.poNo",
								equalTo("PO882")))
				.andExpect(
						jsonPath("content[1].info.sales.id", equalTo("EM001")))
				.andExpect(
						jsonPath("content[1].info.dataEntryClerk.id",
								equalTo("EM001")))
				.andExpect(
						jsonPath("content[1].info.customer.id",
								equalTo("AU00000001")))
				.andExpect(
						jsonPath("content[1].info.warranty",
								equalTo("4 years, and bala...4")))
				.andExpect(
						jsonPath("content[1].extraCharges[0].id", equalTo(2)))
				.andExpect(
						jsonPath("content[1].extraCharges[0].itemName",
								equalTo("q44t yyhhj")))
				.andExpect(
						jsonPath("content[1].extraCharges[0].price",
								equalTo(24.0)))
				.andExpect(
						jsonPath("content[1].extraCharges[0].tax", equalTo(5.0)))
				.andExpect(jsonPath("content[1].shipping.id", equalTo(2)))
				.andExpect(jsonPath("content[1].shipping.info", equalTo("ddd")))
				.andExpect(jsonPath("content[1].shipping.fare", equalTo(44.0)))
				.andExpect(jsonPath("content[1].shipping.tax", equalTo(5.0)));

		// set first order's status

		// Set confirmed

		String requestBodyStatus = "{\"status\":\"CONFIRMED\"}";

		this.mvc.perform(
				put("/orders/" + id1 + "/setStatus").contentType(
						MediaType.APPLICATION_JSON_VALUE)//
						.content(requestBodyStatus))//
				.andDo(print()).andExpect(status().isOk());//

		// find first order by id
		this.mvc.perform(get("/orders/" + id1))
				//
				.andDo(print())
				.andExpect(status().isOk())
				//
				.andExpect(jsonPath("id", equalTo(id1)))
				.andExpect(jsonPath("productItems[0].id", equalTo(1)))
				.andExpect(jsonPath("productItems[0].quantity", equalTo(5)))
				.andExpect(jsonPath("productItems[0].unit", equalTo("PCS")))
				.andExpect(jsonPath("productItems[0].note1", equalTo("note1")))
				.andExpect(
						jsonPath("productItems[0].currency.id", equalTo("USD")))
				.andExpect(
						jsonPath("productItems[0].product.id",
								equalTo("0000-0001")))
				.andExpect(jsonPath("productItems[1].id", equalTo(2)))
				.andExpect(jsonPath("productItems[1].quantity", equalTo(10)))
				.andExpect(jsonPath("productItems[1].unit", equalTo("PCS")))
				.andExpect(jsonPath("productItems[1].note1", equalTo("note1")))
				.andExpect(
						jsonPath("productItems[1].currency.id", equalTo("USD")))
				.andExpect(
						jsonPath("productItems[1].product.id",
								equalTo("0000-0002")))
				.andExpect(jsonPath("info.id", equalTo(1)))
				.andExpect(jsonPath("info.custPo.poNo", equalTo("PO881")))
				.andExpect(jsonPath("info.sales.id", equalTo("EM001")))
				.andExpect(jsonPath("info.dataEntryClerk.id", equalTo("EM001")))
				.andExpect(jsonPath("info.customer.id", equalTo("AU00000001")))
				.andExpect(
						jsonPath("info.warranty",
								equalTo("1 years, and bala...1")))
				.andExpect(jsonPath("extraCharges[0].id", equalTo(1)))
				.andExpect(
						jsonPath("extraCharges[0].itemName",
								equalTo("q11 yyhhj")))
				.andExpect(jsonPath("extraCharges[0].price", equalTo(24.0)))
				.andExpect(jsonPath("extraCharges[0].tax", equalTo(5.0)))
				.andExpect(jsonPath("shipping.id", equalTo(1)))
				.andExpect(jsonPath("shipping.info", equalTo("aaa")))
				.andExpect(jsonPath("shipping.fare", equalTo(11.0)))
				.andExpect(jsonPath("shipping.tax", equalTo(5.0)))
				.andExpect(jsonPath("status", equalTo("CONFIRMED")));

		// search orders by id like
		this.mvc.perform(
				get("/orders/search/findByIdLike?id=" + id1 + "&page=0&size=4"))
				//
				.andDo(print())
				.andExpect(status().isOk())
				//
				.andExpect(jsonPath("content", hasSize(1)))
				.andExpect(jsonPath("content[0].id", equalTo(id1)))
				.andExpect(
						jsonPath("content[0].productItems[0].id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].productItems[0].quantity",
								equalTo(5)))
				.andExpect(
						jsonPath("content[0].productItems[0].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[0].productItems[0].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[0].productItems[0].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[0].productItems[0].product.id",
								equalTo("0000-0001")))
				.andExpect(
						jsonPath("content[0].productItems[1].id", equalTo(2)))
				.andExpect(
						jsonPath("content[0].productItems[1].quantity",
								equalTo(10)))
				.andExpect(
						jsonPath("content[0].productItems[1].unit",
								equalTo("PCS")))
				.andExpect(
						jsonPath("content[0].productItems[1].note1",
								equalTo("note1")))
				.andExpect(
						jsonPath("content[0].productItems[1].currency.id",
								equalTo("USD")))
				.andExpect(
						jsonPath("content[0].productItems[1].product.id",
								equalTo("0000-0002")))
				.andExpect(jsonPath("content[0].info.id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].info.custPo.poNo",
								equalTo("PO881")))
				.andExpect(
						jsonPath("content[0].info.sales.id", equalTo("EM001")))
				.andExpect(
						jsonPath("content[0].info.dataEntryClerk.id",
								equalTo("EM001")))
				.andExpect(
						jsonPath("content[0].info.customer.id",
								equalTo("AU00000001")))
				.andExpect(
						jsonPath("content[0].info.warranty",
								equalTo("1 years, and bala...1")))
				.andExpect(
						jsonPath("content[0].extraCharges[0].id", equalTo(1)))
				.andExpect(
						jsonPath("content[0].extraCharges[0].itemName",
								equalTo("q11 yyhhj")))
				.andExpect(
						jsonPath("content[0].extraCharges[0].price",
								equalTo(24.0)))
				.andExpect(
						jsonPath("content[0].extraCharges[0].tax", equalTo(5.0)))
				.andExpect(jsonPath("content[0].shipping.id", equalTo(1)))
				.andExpect(jsonPath("content[0].shipping.info", equalTo("aaa")))
				.andExpect(jsonPath("content[0].shipping.fare", equalTo(11.0)))
				.andExpect(jsonPath("content[0].shipping.tax", equalTo(5.0)))
				.andExpect(jsonPath("content[0].status", equalTo("CONFIRMED")));

		// update first order
		String requestBody3 = "{"
				+ "\"productItems\" : [ {"
				+ "  \"quantity\" : 5,"
				+ "  \"unit\" : \"PCS\","
				+ "  \"unitPrice\" : 50.00,"
				+ "   \"note1\" : \"note1\","
				+ "   \"note2\" : \"note2\","
				+ "   \"note3\" : \"note3\","
				+ "   \"currency\" : {\"id\" : \"USD\"},"
				+ "   \"product\" : {\"id\" : \"0000-0001\"  }"
				+ " } ],"
				+ "  \"info\" : {"
				+ "    				\"createDate\" : \"2016-04-25\","
				+ "   			    \"contact\" : \"ass\","
				+ "  			    \"shippingDate\" : \"2016-05-01\","
				+ "         	    \"sales\" : { \"id\" : \"EM001\"},"
				+ "   			    \"dataEntryClerk\" : { \"id\" : \"EM001\"},"
				+ "  			    \"customer\" : {\"id\" : \"AU00000001\"},"
				+ " 			    \"corporation\" : \"sddf\","
				+ "   			    \"warranty\" : \"1 years, and bala...1\","
				+ "   			    \"currency\" : {\"id\" : \"USD\"},"
				+ "   			    \"tax\" : \"123\","
				+ "   			    \"discount\" : 100.00  "
				+ "				},"
				+ "  \"extraCharges\" : [ {" 
				+ "    						\"itemName\" : \"q11 yyhhj\","
				+ "    						\"price\" : 24.00," 
				+ "   						\"tax\" : 5.00" 
				+ "                      } ],"
				+ "  \"shipping\" : {" 
				+ "    					\"info\" : \"aaa\"," 
				+ "     				\"fare\" : 11.00,"
				+ "     				\"tax\" : 5.00" 
				+ "  			     }" 
				+ "}";
		System.out.println(requestBody3);
		this.mvc.perform(
				put("/orders/" + id2).contentType(
						MediaType.APPLICATION_JSON_VALUE)//
						.content(requestBody3))//
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("id", equalTo(id2)))
				.andExpect(jsonPath("productItems[0].quantity", equalTo(5)))
				.andExpect(jsonPath("productItems[0].unit", equalTo("PCS")))
				.andExpect(jsonPath("productItems[0].note1", equalTo("note1")))
				.andExpect(
						jsonPath("productItems[0].currency.id", equalTo("USD")))
				.andExpect(
						jsonPath("productItems[0].product.id",
								equalTo("0000-0001")))
				.andExpect(jsonPath("productItems[0].status",equalTo("NORMAL")))
				.andExpect(jsonPath("productItems[0].soldQty",equalTo(0)))					
				.andExpect(jsonPath("info.custPo.poNo", equalTo("PO882")))
				.andExpect(jsonPath("info.sales.id", equalTo("EM001")))
				.andExpect(jsonPath("info.dataEntryClerk.id", equalTo("EM001")))
				.andExpect(jsonPath("info.customer.id", equalTo("AU00000001")))
				.andExpect(
						jsonPath("info.warranty",
								equalTo("1 years, and bala...1")))
				.andExpect(
						jsonPath("extraCharges[0].itemName",
								equalTo("q11 yyhhj")))
				.andExpect(jsonPath("extraCharges[0].price", equalTo(24.0)))
				.andExpect(jsonPath("extraCharges[0].tax", equalTo(5.0)))
				.andExpect(jsonPath("shipping.info", equalTo("aaa")))
				.andExpect(jsonPath("shipping.fare", equalTo(11.0)))
				.andExpect(jsonPath("shipping.tax", equalTo(5.0)))
				.andExpect(jsonPath("status", equalTo("PROCESSING")));
		;

		
		//Get a Order Item List that can be used to create Sales Slip
		this.mvc.perform(get("/salesSlips/orderItemList/search/findByCustIdAndOrdId?custId=AU00000001"))//
		.andDo(print()).andExpect(status().isOk())//
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].orderProductItem.id", equalTo(1)))
		.andExpect(jsonPath("$[1].orderProductItem.id", equalTo(2)))
		;
		
		// create a new sales slip
		String requestBody_create_sales_slip = "{"
				+" \"userId\":\"EM001\", "
				+" \"warehouseId\":\"0049\", "
				+" \"orderItemList\":[ "
				+" 		{\"id\":1}, "
				+"  	{\"id\":2}  "
				+" 	] "
				+" }";
		String fiId1="IN"+dateInString+"0001";
		
		this.mvc.perform( post("/salesSlips/createSalesSlip").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody_create_sales_slip) )
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("salesSlip.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlip.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlip.status", equalTo("PROCESSING")))
			.andExpect(jsonPath("salesSlip.customer.id", equalTo("AU00000001")))
			.andExpect(jsonPath("salesSlip.createUser.id", equalTo("EM001")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.material.id", equalTo("0000-0001")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.ioQty", equalTo(-1.0)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.warehouse.id", equalTo("0049")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.enabled", equalTo(false)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.orderItem.id", equalTo(1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.material.id", equalTo("0000-0001")))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.ioQty", equalTo(-4.0)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.warehouse.id", equalTo("0050")))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.enabled", equalTo(false)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.orderItem.id", equalTo(1)))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.material.id", equalTo("0000-0002")))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.ioQty", equalTo(-9.0)))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.warehouse.id", equalTo("0050")))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.enabled", equalTo(false)))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlipDetails[2].salesSlipDetail.orderItem.id", equalTo(2)))
			;
		
		
	    //update sales slip
		String requestBody_update_sales_slip = "{"
			    +" \"id\" : \"IN201606130012\", "
			    +" \"formDate\" : \"2016-06-13\", "
			    +" \"note\" : \"2232\", "
			    +" \"salesSlipDetails\" : [ "
			    +"   { "
			    +"     \"materialStock\" : { "
			    +"       \"ioQty\" : 5.00, "
			    +"       \"warehouse\" : {\"id\" : \"0050\"} "          
			    +"     }, "
			    +"     \"orderItem\" : {\"id\" : 1} "
			    +"   }, "
			    +"   { "
			    +"     \"materialStock\" : { "
			    +"       \"ioQty\" : 3.00, "
			    +"       \"warehouse\" : {\"id\" : \"0049\"}    "       
			    +"     }, "
			    +"     \"orderItem\" : {\"id\" : 2} "
			    +"   } "
			    +" ] "
				+" }";	
		
		this.mvc.perform( put("/salesSlips/"+fiId1).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody_update_sales_slip) )
			.andDo(MockMvcResultHandlers.print())
			.andExpect(jsonPath("salesSlip.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlip.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlip.status", equalTo("PROCESSING")))
			.andExpect(jsonPath("salesSlip.customer.id", equalTo("AU00000001")))
			.andExpect(jsonPath("salesSlip.createUser.id", equalTo("EM001")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.material.id", equalTo("0000-0001")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.ioQty", equalTo(-5.0)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.warehouse.id", equalTo("0050")))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.enabled", equalTo(false)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.orderItem.id", equalTo(1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.material.id", equalTo("0000-0002")))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.ioQty", equalTo(-3.0)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.warehouse.id", equalTo("0049")))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.enabled", equalTo(false)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
			.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.orderItem.id", equalTo(2)))
			;
		
		// Set confirmed

		String requestBody_set_sales_slip_status = "{\"status\":\"CONFIRMED\"}";

		this.mvc.perform(put("/salesSlips/" + fiId1 + "/setStatus").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(requestBody_set_sales_slip_status))//
				.andDo(print()).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(jsonPath("salesSlip.id", equalTo(fiId1)))
				.andExpect(jsonPath("salesSlip.formNumber.id", equalTo(fiId1)))
				.andExpect(jsonPath("salesSlip.status", equalTo("CONFIRMED")))
				.andExpect(jsonPath("salesSlip.customer.id", equalTo("AU00000001")))
				.andExpect(jsonPath("salesSlip.createUser.id", equalTo("EM001")))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.material.id", equalTo("0000-0001")))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.ioQty", equalTo(-5.0)))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.warehouse.id", equalTo("0050")))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.enabled", equalTo(true)))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
				.andExpect(jsonPath("salesSlipDetails[0].salesSlipDetail.orderItem.id", equalTo(1)))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.material.id", equalTo("0000-0002")))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.batchNumber.code", equalTo(id1)))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.ioQty", equalTo(-3.0)))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.warehouse.id", equalTo("0049")))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.enabled", equalTo(true)))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.materialStock.formNumber.id", equalTo(fiId1)))
				.andExpect(jsonPath("salesSlipDetails[1].salesSlipDetail.orderItem.id", equalTo(2)))
				;
		
		
	}

}
