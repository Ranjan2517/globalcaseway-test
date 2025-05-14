package com.app.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;

public class FillAcroForm {
    private static Map<String, Object> dataMap;

    // Dummy data generator based on field names
 // Assuming clientData is a Map<String, Object>
    public static String generateDummyData(String fieldName, String jsonData) {
        try {
            // Deserialize JSON into a Map
            ObjectMapper objectMapper = new ObjectMapper();
            dataMap = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
            fieldName = fieldName.toLowerCase();
            if(fieldName.contains("cit_0001")) {
            // Check for known field names and return corresponding values
            if (fieldName.contains("certificatedate")) return "";
            if (fieldName.contains("replacement[0].surname[0]")) return getBasicInfoValue("lastName");
            if (fieldName.contains("replacement[0].givenname[0]")) return getBasicInfoValue("firstName");
            if (fieldName.contains("bcsurname[0]")) return getBasicInfoValue("lastName");
            if (fieldName.contains("bcgivenname[0]")) return getBasicInfoValue("firstName");
            if (fieldName.contains("dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
            if (fieldName.contains("pob")) return "";
            if (fieldName.contains("country")) return getBasicInfoValue("citizenship");
            if (fieldName.contains("feet")) return "";
            if (fieldName.contains("clientuci")) return "";
            if (fieldName.contains("inches")) return "";
            if (fieldName.contains("birthcountry")) return "";

            // Parent 1 details
            if (fieldName.contains("parent1[0].information[0].surname[0]")) return getParentInfoValue("fatherInfo", "firstName");
            if (fieldName.contains("parent1[0].information[0].givenname[0]")) return getParentInfoValue("fatherInfo", "lastName");
            if (fieldName.contains("parent1[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));

            // Parent 2 details
            if (fieldName.contains("parent2[0].information[0].surname[0]")) return getParentInfoValue("motherInfo", "firstName");
            if (fieldName.contains("parent2[0].information[0].givenname[0]")) return getParentInfoValue("motherInfo", "lastName");
            if (fieldName.contains("parent2[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));
            }
            
            else if(fieldName.contains("imm_1324")) {
            	 if (fieldName.contains("certificatedate")) return "";
                 if (fieldName.contains("sectiona[0].agreementholder[0]")) return getBasicInfoValue("fir");
                 if (fieldName.contains("sectiona[0].surname[0]")) return getBasicInfoValue("lastName");
                 if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                 if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                 if (fieldName.contains("sectiona[0].agreementid[0]")) return getBasicInfoValue("firs");
                 if (fieldName.contains("sectiona[0].othernames[0]")) return getBasicInfoValue("otherLastName")+getBasicInfoValue("otherFirstName");
                 if (fieldName.contains("sectiona[0].address[0]")) return getAddressInfoValue("streetNo")+getAddressInfoValue("streetName");
                 if (fieldName.contains("sectiona[0].apartment[0]")) return getAddressInfoValue("apt");
                 if (fieldName.contains("sectiona[0].city[0]")) return getAddressInfoValue("city");
                 if (fieldName.contains("sectiona[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                 if (fieldName.contains("sectiona[0].email[0]")) return "";
            }
          
        } catch (Exception e) {
            e.printStackTrace(); // You can log this instead
        }

        return "";
    }

    // Helper methods to extract values from the Map

    private static String getBasicInfoValue(String key) {
        Map<String, Object> basicInfo = (Map<String, Object>) ((Map<String, Object>) dataMap.get("basicInformation")).get("formsdata");
        return basicInfo.containsKey(key) ? basicInfo.get(key).toString() : "";
    }

    private static String getParentInfoValue(String parentKey, String key) {
        Map<String, Object> parentInfo = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) dataMap.get("parentDetails")).get("formsdata")).get(parentKey);
        return parentInfo.containsKey(key) ? parentInfo.get(key).toString() : "";
    }
    
    private static String getAddressInfoValue(String key) {
        try {
            List<Map<String, Object>> addressForms = (List<Map<String, Object>>)
                    ((Map<String, Object>) dataMap.get("addressHistory")).get("formsdata");
            if (addressForms != null && !addressForms.isEmpty()) {
                Map<String, Object> address = addressForms.get(0); // Assuming first entry is desired
                return address.containsKey(key) ? address.get(key).toString() : "";
            }
        } catch (Exception e) {
            e.printStackTrace(); // You may choose to log this instead
        }
        return "";
    }


    private static String formatIsoDate(Object dateObj) {
        if (dateObj != null) {
            try {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
                return isoFormat.format(dateObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }


	
	 public static String formatIsoDate(String isoDate) {
	        if (isoDate == null || isoDate.isEmpty()) return "";
	        try {
	            OffsetDateTime dateTime = OffsetDateTime.parse(isoDate);
	            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	        } catch (Exception e) {
	            return isoDate; // return as-is if parsing fails
	        }
	    }

 
    
	 public static byte[] fillPdfWithDummyData(byte[] inputPdfBytes, String jsonData) throws IOException {
		    try (
		        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputPdfBytes);
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        PdfReader reader = new PdfReader(inputStream);
		        PdfWriter writer = new PdfWriter(outputStream);
		        PdfDocument pdfDoc = new PdfDocument(reader, writer)
		    ) {
		        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
		        if (form == null || form.getFormFields().isEmpty()) {
		            throw new RuntimeException("No AcroForm fields found.");
		        }

		        Map<String, PdfFormField> fields = form.getFormFields();
		        for (Map.Entry<String, PdfFormField> entry : fields.entrySet()) {
		            String fullFieldName = entry.getKey();
		            PdfFormField field = entry.getValue();
		            String value = generateDummyData(fullFieldName, jsonData);
		            field.setValue(value);
		        }

		        pdfDoc.close(); // important: this flushes content to outputStream

		        return outputStream.toByteArray();
		    }
		}

    

}
