package com.demo.ms.tinyurlservice.db;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.demo.ms.tinyurlservice.model.Url;

import io.dropwizard.hibernate.AbstractDAO;

public class UrlDAO extends AbstractDAO<Url>{

	public UrlDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public Url create(String urlStr) {
        return persist(new Url(urlStr));
    }
	public Url save(Url url) {
		currentSession().save(url);
        return persist(url);
    }
	public Optional<Url> findById(Long id) {
        return Optional.ofNullable(get(id));
    }
	public List<com.demo.ms.tinyurlservice.model.Url> findAll() {
        return list(namedQuery("com.demo.ms.tinyurlservice.model.Url.findAll"));
    }

}
