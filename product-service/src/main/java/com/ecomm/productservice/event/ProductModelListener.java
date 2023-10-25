package com.ecomm.productservice.event;

import com.ecomm.productservice.domain.Product;
import com.ecomm.productservice.service.ProductSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
public class ProductModelListener extends AbstractMongoEventListener<Product> {

    private final ProductSequenceService productSequenceService;

//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<Product> productBeforeConvertEvent) {
//        if (productBeforeConvertEvent.getSource().getId() < 1) {
//            productBeforeConvertEvent.getSource().setId(productSequenceService.generateProductSequence(Product.SEQUENCE));
//        }
//    }
}
