package com.increff.pdf.generator;

import java.util.List;

import com.increff.pdf.model.data.BrandReportData;
import com.increff.pdf.model.data.InventoryReportData;
import com.increff.pdf.model.data.SalesReportData;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

public class ReportGenerator {
    
    private XmlUtils xmlUtils;
    private PdfUtil pdfUtil;

    public ReportGenerator(String cacheLocation) {
        this.xmlUtils = new XmlUtils(cacheLocation);
        this.pdfUtil = new PdfUtil(cacheLocation);
    }

    public String generateBrandReport(List<BrandReportData> form) throws ApiException{
        
        try {
            xmlUtils.generateBrandReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("brandReport", "brandReport");
    }


    public String generateInventoryReport(List<InventoryReportData> form) throws ApiException{
        try {
            xmlUtils.generateInventoryReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("inventoryReport", "inventoryReport");
    }

    public String generateSalesReport(List<SalesReportData> form) throws ApiException{
        try {
            xmlUtils.generateSalesReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return pdfUtil.generatePdf("salesReport", "salesReport");
    }


}
