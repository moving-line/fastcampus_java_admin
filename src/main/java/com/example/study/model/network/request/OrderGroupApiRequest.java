package com.example.study.model.network.request;

import com.example.study.model.enumClass.OrderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGroupApiRequest {

    private long id;

    private String status;

    private OrderType orderType;

    private String revAddress;

    private String revName;

    private String paymentType;

    private BigDecimal totalPrice;

    private int totalQuantity;

    private LocalDateTime orderAt;

    private LocalDateTime arrivalDate;

    private long userId;

}