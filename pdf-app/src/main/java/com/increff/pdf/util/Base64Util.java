package com.increff.pdf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import com.increff.pdf.service.ApiException;

public class Base64Util {
    public static String encodeFileToBase64Binary(String fileName)throws ApiException{
        File file = new File(fileName);
        try{
            return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        } catch(Exception e){
            throw new ApiException("error encoding file to base 64");
        }
    }

    public static void decodeBase64ToFile(String base64, String filePath) throws ApiException{
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file);) {
            byte[] decoder = Base64.getDecoder().decode(base64);

            fos.write(decoder);
            System.out.println("PDF File Saved");
        } catch (Exception e) {
            throw new ApiException("Error decoding base 64 to File");
        }
    }

}
