package com.app.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class FillAcroForm {
    private static Map<String, Object> dataMap;

    public static String generateDummyData(String fieldName, String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dataMap = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
            return resolveField(fieldName.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String resolveField(String fieldName) {
        try {
            if (fieldName.contains("cit_0001")) {
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

                // Parent 1
                if (fieldName.contains("parent1[0].information[0].surname[0]")) return getParentInfoValue("fatherInfo", "firstName");
                if (fieldName.contains("parent1[0].information[0].givenname[0]")) return getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("parent1[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));

                // Parent 2
                if (fieldName.contains("parent2[0].information[0].surname[0]")) return getParentInfoValue("motherInfo", "firstName");
                if (fieldName.contains("parent2[0].information[0].givenname[0]")) return getParentInfoValue("motherInfo", "lastName");
                if (fieldName.contains("parent2[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));
            } else if (fieldName.contains("imm_1324")) {
                if (fieldName.contains("certificatedate")) return "";
                if (fieldName.contains("sectiona[0].agreementholder[0]")) return getBasicInfoValue("fir");
                if (fieldName.contains("sectiona[0].surname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("sectiona[0].agreementid[0]")) return getBasicInfoValue("firs");
                if (fieldName.contains("sectiona[0].othernames[0]")) return getBasicInfoValue("otherLastName") + " " + getBasicInfoValue("otherFirstName");
                if (fieldName.contains("sectiona[0].address[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("sectiona[0].apartment[0]")) return getAddressInfoValue("apt");
                if (fieldName.contains("sectiona[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("sectiona[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                if (fieldName.contains("sectiona[0].email[0]")) return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    // ================== Helpers ==================

    private static String getBasicInfoValue(String key) {
        try {
            Map<String, Object> basicInfo = (Map<String, Object>) ((Map<String, Object>) dataMap.get("basicInformation")).get("formsdata");
            return basicInfo != null && basicInfo.containsKey(key) ? basicInfo.get(key).toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String getParentInfoValue(String parentKey, String key) {
        try {
            Map<String, Object> parentInfo = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) dataMap.get("parentDetails")).get("formsdata")).get(parentKey);
            return parentInfo != null && parentInfo.containsKey(key) ? parentInfo.get(key).toString() : "";
        } catch (Exception e) {
            return "";
        }
    }

    private static String getAddressInfoValue(String key) {
        try {
            List<Map<String, Object>> addressForms = (List<Map<String, Object>>) ((Map<String, Object>) dataMap.get("addressHistory")).get("formsdata");
            if (addressForms != null && !addressForms.isEmpty()) {
                Map<String, Object> address = addressForms.get(0);
                return address.containsKey(key) ? address.get(key).toString() : "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String formatIsoDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(isoDate);
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return isoDate;
        }
    }

    public static byte[] fillPdfWithDummyData(byte[] inputPdfBytes, String jsonData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        dataMap = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});

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
                String value = resolveField(fullFieldName.toLowerCase());
                field.setValue(value);
            }

            pdfDoc.close();
            return outputStream.toByteArray();
        }
    }
}
