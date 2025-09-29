package com.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.BadElementException;

import java.nio.file.Path;
import java.nio.file.Paths;
import com.itextpdf.text.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.io.IOException;

public class PDFgeneration {

    public static void main(String[] args) throws FileNotFoundException, DocumentException, URISyntaxException, BadElementException, MalformedURLException, IOException {
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream("sample.pdf"));

        doc.open();

        // Add paragraph
        Paragraph par = new Paragraph("Hola Mundo!!");
        doc.add(par);

        // Add table
        PdfPTable table = new PdfPTable(3); // 3 columns
        tableHeader(table);
        addRow(table,1+"","John","Doe");
        addRow(table,2+"","Jane","Smith");
        addRow(table,3+"","Mike","Johnson");
        addCustomRow(table);
        doc.add(table);

        doc.close();

        System.out.println("PDF generated successfully!");
    }

    private static void tableHeader(PdfPTable table) {
        String[] headers = {"Id", "First Name", "Last Name"};
        for (String title : headers) {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(1);
            header.setPhrase(new Paragraph(title));
            table.addCell(header);
        }
    }

    private static void addRow(PdfPTable table, String id, String firstName, String lastName) {
        table.addCell(id);
        table.addCell(firstName);
        table.addCell(lastName);
    }

    private static void addCustomRow(PdfPTable table)
        throws URISyntaxException, BadElementException, MalformedURLException, IOException {
    
        Path path = Paths.get(ClassLoader.getSystemResource("stillthebest.png").toURI());

        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img, true);
        table.addCell(imageCell);

        PdfPCell desc = new PdfPCell(new Phrase("Still the Best 1973"));
        desc.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(desc);

        PdfPCell remarks = new PdfPCell(new Phrase("Clippy just wants to help"));
        remarks.setVerticalAlignment(Element.ALIGN_CENTER);
        table.addCell(remarks);
    }

}

