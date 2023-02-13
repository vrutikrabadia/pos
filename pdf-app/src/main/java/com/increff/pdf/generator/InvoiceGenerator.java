package com.increff.pdf.generator;

import com.increff.pdf.model.data.InvoiceData;
import com.increff.pdf.util.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

// @Component
public class InvoiceGenerator {
    
    public static String generateInvoice(InvoiceData data) throws ApiException{
        String encodedXML = "";
        try {
            encodedXML = XmlUtils.generateInvoiceXml(data);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return PdfUtil.generatePdf("invoice", encodedXML);


    }

}
