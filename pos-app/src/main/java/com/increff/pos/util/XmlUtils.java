package com.increff.pos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlUtils {

    public static void generateInvoiceXml(JSONObject invoiceData) throws Exception {
        
        JSONArray itemArray = invoiceData.getJSONArray("items");
        
        double subTotal = 0.0;


        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);
            subTotal += item.getInt("quantity") * item.getDouble("sellingPrice");
        }

        double tax = subTotal * 0.18;
        double total = subTotal + tax;

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
        invoice_date.setTextContent(invoiceData.getString("updated"));
        gdkRow.appendChild(invoice_date);

        // invoice number
        Element invoice_number = doc.createElement("Invoice_number");
        invoice_number.setTextContent(invoiceData.get("id").toString());
        gdkRow.appendChild(invoice_number);

        // invoice tax
        Element invoice_total = doc.createElement("SALES_TAX");
        invoice_total.setTextContent(String.format("%.2f",tax));
        gdkRow.appendChild(invoice_total);

        // sub total
        Element sub_total = doc.createElement("SUBTOTAL");
        sub_total.setTextContent(String.format("%.2f",subTotal));
        gdkRow.appendChild(sub_total);

        // total
        Element total_amount = doc.createElement("TOTAL");
        total_amount.setTextContent(String.format("%.2f",total));
        gdkRow.appendChild(total_amount);

        Integer recordCount = 1;

        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject item = itemArray.getJSONObject(i);
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
            name.setTextContent(item.getString("productName"));
            recordset.appendChild(name);

            //BARCODE 
            Element barcode = doc.createElement("BARCODE");
            barcode.setTextContent(item.getString("barcode"));
            recordset.appendChild(barcode);
            
            //QUANTITY
            Element quantity = doc.createElement("QUANTITY");
            quantity.setTextContent(item.get("quantity").toString());
            recordset.appendChild(quantity);

            //UNIT_PRICE
            Element rate = doc.createElement("UNIT_PRICE");
            rate.setTextContent(item.get("sellingPrice").toString());
            recordset.appendChild(rate);

            double itemTotal = item.getInt("quantity") * item.getDouble("sellingPrice");

            //AMOUNT
            Element amount = doc.createElement("TOTAL");
            amount.setTextContent(String.format("%.2f",itemTotal));
            recordset.appendChild(amount);

            recordCount++;
        }

        try (FileOutputStream output = new FileOutputStream(new File("src/main/resources/com/increff/pos/invoice"+invoiceData.get("id").toString()+".xml").getAbsolutePath())) {
            writeXml(doc, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}