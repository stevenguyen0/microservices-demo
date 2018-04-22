package com.demo.ms.tinyurlservice.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.demo.ms.tinyurlservice.business.StringShortener;
import com.demo.ms.tinyurlservice.db.UrlDAO;
import com.demo.ms.tinyurlservice.model.Url;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/api/v1/tinyurls")
@Produces(MediaType.APPLICATION_JSON)
public class TinyUrlResource {
	//In actual production code, should include server side validation as well, should not trust client validation
	private Validator validator;

	private final UrlDAO urlDAO;

	public TinyUrlResource(Validator validator, UrlDAO urlDAO) {
		this.validator = validator;
		this.urlDAO = urlDAO;
	}

	@POST
	@UnitOfWork
	public Response createShortUrl(String urlStr) {
		Url url = urlDAO.create(urlStr);
		url.setShortName(StringShortener.encode(url.getId()));
		urlDAO.save(url);
		return Response.status(Response.Status.CREATED).entity(url).build();
	}

	@GET
	@Timed
	@Path("/{id}")
	@UnitOfWork
	public Response findById(@PathParam("id") long id) {
		Optional<Url> result = urlDAO.findById(id);
		if (!result.isPresent()) {
			emptyResponse();
		}
		return Response.ok(result.get()).build();
	}
	
	@GET
	@Timed
	@UnitOfWork
	public Response findAll() {
		List<Url> result = urlDAO.findAll();
		return Response.ok(result).build();
	}
	@GET
	@Timed
	@Path("/realurl/{shortName}")
	@UnitOfWork
	public Response gotoActualUrl(@PathParam("shortName") String shortName) {
		long id = StringShortener.decode(shortName);
		Optional<Url> result = urlDAO.findById(id);
		if (!result.isPresent()) {
			emptyResponse();
		}
		return redirectResponse(result.get().getActualUrl());
		
	}
	private Response emptyResponse() {
		return Response.status(Response.Status.NOT_FOUND).entity(Optional.empty()).build();
	}
	private Response redirectResponse(String urlLocation) {
		URI location;
		try {
			location = new URI(urlLocation);
		} catch (URISyntaxException e) {
			return emptyResponse();
		}
		return Response.temporaryRedirect(location).build();
	}
}