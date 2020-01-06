package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class ItemRepositoryTest extends StudyApplicationTests {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void create() {
//        Item item = new Item();
//        item.setName("노트북");
//        item.setPrice(5000);
//        item.setContent("삼성 노트북이야");
//
//        Item newItem = itemRepository.save(item);
//
//        Assertions.assertNotNull(newItem);

        Item item = new Item();
        item.setStatus("UNREGISTERED");
        item.setName("삼성 노트북");
        item.setTitle("삼성 노트북 a100");
        item.setContent("2019년형 최신 노트북입니다");
        item.setPrice(900000);
        item.setBrandName("삼성");
        item.setRegisteredAt(LocalDateTime.now());
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("Partner01");
//        item.setPartnerId(1L);

        Item newItem = itemRepository.save(item);
        Assertions.assertNotNull(newItem);
    }

    @Test
    public void read() {
        long id = 1L;

        Optional<Item> item = itemRepository.findById(id);

        Assertions.assertNotNull(item);
//        item.ifPresent(System.out::println);
    }
}
