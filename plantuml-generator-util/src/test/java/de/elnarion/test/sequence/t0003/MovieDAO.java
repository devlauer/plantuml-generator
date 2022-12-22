package de.elnarion.test.sequence.t0003;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class MovieDAO {

	@PersistenceContext
	private
	EntityManager entityManager;
	

	public void saveMovie(Movie paramMovie) {
	    EntityManager em = getEntityManager();
	    
	    em.getTransaction().begin();

	    em.persist(paramMovie);
	    em.getTransaction().commit();
	}

	public Movie getMovie(Long movieId) {
	    EntityManager em = getEntityManager();
	    Movie movie = em.find(Movie.class, Long.valueOf(movieId));
	    em.detach(movie);
	    return movie;
	}	

	public void changeMovieLanguage(Long paramId, String paramLanguage) {
	    EntityManager em = getEntityManager();
	    Movie movie = getMovie(paramId);
	    em.detach(movie);
	    movie.setLanguage(paramLanguage);
	    em.getTransaction().begin();
	    em.merge(movie);
	    em.getTransaction().commit();
	}
	
	public List<?> queryForMovies() {
	    EntityManager em = getEntityManager();
	    List<?> movies = em.createQuery("SELECT movie from Movie movie where movie.language = ?1")
	      .setParameter(1, "English")
	      .getResultList();
	    return movies;
	}
	
	public void removeMovie() {
	    EntityManager em = getEntityManager();
	    em.getTransaction().begin();
	    Movie movie = em.find(Movie.class, Long.valueOf(1L));
	    em.remove(movie);
	    em.getTransaction().commit();
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}


	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	
}
