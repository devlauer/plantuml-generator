package de.elnarion.test.sequence.t0003jakarta;

import de.elnarion.test.sequence.t0003.Movie;
import de.elnarion.test.sequence.t0003.MovieDAO;

public class MovieService {
	
	MovieDAO movieDAO;
	
	public void doSomeBusiness() {
		Movie movie = movieDAO.getMovie((long) 111);
		movie.setMovieName("asdfasdfasdf");
		movieDAO.saveMovie(movie);
	}

}
