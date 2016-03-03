package emmet.sales.entity.pi.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.pi.SalesProformaInvoiceApplication;
import emmet.sales.pi.domain.ProformaInvoiceModel;
import emmet.sales.pi.service.ProformaInvoiceService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SalesProformaInvoiceApplication.class)
public class ProformaInvoiceServiceTest {

	@Autowired
	ProformaInvoiceService proformaInvoiceService;

	String dateInString;

	@Before
	public void setUp() {
		DateFormat df = new SimpleDateFormat("YYYYMMdd");
		Date today = Calendar.getInstance().getTime();
		dateInString = df.format(today);
	}
	
	@Test
	public void normalInfoTest() {
		ProformaInvoiceModel model = new ProformaInvoiceModel();
		
	}
	

/*	@Test
	public void normalExtraChargeTest() {

		ProformaInvoiceModel model = new ProformaInvoiceModel();
		List<ProformaInvoiceExtraCharge> extraCharges = new ArrayList<ProformaInvoiceExtraCharge>();
		ProformaInvoiceExtraCharge piec = new ProformaInvoiceExtraCharge();
		piec.setItemName("Foo");
		piec.setPrice(BigDecimal.valueOf(100.01));
		piec.setTax(BigDecimal.valueOf(0.01));
		extraCharges.add(piec);

		piec = new ProformaInvoiceExtraCharge();
		piec.setItemName("Bar");
		piec.setPrice(BigDecimal.valueOf(100.02));
		piec.setTax(BigDecimal.valueOf(0.02));
		extraCharges.add(piec);

		model.setExtraCharges(extraCharges);

		ProformaInvoice entity = proformaInvoiceService.saveProformaInvoice(model);
		String id = "PI" + dateInString + "01";
		assertEquals(id, entity.getId());

		assertEquals("Foo", entity.getExtraCharges().get(0).getItemName());
		assertEquals(BigDecimal.valueOf(100.01), entity.getExtraCharges().get(0).getPrice());
		assertEquals(BigDecimal.valueOf(0.01), entity.getExtraCharges().get(0).getTax());

		assertEquals("Bar", entity.getExtraCharges().get(1).getItemName());
		assertEquals(BigDecimal.valueOf(100.02), entity.getExtraCharges().get(1).getPrice());
		assertEquals(BigDecimal.valueOf(0.02), entity.getExtraCharges().get(1).getTax());

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void extraChargeDuplcateTest() {
		ProformaInvoiceModel model = new ProformaInvoiceModel();
		List<ProformaInvoiceExtraCharge> extraCharges = new ArrayList<ProformaInvoiceExtraCharge>();
		ProformaInvoiceExtraCharge piec = new ProformaInvoiceExtraCharge();
		piec.setItemName("Foo");
		extraCharges.add(piec);

		piec = new ProformaInvoiceExtraCharge();
		piec.setItemName("Foo");
		extraCharges.add(piec);

		model.setExtraCharges(extraCharges);

		proformaInvoiceService.saveProformaInvoice(model);

	}*/

}
