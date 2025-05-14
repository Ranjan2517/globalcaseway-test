package com.app.controller;


import com.app.util.FillAcroForm;


import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

	@PostMapping("/fill-upload")
	public ResponseEntity<byte[]> uploadAndFillPdf(
	        @RequestParam("file") MultipartFile multipartFile,
	        @RequestParam("clientData1") String clientData1Json
	) {
	    try {
	        byte[] filledPdfBytes = FillAcroForm.fillPdfWithDummyData(multipartFile.getBytes(), clientData1Json);

	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"filled_uploaded.pdf\"")
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(filledPdfBytes);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.internalServerError().body(null);
	    }
	}

}
