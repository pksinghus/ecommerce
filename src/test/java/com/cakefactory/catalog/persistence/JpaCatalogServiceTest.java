package com.cakefactory.catalog.persistence;

import java.math.BigDecimal;

import com.cakefactory.catalog.Item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class JpaCatalogServiceTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    ItemRepository itemRepository;

    private JpaCatalogService jpaCatalogService;

    @BeforeEach
    void setup() {
        this.jpaCatalogService = new JpaCatalogService(this.itemRepository);
    }

    @Test
    @DisplayName("returns data from the database")
    void returnsDataFromDatabase() {
        String expectedTitle = "Victoria Sponge";
        saveTestItem(expectedTitle, BigDecimal.valueOf(5.55));

        Iterable<Item> items = jpaCatalogService.getItems();

        org.assertj.core.api.Assertions.assertThat(items).anyMatch(item -> expectedTitle.equals(item.getTitle()));
    }

    private void saveTestItem(String title, BigDecimal price) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.sku = "test-item-1";
        itemEntity.title = title;
        itemEntity.price = price;

        testEntityManager.persistAndFlush(itemEntity);
    }

}