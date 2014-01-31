package fr.jeromesengel.documentgenerator.document;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.jeromesengel.documentgenerator.common.ExportType;
import fr.jeromesengel.documentgenerator.common.Page;

@Controller
public class DocumentController {

	@Inject
	private DocumentService documentService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDocumentPage(Model model) {
		return Page.DOCUMENT.getView();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> getFile(@RequestParam(value = "html") String html, @RequestParam(value = "extension") String extension) {
		ExportType exportType = null;

		try {
			exportType = ExportType.valueOf(extension);
		} catch (IllegalArgumentException | NullPointerException e) {
			return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}

		ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) documentService.generateDocument(html, exportType);

		if (byteArrayOutputStream != null) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", exportType.getContentType());
			headers.add("Content-Disposition", "attachment; filename=\"document." + exportType.getExtension() + "\"");
			return new ResponseEntity<byte[]>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<byte[]>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
	}
}