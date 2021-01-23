package trrp_3.service;

import trrp_3.CustomerProtos;

import java.util.List;

public interface CustomerRepository {
    CustomerProtos.Customer read(int id);
    List<CustomerProtos.Customer> readAll();
    void create(CustomerProtos.Customer customer);
    boolean update(CustomerProtos.Customer customer, int id);
    boolean delete(int id);
}
