package com.legion.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class CsvUtils {

	public static ArrayList<HashMap<String,String>> getDataFromCSVFileWithHeader(String filePathWithName) {
		ArrayList<HashMap< String,String>> responseData = new ArrayList<HashMap< String,String>>();
        try {
        	BufferedReader br = new BufferedReader(new FileReader(filePathWithName));
 		    String line =  null;
  		    String[] headerData = null;
 		    boolean isHeader = true;
 		    while((line=br.readLine())!=null){
 		    	HashMap<String,String> map = new HashMap<String, String>();
 		    	if(isHeader) {
 		    		headerData = line.split(",");
 		    		isHeader = false;
 		    	}
 		    	else {
 			    	String str[] = line.trim().split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)",-1);
 			        for(int i=0;i<str.length;i++) {
 			        	map.put(headerData[i], str[i]);
 			        }
 		    	}
 		    	if(map.size() > 0)
 		    		responseData.add(map);
 		    }
        } catch (JsonGenerationException e) {
            System.err.println("The CSV configuration file is not valid. Please verify the file.");

        } catch (JsonMappingException  e) {
            System.err.println("The CSV configuration file is not valid. Please verify the file.");

        } catch (IOException e) {
            System.err.println("No configuration file available.");
        }
        return responseData;

    }

    public static ArrayList<String> getHeaderFromCSVFileByPath(String filePathWithName) {
		ArrayList<String> header = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePathWithName));
			String line = "";
			while((line = br.readLine()) != null){
				Collections.addAll(header, line.split(","));
				break;
			}
		} catch (JsonGenerationException e) {
			System.err.println("The CSV configuration file is not valid. Please verify the file.");

		} catch (JsonMappingException  e) {
			System.err.println("The CSV configuration file is not valid. Please verify the file.");

		} catch (IOException e) {
			System.err.println("No configuration file available.");
		}
		return header;
	}
}
