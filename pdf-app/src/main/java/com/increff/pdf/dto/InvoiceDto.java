package com.increff.pdf.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pdf.model.form.InvoiceForm;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

@Component
public class InvoiceDto {
    
    @Autowired
    private XmlUtils xmlUtils;

    @Autowired
    private PdfUtil pdfUtil;

    public String generateInvoice(InvoiceForm form) throws ApiException{
        
        String encodedXml = "";
        try {
            encodedXml = xmlUtils.generateInvoiceXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("invoice", encodedXml);


    }

}
