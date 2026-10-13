package com.example.lambda;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class OrderNotificationHandler
        implements RequestHandler<OrderNotificationRequest, String> {

    @Override
    public String handleRequest(OrderNotificationRequest request,
                                Context context) {

        String message =
                "Order " + request.getOrderId()
                + " created successfully";
        context.getLogger().log(
        	    "Received orderId = " + request.getOrderId());

        	context.getLogger().log(
        	    "Received customerName = " + request.getCustomerName());

        context.getLogger().log(message);

        DynamoDbClient dynamoDbClient =
                DynamoDbClient.create();

        Map<String, AttributeValue> item =
                new HashMap<>();

        item.put("notificationId",
                AttributeValue.builder()
                        .s(UUID.randomUUID().toString())
                        .build());

        item.put("orderId",
                AttributeValue.builder()
                        .s(String.valueOf(request.getOrderId()))
                        .build());

        item.put("customerName",
                AttributeValue.builder()
                        .s(request.getCustomerName())
                        .build());

        item.put("message",
                AttributeValue.builder()
                        .s(message)
                        .build());

        item.put("timestamp",
                AttributeValue.builder()
                        .s(LocalDateTime.now().toString())
                        .build());

        PutItemRequest putItemRequest =
                PutItemRequest.builder()
                        .tableName("OrderNotifications")
                        .item(item)
                        .build();
        context.getLogger().log("orderId = " + request.getOrderId());

        context.getLogger().log("customerName = " + request.getCustomerName());

        context.getLogger().log("message = " + message);

        dynamoDbClient.putItem(putItemRequest);
        context.getLogger().log(
                "Notification stored successfully in DynamoDB");

        return message;
    }
}