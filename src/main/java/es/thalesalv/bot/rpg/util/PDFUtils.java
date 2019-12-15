package es.thalesalv.bot.rpg.util;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtils.class);

    public static PDDocument getDocument(String path) throws Exception {
        try {
            File filePath = new File(path);
            PDDocument sheet = PDDocument.load(filePath);
            return sheet;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public static void setField(String fieldName, String value, PDDocument document) throws Exception {
        try {
            PDDocumentCatalog docCatalog = document.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();
            PDField field = acroForm.getField(fieldName);
            if (field != null) {
                field.setValue(value);
            } else {
                LOGGER.error("Não foi encontrado o campo de nome " + fieldName);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public static void saveDocument(PDDocument document, String destFile) throws Exception {
        try {
            document.save(new File(destFile));
            document.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }

    public static void checkField(String fieldName, PDDocument document) throws Exception {
        try {
            PDDocumentCatalog docCatalog = document.getDocumentCatalog();
            PDAcroForm acroForm = docCatalog.getAcroForm();
            PDField field = acroForm.getField(fieldName);
            ((PDCheckBox) field).check();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
    }
}
