package com.test.phoneshope.controller;

import com.test.phoneshope.models.Phone;
import com.test.phoneshope.models.Purchase;
import com.test.phoneshope.repository.PhoneRepository;
import com.test.phoneshope.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.*;

import static com.test.phoneshope.controller.PhoneControllerTest.asJsonString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShopContrtoller.class)
@DisplayName("ТестированиеShopController ")
class ShopContrtollerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PhoneRepository phoneRepository;

    @MockBean
    PurchaseRepository purchaseRepository;

    Phone phone1 = new Phone();
    Phone phone2 = new Phone();
    Purchase purchase1 = new Purchase();
    Purchase purchase2 = new Purchase();

    @BeforeEach
    void setUp() {
        phone1.setQuantity(1);
        phone1.setPhone_id(1);
        phone1.setPrice(1);
        phone1.setName("test1");

        phone2.setQuantity(2);
        phone2.setPhone_id(2);
        phone2.setPrice(2);
        phone2.setName("test2");

        purchase1.setId(1);
        purchase1.setPhone_id(1);
        purchase1.setClientName("test1");
        purchase1.setDate(new Timestamp(System.currentTimeMillis()));

        purchase2.setId(2);
        purchase2.setPhone_id(2);
        purchase2.setClientName("test2");
        purchase2.setDate(new Timestamp(System.currentTimeMillis()));
    }

    @DisplayName("Проверка метода getGoods")
    @Test
    void getGoodsTest() throws Exception {
        //arrange
        List<Phone> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);

        //act and assert
        when(phoneRepository.findAll()).thenReturn(phones);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/shop/stock")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка метода getPurchase")
    @Test
    void getPurchaseTest() throws Exception {
        //arrange
        List<Purchase> purchases = new ArrayList<>();
        purchases.add(purchase1);
        purchases.add(purchase2);

        //act and assert
        when(purchaseRepository.findAll()).thenReturn(purchases);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/shop/purchase")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка метода makePurchase")
    @Test
    void makePurchaseTest() throws Exception {
        //arrange
        when(phoneRepository.existsById(Mockito.any())).thenReturn(true);
        when(phoneRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(phone1));
        when(phoneRepository.save(Mockito.any())).thenReturn(phone1);
        when(purchaseRepository.save(Mockito.any())).thenReturn(purchase1);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/shop/purchase")
                        .content(asJsonString(purchase1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @DisplayName("Проверка метода replenishStock")
    @Test
    void replenishStockTest() throws Exception {
        //arrange
        List<Phone> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);
        when(phoneRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(phone1));
        when(purchaseRepository.save(Mockito.any())).thenReturn(purchase1);
        when(phoneRepository.existsById(Mockito.any())).thenReturn(true);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/shop/stock")
                        .content(asJsonString(phones))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка метода updatePrices")
    @Test
    void updatePricesTest() throws Exception {
        //arrange
        Map<Integer, Integer> prices = new HashMap<>();
        prices.put(1, 1);
        prices.put(2, 2);
        when(phoneRepository.findById(1)).thenReturn(Optional.ofNullable(phone1));
        when(phoneRepository.findById(2)).thenReturn(Optional.ofNullable(phone2));
        when(phoneRepository.save(phone1)).thenReturn(phone1);
        when(phoneRepository.save(phone2)).thenReturn(phone2);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/shop/stock")
                        .content(asJsonString(prices))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка метода clearStock")
    @Test
    void clearStockTest() throws Exception {
        //arrange
        doNothing().when(phoneRepository).deleteAll(Mockito.any());

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/shop/stock", 1))
                .andExpect(status().isOk());
    }
}