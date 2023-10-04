package com.ecomm.userservice.projection;

import com.ecomm.axonservice.model.Card;
import com.ecomm.axonservice.model.User;
import com.ecomm.axonservice.query.GetUserPaymentQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserProjection {

    @QueryHandler
    public User getUserPayment(GetUserPaymentQuery query) {
        //Ideally Get the details from the DB
        Card card
                = Card.builder()
                .name("Golden Card")
                .validUntilYear(2022)
                .validUntilMonth(1)
                .cardNumber("123456789")
                .cvv(111)
                .build();

        return User.builder()
                .userId(query.getUserId())
                .firstName("John")
                .lastName("Doe")
                .card(card)
                .build();
    }
}