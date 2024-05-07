package com.test.phoneshope.controller;

import com.test.phoneshope.exception.ResourceNotFoundException;
import com.test.phoneshope.models.Phone;
import com.test.phoneshope.models.Purchase;
import com.test.phoneshope.repository.PhoneRepository;
import com.test.phoneshope.repository.PurchaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/v1/")
public class ShopContrtoller {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @GetMapping("/shop/stock")
    public List<Phone> getGoods() {
        return phoneRepository.findAll();
    }

    @GetMapping("/shop/purchase")
    public List<Purchase> getPurchase() {
        return purchaseRepository.findAll();
    }

    @PostMapping("/shop/purchase")
    public void makePurchase() throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));
        if(phone.getQuantity()<1){
            throw new ResourceNotFoundException("Phone quantity is less than 1 :: " + phone.getQuantity());
        }
        else {
            phone.setQuantity(phone.getQuantity()-1);
            phoneRepository.save(phone);
            Purchase purchase = new Purchase();
            purchase.setDate(new Timestamp(System.currentTimeMillis()));
            purchase.setPhone(phone);
            purchase.setClientName(name);
            purchaseRepository.save(purchase);
        }

    }

    @PostMapping("shop/stock")
    public void replenishStock(Map<Phone,Integer> phones) throws ResourceNotFoundException {
        for (Phone phone : phones.keySet()) {
            phoneRepository.findById(phone.getId()).isPresent()
            if () {

            }
        }
    }

    @PutMapping("shop/stock")
    public void updatePrices(Map<Integer,Integer> prices) throws ResourceNotFoundException {

        for (int phoneId : prices.keySet()) {
            Phone phone = phoneRepository.findById(phoneId)
                    .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));
            phone.setPrice(prices.get(phoneId));
            phoneRepository.save(phone);
        }
    }

    @DeleteMapping("shop/stock")
    public void clearStock(){
        phoneRepository.deleteAll();
    }
}
