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
                if (fieldName.contains("section2[0].clientuci[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("replacement[0].surname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("replacement[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section4[0].bcsurname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section4[0].bcgivenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section4[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("section4[0].pob")) return getBasicInfoValue("birthCountry");;
                if (fieldName.contains("country")) return getBasicInfoValue("citizenship");
                if (fieldName.contains("section4[0].height[0].centimeters[0]")) return getBasicInfoValue("heightCM");;
                if (fieldName.contains("section4[0].othernames[0]")) return getBasicInfoValue("otherLastName") + " " + getBasicInfoValue("otherFirstName");

                // Parent 1
                if (fieldName.contains("parent1[0].information[0].surname[0]")) return getParentInfoValue("fatherInfo", "firstName");
                if (fieldName.contains("parent1[0].information[0].givenname[0]")) return getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("parent1[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));

                // Parent 2
                if (fieldName.contains("parent2[0].information[0].surname[0]")) return getParentInfoValue("motherInfo", "firstName");
                if (fieldName.contains("parent2[0].information[0].givenname[0]")) return getParentInfoValue("motherInfo", "lastName");
                if (fieldName.contains("parent2[0].information[0].birthdate[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));
                
                if (fieldName.contains("section12[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section12[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section12[0].home[0].address[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("section12[0].home[0].appartment[0]")) return getAddressInfoValue("apt");                if (fieldName.contains("section12[0].home[0].postalcode[0]")) return getAddressInfoValue("dob");
                if (fieldName.contains("section12[0].home[0].postalcode[0]")) return getAddressInfoValue("postalcode");;
                if (fieldName.contains("section12[0].home[0].country[0]")) return getBasicInfoValue("citizenship");
                if (fieldName.contains("section12[0].home[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("section12[0].home[0].province[0]")) return getAddressInfoValue("state");


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
            else if (fieldName.contains("cit_0012")) {
                if (fieldName.contains("section1to3[0].textfield1[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section1to3[0].textfield1[1]")) return getBasicInfoValue("firstName");
                
                if (fieldName.contains("section4[0].textfield1[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section4[0].textfield1[1]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section4[0].textfield1[2]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("section4[0].textfield1[3]")) return getAddressInfoValue("apt");
                if (fieldName.contains("section4[0].textfield1[4]")) return getAddressInfoValue("city");
                if (fieldName.contains("section4[0].textfield1[5]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section4[0].textfield1[6]")) return getAddressInfoValue("postalCode");
                if (fieldName.contains("section4[0].textfield1[7]")) return getBasicInfoValue("citizenship");
               
               
    
            }
            else if (fieldName.contains("cit_0301")) {
                if (fieldName.contains("page1[0].header[0].textfield1[1]")) return getBasicInfoValue("uci");;
                if (fieldName.contains("page1[0].questions1to3[0].radiobuttonlist[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("questions1to3[0].textfield1[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("questions1to3[0].textfield1[1]")) return getBasicInfoValue("firstName");

                if (fieldName.contains("page1[0].textfield1[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("page1[0].textfield1[1]")) return getBasicInfoValue("firstName");

                if (fieldName.contains("page1[0].textfield1[2]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("page1[0].textfield1[3]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("page1[0].textfield1[4]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("page1[0].textfield1[5]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("page2[0].currentdate[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("page2[0].textfield1[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("page2[0].textfield1[1]")) return getBasicInfoValue("birthCountry");
   
                if (fieldName.contains("page2[0].textfield3[0]")) return getBasicInfoValue("heightCM");

                if (fieldName.contains("page2[0].textfield6[0]")) return getBasicInfoValue("eyeColor");
                if (fieldName.contains("page2[0].textfield7[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("page2[0].textfield8[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("page2[0].textfield9[0]")) return getAddressInfoValue("postalCode");

                if (fieldName.contains("page4[0].father[0].textfield1[0]")) return getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("page4[0].father[0].textfield1[1]")) return getParentInfoValue("fatherInfo", "firstName");
                if (fieldName.contains("page4[0].father[0].currentdate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "birthCountry"));
                if (fieldName.contains("page4[0].father[0].currentDate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));

                if (fieldName.contains("page4[0].mother[0].textfield1[0]")) return getParentInfoValue("motherInfo", "lastName");
                if (fieldName.contains("page4[0].mother[0].textfield1[1]")) return getParentInfoValue("motherInfo", "firstName");
                if (fieldName.contains("page4[0].mother[0].textfield1[2]")) return getParentInfoValue("motherInfo", "birthCountry");
                if (fieldName.contains("page4[0].mother[0].currentdate[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));

    
            }
            else if (fieldName.contains("cit_0457")) {
                if (fieldName.contains("contentmain[0].textfield1[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("contentmain[0].textfield1[1]")) return getBasicInfoValue("firstName");
                
                if (fieldName.contains("contentmain[0].currentdate[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("contentmain[0].textfield1[3]")) return getAddressInfoValue("city");
                if (fieldName.contains("contentmain[0].textfield1[4]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("contentmain[0].textfield1[5]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("contentmain[0].textfield1[6]")) return getAddressInfoValue("apt");
                if (fieldName.contains("contentmain[0].textfield1[7]")) return getAddressInfoValue("city");
                if (fieldName.contains("contentmain[0].textfield1[9]")) return getAddressInfoValue("postalCode");
               
    
            }
            else if (fieldName.contains("cit_0496")) {
                if (fieldName.contains("header[0].textfield1[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("question1to3[0].textfield1[1]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("question1to3[0].textfield1[2]")) return getBasicInfoValue("firstName");

                if (fieldName.contains("page1[0].currentdate[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("page1[0].textfield1[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("page1[0].textfield1[1]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("page1[0].textfield1[2]")) return getBasicInfoValue("heightCM");
                if (fieldName.contains("page1[0].textfield1[5]")) return getBasicInfoValue("eyeColor");;
               
                if (fieldName.contains("page2[0].textfield1[0]")) return  getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("page2[0].textfield1[1]")) return getAddressInfoValue("city");
                if (fieldName.contains("page2[0].textfield1[2]")) return getAddressInfoValue("postalCode");

                if (fieldName.contains("father[0].textfield1[0]")) return getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("father[0].textfield1[1]")) return getParentInfoValue("fatherInfo", "firstName");
                if (fieldName.contains("father[0].textfield1[3]")) return getParentInfoValue("fatherInfo", "birthCountry");
                if (fieldName.contains("father[0].currentdate[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));

                if (fieldName.contains("mother[0].textfield1[0]")) return getParentInfoValue("motherInfo", "lastName");
                if (fieldName.contains("mother[0].textfield1[1]")) return getParentInfoValue("motherInfo", "firstName");
                if (fieldName.contains("mother[0].textfield1[3]")) return getParentInfoValue("motherInfo", "birthCountry");
                if (fieldName.contains("mother[0].currentdate[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));

    

            }
            else if (fieldName.contains("imm_0130")) {
               
                if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));

                
            }
            else if (fieldName.contains("imm_0156")) {
                
                if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("sectiona[0].uci[0].ucino[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("sectiona[0].citycountrybirth[0]")) return  getAddressInfoValue("city")+  " "+getBasicInfoValue("birthCountry");
                if (fieldName.contains("sectiona[0].citizenship[0]")) return getBasicInfoValue("citizenship");
  
            }
              else if (fieldName.contains("imm_0157")) {
                
                if (fieldName.contains("section2[0].familyname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("section2[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].noccode[0]")) return getEmploymentInfoValue("nocCode");

                if (fieldName.contains("sectiona[0].givenname[0]")) return getEmploymentInfoValue("jobDescription");
                if (fieldName.contains("Section3[0].startdate[0]")) return getEmploymentInfoValue("from");
                if (fieldName.contains("Section3[0].enddate[0]")) return getEmploymentInfoValue("to");
                if (fieldName.contains(".Section3[0].businessaddress[0].city[0]")) return getEmploymentInfoValue("city");
                if (fieldName.contains(".Section3[0].businessaddress[0].province[0]")) return getEmploymentInfoValue("state");
                if (fieldName.contains("sectiona[0].givenname[0]")) return getEmploymentInfoValue("hoursPerWeek");

                if (fieldName.contains("section4[0].familyname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("section4[0].countrybirth[0]")) return getEmploymentInfoValue("country");
                if (fieldName.contains("section3[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].noccode[0]")) return getEmploymentInfoValue("nocCode");
                if (fieldName.contains("section4[0].citizenship[0]")) return getEmploymentInfoValue("country");
                if (fieldName.contains("section4[0].country[0]")) return getEmploymentInfoValue("country");
                if (fieldName.contains("section4[0].province[0]")) return getEmploymentInfoValue("state");
                if (fieldName.contains("section4[0].city[0]")) return getEmploymentInfoValue("city");


            
            }
            else if (fieldName.contains("imm_0171")) {
                
            	if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("section1[0].uci[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("section1[0].birthplace[0]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("section1[0].citizenship[0]")) return getBasicInfoValue("citizenship");
                if (fieldName.contains("section1[0].address[0].streetnumber[0]")) return getAddressInfoValue("streetNo");
                if (fieldName.contains("section1[0].address[0].appartment[0]")) return getAddressInfoValue("apt");
                if (fieldName.contains("section1[0].address[0].streetname[0]")) return getAddressInfoValue("streetName");
                if (fieldName.contains("section1[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("section1[0].province[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("section1[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                
                if (fieldName.contains("section2[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section2[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section2[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("section2[0].uci[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("section2[0].birthplace[0]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("section2[0].citizenship[0]")) return getBasicInfoValue("citizenship");

                
                if (fieldName.contains("section4[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section4[0].givenname[0]")) return getBasicInfoValue("firstName");
            }
              else if (fieldName.contains("imm_0185")) {
                
                if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("sectiona[0].uci[0].ucino[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("sectiona[0].citycountrybirth[0]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("sectiona[0].citizenship[0]")) return getBasicInfoValue("citizenship");
                if (fieldName.contains("sectiona[0].countrybrith[0]")) return getBasicInfoValue("birthCountry");
                if (fieldName.contains("sectiona[0].countryresidence[0]")) return getBasicInfoValue("residenceCountry");
                if (fieldName.contains("addressblock[0].numberstreet[0]")) return getAddressInfoValue("streetNo")+" "+getAddressInfoValue("streetName") ;
                if (fieldName.contains("addressblock[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("addressblock[0].province[0]")) return getAddressInfoValue("state");
                if (fieldName.contains("addressblock[0].country[0]")) return getAddressInfoValue("country");
                if (fieldName.contains("addressblock[0].postalcode[0]")) return getAddressInfoValue("postalCode");

  
            }
              else if (fieldName.contains("imm_0186")) {
                  
            		if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                    if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                    if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                    if (fieldName.contains("section1[0].uci[0]")) return getBasicInfoValue("uci");
                    if (fieldName.contains("section1[0].birthplace[0]")) return getBasicInfoValue("birthCountry");
                    if (fieldName.contains("section1[0].citizenship[0]")) return getBasicInfoValue("citizenship");
                    if (fieldName.contains("section1[0].address[0].streetnumber[0]")) return getAddressInfoValue("streetNo");
                    if (fieldName.contains("section1[0].address[0].appartment[0]")) return getAddressInfoValue("apt");
                    if (fieldName.contains("section1[0].address[0].streetname[0]")) return getAddressInfoValue("streetName");
                    if (fieldName.contains("section1[0].city[0]")) return getAddressInfoValue("city");
                    if (fieldName.contains("section1[0].province[0]")) return getAddressInfoValue("state");
                    if (fieldName.contains("section1[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                    
                	if (fieldName.contains("section2[0].familyname[0]")) return getBasicInfoValue("lastName");
                    if (fieldName.contains("section2[0].givenname[0]")) return getBasicInfoValue("firstName");
                  
                    if (fieldName.contains("section3[0].familyname[0]")) return getBasicInfoValue("lastName");
                    if (fieldName.contains("section3[0].givenname[0]")) return getBasicInfoValue("firstName");
                    if (fieldName.contains("section3[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                    if (fieldName.contains("section3[0].uci[0]")) return getBasicInfoValue("uci");
                    if (fieldName.contains("section3[0].birthplace[0]")) return getBasicInfoValue("birthCountry");
                    if (fieldName.contains("section3[0].citizenship[0]")) return getBasicInfoValue("citizenship");
                    
            		if (fieldName.contains("section5[0].familyname[0]")) return getBasicInfoValue("lastName");
                    if (fieldName.contains("section5[0].givenname[0]")) return getBasicInfoValue("firstName");

              }
              else if (fieldName.contains("imm_5009")) {
                  
          		if (fieldName.contains("clientidno")) return getBasicInfoValue("uci");
                  if (fieldName.contains("surname[0]")) return getBasicInfoValue("lastName");
                  if (fieldName.contains("given_name[0]")) return getBasicInfoValue("firstName");
                  if (fieldName.contains("current_surname[0]")) return getBasicInfoValue("otherLastName");
                  if (fieldName.contains("current_given_name")) return getBasicInfoValue("otherFirstName");
                  if (fieldName.contains("citizenship[0]")) return getBasicInfoValue("citizenship");
                  if (fieldName.contains("datebox")) return  formatIsoDate(getBasicInfoValue("dob"));
                  if (fieldName.contains("place_of_birth[0]")) return getBasicInfoValue("birthCountry");

                  if (fieldName.contains("street_no[0]")) return getAddressInfoValue("streetNo");
                  if (fieldName.contains("apt[0]")) return getAddressInfoValue("apt");
                  if (fieldName.contains("street_name[0]")) return getAddressInfoValue("streetName");
                  if (fieldName.contains("city-town[0]")) return getAddressInfoValue("city");
                  if (fieldName.contains("textfield4[0]")) return getAddressInfoValue("state");
                  if (fieldName.contains("postal_code[0]")) return getAddressInfoValue("postalCode");
                  if (fieldName.contains("textfield5[0]")) return getAddressInfoValue("country");
                  if (fieldName.contains("district[0]")) return getAddressInfoValue("district");

              	
            }
              else if (fieldName.contains("imm_5476")) {
                  
                  if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                  if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                  if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                  if (fieldName.contains("sectiona[0].uci[0]")) return getBasicInfoValue("uci");
                  if (fieldName.contains("sectionb[0].familyname[0]")) return  getBasicInfoValue("lastName");
                  if (fieldName.contains("sectionb[0].givenname[0]")) return getBasicInfoValue("firstName");
    
              }
              else if (fieldName.contains("imm_5494")) {
                  
     
                   
                   if (fieldName.contains("sectiona[0].textfield2[2]")) return getBasicInfoValue("lastName");
                   if (fieldName.contains("sectiona[0].textfield2[3]")) return getBasicInfoValue("firstName");
                   if (fieldName.contains("sectiona[0].currentdate[0]")) return  formatIsoDate(getBasicInfoValue("dob"));

                   if (fieldName.contains("sectiona[0].textfield2[8]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                   if (fieldName.contains("sectiona[0].textfield2[9]")) return getAddressInfoValue("city");
                   if (fieldName.contains("sectiona[0].textfield2[11]")) return getAddressInfoValue("postalCode");
                   if (fieldName.contains("sectiona[0].textfield2[10]")) return getAddressInfoValue("state");
                  
              }
            else if (fieldName.contains("imm_5501")) {
                  
                  if (fieldName.contains("question12[0].familyname[0]")) return getBasicInfoValue("lastName");
                  if (fieldName.contains("question12[0].givenname1[0]")) return getBasicInfoValue("firstName");
                  if (fieldName.contains("question12[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
              
              }
            else if (fieldName.contains("imm_5519")) {
                
                if (fieldName.contains("contentmain[0].name[0]")) return getBasicInfoValue("lastName")+" "+getBasicInfoValue("firstName");
                if (fieldName.contains("contentmain[0].province[0]")) return getAddressInfoValue("state");
                if (fieldName.contains("contentmain[0].country[0]")) return getAddressInfoValue("country");
                if (fieldName.contains("contentmain[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("contentmain[0].province[1]")) return getAddressInfoValue("state");

                if (fieldName.contains("contentmain[0].namedeclarant[0]")) return getBasicInfoValue("lastName")+" "+getBasicInfoValue("firstName");
                if (fieldName.contains("contentmain[0].declaredbeforeme[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("contentmain[0].declaredbeforeme[0].province[0]")) return getAddressInfoValue("state");
                if (fieldName.contains("contentmain[0].declaredbeforeme[0].country[0]")) return getAddressInfoValue("country");

            }
            
            else if (fieldName.contains("imm_5564")) {
                if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("sectiona[0].thedate[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("sectiona[0].address[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                if (fieldName.contains("sectiona[0].city[0]")) return  getAddressInfoValue("city");
                if (fieldName.contains("sectiona[0].prov[0]")) return getAddressInfoValue("state");
                if (fieldName.contains("sectiona[0].pc[0]")) return getAddressInfoValue("postalCode");

  
            }
            
            else if (fieldName.contains("imm_5634")) {
            	
                if (fieldName.contains("subemployer_1[0].lastname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("subemployer_1[0].prov[0]")) return getEmploymentInfoValue("state");
                if (fieldName.contains("subemployer_1[0].city[0]")) return getEmploymentInfoValue("city");

  
            }
            else if (fieldName.contains("imm_5645")) {
            	   if (fieldName.contains("sectiona[0].applicant[0].appname[0]")) return getBasicInfoValue("lastName")+" "+ getBasicInfoValue("firstName");
                   if (fieldName.contains("sectiona[0].applicant[0].appdob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                   if (fieldName.contains("sectiona[0].applicant[0].appaddress[0]")) return getAddressInfoValue("city") + " " + getAddressInfoValue("country");

                   if (fieldName.contains("sectiona[0].spouse[0].spousename[0]")) return getRelationshipInfoValue("spouseLastName")+" "+ getRelationshipInfoValue("spouseFirstName");
                   if (fieldName.contains("sectiona[0].spouse[0].spousedob[0]")) return formatIsoDate(getRelationshipInfoValue("spouseDOB"));
                   if (fieldName.contains("sectiona[0].spouse[0].childmstatus[0]")) return getRelationshipInfoValue("maritalStatus") ;

                   if (fieldName.contains("sectiona[0].mother[0].mothername[0]")) return getParentInfoValue("motherInfo", "lastName")+" "+getParentInfoValue("motherInfo", "firstName");
                   if (fieldName.contains("sectiona[0].mother[0].motherdob[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));
                   if (fieldName.contains("sectiona[0].mother[0].motheraddress[0]")) return getParentInfoValue("motherInfo", "address") + " " + getParentInfoValue("motherInfo","residenceCountry");
                   if (fieldName.contains("sectiona[0].mother[0].childmstatus[0]")) return getParentInfoValue("motherInfo", "maritalStatus") ;

                   if (fieldName.contains("sectiona[0].father[0].fathername[0]")) return getParentInfoValue("fatherInfo", "lastName")+" "+getParentInfoValue("fatherInfo", "firstName");
                   if (fieldName.contains("sectiona[0].father[0].fatherdob[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));
                   if (fieldName.contains("sectiona[0].father[0].fatheraddress[0]")) return getParentInfoValue("fatherInfo", "address") + " " + getParentInfoValue("fatherInfo","residenceCountry");
                   if (fieldName.contains("sectiona[0].father[0].childmstatus[0]")) return getParentInfoValue("fatherInfo", "maritalStatus") ;


                }
            
            else if (fieldName.contains("imm_5744")) {
                if (fieldName.contains("appinfosub[0].family_name[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("appinfosub[0].given_name[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("appinfosub[0].datebirth[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                
                
                if (fieldName.contains("individsub[0].family_name[0]")) return getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("individsub[0].given_name[0]")) getParentInfoValue("fatherInfo", "lastName");
                if (fieldName.contains("individsub[0].datebirth[0]")) return formatIsoDate(getParentInfoValue("fatherInfo", "dob"));
                if (fieldName.contains("individsub[0].relationship[0]")) return "Father";
                
                if (fieldName.contains("individual4sub[0].family_name[0]")) return getParentInfoValue("motherInfo", "lastName");
                if (fieldName.contains("individual4sub[0].given_name[0]")) getParentInfoValue("motherInfo", "firstName");
                if (fieldName.contains("individual4sub[0].datebirth[0]")) return formatIsoDate(getParentInfoValue("motherInfo", "dob"));
                if (fieldName.contains("individual4sub[0].telephonesub[0].relationship[0]")) return "Mother";
                
                if (fieldName.contains("individual5sub[0].family_name[0]")) return getRelationshipInfoValue("spouseLastName");
                if (fieldName.contains("individual5sub[0].given_name[0]")) return getRelationshipInfoValue("spouseFirstName");
                if (fieldName.contains("individual5sub[0].datebirth[0]")) return formatIsoDate(getRelationshipInfoValue("spouseDOB"));
                if (fieldName.contains("individual5sub[0].telephonesub[0].relationship[0]")) return "Partner ";
                
  
  
            }
            else if (fieldName.contains("irm_0002")) {
                if (fieldName.contains("statement[0].name[0]")) return getBasicInfoValue("lastName")+" "+getBasicInfoValue("firstName");
                if (fieldName.contains("statement[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("statement[0].birthlocation[0]")) return getAddressInfoValue("city")+" "+ getAddressInfoValue("state")+" "+getAddressInfoValue("country");
            }
            else if (fieldName.contains("irm_0005")) {
                if (fieldName.contains("sectiona[0].sfapplicant[0].name[0]")) return getBasicInfoValue("lastName")+" "+getBasicInfoValue("firstName");
                if (fieldName.contains("sectiona[0].sfapplicant[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("sectiona[0].sfapplicant[0].birthlocation[0]")) return getAddressInfoValue("city")+" "+ getAddressInfoValue("state")+" "+getAddressInfoValue("country");
                if (fieldName.contains("sectiona[0].part2[0].uci[0]")) return getBasicInfoValue("uci");
            }
            else if (fieldName.contains("imm_0148")) {
            	   if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                   if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                   if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                   if (fieldName.contains("sectiona[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                   if (fieldName.contains("sectiona[0].countrybrith[0]")) return  getBasicInfoValue("birthCountry");
                  
                   if (fieldName.contains("sectiona[0].countryresidence[0]")) return getBasicInfoValue("residenceCountry");
                   if (fieldName.contains("sectiona[0].addressblock[0].numberstreet[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                   if (fieldName.contains("sectiona[0].addressblock[0].city[0]")) return  getAddressInfoValue("city");
                   if (fieldName.contains("sectiona[0].addressblock[0].province[0]")) return getAddressInfoValue("state");
                   if (fieldName.contains("sectiona[0].addressblock[0].postalcode[0]")) return getAddressInfoValue("postalCode");

                 }
            else if (fieldName.contains("imm_0151")) {
         	   if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section1[0].uci[0]")) return getBasicInfoValue("uci");

                if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("section1[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                if (fieldName.contains("section1[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

                if (fieldName.contains("section1[0].address[0].streetnumber[0]")) return getAddressInfoValue("streetNo");
                if (fieldName.contains("section1[0].address[0].appartment[0]")) return getAddressInfoValue("apt");
                if (fieldName.contains("section1[0].address[0].streetname[0]")) return getAddressInfoValue("streetName");
                if (fieldName.contains("section1[0].city[0]")) return getAddressInfoValue("city");
                if (fieldName.contains("section1[0].province[0]")) return getAddressInfoValue("state");
                if (fieldName.contains("section1[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                
                if (fieldName.contains("section2[0].familyname[0]")) return getBasicInfoValue("lastName");
                if (fieldName.contains("section2[0].givenname[0]")) return getBasicInfoValue("firstName");
                if (fieldName.contains("section2[0].uci[0]")) return getBasicInfoValue("uci");
                if (fieldName.contains("section2[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                if (fieldName.contains("section2[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                if (fieldName.contains("section2[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

            
              }
            else if (fieldName.contains("imm_0191")) {
          	   if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                 if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                 if (fieldName.contains("section1[0].uci[0]")) return getBasicInfoValue("uci");

                 if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                 if (fieldName.contains("section1[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                 if (fieldName.contains("section1[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

                 if (fieldName.contains("section1[0].address[0].streetnumber[0]")) return getAddressInfoValue("streetNo");
                 if (fieldName.contains("section1[0].address[0].appartment[0]")) return getAddressInfoValue("apt");
                 if (fieldName.contains("section1[0].address[0].streetname[0]")) return getAddressInfoValue("streetName");
                 if (fieldName.contains("section1[0].city[0]")) return getAddressInfoValue("city");
                 if (fieldName.contains("section1[0].province[0]")) return getAddressInfoValue("state");
                 if (fieldName.contains("section1[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                 
                 if (fieldName.contains("section2[0].familyname[0]")) return getBasicInfoValue("lastName");
                 if (fieldName.contains("section2[0].givenname[0]")) return getBasicInfoValue("firstName");
                 if (fieldName.contains("section2[0].uci[0]")) return getBasicInfoValue("uci");
                 if (fieldName.contains("section2[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                 if (fieldName.contains("section2[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                 if (fieldName.contains("section2[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

             
               }
                else if (fieldName.contains("imm_0197")) {
                
                if (fieldName.contains("section2[0].contactsurname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("section2[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].job_title[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].noc_code[0]")) return getEmploymentInfoValue("nocCode");

            
                if (fieldName.contains("section4[0].familyname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("section4[0].countrybirth[0]")) return getEmploymentInfoValue("country");
                if (fieldName.contains("section3[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].noccode[0]")) return getEmploymentInfoValue("nocCode");
                if (fieldName.contains("section3[0].employmentstartdate[0]")) return getEmploymentInfoValue("from");
                if (fieldName.contains("section3[0].detailsjob[0].job_duties[0]")) return getEmploymentInfoValue("jobDescription");
                if (fieldName.contains("section3[0].question20[1].province[0]")) return getEmploymentInfoValue("state");
                if (fieldName.contains("section3[0].question20[1].city[0]")) return getEmploymentInfoValue("city");
                if (fieldName.contains("section3[0].detailsjob[0].question27[0].hoursweek[0]")) return getEmploymentInfoValue("hoursPerWeek");

                if (fieldName.contains("section4[0].surname[0]")) return getEmploymentInfoValue("employer");
                if (fieldName.contains("section4[0].countrybirth[0]")) return getEmploymentInfoValue("country");
                if (fieldName.contains("section3[0].jobtitle[0]")) return getEmploymentInfoValue("jobTitle");
                if (fieldName.contains("section3[0].noccode[0]")) return getEmploymentInfoValue("nocCode");
                if (fieldName.contains("section3[0].employmentstartdate[0]")) return getEmploymentInfoValue("from");
                if (fieldName.contains("section3[0].detailsjob[0].job_duties[0]")) return getEmploymentInfoValue("jobDescription");
                if (fieldName.contains("section4[0].question41[0].province[0]")) return getEmploymentInfoValue("state");
                if (fieldName.contains("section4[0].question41[0].city[0]")) return getEmploymentInfoValue("city");
                if (fieldName.contains("section4[0].question41[0].country[0]")) return getEmploymentInfoValue("country");


            
            }
            else if (fieldName.contains("imm_0210")) {
           	   if (fieldName.contains("section1[0].familyname[0]")) return getBasicInfoValue("lastName");
                  if (fieldName.contains("section1[0].givenname[0]")) return getBasicInfoValue("firstName");
                  if (fieldName.contains("section1[0].uci[0]")) return getBasicInfoValue("uci");

                  if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                  if (fieldName.contains("section1[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                  if (fieldName.contains("section1[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

                  if (fieldName.contains("section1[0].address[0].streetnumber[0]")) return getAddressInfoValue("streetNo");
                  if (fieldName.contains("section1[0].address[0].appartment[0]")) return getAddressInfoValue("apt");
                  if (fieldName.contains("section1[0].address[0].streetname[0]")) return getAddressInfoValue("streetName");
                  if (fieldName.contains("section1[0].city[0]")) return getAddressInfoValue("city");
                  if (fieldName.contains("section1[0].province[0]")) return getAddressInfoValue("state");
                  if (fieldName.contains("section1[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                  
                  if (fieldName.contains("section3[0].familyname[0]")) return getBasicInfoValue("lastName");
                  if (fieldName.contains("section3[0].givenname[0]")) return getBasicInfoValue("firstName");
                  if (fieldName.contains("section3[0].uci[0]")) return getBasicInfoValue("uci");
                  if (fieldName.contains("section3[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                  if (fieldName.contains("section3[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                  if (fieldName.contains("section3[0].birthplace[0]")) return  getBasicInfoValue("birthCountry");

              
                }
            else if (fieldName.contains("imm_5515")) {
            	   if (fieldName.contains("page1[0].applicantlastname[0]")) return getBasicInfoValue("lastName");
                   if (fieldName.contains("page1[0].applicantgivenname[0]")) return getBasicInfoValue("firstName");
                   if (fieldName.contains("page1[0].applicantbirthdate[0]")) return formatIsoDate(getBasicInfoValue("dob"));

                   if (fieldName.contains("section1[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                  
               
                 }
            else if (fieldName.contains("imm_5989")) {
            	 if (fieldName.contains("sectiona[0].familyname[0]")) return getBasicInfoValue("lastName");
                 if (fieldName.contains("sectiona[0].givenname[0]")) return getBasicInfoValue("firstName");
                 if (fieldName.contains("sectiona[0].dob[0]")) return formatIsoDate(getBasicInfoValue("dob"));
                 if (fieldName.contains("sectiona[0].citizenship[0]")) return getBasicInfoValue("citizenship") ;
                 if (fieldName.contains("sectiona[0].countrybrith[0]")) return  getBasicInfoValue("birthCountry");
                
                 if (fieldName.contains("sectiona[0].countryresidence[0]")) return getBasicInfoValue("residenceCountry");
                 if (fieldName.contains("sectiona[0].addressblock[0].numberstreet[0]")) return getAddressInfoValue("streetNo") + " " + getAddressInfoValue("streetName");
                 if (fieldName.contains("sectiona[0].addressblock[0].city[0]")) return  getAddressInfoValue("city");
                 if (fieldName.contains("sectiona[0].addressblock[0].province[0]")) return getAddressInfoValue("state");
                 if (fieldName.contains("sectiona[0].addressblock[0].postalcode[0]")) return getAddressInfoValue("postalCode");
                 if (fieldName.contains("sectiona[0].addressblock[0].country[0]")) return getAddressInfoValue("country");

            
              }
            else if (fieldName.contains("imm_5409")) {
           	 if (fieldName.contains("info[0].firstname[0]")) return getBasicInfoValue("lastName")+" "+getBasicInfoValue("firstName");
                if (fieldName.contains("info[0].secondname[0]")) return getRelationshipInfoValue("spouseLastName")+" "+getRelationshipInfoValue("spouseFirstName");
    
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
    
    private static String getRelationshipInfoValue(String key) {
        try {
            Map<String, Object> relationshipMap = (Map<String, Object>) dataMap.get("relationshipInfoDetails");
            if (relationshipMap != null) {
                Map<String, Object> formsData = (Map<String, Object>) relationshipMap.get("formsdata");
                if (formsData != null && formsData.containsKey(key)) {
                    return formsData.get(key).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    private static String getEmploymentInfoValue(String key) {
        try {
            Map<String, Object> employmentMap = (Map<String, Object>) dataMap.get("employmentDetails");
            if (employmentMap != null) {
                List<Map<String, Object>> employmentForms = (List<Map<String, Object>>) employmentMap.get("formsdata");
                if (employmentForms != null && !employmentForms.isEmpty()) {
                    Map<String, Object> job = employmentForms.get(0);
                    return job.containsKey(key) ? job.get(key).toString() : "";
                }
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
                // Print the field name
                System.out.println("Field: " + fullFieldName);
                String value = resolveField(fullFieldName.toLowerCase());
                field.setValue(value);
            }

            pdfDoc.close();
            return outputStream.toByteArray();
        }
    }
}
