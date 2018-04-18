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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import za.co.digitalplatoon.invoiceservice.invoice.Invoice.View;

/**
 * An invoice line item entity class
 *
 * @author William Gadney
 */
// JPA annotations
@Entity
// Jackson annotations
@JsonPropertyOrder({
    "id",
    "version",
    "quantity",
    "description",
    "unitPrice",
    "lineItemTotal"
})
// Lombok annotations
@Data
public class InvoiceLineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // JPA annotations
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_LINE_ITEM_ID")
    // Jackson annotations
    @JsonView({
        View.List.class,
        View.All.class,
        View.Edit.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The unique ID of the invoice line item. "
            + "The id must not be specified when adding a new invoice line item.",
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
            + "The version must not be specified when adding a new invoice line item.",
            example = "1",
            readOnly = true,
            position = 2
    )
    private Long version;

    // JPA annotations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID", foreignKey = @ForeignKey(name = "FK_INVOICE_LINE_ITEM_INVOICE"))
    // Jackson annotations
    @JsonBackReference
    private Invoice invoice;

    // Bean validation annotations
    @NotNull(message = "{InvoiceLineItem.quantity.NotNull}")
    @Min(value = 1, message = "{InvoiceLineItem.quantity.Min}")
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
            value = "The quantity of the invoice line item.",
            required = true,
            example = "3",
            position = 3
    )
    private Long quantity;

    // Bean validation annotations
    @NotNull(message = "{InvoiceLineItem.description.NotNull}")
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
            value = "The description of the invoice line item.",
            required = true,
            example = "Widget",
            position = 4
    )
    private String description;

    // Bean validation annotations
    @NotNull(message = "{InvoiceLineItem.unitPrice.NotNull}")
    @DecimalMin(value = "0.01", message = "{InvoiceLineItem.unitPrice.DecimalMin}")
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
            value = "The description of the invoice line item.",
            required = true,
            example = "1.99",
            position = 4
    )
    private BigDecimal unitPrice;

    // Jackson annotations
    @JsonProperty(required = true)
    @JsonView({
        View.All.class
    })
    // Swagger annotations
    @ApiModelProperty(
            value = "The invoice line item total.",
            example = "5.97",
            readOnly = true,
            position = 5
    )
    public BigDecimal getLineItemTotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }
    
}
