package trrp_3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trrp_3.CustomerProtos;
import trrp_3.service.CustomerRepository;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping(value = "/customers")
    public ResponseEntity<?> create(@RequestBody CustomerProtos.Customer customer) {
        customerRepository.create(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/customers")
    public ResponseEntity<List<CustomerProtos.Customer>> read() {
        final List<CustomerProtos.Customer> customers = customerRepository.readAll();

        return customers != null &&  !customers.isEmpty()
                ? new ResponseEntity<>(customers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/customers/{id}")
    public ResponseEntity<CustomerProtos.Customer> read(@PathVariable(name = "id") int id) {
        final CustomerProtos.Customer customer = customerRepository.read(id);
        return customer != null
                ? new ResponseEntity<>(customer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/customers/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody CustomerProtos.Customer customer) {
        final boolean updated = customerRepository.update(customer, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/customers/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = customerRepository.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}