package com.ecomm.userservice.controller;

import com.ecomm.axonservice.model.User;
import com.ecomm.axonservice.query.GetUserPaymentQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final QueryGateway queryGateway;

    @GetMapping("/{userId}")
    public User getUserPayment(@PathVariable String userId) {
        GetUserPaymentQuery getUserPaymentQuery
                = new GetUserPaymentQuery(userId);
        return queryGateway.query(getUserPaymentQuery,
                        ResponseTypes.instanceOf(User.class)).join();
    }
}