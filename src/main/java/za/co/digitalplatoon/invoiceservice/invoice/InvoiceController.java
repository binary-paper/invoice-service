/*
 * Copyright 2018 William Gadney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package za.co.digitalplatoon.invoiceservice.invoice;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

/**
 * A REST Web Service controller for managing invoices
 *
 * @author William Gadney
 */
// Spring annotations
@RestController
@RequestMapping(path = "invoices", produces = {MediaType.APPLICATION_JSON_VALUE})
// Security annotations
@RolesAllowed("view-invoices")
// Swagger annotations
@Api(tags = {"Invoice"}, authorizations = { //    @Authorization(value = SwaggerConfig.O_AUTH_2)
})
// Lombok annotations
@Log
public class InvoiceController {

    @PersistenceContext
    private EntityManager em;

    // Spring annotations
    @PostMapping
    @Transactional
    // Security annotations
    @RolesAllowed("add-invoice")
    // Jackson annotations
    @JsonView(Invoice.View.All.class)
    // Swagger annotations
    @ApiOperation(value = "Add an invoice",
            notes = "Add an invoice",
            code = 201,
            response = Invoice.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "The input data is invalid")
    })
    public ResponseEntity<Invoice> addInvoice(
            @RequestBody
            @JsonView(Invoice.View.Add.class)
            @Valid Invoice invoice,
            @ApiIgnore Principal principal) {
        em.persist(invoice);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(invoice.getId())
                .toUri();
        return ResponseEntity.created(location).body(invoice);
    }

    // Spring annotations
    @GetMapping
    // Security annotations
    @RolesAllowed("view-invoice")
    // Jackson annotations
    @JsonView(Invoice.View.List.class)
    // Swagger annotations
    @ApiOperation(value = "View all invoices",
            notes = "View all invoices",
            code = 200,
            responseContainer = "List",
            response = Invoice.class
    )
    public ResponseEntity<List<Invoice>> viewAllInvoices(
            @ApiIgnore Principal principal) {
        TypedQuery<Invoice> query = em.createNamedQuery("Invoice.FindAllInvoices", Invoice.class);
        List<Invoice> invoices = query.getResultList();
        return ResponseEntity.ok(invoices);
    }

    // Spring annotations
    @GetMapping(path = "{invoiceId}")
    // Security annotations
    @RolesAllowed("view-invoice")
    // Jackson annotations
    @JsonView(Invoice.View.All.class)
    // Swagger annotations
    @ApiOperation(value = "View an invoice by ID",
            notes = "View an invoice by ID",
            code = 200,
            response = Invoice.class
    )
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "The input data is invalid")
        ,@ApiResponse(code = 404, message = "The invoiceId is invalid")
    })
    public ResponseEntity<Invoice> viewInvoice(
            @PathVariable Long invoiceId,
            @ApiIgnore Principal principal) {
        Invoice invoice = em.find(Invoice.class, invoiceId);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(invoice);
    }

}
