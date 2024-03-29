package com.example.study.sample;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Category;
import com.example.study.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Arrays;
import java.util.List;

@Slf4j
public class CategorySample extends StudyApplicationTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void createSample() {
        List<String> category = Arrays.asList("COMPUTER", "CLOTHING", "MULTI_SHOP", "INTERIOR", "FOOD", "SPORTS", "SHOPPING_MALL", "DUTY_FREE", "BEAUTY");
        List<String> title = Arrays.asList("컴퓨터-전자제품", "의류", "멀티샵", "인테리어", "음식", "스포츠", "쇼핑몰", "면세점", "화장");

        for (int i = 0; i < category.size(); i++) {
            Category create = Category.builder()
                                    .type(category.get(i))
                                    .title(title.get(i))
                                    .build();

            categoryRepository.save(create);
        }
    }
}
