package com.increff.pdf.dto;

import org.springframework.stereotype.Component;

import com.increff.pdf.model.form.InvoiceForm;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

@Component
public class InvoiceDto {
    
    public String generateInvoice(InvoiceForm form) throws ApiException{
        
        try {
            XmlUtils.generateInvoiceXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return PdfUtil.generateInvoicePdf(form.getId());


    }

}
