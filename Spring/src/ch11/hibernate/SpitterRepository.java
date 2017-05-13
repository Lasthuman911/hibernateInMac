package ch11.hibernate;

import ch11.hibernate.domain.Spitter;

import java.util.List;


/**
 * Repository interface with operations for {@link Spitter} persistence.
 * @author habuma
 */
public interface SpitterRepository {

  long count();

  Spitter save(Spitter spitter);

  Spitter findOne(String id);

  Spitter findByUsername(String username);

  List<Spitter> findAll();

}
