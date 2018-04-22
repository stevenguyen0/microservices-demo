package com.demo.ms.customerservice.db;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.demo.ms.customerservice.model.Customer;

@Stateless
public class CustomerRepository {
	@PersistenceContext(unitName = "customerPU")
	private EntityManager entityManager;
	
	public Customer create(Customer customer) {
	    this.entityManager.persist(customer);
	    return customer;
	}
	
	public Optional<Customer> findById(String customerId) {
		Customer c = entityManager.find(Customer.class, customerId);
		return Optional.ofNullable(c);
	}
}
