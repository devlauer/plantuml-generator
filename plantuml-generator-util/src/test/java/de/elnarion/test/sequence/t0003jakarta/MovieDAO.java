package de.elnarion.test.sequence.t0003jakarta;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class MovieDAO {

	@PersistenceContext
	private
	EntityManager entityManager;
	

	public void saveMovie(de.elnarion.test.sequence.t0003jakarta.Movie paramMovie) {
	    EntityManager em = getEntityManager();
	    
	    em.getTransaction().begin();

	    em.persist(paramMovie);
	    em.getTransaction().commit();
	}

	public de.elnarion.test.sequence.t0003jakarta.Movie getMovie(Long movieId) {
	    EntityManager em = getEntityManager();
	    de.elnarion.test.sequence.t0003jakarta.Movie movie = em.find(de.elnarion.test.sequence.t0003jakarta.Movie.class, Long.valueOf(movieId));
	    em.detach(movie);
	    return movie;
	}	

	public void changeMovieLanguage(Long paramId, String paramLanguage) {
	    EntityManager em = getEntityManager();
	    de.elnarion.test.sequence.t0003jakarta.Movie movie = getMovie(paramId);
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
	    de.elnarion.test.sequence.t0003jakarta.Movie movie = em.find(de.elnarion.test.sequence.t0003jakarta.Movie.class, Long.valueOf(1L));
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
