package com.mydeveloperplanet.myjooqplanet;

import static com.mydeveloperplanet.myjooqplanet.jooq.tables.Customer.CUSTOMER;

import java.util.List;

import com.mydeveloperplanet.myjooqplanet.jooq.tables.records.CustomerRecord;

import com.mydeveloperplanet.myjooqplanet.model.Customer;
import org.jooq.DSLContext;
import org.jooq.Records;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

    private final DSLContext create;

    CustomerRepository(DSLContext create) {
        this.create = create;
    }

    public CustomerRecord addCustomer(final Customer customer) {
        return create.insertInto(CUSTOMER, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.COUNTRY)
                .values(customer.getFirstName(), customer.getLastName(), customer.getCountry())
                .returning(CUSTOMER.ID, CUSTOMER.FIRST_NAME, CUSTOMER.LAST_NAME, CUSTOMER.COUNTRY)
                .fetchOne(Records.mapping(CustomerRecord::new));
    }

    public CustomerRecord getCustomer(int customerId) {
        return create
                .selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetchOne(Records.mapping(CustomerRecord::new));
    }

    public List<CustomerRecord> getAllCustomers() {
        return create
                .selectFrom(CUSTOMER)
                .fetch(Records.mapping(CustomerRecord::new));
    }

}
