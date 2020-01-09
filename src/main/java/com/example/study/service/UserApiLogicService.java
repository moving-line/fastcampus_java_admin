package com.example.study.service;

import com.example.study.model.entity.OrderDetail;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.User;
import com.example.study.model.enumClass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.model.network.response.UserOrderInfoApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    @Autowired
    private OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    @Override
    public Header<List<UserApiResponse>> search(Pageable pageable) {
        Page<User> users = baseRepository.findAll(pageable);
        List<UserApiResponse> userApiResponseList = users.stream()
                .map(this::response)
                .collect(Collectors.toList());

        return Header.OK(userApiResponseList);
    }

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {
        UserApiRequest userApiRequest = request.getData();

        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();
        User newUSer = baseRepository.save(user);

        return Header.OK(response(newUSer));
    }

    @Override
    public Header<UserApiResponse> read(long id) {
        Optional<User> optionalUser = baseRepository.findById(id);
        return optionalUser
                .map(user -> Header.OK(response(user)))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        UserApiRequest userApiRequest = request.getData();
        Optional<User> optionalUser = baseRepository.findById(userApiRequest.getId());
        return optionalUser
                .map(user -> user
                        .setAccount(userApiRequest.getAccount())
                        .setPassword(userApiRequest.getPassword())
                        .setStatus(userApiRequest.getStatus())
                        .setPhoneNumber(userApiRequest.getPhoneNumber())
                        .setEmail(userApiRequest.getEmail())
                        .setRegisteredAt(userApiRequest.getRegisteredAt())
                        .setUnregisteredAt(userApiRequest.getUnregisteredAt()))
                .map(updatedUser -> baseRepository.save(updatedUser))
                .map(updatedUser -> Header.OK(response(updatedUser)))
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }


    @Override
    public Header delete(long id) {
        Optional<User> optionalUser = baseRepository.findById(id);
        return optionalUser
                .map(user -> {
                    baseRepository.delete(user);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    private UserApiResponse response(User user) {
        return UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(UserStatus.REGISTERED)
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();
    }

    public Header<UserOrderInfoApiResponse> orderInfo(long id) {

        User user = baseRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);
        // 여기까지 기초1 userOrderInfoApiResponse 소환.

        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        // userOrderInfoApiResponse 에 들어갈 List<OrderGroupApiResponse>를 위해 List<OrderGroup>를 소환.

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream() //각 OrderGroup에 담긴것 체크.
                .map(orderGroup -> {
                    OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.response(orderGroup);
                            // 일단 기초2 OrderGroupApiResponse 소환.

                    List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                            .map(OrderDetail::getItem)
                            .map(item -> itemApiLogicService.response(item))
                            .collect(Collectors.toList());
                    // 각 OrderGroupApiResponse에 들어갈 List<ItemApiResponse>를 위해 OrderDetailList()를 소환
                    // 그리고 각각 Detail에 매칭된 item을 찾아서 다시 List<ItemApiResponse>로 조립한다.

                    orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                    // 완성된 List<ItemApiResponse>을 orderGroupApiResponse 에 세팅.

                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList());

        userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);
        // 완성된 List<OrderGroupApiResponse>을 userApiResponse 에 세팅.

        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        //마지막으로 UserOrderInfoApiResponse 에 userApiResponse 세팅.

        return Header.OK(userOrderInfoApiResponse);
    }
}
