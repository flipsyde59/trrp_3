package trrp_3.service;

import org.springframework.stereotype.Service;
import trrp_3.CustomerProtos;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CustomerRepositoryImpl implements CustomerRepository {


// Хранилище клиентов
    private CustomerProtos.Customer Customer(int id, String f, String l, Collection<String> emails) {
    Collection<CustomerProtos.Customer.EmailAddress> emailAddresses =
            emails.stream().map(e -> CustomerProtos.Customer.EmailAddress.newBuilder()
                    .setType(CustomerProtos.Customer.EmailType.PROFESSIONAL)
                    .setEmail(e).build())
                    .collect(Collectors.toList());

    return CustomerProtos.Customer.newBuilder()
            .setFirstName(f)
            .setLastName(l)
            .setId(id)
            .addAllEmail(emailAddresses)
            .build();
}
    private static final Map<Integer, CustomerProtos.Customer> CUSTOMER_REPOSITORY_MAP = new HashMap<>();
    CustomerRepositoryImpl() {
        Arrays.asList(
                Customer(1, "Chris", "Richardson", Arrays.asList("crichardson@email.com")),
                Customer(2, "Josh", "Long", Arrays.asList("jlong@email.com")),
                Customer(3, "Matt", "Stine", Arrays.asList("mstine@email.com")),
                Customer(4, "Russ", "Miles", Arrays.asList("rmiles@email.com"))
        ).forEach(c -> CUSTOMER_REPOSITORY_MAP.put(c.getId(), c));
    }

    private static final AtomicInteger CUSTOMER_ID_HOLDER = new AtomicInteger();

    @Override
    public void create(CustomerProtos.Customer customer) {
        final int customerId = CUSTOMER_ID_HOLDER.incrementAndGet();
        CUSTOMER_REPOSITORY_MAP.put(customerId, customer);
    }

    @Override
    public List<CustomerProtos.Customer> readAll() {
        return new ArrayList<>(CUSTOMER_REPOSITORY_MAP.values());
    }

    @Override
    public CustomerProtos.Customer read(int id) {
        return CUSTOMER_REPOSITORY_MAP.get(id);
    }

    @Override
    public boolean update(CustomerProtos.Customer customer, int id) {
        if (CUSTOMER_REPOSITORY_MAP.containsKey(id)) {
            CUSTOMER_REPOSITORY_MAP.put(id, customer);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        return CUSTOMER_REPOSITORY_MAP.remove(id) != null;
    }
}