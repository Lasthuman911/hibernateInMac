package spittr.data;


import java.util.List;

public interface SpitterRepository {
/*
  Spitter save(Spitter spitter);

  Spitter findByUsername(String username);*/

  List<Spittle> findSpittles(long max, int count);

}
