package com.increff.pdf.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pdf.model.form.BrandReportForm;
import com.increff.pdf.model.form.InventoryReportForm;
import com.increff.pdf.model.form.SalesReportForm;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

@Component
public class ReportDto {
    
    @Autowired
    private XmlUtils xmlUtils;

    @Autowired
    private PdfUtil pdfUtil;

    public String generateBrandReport(List<BrandReportForm> form) throws ApiException{
        
        try {
            xmlUtils.generateBrandReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generateBrandReportPdf();
    }


    public String generateInventoryReport(List<InventoryReportForm> form) throws ApiException{
        try {
            xmlUtils.generateInventoryReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generateInventoryReportPdf();
    }

    public String generateSalesReport(List<SalesReportForm> form) throws ApiException{
        try {
            xmlUtils.generateSalesReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generateSalesReportPdf();
    }


}
