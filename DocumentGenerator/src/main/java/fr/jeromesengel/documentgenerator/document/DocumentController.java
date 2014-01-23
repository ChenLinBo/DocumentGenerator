package fr.jeromesengel.documentgenerator.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.inject.Inject;

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

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public byte[] getFile(@RequestParam(value = "html") String html, @RequestParam(value = "extension") String extension) throws IOException {
		ExportType exportType = ExportType.valueOf(extension);
		File file = documentService.generateDocument(html, "document" + extension, exportType);

		if (file != null) {
			return Files.readAllBytes(file.toPath());
		} else {
			return null;
		}
	}

}