package com.example.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;

@Service
public class SepayService {
    @Value("${sepay.api.url}")
    private String API_URL;

    @Value("${sepay.merchant.id}")
    private String MERCHANT_ID;

    @Value("${sepay.api.key}")
    private String API_KEY;

    @Value("${sepay.notify.url}")
    private String NOTIFY_URL;

    @Value("${sepay.merchant.name}")
    private String MERCHANT_NAME;
    
    public String createPayment(Double amount, String orderId, String returnUrl) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> request = new HashMap<>();
        request.put("merchant_id", MERCHANT_ID);
        request.put("api_key", API_KEY);
        request.put("amount", amount);
        request.put("order_id", orderId);
        request.put("return_url", returnUrl);
        request.put("notify_url", NOTIFY_URL);
        request.put("merchant_name", MERCHANT_NAME);
        // Thêm các tham số khác nếu Sepay yêu cầu

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);
        System.out.println("SEPAY status: " + response.getStatusCodeValue());
        System.out.println("SEPAY response: " + response.getBody());
        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("payment_url")) {
                return (String) body.get("payment_url");
            }
        }
        return null;
    }
} 