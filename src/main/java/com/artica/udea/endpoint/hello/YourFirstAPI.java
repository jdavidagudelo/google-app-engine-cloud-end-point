package com.artica.udea.endpoint.hello;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.jdo.PersistenceManager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/** 
 * An endpoint class we are exposing 
 * */
@Api(name = "greetingApi", version = "v1", title = "Greetings API", 
description = "Simple API for greetings using DataStore API of Google", 
namespace = @ApiNamespace(ownerDomain = "helloworld.example.com", 
ownerName = "helloworld.example.com", 
packagePath = ""))
public class YourFirstAPI {
	PersistenceManager pm = PMF.get().getPersistenceManager();

	/**
	 * Permite crear una nueva instancia de la clase persistente usando JDO.
	 * @return instancia creada en el DataStore de Google.
	 */
	@ApiMethod(name = "createPersistentClass", httpMethod = HttpMethod.POST)
	public PersistentClass createPersistentClass(@Named("name") String name) {
		pm = PMF.get().getPersistenceManager();
		PersistentClass persistent = new PersistentClass(name);
		try {
			pm.makePersistent(persistent);
		} finally {
			pm.close();
		}
		return persistent;
	}
	/**
	 * Permite obtener la lista de los objetos mapeados usando JDO.
	 * @return
	 */
	@ApiMethod(name = "getPersistentClasses",  httpMethod = HttpMethod.GET, path = "getPersistentClasses")
	public List<PersistentClass> getPersistenClasses() {
		pm = PMF.get().getPersistenceManager();
		javax.jdo.Query query = pm.newQuery(PersistentClass.class);
		try {
			@SuppressWarnings("unchecked")
			List<PersistentClass> result = (List<PersistentClass>) query
					.execute();
			return result;
		} finally {
			query.closeAll();
		}
	}

	private DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();

	/** A simple endpoint method that takes a name and says Hi back */
	@ApiMethod(name = "sayHi", httpMethod = HttpMethod.POST)
	public MyBean sayHi(@Named("name") String name) {
		MyBean response = new MyBean();
		response.setData("Hi, " + name);
		Entity greeting = new Entity("Greeting");
		greeting.setProperty("name", name);
		datastore.put(greeting);
		return response;
	}
	/**
	 * Metodo simple para obtener lista de saludos almacenados en el 
	 * Datastore de Google.
	 * @return
	 */
	@ApiMethod(name = "getGreetings", path = "getGreetings", httpMethod = HttpMethod.GET)
	public List<MyBean> getGreetings() {

		List<MyBean> beans = new ArrayList<MyBean>();
		Query q = new Query("Greeting");
		PreparedQuery pq = datastore.prepare(q);
		for (Entity result : pq.asIterable()) {
			String greeting = (String) result.getProperty("name");
			MyBean bean = new MyBean();
			bean.setData(greeting);
			beans.add(bean);
		}
		return beans;
	}

}