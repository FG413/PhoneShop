package com.test.phoneshope.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.phoneshope.models.Phone;
import com.test.phoneshope.models.Purchase;
import com.test.phoneshope.repository.PhoneRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhoneController.class)
@DisplayName("Тестирование PhoneController")
class PhoneControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PhoneRepository phoneRepository;

    Phone phone1 = new Phone();
    Phone phone2 = new Phone();


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


    }

    @DisplayName("Проверка метода getAllPhones")
    @Test
    void getAllPhonesTest() throws Exception {
        //arrange
        List<Phone> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);

        //act and assert
        when(phoneRepository.findAll()).thenReturn(phones);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/phones")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("Проверка метода getPhoneByPrice")
    @Test
    void getPhoneByPriceTest() throws Exception {
        //arrange
        List<Phone> phones = new ArrayList<>();
        phones.add(phone1);
        phones.add(phone2);
        when(phoneRepository.findAll()).thenReturn(phones);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/phones/bestprice")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(1)));
        ;
    }

    @DisplayName("Проверка метода getPhoneById")
    @Test
    void getPhoneByIdTest() throws Exception {
        //arrange
        when(phoneRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(phone1));

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/phones/0")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_id").isNotEmpty());
    }

    @DisplayName("Проверка метода createPhone")
    @Test
    void createPhoneTest() throws Exception {
        //arrange
        when(phoneRepository.save(Mockito.any())).thenReturn(phone1);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/phones")
                        .content(asJsonString(phone1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone_id").exists());
    }

    @DisplayName("Проверка метода updatePhone")
    @Test
    void updatePhoneTest() throws Exception {
        //arrange
        when(phoneRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(phone1));
        when(phoneRepository.save(Mockito.any())).thenReturn(phone2);

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/phones/0", 2)
                        .content(asJsonString(phone2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(2));

    }

    @DisplayName("Проверка метода deletePhone")
    @Test
    void deletePhoneTest() throws Exception {
        //arrange
        when(phoneRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(phone1));
        doNothing().when(phoneRepository).delete(Mockito.any());

        //act and assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/phones/0", 1))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


