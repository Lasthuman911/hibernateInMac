package ch11.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ch11.hibernate.domain.Spitter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class HibernateSpitterRepository implements SpitterRepository {

	private SessionFactory sessionFactory;

	/**
	 * 注入sessionFactory
	 */
	@Inject
	public HibernateSpitterRepository(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 从SessionFactory中获取当前Session
	 */
	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public long count() {
		return findAll().size();
	}

	public Spitter save(Spitter spitter) {
		Serializable id = currentSession().save(spitter);  //使用当前session
		return new Spitter((String) id,
				spitter.getUsername(),
				spitter.getPassword());
	}

	public Spitter findOne(String id) {
		return (Spitter) currentSession().get(Spitter.class, id); 
	}

	public Spitter findByUsername(String username) {		
		return (Spitter) currentSession() 
				.createCriteria(Spitter.class) 
				.add(Restrictions.eq("username", username))
				.list().get(0);
	}

	public List<Spitter> findAll() {
		return (List<Spitter>) currentSession() 
				.createCriteria(Spitter.class).list();
	}
	
}
