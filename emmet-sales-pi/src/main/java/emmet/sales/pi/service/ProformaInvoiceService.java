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
import emmet.sales.entity.pi.ProformaInvoiceProductItem;
import emmet.sales.entity.pi.ProformaInvoiceVersion;
import emmet.sales.pi.exception.DataNotFoundException;
import emmet.sales.pi.exception.OperationNotPermitException;
import emmet.sales.pi.model.ProformaInvoiceModel;
import emmet.sales.pi.model.SetStatusModel;
import emmet.sales.pi.repository.ProformaInvoiceRepsitory;
import emmet.sales.pi.repository.ProformaInvoiceVersionRepsitory;

@Service
public class ProformaInvoiceService {



	@Autowired
	ProformaInvoiceRepsitory proformaInvoiceRepository;

	@Autowired
	ProformaInvoiceVersionRepsitory proformaInvoiceVersionRepsitory;

	@Transactional
	public ProformaInvoice createProformaInvoice(ProformaInvoiceVersion thisVersion) {

		ProformaInvoice newProformaInvoice = new ProformaInvoice();
		thisVersion.setStatus(ProformainvoiceStatus.PROCESSING.getName());
		newProformaInvoice = proformaInvoiceRepository.save(newProformaInvoice);
		persistNewVersion(thisVersion, newProformaInvoice);
		return thisVersion.getProformaInvoice();

	}

	@Transactional
	public ProformaInvoice updateProformaInvoice(ProformaInvoiceVersion thisVersion, String id)
			throws OperationNotPermitException, DataNotFoundException {

		ProformaInvoice proformaInvoice = proformaInvoiceRepository.findOne(id);
		if (proformaInvoice == null) {
			throw new DataNotFoundException("Can not find the proforma invoice, ID:" + id);
		}

		thisVersion.setProformaInvoice(proformaInvoice);

		if (! ProformainvoiceStatus.PROCESSING.getName().equals(thisVersion.getStatus())) {
			throw new OperationNotPermitException("This proforma invoice had comfirmed, you can not modify it.");
		}

		persistNewVersion(thisVersion, proformaInvoice);
		return thisVersion.getProformaInvoice();
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
			if(proformaInvoiceVersionRepsitory.findVersionOrderCount(pi.getId())>0){
				pim.setHasOrder(true);
			}else{
				pim.setHasOrder(false);
			}
						
			resultList.add(pim);
		}	
		
		Page<ProformaInvoiceModel> resultPage = new PageImpl<ProformaInvoiceModel>(resultList, page,
				piList.getTotalElements());
		
		return resultPage;
	}

}
