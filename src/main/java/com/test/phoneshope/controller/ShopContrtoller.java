package com.test.phoneshope.controller;

import com.test.phoneshope.exception.ResourceNotFoundException;
import com.test.phoneshope.models.Phone;
import com.test.phoneshope.models.Purchase;
import com.test.phoneshope.repository.PhoneRepository;
import com.test.phoneshope.repository.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс контроллера магазина.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class ShopContrtoller {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    /**
     * Метод показывающий все телефоны на складе
     *
     * @return возвращает список всех телефонов на складе
     */
    @GetMapping("/shop/stock")
    public List<Phone> getGoods() {
        return phoneRepository.findAll();
    }

    /**
     * Метод показывающий все прошедшие покупки
     *
     * @return возвращает список всех покупок
     */
    @GetMapping("/shop/purchase")
    public List<Purchase> getPurchase() {
        return purchaseRepository.findAll();
    }

    /**
     * Метод совершения покупки
     *
     * @param purchase - покупка
     * @return возвращает совершенную покупку
     * @throws ResourceNotFoundException - ошибка, вызываемая, если телефон отсутсвует на складе
     */
    @PostMapping("/shop/purchase")
    public Purchase makePurchase(@Validated @RequestBody Purchase purchase) throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(purchase.getPhone_id())
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + purchase.getPhone_id()));
        if (phone.getQuantity() < 1) {
            throw new ResourceNotFoundException("Phone quantity is less than 1 :: " + phone.getQuantity());
        } else {
            phone.setQuantity(phone.getQuantity() - 1);
            phoneRepository.save(phone);
            log.info(purchase.toString());
            return purchaseRepository.save(purchase);
        }

    }

    /**
     * Метод пополнения склада телефонами
     *
     * @param phones - список новых телефонов
     * @throws ResourceNotFoundException - прокидывается, из-за жалоб IDE на get() без isPresent()
     */
    @PostMapping("shop/stock")
    public void replenishStock(@RequestBody List<Phone> phones) throws ResourceNotFoundException {
        for (Phone phone : phones) {
            if (!phoneRepository.existsById(phone.getPhone_id())) {
                phoneRepository.save(phone);
            } else {
                phone.setQuantity(phone.getQuantity() + phoneRepository.findById(phone.getPhone_id()).orElseThrow(() -> new ResourceNotFoundException("phone not found for this id :: " + phone.getPhone_id())).getQuantity());
                phoneRepository.save(phone);
            }
            log.info(phone.toString());
        }
    }

    /**
     * Метод обновления цен
     *
     * @param prices- обновленные цены
     * @throws ResourceNotFoundException - ошибка, вызываемая, если телефон отсутсвует на складе
     */
    @PutMapping("shop/stock")
    public void updatePrices(@RequestBody Map<Integer, Integer> prices) throws ResourceNotFoundException {

        for (int phoneId : prices.keySet()) {
            Phone phone = phoneRepository.findById(phoneId)
                    .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));
            phone.setPrice(prices.get(phoneId));
            phoneRepository.save(phone);
            log.info(phone.toString());
        }
    }

    /**
     * Метод очистки склада
     */
    @DeleteMapping("shop/stock")
    public Map<String, Boolean> clearStock() {
        phoneRepository.deleteAll();
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
