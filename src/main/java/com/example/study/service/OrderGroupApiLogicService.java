package com.example.study.service;

import com.example.study.model.entity.OrderGroup;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderGroupApiLogicService extends BaseService<OrderGroupApiRequest, OrderGroupApiResponse, OrderGroup> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<List<OrderGroupApiResponse>> search(Pageable pageable) {
        Page<OrderGroup> orderGroups = baseRepository.findAll(pageable);
        List<OrderGroupApiResponse> orderGroupList = orderGroups.stream()
                .map(this::response)
                .collect(Collectors.toList());

        return Header.OK(orderGroupList);
    }

    @Override
    public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> request) {

        OrderGroupApiRequest body = request.getData();
        OrderGroup orderGroup = OrderGroup.builder()
                .status(body.getStatus())
                .orderType(body.getOrderType())
                .revAddress(body.getRevAddress())
                .revName(body.getRevName())
                .paymentType(body.getPaymentType())
                .totalPrice(body.getTotalPrice())
                .totalQuantity(body.getTotalQuantity())
                .orderAt(LocalDateTime.now())
                .arrivalDate(body.getArrivalDate())
                .user(userRepository.getOne(body.getUserId()))
                .build();
        OrderGroup newOrderGroup = baseRepository.save(orderGroup);

        return Header.OK(response(newOrderGroup));
    }

    @Override
    public Header<OrderGroupApiResponse> read(long id) {

        Optional<OrderGroup> optionalOrderGroup = baseRepository.findById(id);

        return optionalOrderGroup
                .map(orderGroup -> Header.OK(response(orderGroup)))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request) {

        OrderGroupApiRequest body = request.getData();
        Optional<OrderGroup> optionalOrderGroup = baseRepository.findById(body.getId());

        return optionalOrderGroup
                .map(orderGroup -> orderGroup
                        .setStatus(body.getStatus())
                        .setOrderType(body.getOrderType())
                        .setRevName(body.getRevName())
                        .setRevAddress(body.getRevAddress())
                        .setPaymentType(body.getPaymentType())
                        .setTotalQuantity(body.getTotalQuantity())
                        .setTotalPrice(body.getTotalPrice())
                        .setOrderAt(body.getOrderAt())
                        .setArrivalDate(body.getArrivalDate()))
                .map(updatedOrderGroup -> baseRepository.save(updatedOrderGroup))
                .map(updatedOrderGroup -> Header.OK(response(updatedOrderGroup)))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(long id) {
        Optional<OrderGroup> optionalOrderGroup = baseRepository.findById(id);

        return optionalOrderGroup
                .map(orderGroup -> {
                    baseRepository.delete(orderGroup);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    OrderGroupApiResponse response(OrderGroup orderGroup) {
        return OrderGroupApiResponse.builder()
                .id((orderGroup.getId()))
                .status(orderGroup.getStatus())
                .orderType(orderGroup.getOrderType())
                .revAddress(orderGroup.getRevAddress())
                .revName(orderGroup.getRevName())
                .paymentType(orderGroup.getPaymentType())
                .totalPrice(orderGroup.getTotalPrice())
                .totalQuantity(orderGroup.getTotalQuantity())
                .orderAt(orderGroup.getOrderAt())
                .arrivalDate(orderGroup.getArrivalDate())
                .userId(orderGroup.getUser().getId())
                .build();
    }
}
