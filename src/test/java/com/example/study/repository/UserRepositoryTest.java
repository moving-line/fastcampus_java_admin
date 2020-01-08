package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.User;
import com.example.study.model.enumClass.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserRepositoryTest extends StudyApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
//        User user = new User();
//
//        user.setAccount("TEstUser03");
//        user.setEmail("TestUser03@naver.com");
//        user.setPhoneNumber("010-1111-3333");
//        user.setCreatedAt(LocalDateTime.now());
//        user.setCreatedBy("TestUser03");
//
//        User newUser = userRepository.save(user);
//        System.out.println("newUser : " + newUser);

        String account = "Test02";
        String password = "1234";
        UserStatus status = UserStatus.REGISTERED;
        String email = "Test@gmail.com";
        String phoneNumber = "010-1111-3333";
        LocalDateTime registeredAt = LocalDateTime.now();
//        LocalDateTime createdAt = LocalDateTime.now();
//        String createdBy = "AdminServer";

        User user1 = User.builder()
                .account(account)
                .password(password)
                .status(status)
                .email(email)
                .build();

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setStatus(status);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setRegisteredAt(registeredAt);
        User newUser = userRepository.save(user);

        Assertions.assertNotNull(newUser);
    }

    @Test
    @Transactional
    public void read() {
//        Optional<User> user = userRepository.findByAccount("TEstUser03");
//
//        user.ifPresent(selectUser -> {
//            selectUser.getOrderDetailList().forEach(detail -> {
//                System.out.println(detail.getItem());
//            });
//        });

        String phoneNumber = "010-1111-2222";
        User user = userRepository.findFirstByPhoneNumberOrderByIdDesc(phoneNumber);


        if(user != null) {
            user.getOrderGroupList().stream().forEach(orderGroup -> {
                System.out.println("------------주문 묶음---------------");
                System.out.println("수령인 : " + orderGroup.getRevName());
                System.out.println("수령 : " + orderGroup.getRevAddress());
                System.out.println("총금액 : " + orderGroup.getTotalPrice());
                System.out.println("총갯수 : " + orderGroup.getTotalQuantity());

                System.out.println("------------주문 상세---------------");
                orderGroup.getOrderDetailList().stream().forEach(orderDetail -> {
                    System.out.println("파트너사 이름 : " + orderDetail.getItem().getPartner().getName());
                    System.out.println("카테고리 : " + orderDetail.getItem().getPartner().getCategory().getType());

                    System.out.println("주문 상품 : " + orderDetail.getItem().getName());
                    System.out.println("고객센터 번호 : " + orderDetail.getItem().getPartner().getCallCenter());

                    System.out.println("주문상태 : " + orderDetail.getStatus());
                    System.out.println("주문도착예정일 : " + orderDetail.getArrivalDate());
                });
            });
        }

        Assertions.assertNotNull(user);

        user
                .setEmail("")
                .setPhoneNumber("")
                .setStatus(null);

        User user2 = new User().setAccount("").setEmail("");
    }

    @Test
    public void update() {
        Optional<User> user = userRepository.findById(3L);

        user.ifPresent(selectUser -> {
            selectUser.setAccount("PPPP");
            selectUser.setUpdatedAt(LocalDateTime.now());
            selectUser.setUpdatedBy("update method()");

            userRepository.save(selectUser);
        });
    }

    @Test
    @Transactional
    public void delete() {
        Optional<User> user = userRepository.findById(1L);
        Assertions.assertTrue(user.isPresent());

        user.ifPresent(selectUser -> {
            userRepository.delete(selectUser);
        });

        Optional<User> deletedUser = userRepository.findById(1L);
        Assertions.assertFalse(deletedUser.isPresent());
    }
}
