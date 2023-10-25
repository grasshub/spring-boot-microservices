package com.ecomm.productservice.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductSequenceService {

    private final MongoClient mongoClient;

    public Long generateProductSequence(String sequenceName) {

        MongoCollection<Document> collection = mongoClient.getDatabase("product_service")
                .getCollection("product_sequence");

        Document productSequence = collection.findOneAndUpdate(
                Filters.eq("_id", sequenceName),
                Updates.inc("sequence", 1L),
                new FindOneAndUpdateOptions()
                        .upsert(true)
                        .returnDocument(ReturnDocument.AFTER)
        );

        return productSequence.getLong("sequence");
    }
}
