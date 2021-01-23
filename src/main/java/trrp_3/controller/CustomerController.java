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

//    @RequestMapping("/customers/{id}")
//    CustomerProtos.Customer customer(@PathVariable Integer id) {
//        return this.customerRepository.findById(id);
//    }
//
//    @RequestMapping("/customers/new")
//    String addCustomer(@RequestBody CustomerProtos.Customer customer) {
//        customerRepository.save(customer);
//        return "Saved";
//    }

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping(value = "/clients")
    public ResponseEntity<?> create(@RequestBody CustomerProtos.Customer customer) {
        customerRepository.create(customer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/clients")
    public ResponseEntity<List<CustomerProtos.Customer>> read() {
        final List<CustomerProtos.Customer> clients = customerRepository.readAll();

        return clients != null &&  !clients.isEmpty()
                ? new ResponseEntity<>(clients, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/clients/{id}")
    public ResponseEntity<CustomerProtos.Customer> read(@PathVariable(name = "id") int id) {
        final CustomerProtos.Customer client = customerRepository.read(id);

        return client != null
                ? new ResponseEntity<>(client, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/clients/{id}")
    public ResponseEntity<?> update(@PathVariable(name = "id") int id, @RequestBody CustomerProtos.Customer client) {
        final boolean updated = customerRepository.update(client, id);

        return updated
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping(value = "/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") int id) {
        final boolean deleted = customerRepository.delete(id);

        return deleted
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}