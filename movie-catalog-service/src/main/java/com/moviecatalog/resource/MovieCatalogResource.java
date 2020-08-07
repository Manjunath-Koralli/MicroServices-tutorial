package com.moviecatalog.resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.moviecatalog.model.CatalogItem;
import com.moviecatalog.model.Movie;
import com.moviecatalog.model.Rating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		/*return Collections.singletonList(
				(CatalogItem) new CatalogItem("3 idiots","Test",4)
		);*/
		//get all rated movie IDs
		
		List<Rating> ratings = Arrays.asList(
			new Rating("1234","4"),
			new Rating("5678","4")
		);
		//for each movie ID, call movie info service and get details
		
		return ratings.stream().map(rating -> {
			Movie movie = restTemplate
					.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), 
							Movie.class);
			return new CatalogItem(movie.getName(),"Test", rating.getRating());
		}).collect(Collectors.toList());
		//Put them all together
		
	}
}
