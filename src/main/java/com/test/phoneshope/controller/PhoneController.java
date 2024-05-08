package com.test.phoneshope.controller;

import com.test.phoneshope.exception.ResourceNotFoundException;
import com.test.phoneshope.models.Phone;
import com.test.phoneshope.repository.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class PhoneController {
    @Autowired
    private PhoneRepository phoneRepository;

    /**
     * Метод вызова всех телефонов из таблицы
     */
    @GetMapping("/phones")
    public List<Phone> getAllPhones() {
        return phoneRepository.findAll();
    }

    /**
     * Метод вызова самой маленькой цены среди всех
     *
     * @return возвращает цену
     * @throws ResourceNotFoundException - ошибка, вызываемая, если телефоны отсутсвуют на складе
     */
    @GetMapping("/phones/bestprice")
    public int getPhoneByPrice() throws ResourceNotFoundException {
        List<Integer> prices = phoneRepository.findAll().stream().map(Phone::getPrice).toList();
        int bestPrice = prices.stream()
                .mapToInt(v -> v)
                .min().orElseThrow(() -> new ResourceNotFoundException("Shop is Empty"));
        log.info(prices.toString());
        return bestPrice;

    }

    /**
     * Метод вызова по ключу телефона из таблицы
     */
    @GetMapping("/phones/{id}")
    public ResponseEntity<Phone> getPhoneById(@PathVariable(value = "id") Integer phoneId)
            throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("phone not found for this id :: " + phoneId));
        log.info(phone.toString());
        return ResponseEntity.ok().body(phone);
    }

    /**
     * Метод создания телефона в таблице
     */
    @PostMapping("/phones")
    public Phone createPhone(@Validated @RequestBody Phone phone) {
        log.info(phone.toString());
        return phoneRepository.save(phone);
    }

    /**
     * Метод обновления данных телефона из таблицы
     */
    @PutMapping("/phones/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable(value = "id") Integer phoneId,
                                             @Validated @RequestBody Phone phoneDetails) throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));
        log.info(phone.toString());
        phone.setPhone_id(phoneDetails.getPhone_id());
        phone.setName(phoneDetails.getName());
        phone.setPrice(phoneDetails.getPrice());
        phone.setQuantity(phoneDetails.getQuantity());
        log.info(phone.toString());
        final Phone updatePhone = phoneRepository.save(phone);
        return ResponseEntity.ok(updatePhone);
    }

    /**
     * Метод удаления телефона из таблицы
     */
    @DeleteMapping("/phones/{id}")
    public Map<String, Boolean> deletePhone(@PathVariable(value = "id") Integer phoneId)
            throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));

        phoneRepository.delete(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}



