package emmet.sales.pi.service;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import emmet.sales.entity.pi.ProformaInvoice;
import emmet.sales.entity.pi.ProformaInvoice.ProformainvoiceStatus;
import emmet.sales.entity.pi.ProformaInvoiceExtraCharge;
import emmet.sales.entity.pi.ProformaInvoiceInfo;
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceShipping;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.exception.DataNotFoundException;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.model.ProformaInvoiceModel;
import emmet.sales.pi.model.SetStatusModel;
import emmet.sales.pi.repository.ProformaInvoiceExtraChargeRepository;
import emmet.sales.pi.repository.ProformaInvoiceProductItemRepository;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.repository.ProformaInvoiceVersionRepsitory;

@Service
public class ProformaInvoiceService {



	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepository;

	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;
	
	@Autowired
	ProformaInvoiceExtraChargeRepository proformaInvoiceExtraChargeRepository;
	
	@Autowired
	ProformaInvoiceProductItemRepository proformaInvoiceProductItemRepository;
	
	@Transactional
	public ProformaInvoiceVersion createProformaInvoice(ProformaInvoiceVersion thisVersion) {

		ProformaInvoice newProformaInvoice = new ProformaInvoice();
		thisVersion.setStatus(ProformainvoiceStatus.PROCESSING.getName());
		newProformaInvoice = proformaInvoiceRepository.save(newProformaInvoice);
		persistNewVersion(thisVersion, newProformaInvoice);
		return thisVersion;

	}

	@Transactional
	public ProformaInvoiceVersion generateNewProformaInvoiceVersion(String id)
			throws OperationNotPermitException, DataNotFoundException {

		ProformaInvoiceVersion thisVersion = proformaInvoiceVersionRepsitory.findOne(id);
			
		if (thisVersion == null) {
			throw new DataNotFoundException("Can not find the proforma invoice version, ID:" + id);
		}

		if (checkPIhasOrder(thisVersion.getProformaInvoice())) {
			throw new OperationNotPermitException("This proforma invoice had order, you can not copy .");
		}

		ProformaInvoiceVersion newVersion= new ProformaInvoiceVersion();				
		
		ProformaInvoiceShipping newShipping = new ProformaInvoiceShipping();
		newShipping.setFare(thisVersion.getShipping().getFare());
		newShipping.setInfo(thisVersion.getShipping().getInfo());
		newShipping.setTax(thisVersion.getShipping().getTax());		
		newVersion.setShipping(newShipping);
		
		ProformaInvoiceInfo newInfo = new ProformaInvoiceInfo();
		newInfo.setContact(thisVersion.getInfo().getContact());
		newInfo.setCorporation(thisVersion.getInfo().getCorporation());
		newInfo.setCurrency(thisVersion.getInfo().getCurrency());
		newInfo.setCustomer(thisVersion.getInfo().getCustomer());
		newInfo.setCustomerDocumentId(thisVersion.getInfo().getCustomerDocumentId());
		newInfo.setDataEntryClerk(thisVersion.getInfo().getDataEntryClerk());
		newInfo.setDiscount(thisVersion.getInfo().getDiscount());
		newInfo.setProformaInvoiceDate(thisVersion.getInfo().getProformaInvoiceDate());
		newInfo.setSales(thisVersion.getInfo().getSales());
		newInfo.setShippingDate(thisVersion.getInfo().getShippingDate());
		newInfo.setTax(thisVersion.getInfo().getTax());
		newInfo.setWarranty(thisVersion.getInfo().getWarranty());

		newVersion.setInfo(newInfo);
		
		List<ProformaInvoiceProductItem> newProductItems =new ArrayList<ProformaInvoiceProductItem>();

		if(thisVersion.getProductItems()!=null||!thisVersion.getProductItems().isEmpty()){
			for(ProformaInvoiceProductItem productItem:thisVersion.getProductItems()){
				
				ProformaInvoiceProductItem newProduct =new ProformaInvoiceProductItem();
				newProduct.setCurrency(productItem.getCurrency());
				newProduct.setNote1(productItem.getNote1());
				newProduct.setNote2(productItem.getNote2());
				newProduct.setNote3(productItem.getNote3());
				newProduct.setProduct(productItem.getProduct());
				newProduct.setQuantity(productItem.getQuantity());
				newProduct.setUnit(productItem.getUnit());
				newProduct.setUnitPrice(productItem.getUnitPrice());

								
				newProductItems.add(newProduct);
			}
		}		
		newVersion.setProductItems(newProductItems);
		
		List<ProformaInvoiceExtraCharge> newExtraCharges =new ArrayList<ProformaInvoiceExtraCharge>();
		
		if(thisVersion.getExtraCharges()!=null||!thisVersion.getExtraCharges().isEmpty()){
			for(ProformaInvoiceExtraCharge extraCharge:thisVersion.getExtraCharges()){
				
				ProformaInvoiceExtraCharge newExtraCharge=new ProformaInvoiceExtraCharge();
				newExtraCharge.setItemName(extraCharge.getItemName());
				newExtraCharge.setPrice(extraCharge.getPrice());
				newExtraCharge.setTax(extraCharge.getTax());

				newExtraCharges.add(newExtraCharge);
			}
		}				
		newVersion.setExtraCharges(newExtraCharges);

								
		persistNewVersion(newVersion, thisVersion.getProformaInvoice());
		return thisVersion;
	}

	private ProformaInvoiceVersion persistNewVersion(ProformaInvoiceVersion thisVersion,
			ProformaInvoice proformaInvoice) {

		thisVersion.setCreateDateTime(Calendar.getInstance());
		thisVersion.setProformaInvoice(proformaInvoice);
		thisVersion.setVersionSequence(getNextVersionSequence(proformaInvoice.getId()));
		thisVersion.setId(getNewVersionId(proformaInvoice.getId()));

		
		thisVersion = linkAssociationEntities(thisVersion);

		thisVersion = proformaInvoiceVersionRepsitory.save(thisVersion);
		saveSnapshoot(thisVersion);



		proformaInvoiceRepository.save(proformaInvoice);

		return thisVersion;
	}

	private String getNewVersionId(String proformaInvoiceId) {

		return proformaInvoiceId + "-" + (getNextVersionSequence(proformaInvoiceId));

	}

	
	@Transactional
	public ProformaInvoice updateProformaInvoice(ProformaInvoiceVersion thisVersion, String pivId)
			throws OperationNotPermitException, DataNotFoundException {

		ProformaInvoiceVersion piv = proformaInvoiceVersionRepsitory.findOne(pivId);
		if (piv == null) {
			throw new DataNotFoundException("Can not find the proforma invoice version, ID:" + pivId);
		}

		

		if (! ProformainvoiceStatus.PROCESSING.getName().equals(piv.getStatus())) {
			throw new OperationNotPermitException("This proforma invoice is read only, you can not modify it.");
		}

		persistVersion(thisVersion, piv);
		return thisVersion.getProformaInvoice();
	}
	
	private ProformaInvoiceVersion persistVersion(ProformaInvoiceVersion thisVersion,
			ProformaInvoiceVersion dbVersion) {


		ProformaInvoiceInfo info = dbVersion.getInfo();
		ProformaInvoiceInfo thisInfo = thisVersion.getInfo();
		info.setContact(thisInfo.getContact());
		info.setCorporation(thisInfo.getCorporation());
		info.setCurrency(thisInfo.getCurrency());
		info.setCustomer(thisInfo.getCustomer());
		info.setCustomerDocumentId(thisInfo.getCustomerDocumentId());
		info.setDataEntryClerk(thisInfo.getDataEntryClerk());
		info.setDiscount(thisInfo.getDiscount());
		info.setProformaInvoiceDate(thisInfo.getProformaInvoiceDate());
		info.setSales(thisInfo.getSales());
		info.setShippingDate(thisInfo.getShippingDate());
		info.setTax(thisInfo.getTax());
		info.setWarranty(thisInfo.getWarranty());


		
		
		ProformaInvoiceShipping shipping = dbVersion.getShipping();
		ProformaInvoiceShipping thisShipping = thisVersion.getShipping();
		shipping.setFare(thisShipping.getFare());
		shipping.setInfo(thisShipping.getInfo());
		shipping.setTax(thisShipping.getTax());
		
		proformaInvoiceProductItemRepository.delete(dbVersion.getProductItems());
		dbVersion.setProductItems(thisVersion.getProductItems());
		if(dbVersion.getProductItems()!=null||!dbVersion.getProductItems().isEmpty()){
			for(ProformaInvoiceProductItem productItem:dbVersion.getProductItems()){
				productItem.setVersion(dbVersion);
			}
		}
		

		proformaInvoiceExtraChargeRepository.delete(dbVersion.getExtraCharges());
		dbVersion.setExtraCharges(thisVersion.getExtraCharges());
		if(dbVersion.getExtraCharges()!=null||!dbVersion.getExtraCharges().isEmpty()){
			for(ProformaInvoiceExtraCharge extraCharge:dbVersion.getExtraCharges()){
				extraCharge.setVersion(dbVersion);
			}
		}
		
		thisVersion = proformaInvoiceVersionRepsitory.save(thisVersion);
		saveSnapshoot(thisVersion);


		return thisVersion;
	}
	
	private void saveSnapshoot(ProformaInvoiceVersion piVersion) {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, piVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		piVersion.setSnapshot(writer.toString());

		proformaInvoiceVersionRepsitory.save(piVersion);

	}

	private Integer getNextVersionSequence(String proformaInvoiceId) {
		if (proformaInvoiceVersionRepsitory.findByProformaInvoiceId(proformaInvoiceId).isEmpty()) {
			return 1;
		}

		return proformaInvoiceVersionRepsitory.findLastVersionSequenceOfProformainvoice(proformaInvoiceId) + 1;
	}

	private ProformaInvoiceVersion linkAssociationEntities(ProformaInvoiceVersion invoiceVersionEntity) {

		// Invoice Info
		if (invoiceVersionEntity.getInfo() != null) {
			invoiceVersionEntity.getInfo().setVersion(invoiceVersionEntity);
			invoiceVersionEntity.getInfo().setCreateDate(new Date(System.currentTimeMillis()));
		}

		// Extra Charge
		List<ProformaInvoiceExtraCharge> extraChagres = invoiceVersionEntity.getExtraCharges();
		if (extraChagres != null && !extraChagres.isEmpty()) {

			for (ProformaInvoiceExtraCharge extraCharge : extraChagres) {
				extraCharge.setVersion(invoiceVersionEntity);
			}
		}

		// ProductItems
		List<ProformaInvoiceProductItem> productItems = invoiceVersionEntity.getProductItems();
		if (productItems != null && !productItems.isEmpty()) {

			for (ProformaInvoiceProductItem productItem : productItems) {
				productItem.setVersion(invoiceVersionEntity);
			}
		}

		// Shipping
		if (invoiceVersionEntity.getShipping() != null) {
			invoiceVersionEntity.getShipping().setVersion(invoiceVersionEntity);
		}

		return invoiceVersionEntity;

	}

	public ProformaInvoiceVersion findProformaInvoiceWithVersion(String proformaInvoiceId, String versionId) throws DataNotFoundException {

		ProformaInvoiceVersion version = proformaInvoiceVersionRepsitory.findByProformaInvoiceIdAndId(proformaInvoiceId, versionId);
		if(version == null){
			throw new DataNotFoundException("The version is not existed.");
		}
		
		return version;

	}
	
	@Transactional
	public ProformaInvoiceVersion setVersionStatus(String versionId,SetStatusModel model) throws DataNotFoundException{
		
		ProformaInvoiceVersion piVersion = proformaInvoiceVersionRepsitory.findOne(versionId);
		
		if(piVersion == null){
			throw new DataNotFoundException("can not find pi version by id");
		}
		
		//check if status is legal 
		boolean isSatusLegal = false;
		
		for(ProformainvoiceStatus pvStatus:ProformainvoiceStatus.values()){
			if(pvStatus.getName().equals(model.getStatus())){
				isSatusLegal = true;
				break;
			}
		}
			
		if(!isSatusLegal){
			throw new DataNotFoundException("status is error");
		} 
		
		piVersion.setStatus(model.getStatus());		
		proformaInvoiceVersionRepsitory.save(piVersion);
		return piVersion;
	}
	
	
	public Page<ProformaInvoiceModel> getPiList(String piId,String salesId,Pageable page){
		
		Page<ProformaInvoice> piList= proformaInvoiceRepository.findBySales(piId, salesId, page);
		
		List<ProformaInvoiceModel> resultList = new ArrayList<ProformaInvoiceModel>();

		for(ProformaInvoice pi:piList){
			ProformaInvoiceModel pim = new ProformaInvoiceModel();
			pim.setProformaInvoice(pi);
			pim.setFinalVersion(proformaInvoiceVersionRepsitory.findFirstByProformaInvoiceIdOrderByIdDesc(pi.getId()));

			pim.setHasOrder(checkPIhasOrder(pi));	
						
			resultList.add(pim);
		}	
		
		Page<ProformaInvoiceModel> resultPage = new PageImpl<ProformaInvoiceModel>(resultList, page,
				piList.getTotalElements());
		
		return resultPage;
	}

	private boolean checkPIhasOrder(ProformaInvoice pi){
		boolean result = false;
		
		if(proformaInvoiceVersionRepsitory.findVersionOrderCount(pi.getId())>0){
			result = true;
		}
		
		return result;
	} 
	
}
