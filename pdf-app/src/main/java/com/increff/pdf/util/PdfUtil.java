package com.increff.pdf.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.increff.pdf.service.ApiException;

// @Component
public class PdfUtil {

    private String cacheLocation;

    public String generatePdf(String xslFile, String xmlFile) throws ApiException{
        File xsltFile = new File(new File("src/main/resources/com/increff/pdf/"+xslFile+".xsl").getAbsolutePath());

        StreamSource xmlSource = new StreamSource(
                new File(new File(cacheLocation+"/" + xmlFile + ".xml")
                        .getAbsolutePath()));

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, baos);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } catch (Exception e) {
            throw new ApiException("Error generating PDF");
        }
        byte[] pdf = baos.toByteArray();
        String base64 = Base64.getEncoder().encodeToString(pdf);

        return base64;
    }

   
}
