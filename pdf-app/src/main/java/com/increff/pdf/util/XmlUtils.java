package com.increff.pdf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.pdf.model.data.BrandReportData;
import com.increff.pdf.model.data.InventoryReportData;
import com.increff.pdf.model.data.InvoiceData;
import com.increff.pdf.model.data.InvoiceItemsData;
import com.increff.pdf.model.data.SalesReportData;

// @Component
public class XmlUtils {

    
    private String cacheLocation;

    public XmlUtils(String cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    

    public void generateInvoiceXml(InvoiceData invoiceData) throws Exception {
        
        double subTotal = 0.0;

        for(InvoiceItemsData item: invoiceData.getItemsList()){
            subTotal += item.getQuantity()*item.getSellingPrice();
        }


        double total = subTotal;

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("GDK");
        doc.appendChild(rootElement);

        // GDK Row
        Element gdkRow = doc.createElement("GDK_ROW");
        rootElement.appendChild(gdkRow);
        gdkRow.setAttribute("RowNumber", "1");

        // client city
        Element client_city = doc.createElement("Client_City");
        client_city.setTextContent("client city");
        gdkRow.appendChild(client_city);

        // client name
        Element client_name = doc.createElement("Client_Name");
        client_name.setTextContent("client name");
        gdkRow.appendChild(client_name);

        // client street address
        Element client_street_address = doc.createElement("Client_Street_Address");
        client_street_address.setTextContent("client street address");
        gdkRow.appendChild(client_street_address);

        // client zip code
        Element client_zip_code = doc.createElement("Client_Zip_Code");
        client_zip_code.setTextContent("client zip code");
        gdkRow.appendChild(client_zip_code);

        // company city
        Element company_city = doc.createElement("Company_City");
        company_city.setTextContent("Bengaluru");
        gdkRow.appendChild(company_city);

        // company email
        Element company_email = doc.createElement("Company_Email");
        company_email.setTextContent("support@increff.com");
        gdkRow.appendChild(company_email);

        // company name
        Element company_name = doc.createElement("Company_Name");
        company_name.setTextContent("Increff");
        gdkRow.appendChild(company_name);

        // company phone
        Element company_phone = doc.createElement("Company_Phone");
        company_phone.setTextContent("080-12345678");
        gdkRow.appendChild(company_phone);

        // company street address
        Element company_street_address = doc.createElement("Company_Street_Address");
        company_street_address.setTextContent("HSr Layout Sector 6");
        gdkRow.appendChild(company_street_address);

        // company zip code
        Element company_zip_code = doc.createElement("Company_Zip_Code");
        company_zip_code.setTextContent("560102");
        gdkRow.appendChild(company_zip_code);

        // invoice date
        Element invoice_date = doc.createElement("Invoice_date");
        invoice_date.setTextContent(invoiceData.getUpdated().format(new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy HH:mm:ss").toFormatter()) + " UTC");
        gdkRow.appendChild(invoice_date);

        // invoice number
        Element invoice_number = doc.createElement("Invoice_number");
        invoice_number.setTextContent(invoiceData.getId().toString());
        gdkRow.appendChild(invoice_number);


        // sub total
        Element sub_total = doc.createElement("SUBTOTAL");
        sub_total.setTextContent(String.format("%.2f",subTotal));
        gdkRow.appendChild(sub_total);

        // total
        Element total_amount = doc.createElement("TOTAL");
        total_amount.setTextContent(String.format("%.2f",total));
        gdkRow.appendChild(total_amount);

        Integer recordCount = 1;

        for (InvoiceItemsData item: invoiceData.getItemsList()) {
            // RECORDSET
            Element recordset = doc.createElement("RECORDSET");
            recordset.setAttribute("RecordsetNumber", recordCount.toString());
            gdkRow.appendChild(recordset);

            //SNO
            Element sno = doc.createElement("SNO");
            sno.setTextContent(recordCount.toString());
            recordset.appendChild(sno);

            //NAME
            Element name = doc.createElement("NAME");
            name.setTextContent(item.getName());
            recordset.appendChild(name);

            //BARCODE 
            Element barcode = doc.createElement("BARCODE");
            barcode.setTextContent(item.getBarcode());
            recordset.appendChild(barcode);
            
            //QUANTITY
            Element quantity = doc.createElement("QUANTITY");
            quantity.setTextContent(item.getQuantity().toString());
            recordset.appendChild(quantity);

            //UNIT_PRICE
            Element rate = doc.createElement("UNIT_PRICE");
            rate.setTextContent(String.format("%.2f",item.getSellingPrice()));
            recordset.appendChild(rate);

            double itemTotal = item.getQuantity() * item.getSellingPrice();

            //AMOUNT
            Element amount = doc.createElement("TOTAL");
            amount.setTextContent(String.format("%.2f",itemTotal));
            recordset.appendChild(amount);

            recordCount++;
        }


        try (FileOutputStream output = new FileOutputStream(new File(cacheLocation+"/invoice"+invoiceData.getId().toString()+".xml").getAbsolutePath())) {
            writeXml(doc, output);
        } catch (IOException e) {
            
        }
    }


    public  void generateBrandReportXml(List<BrandReportData> brands) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("GDK");
        doc.appendChild(rootElement);

        // GDK Row
        Element gdkRow = doc.createElement("GDK_ROW");
        rootElement.appendChild(gdkRow);
        gdkRow.setAttribute("RowNumber", "1");


        // company city
        Element company_city = doc.createElement("Company_City");
        company_city.setTextContent("Bengaluru");
        gdkRow.appendChild(company_city);

        // company email
        Element company_email = doc.createElement("Company_Email");
        company_email.setTextContent("support@increff.com");
        gdkRow.appendChild(company_email);

        // company name
        Element company_name = doc.createElement("Company_Name");
        company_name.setTextContent("Increff");
        gdkRow.appendChild(company_name);

        // company phone
        Element company_phone = doc.createElement("Company_Phone");
        company_phone.setTextContent("080-12345678");
        gdkRow.appendChild(company_phone);

        // company street address
        Element company_street_address = doc.createElement("Company_Street_Address");
        company_street_address.setTextContent("HSr Layout Sector 6");
        gdkRow.appendChild(company_street_address);

        // company zip code
        Element company_zip_code = doc.createElement("Company_Zip_Code");
        company_zip_code.setTextContent("560102");
        gdkRow.appendChild(company_zip_code);

        // report date
        Element report_date = doc.createElement("Report_date");
        report_date.setTextContent(ZonedDateTime.now().format(new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy HH:mm:ss z").toFormatter()));
        gdkRow.appendChild(report_date);
        
        //Report name
        Element report_name = doc.createElement("Report_name");
        report_name.setTextContent("Brand Report");
        gdkRow.appendChild(report_name);

        Integer recordCount = 1;

        for (BrandReportData brand: brands) {
            // RECORDSET
            Element recordset = doc.createElement("RECORDSET");
            recordset.setAttribute("RecordsetNumber", recordCount.toString());
            gdkRow.appendChild(recordset);

            //BRAND
            Element brand_name = doc.createElement("BRAND");
            brand_name.setTextContent(brand.getBrand());
            recordset.appendChild(brand_name);

            //CATEGORY
            Element category = doc.createElement("CATEGORY");
            category.setTextContent(brand.getCategory());
            recordset.appendChild(category);

            recordCount++;
        }

        try (FileOutputStream output = new FileOutputStream(new File(cacheLocation+"/brandReport.xml").getAbsolutePath())) {
            writeXml(doc, output);
        } catch (IOException e) {
            
        }
    }


    public  void generateInventoryReportXml(List<InventoryReportData> inventory) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("GDK");
        doc.appendChild(rootElement);

        // GDK Row
        Element gdkRow = doc.createElement("GDK_ROW");
        rootElement.appendChild(gdkRow);
        gdkRow.setAttribute("RowNumber", "1");


        // company city
        Element company_city = doc.createElement("Company_City");
        company_city.setTextContent("Bengaluru");
        gdkRow.appendChild(company_city);

        // company email
        Element company_email = doc.createElement("Company_Email");
        company_email.setTextContent("support@increff.com");
        gdkRow.appendChild(company_email);

        // company name
        Element company_name = doc.createElement("Company_Name");
        company_name.setTextContent("Increff");
        gdkRow.appendChild(company_name);

        // company phone
        Element company_phone = doc.createElement("Company_Phone");
        company_phone.setTextContent("080-12345678");
        gdkRow.appendChild(company_phone);

        // company street address
        Element company_street_address = doc.createElement("Company_Street_Address");
        company_street_address.setTextContent("HSr Layout Sector 6");
        gdkRow.appendChild(company_street_address);

        // company zip code
        Element company_zip_code = doc.createElement("Company_Zip_Code");
        company_zip_code.setTextContent("560102");
        gdkRow.appendChild(company_zip_code);

        // report date
        Element report_date = doc.createElement("Report_date");
        report_date.setTextContent(ZonedDateTime.now().format(new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy HH:mm:ss z").toFormatter()));
        gdkRow.appendChild(report_date);
        
        //Report name
        Element report_name = doc.createElement("Report_name");
        report_name.setTextContent("Inventory Report");
        gdkRow.appendChild(report_name);

        Integer recordCount = 1;

        for (InventoryReportData inv: inventory) {
            // RECORDSET
            Element recordset = doc.createElement("RECORDSET");
            recordset.setAttribute("RecordsetNumber", recordCount.toString());
            gdkRow.appendChild(recordset);

            //BRAND
            Element brand_name = doc.createElement("BRAND");
            brand_name.setTextContent(inv.getBrand());
            recordset.appendChild(brand_name);

            //CATEGORY
            Element category = doc.createElement("CATEGORY");
            category.setTextContent(inv.getCategory());
            recordset.appendChild(category);

            //QUANTITY
            Element quantity = doc.createElement("QUANTITY");
            quantity.setTextContent(inv.getQuantity().toString());
            recordset.appendChild(quantity);

            recordCount++;
        }

        try (FileOutputStream output = new FileOutputStream(new File(cacheLocation+"/inventoryReport.xml").getAbsolutePath())) {
            writeXml(doc, output);
        } catch (IOException e) {
            
        }
    }

    public  void generateSalesReportXml(List<SalesReportData> sales) throws Exception{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("GDK");
        doc.appendChild(rootElement);

        // GDK Row
        Element gdkRow = doc.createElement("GDK_ROW");
        rootElement.appendChild(gdkRow);
        gdkRow.setAttribute("RowNumber", "1");


        // company city
        Element company_city = doc.createElement("Company_City");
        company_city.setTextContent("Bengaluru");
        gdkRow.appendChild(company_city);

        // company email
        Element company_email = doc.createElement("Company_Email");
        company_email.setTextContent("support@increff.com");
        gdkRow.appendChild(company_email);

        // company name
        Element company_name = doc.createElement("Company_Name");
        company_name.setTextContent("Increff");
        gdkRow.appendChild(company_name);

        // company phone
        Element company_phone = doc.createElement("Company_Phone");
        company_phone.setTextContent("080-12345678");
        gdkRow.appendChild(company_phone);

        // company street address
        Element company_street_address = doc.createElement("Company_Street_Address");
        company_street_address.setTextContent("HSr Layout Sector 6");
        gdkRow.appendChild(company_street_address);

        // company zip code
        Element company_zip_code = doc.createElement("Company_Zip_Code");
        company_zip_code.setTextContent("560102");
        gdkRow.appendChild(company_zip_code);

        // report date
        Element report_date = doc.createElement("Report_date");
        report_date.setTextContent(ZonedDateTime.now().format(new DateTimeFormatterBuilder().appendPattern("dd-MM-yyyy HH:mm:ss z").toFormatter()));
        gdkRow.appendChild(report_date);
        
        //Report name
        Element report_name = doc.createElement("Report_name");
        report_name.setTextContent("Sales Report");
        gdkRow.appendChild(report_name);

        Integer recordCount = 1;

        for (SalesReportData sale: sales) {
            // RECORDSET
            Element recordset = doc.createElement("RECORDSET");
            recordset.setAttribute("RecordsetNumber", recordCount.toString());
            gdkRow.appendChild(recordset);

            //BRAND
            Element brand_name = doc.createElement("BRAND");
            brand_name.setTextContent(sale.getBrand());
            recordset.appendChild(brand_name);

            //CATEGORY
            Element category = doc.createElement("CATEGORY");
            category.setTextContent(sale.getCategory());
            recordset.appendChild(category);

            //QUANTITY
            Element quantity = doc.createElement("QUANTITY");
            quantity.setTextContent(sale.getQuantity().toString());
            recordset.appendChild(quantity);

            //REVENUE
            Element revenue = doc.createElement("REVENUE");
            revenue.setTextContent(String.format("%.2f",sale.getRevenue()));
            recordset.appendChild(revenue);

            recordCount++;
        }

        try (FileOutputStream output = new FileOutputStream(new File(cacheLocation+"/salesReport.xml").getAbsolutePath())) {
            writeXml(doc, output);
        } catch (IOException e) {
            
        }
    }


    private  void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}
