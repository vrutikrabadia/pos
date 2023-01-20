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
        

        // return "";

        // try {

        //     File file = new File(new File("src/main/resources/com/increff/pos/invoice"+orderId.toString()+".pdf").getAbsolutePath());

        //     InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        //     String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

        //     if (mimeType == null) {
        //         mimeType = "application/octet-stream";
        //     }

        //     response.setContentType(mimeType);
        //     response.setContentLength((int) file.length());
        //     response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));

        //     FileCopyUtils.copy(inputStream, response.getOutputStream());
        // } catch (Exception e) {
        //     throw new ApiException("Unable to download the file");
        // }


    }

}
