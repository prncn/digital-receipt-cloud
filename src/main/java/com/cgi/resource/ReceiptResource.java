package com.cgi.resource;

import com.cgi.model.EKaBS;
import com.cgi.service.ReceiptService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestPath;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/v2/receipt")
public class ReceiptResource {

    @Inject
    ObjectMapper mapper;

    List<EKaBS> store = new ArrayList<>();

    @ConfigProperty(name = "app.host")
    String HOST_NAME;

    @ConfigProperty(name = "quarkus.http.port")
    String PORT;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public URI createReceiptURL(EKaBS receipt) throws NumberFormatException, URISyntaxException {
        String id = UUID.randomUUID().toString();
        receipt.head.id = id;

        URI baseURI = new URI("http", null, HOST_NAME, Integer.parseInt(PORT), "/v2/receipt/", null, null);
        URI resultURI = baseURI.resolve(id);
        System.out.println("URI: " + resultURI);
        store.add(receipt);
        return resultURI;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EKaBS> getAllReceipts() {
        return store;
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public EKaBS getTestReceipt() {
        return EKaBS.createTestReceipt("550e8400-e29b-11d4-a716-446655440000");
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public EKaBS getReceiptById(@RestPath String id) {
        for (EKaBS receipt : store) {
            if (receipt.head.id.equals(id)) {
                return receipt;
            }
        }
        return null;
    }

    @GET
    @Path("/{id}/view")
    @Produces("application/pdf")
    public byte[] createPDF(@RestPath String id) throws IOException, DocumentException {
        EKaBS receipt = getReceiptById(id);
        byte[] pdfData = ReceiptService.generateReceiptDocument(receipt);
        return pdfData;
    }

    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public byte[] generateFormattedReceiptPDF() {
        EKaBS receipt = EKaBS.createTestReceipt("550e8400-e29b-11d4-a716-446655440000");
        byte[] pdfData = null;
        try {
            pdfData = ReceiptService.generateReceiptDocument(receipt);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        return pdfData;
    }
}
