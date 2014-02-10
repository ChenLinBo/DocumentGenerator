package fr.jeromesengel.documentgenerator.utils;

import java.util.List;

import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.FooterPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Br;
import org.docx4j.wml.FldChar;
import org.docx4j.wml.FooterReference;
import org.docx4j.wml.Ftr;
import org.docx4j.wml.HdrFtrRef;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.STFldCharType;
import org.docx4j.wml.SectPr;
import org.docx4j.wml.Text;

public final class Docx4jGenerationHelper {
	private WordprocessingMLPackage wordMLPackage;
	private ObjectFactory factory;

	public Docx4jGenerationHelper(WordprocessingMLPackage wordMLPackage, ObjectFactory factory) {
		this.wordMLPackage = wordMLPackage;
		this.factory = factory;
	}

	/**
	 * As in the previous example, this method creates a footer part and adds it to the main document and then returns
	 * the corresponding relationship.
	 * 
	 * @return
	 * @throws InvalidFormatException
	 */
	public Relationship createFooterPart() throws InvalidFormatException {
		FooterPart footerPart = new FooterPart();
		footerPart.setPackage(wordMLPackage);

		footerPart.setJaxbElement(createFooterWithPageNr());

		return wordMLPackage.getMainDocumentPart().addTargetPart(footerPart);
	}

	/**
	 * As in the previous example, we create a footer and a paragraph object. But this time, instead of adding text to a
	 * run, we add a field. And just as with the table of content, we have to add a begin and end character around the
	 * actual field with the page number. Finally we add the paragraph to the content of the footer and then return it.
	 * 
	 * @return
	 */
	public Ftr createFooterWithPageNr() {
		Ftr ftr = factory.createFtr();
		P paragraph = factory.createP();

		addFieldBegin(paragraph);
		addPageNumberField(paragraph);
		addFieldEnd(paragraph);
		ftr.getContent().add(paragraph);

		return ftr;
	}

	/**
	 * Creating the page number field is nearly the same as creating the field in the TOC example. The only difference
	 * is in the value. We use the PAGE command, which prints the number of the current page, together with the
	 * MERGEFORMAT switch, which indicates that the current formatting should be preserved when the field is updated.
	 * 
	 * @param paragraph
	 */
	public void addPageNumberField(P paragraph) {
		R run = factory.createR();
		Text txt = new Text();
		txt.setSpace("preserve");
		txt.setValue(" PAGE   \\* MERGEFORMAT");
		run.getContent().add(factory.createRInstrText(txt));
		paragraph.getContent().add(run);
	}

	/**
	 * Every fields needs to be delimited by complex field characters. This method adds the delimiter that precedes the
	 * actual field to the given paragraph.
	 * 
	 * @param paragraph
	 */
	public void addFieldBegin(P paragraph) {
		R run = factory.createR();
		FldChar fldchar = factory.createFldChar();
		fldchar.setFldCharType(STFldCharType.BEGIN);
		run.getContent().add(fldchar);
		paragraph.getContent().add(run);
	}

	/**
	 * Every fields needs to be delimited by complex field characters. This method adds the delimiter that follows the
	 * actual field to the given paragraph.
	 * 
	 * @param paragraph
	 */
	public void addFieldEnd(P paragraph) {
		FldChar fldcharend = factory.createFldChar();
		fldcharend.setFldCharType(STFldCharType.END);
		R run3 = factory.createR();
		run3.getContent().add(fldcharend);
		paragraph.getContent().add(run3);
	}

	/**
	 * This method fetches the document final section properties, and adds a newly created footer reference to them.
	 * 
	 * @param relationship
	 */
	public void createFooterReference(Relationship relationship) {

		List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();

		SectPr sectPr = sections.get(sections.size() - 1).getSectPr();
		// There is always a section wrapper, but it might not contain a sectPr
		if (sectPr == null) {
			sectPr = factory.createSectPr();
			wordMLPackage.getMainDocumentPart().addObject(sectPr);
			sections.get(sections.size() - 1).setSectPr(sectPr);
		}

		FooterReference footerReference = factory.createFooterReference();
		footerReference.setId(relationship.getId());
		footerReference.setType(HdrFtrRef.DEFAULT);
		sectPr.getEGHdrFtrReferences().add(footerReference);
	}

	/**
	 * Adds a page break to the document.
	 * 
	 * @param documentPart
	 */
	public void addPageBreak(MainDocumentPart documentPart) {
		Br breakObj = new Br();
		breakObj.setType(STBrType.PAGE);

		P paragraph = factory.createP();
		paragraph.getContent().add(breakObj);
		documentPart.getJaxbElement().getBody().getContent().add(paragraph);
	}
}
