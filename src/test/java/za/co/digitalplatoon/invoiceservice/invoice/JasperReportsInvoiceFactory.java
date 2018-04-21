/*
 * Copyright 2018 A100750.
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Jasper Reports Invoice Factory used for the development of the invoice report template
 * 
 * @author William Gadney
 */
public class JasperReportsInvoiceFactory {
    
    public static List<Invoice> getInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setVersion(0L);
        invoice.setClient("ABC Tranport");
        invoice.setInvoiceDate(new Date());
        invoice.setVatRate(15L);
        List<InvoiceLineItem> lineItems = new ArrayList<>();
        
        InvoiceLineItem lineItem1 = new InvoiceLineItem();
        lineItem1.setInvoice(invoice);
        lineItem1.setId(1L);
        lineItem1.setVersion(0L);
        lineItem1.setDescription("Small Widget");
        lineItem1.setQuantity(3L);
        lineItem1.setUnitPrice(new BigDecimal(1.99));
        lineItems.add(lineItem1);
        
        InvoiceLineItem lineItem2 = new InvoiceLineItem();
        lineItem2.setInvoice(invoice);
        lineItem2.setId(2L);
        lineItem2.setVersion(0L);
        lineItem2.setDescription("Standard Widget");
        lineItem2.setQuantity(10L);
        lineItem2.setUnitPrice(new BigDecimal(10.49));
        lineItems.add(lineItem2);
        
        InvoiceLineItem lineItem3 = new InvoiceLineItem();
        lineItem3.setInvoice(invoice);
        lineItem3.setId(3L);
        lineItem3.setVersion(0L);
        lineItem3.setDescription("Large Widget");
        lineItem3.setQuantity(10L);
        lineItem3.setUnitPrice(new BigDecimal(122.65));
        lineItems.add(lineItem3);
        
        invoice.setLineItems(lineItems);
        invoices.add(invoice);
        
        return invoices;
    }
    
}
