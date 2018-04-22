package com.demo.ms.tinyurlservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.ms.tinyurlservice.db.UrlDAO;
import com.demo.ms.tinyurlservice.model.Url;
import com.demo.ms.tinyurlservice.resources.TinyUrlResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class App extends Application<AppConfiguration> {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	private final HibernateBundle<AppConfiguration> hibernateBundle = new HibernateBundle<AppConfiguration>(
			Url.class) {
		public DataSourceFactory getDataSourceFactory(
				AppConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.addBundle(hibernateBundle);
		bootstrap.addBundle(new AssetsBundle("/views", "/app", "index.html", "static"));
	}

	public void run(AppConfiguration configuration, Environment environment) {
		final UrlDAO dao = new UrlDAO(hibernateBundle.getSessionFactory());
		environment.jersey().register(
				new TinyUrlResource(environment.getValidator(), dao));

	}

	public static void main(String[] args) throws Exception {
		new App().run(args);
	}

}
