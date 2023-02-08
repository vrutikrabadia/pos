package com.increff.pdf.generator;

import java.util.List;

import com.increff.pdf.model.data.BrandReportData;
import com.increff.pdf.model.data.InventoryReportData;
import com.increff.pdf.model.data.SalesReportData;
import com.increff.pdf.service.ApiException;
import com.increff.pdf.util.PdfUtil;
import com.increff.pdf.util.XmlUtils;

public class ReportGenerator {
    

    public static String generateBrandReport(List<BrandReportData> form) throws ApiException{
        String encodedXML = "";
        try {
            encodedXML = XmlUtils.generateBrandReportXml(form);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("Error generating XML");
        }

        return PdfUtil.generatePdf("brandReport",encodedXML);
    }


    public static String generateInventoryReport(List<InventoryReportData> form) throws ApiException{
        String encodedXML = "";
        try {
            encodedXML = XmlUtils.generateInventoryReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return PdfUtil.generatePdf("inventoryReport", encodedXML);
    }

    public static String generateSalesReport(List<SalesReportData> form) throws ApiException{
        String encodedXML = "";
        try {
            encodedXML = XmlUtils.generateSalesReportXml(form);
        } catch (Exception e) {

            throw new ApiException("Error generating XML");
        }

        return PdfUtil.generatePdf("salesReport", encodedXML);
    }


}
