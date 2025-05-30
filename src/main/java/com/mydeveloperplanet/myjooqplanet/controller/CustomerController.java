package com.mydeveloperplanet.myjooqplanet.controller;

import java.util.List;

import com.mydeveloperplanet.myjooqplanet.jooq.tables.records.CustomerRecord;
import com.mydeveloperplanet.myjooqplanet.model.Customer;
import com.mydeveloperplanet.myjooqplanet.model.CustomerFullData;

import com.mydeveloperplanet.myjooqplanet.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {

    public final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerFullData> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity
                .ok()
                .body(repoToApi(customerRepository.addCustomer(customer)));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerFullData>> getCustomers() {
        return ResponseEntity
                .ok(customerRepository.getAllCustomers().stream().map(this::repoToApi).toList());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerFullData> getCustomer(@PathVariable Long customerId) {
        var customer = customerRepository.getCustomer(customerId.intValue());
        return ResponseEntity
                .ok(repoToApi(customer));
    }

    private CustomerFullData repoToApi(CustomerRecord customer) {
        CustomerFullData cfd = new CustomerFullData();
        cfd.setCustomerId(customer.getId().longValue());
        cfd.setFirstName(customer.getFirstName());
        cfd.setLastName(customer.getLastName());
        cfd.setCountry(customer.getCountry());
        return cfd;
    }

}
