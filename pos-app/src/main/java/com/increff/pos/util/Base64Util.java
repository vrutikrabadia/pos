package com.increff.pos.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import com.increff.pos.service.ApiException;

/**
 * Used for conversionn of File to Base64 string and vice versa.
 */
public class Base64Util {
    
    /** 
     * This method is used to read a file saved at the specified path and convert it into base64 string.
     * @param fileName
     * @return String
     * @throws ApiException
     */
    public static String encodeFileToBase64Binary(String fileName)throws ApiException{
        File file = new File(fileName);
        try{
            return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
        } catch(Exception e){
            throw new ApiException("Error encoding file to base64");
        }
    }
    
    
    /** 
     * This method takes two arguments a base64 string and a file path. 
     * It converts base64 string to a file and saves it at the specified location.
     * @param base64
     * @param filePath
     * @throws ApiException
     */
    public static void decodeBase64ToFile(String base64, String filePath) throws ApiException{
        File file = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] decoder = Base64.getDecoder().decode(base64);

            fos.write(decoder);
        } catch (Exception e) {
            
            throw new ApiException("Error decoding base64 to File");
        }
    }
    
}
