package com.increff.pos.util;

import java.io.File;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;



public class PdfUtil {
    
    public static void generateInvoicePdf(Integer orderId) throws Exception {
        File xsltFile = new File(new File("src/main/resources/com/increff/pos/invoice.xsl").getAbsolutePath());

        StreamSource xmlSource = new StreamSource(new File(new File("src/main/resources/com/increff/pos/invoice"+orderId.toString()+".xml").getAbsolutePath()));

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out;

        out = new java.io.FileOutputStream(new File("src/main/resources/com/increff/pos/invoice"+orderId.toString()+".pdf").getAbsolutePath());

        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }
}
