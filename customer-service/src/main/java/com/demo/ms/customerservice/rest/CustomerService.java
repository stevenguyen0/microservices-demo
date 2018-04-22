package com.demo.ms.customerservice.rest;


import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.demo.ms.customerservice.db.CustomerRepository;
import com.demo.ms.customerservice.model.Customer;


@Path("/api/v1/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerService {
	
	@Inject
	CustomerRepository customerRepository;

	@POST
	@Path("")
	@Consumes("application/json")
	@Transactional
	public Response register(Customer customer) {
		Customer c = customerRepository.create(customer);
		//Send email with tiny url:
		//
		return Response.status(Response.Status.CREATED).entity(c).build();
	}

	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") String id) {
		Optional<Customer> result = customerRepository.findById(id);
		if (!result.isPresent()) {
			emptyResponse();
		}
		return Response.ok(result.get()).build();
	}
	
	private Response emptyResponse() {
		return Response.status(Response.Status.NOT_FOUND).entity(Optional.empty()).build();
	}
}