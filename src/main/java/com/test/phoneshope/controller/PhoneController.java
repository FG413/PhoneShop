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

        @GetMapping("/phones")
        public List<Phone> getAllPhones() {
            return phoneRepository.findAll();
        }

    @GetMapping("/phones/bestprice")
    public int getPhoneByPrice() throws Exception {
            List<Integer> prices = phoneRepository.findAll().stream().map(Phone::getPrice).toList();
            int bestPrice = prices.stream()
                    .mapToInt(v -> v)
                    .min().orElseThrow(() -> new Exception("Shop is Empty"));
            log.info(prices.toString());
        return bestPrice;

    }

    @GetMapping("/phones/{id}")
    public ResponseEntity<Phone> getPhoneById(@PathVariable(value = "id") Long phoneId)
            throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("phone not found for this id :: " + phoneId));
        return ResponseEntity.ok().body(phone);
    }

    @PostMapping("/phones")
    public Phone createPhone(@Validated @RequestBody Phone phone) {
        return phoneRepository.save(phone);
    }

    @PutMapping("/phones/{id}")
    public ResponseEntity<Phone> updatePhone(@PathVariable(value = "id") Long phoneId,
                                                   @Validated @RequestBody Phone phoneDetails) throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));

        phone.setId(phoneDetails.getId());
        phone.setName(phoneDetails.getName());
        phone.setPrice(phoneDetails.getPrice());
        final Phone updatePhone = phoneRepository.save(phone);
        return ResponseEntity.ok(updatePhone);
    }


    @DeleteMapping("/phones/{id}")
    public Map<String, Boolean> deletePhone(@PathVariable(value = "id") Long phoneId)
            throws ResourceNotFoundException {
        Phone phone = phoneRepository.findById(phoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found for this id :: " + phoneId));

        phoneRepository.delete(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}



