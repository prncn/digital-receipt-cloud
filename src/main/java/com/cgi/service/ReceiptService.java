package com.cgi.service;

import com.cgi.model.EKaBS;
import com.cgi.model.Line;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ReceiptService {

    public static byte[] generateReceiptDocument(EKaBS receipt) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Font fontHeading = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(100, 100, 100));
        Font fontSubHeading = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(100, 100, 100));
        Font fontBody = FontFactory.getFont(FontFactory.HELVETICA, 10);

        Paragraph title = new Paragraph("DIGITAL RECEIPT", fontHeading);
        title.setAlignment(Paragraph.ALIGN_JUSTIFIED_ALL);
        document.add(title);
        for (String line : List.of(
                "This document was created on",
                "the digital receipt cloud.",
                receipt.head.id)) {
            Paragraph headerLine = new Paragraph(line, fontSubHeading);
            headerLine.setAlignment(Paragraph.ALIGN_JUSTIFIED_ALL);
            document.add(headerLine);
        }

        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        for (String line : List.of(
                "CGI CAFÉ",
                "65760 Eschborn",
                "Frankfurter Straße 102-110",
                "UstId: DE123456789")) {
            Paragraph merchant = new Paragraph(line, fontBody);
            merchant.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(merchant);
        }

        PdfPTable metaDataTable = createTable(2, fontBody, List.of(
                "Datum:",
                receipt.head.date,
                "Beleg Nr:",
                "5458216",
                "Mitarbeiter:in:",
                "Margot Dürer"));

        document.add(Chunk.NEWLINE);
        document.add(metaDataTable);

        PdfPTable lineTableHeader = createTable(4, fontBody, List.of(
                "Anzahl",
                "Artikel",
                "",
                "EUR"));

        List<String> lineContent = new ArrayList<>();
        for (Line line : receipt.data.lines) {
            lineContent.add(String.valueOf(line.item.quantity));
            lineContent.add(line.text);
            lineContent.add("A");
            lineContent.add(String.format("%.2f", receipt.data.full_amount_incl_vat));
        }
        PdfPTable lineTable = createTable(4, fontBody, lineContent);

        lineTableHeader.setWidths(new int[] { 50, 100, 50, 50 });
        lineTable.setWidths(new int[] { 50, 100, 50, 50 });
        document.add(Chunk.NEWLINE);
        document.add(lineTableHeader);
        document.add(lineTable);

        PdfPTable paymentTable = createTable(4, fontBody, List.of(
                "Rechnungsbetrag",
                "EUR",
                "",
                String.format("%.2f", receipt.data.full_amount_incl_vat),

                "Kartenzahlung",
                "",
                "",
                String.format("%.2f", receipt.data.full_amount_incl_vat),

                "Rückgeld",
                "",
                "",
                "0,00"));
        paymentTable.setWidths(new int[] { 200, 50, 50, 50 });

        document.add(Chunk.NEWLINE);
        document.add(paymentTable);

        PdfPTable vatTable = createTable(4, fontBody, List.of(
                "Steuersatz",
                "Netto",
                "MwSt",
                "Brutto",
                "A 7% MwSt",
                String.format("%.2f", receipt.calculateAmountExclVat()),
                String.format("%.2f", receipt.calculateVat()),
                String.format("%.2f", receipt.data.full_amount_incl_vat),
                "Summe MwSt",
                String.format("%.2f", receipt.calculateAmountExclVat()),
                String.format("%.2f", receipt.calculateVat()),
                String.format("%.2f", receipt.data.full_amount_incl_vat)));
        vatTable.setWidths(new int[] { 200, 50, 50, 50 });

        document.add(Chunk.NEWLINE);
        document.add(vatTable);

        document.close();
        return outputStream.toByteArray();
    }

    private static PdfPTable createTable(int columns, Font font, List<String> content) {
        PdfPTable table = new PdfPTable(columns);
        int metaDataTableIndex = 1;
        for (String text : content) {
            PdfPCell cell = new PdfPCell(new Phrase(text.toUpperCase(), font));
            cell.setBorder(0);
            // cell.setColspan(metaDataTableIndex);
            if (metaDataTableIndex % columns == 0) {
                cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            }
            table.addCell(cell);
            metaDataTableIndex++;
        }
        return table;
    }
}