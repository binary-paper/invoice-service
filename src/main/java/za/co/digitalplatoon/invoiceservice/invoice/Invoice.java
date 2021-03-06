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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * An invoice entity class
 *
 * @author William Gadney
 */
// JPA annotations
@Entity
@NamedQueries({
    @NamedQuery(name = "Invoice.FindAllInvoices", query = "SELECT i FROM Invoice AS i ORDER BY i.client")
})
// Jackson annotations
@JsonPropertyOrder({
    "id",
    "version",
    "client",
    "vatRate",
    "invoiceDate",
    "createdBy",
    "lineItems",
    "subTotal",
    "vat",
    "total"
})
// Lombok annotations
@Data
@EqualsAndHashCode(exclude = {"lineItems"})
@ToString(exclude = {"lineItems"})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    // JPA annotations
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_ID")
    // Jackson annotations
    @JsonView({
        View.List.class,
        View.All.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The unique ID of the invoice. "
            + "The id must not be specified when adding a new invoice.",
            example = "1",
            readOnly = true,
            position = 1
    )
    private Long id;

    // JPA annotations
    @Version
    // Jackson annotations
    @JsonView({
        View.All.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The version of the database record used for concurrency control. "
            + "The version must not be specified when adding a new invoice.",
            example = "1",
            readOnly = true,
            position = 2
    )
    private Long version;

    // Bean validation annotations
    @NotNull(message = "{Invoice.client.NotNull}")
    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.List.class,
        View.All.class,
        View.Add.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice client.",
            required = true,
            example = "ABC Transport",
            position = 3
    )
    private String client;

    // Bean validation annotations
    @NotNull(message = "{Invoice.vatRate.NotNull}")
    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class,
        View.Add.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice VAT rate.",
            required = true,
            example = "15",
            position = 4
    )
    private Long vatRate;

    // JPA annotations
    @Temporal(TemporalType.DATE)
    // Bean validation annotations
    @NotNull(message = "{Invoice.invoiceDate.NotNull}")
    @Past(message = "{Invoice.invoiceDate.Past}")
    // Jackson annotations
    @JsonProperty(required = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonView({
        View.List.class,
        View.All.class,
        View.Add.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice date.",
            example = "2018-04-17",
            required = true,
            position = 5
    )
    private Date invoiceDate;

    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The logged in user who created the invoice.",
            example = "willy",
            readOnly = true,
            position = 6
    )
    private String createdBy;
    
    // JPA annotations
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    // Bean validation annotations
    @NotNull(message = "{Invoice.lineItems.NotNull}")
    @Size(min = 1, message = "{Invoice.lineItems.Size}")
    @Valid
    // Jackson annotations
    @JsonProperty(required = true)
    @JsonManagedReference
    @JsonView({
        View.All.class,
        View.Add.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice line items.",
            required = true,
            position = 7
    )
    private List<InvoiceLineItem> lineItems = new ArrayList<>();

    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice sub total excluding VAT.",
            example = "5.97",
            readOnly = true,
            position = 8
    )
    public BigDecimal getSubTotal() {
        BigDecimal subTotal = new BigDecimal(0);
        for (InvoiceLineItem lineItem : lineItems) {
            subTotal = subTotal.add(lineItem.getLineItemTotal());
        }
        return subTotal;
    }

    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice VAT amount.",
            example = "0.90",
            readOnly = true,
            position = 9
    )
    public BigDecimal getVat() {
        BigDecimal subTotal = getSubTotal();
        BigDecimal vat = new BigDecimal(vatRate).divide(new BigDecimal(100));
        return subTotal.multiply(vat).setScale(2, RoundingMode.HALF_UP);
    }

    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice total including VAT.",
            example = "6.87",
            readOnly = true,
            position = 10
    )
    public BigDecimal getTotal() {
        return getSubTotal().add(getVat()).setScale(2, RoundingMode.HALF_UP);
    }

    public interface View {

        public interface List {
        }

        public interface All {
        }

        public interface Add {
        }

        public interface Edit {
        }
    }

}
