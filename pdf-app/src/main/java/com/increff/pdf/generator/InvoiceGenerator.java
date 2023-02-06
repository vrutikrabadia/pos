package com.increff.pdf.generator;

import com.increff.pdf.model.data.InvoiceData;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

// @Component
public class InvoiceGenerator {
    
    private XmlUtils xmlUtils;
    private PdfUtil pdfUtil;

    public InvoiceGenerator(String cacheLocation) {
        this.xmlUtils = new XmlUtils(cacheLocation);
        this.pdfUtil = new PdfUtil(cacheLocation);
    }

    public String generateInvoice(InvoiceData data) throws ApiException{
        
        try {
            xmlUtils.generateInvoiceXml(data);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generateInvoicePdf(data.getId());


    }

}
