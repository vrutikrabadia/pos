package com.increff.pdf.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pdf.model.form.BrandReportForm;
import com.increff.pdf.model.form.InventoryReportForm;
import com.increff.pdf.model.form.SalesReportForm;
import com.increff.pdf.util.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

@Component
public class ReportDto {
    
    @Autowired
    private XmlUtils xmlUtils;

    @Autowired
    private PdfUtil pdfUtil;

    public String generateBrandReport(List<BrandReportForm> form) throws ApiException{
        String encodedXml = "";
        try {
            encodedXml = xmlUtils.generateBrandReportXml(form);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("brandReport", encodedXml);
    }


    public String generateInventoryReport(List<InventoryReportForm> form) throws ApiException{
        String encodedXml = "";
        try {
            encodedXml =xmlUtils.generateInventoryReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("inventoryReport", encodedXml);
    }

    public String generateSalesReport(List<SalesReportForm> form) throws ApiException{
        String encodedXml = "";
        try {
            encodedXml = xmlUtils.generateSalesReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("salesReport", encodedXml);
    }


}
