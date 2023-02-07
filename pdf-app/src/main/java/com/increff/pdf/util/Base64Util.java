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
            throw new ApiException("Error Encoding file to base64");
        }
    }

    public static void decodeBase64ToFile(String base64, String filePath) throws ApiException{
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file);) {
            byte[] decoder = Base64.getDecoder().decode(base64);

            fos.write(decoder);
        } catch (Exception e) {
            throw new ApiException("Error decoding base64 to file");
        }
    }

}
