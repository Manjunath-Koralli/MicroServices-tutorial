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
import org.springframework.web.reactive.function.client.WebClient;

import com.moviecatalog.model.CatalogItem;
import com.moviecatalog.model.Movie;
import com.moviecatalog.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	//with REST TEMPLATE
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		
		//get all rated movie IDs
		/*List<Rating> ratings = Arrays.asList(
			new Rating("1234","4"),
			new Rating("5678","4")
		);*/
		//Making a API call instead of hard coding ratings
		UserRating ratings = restTemplate
				.getForObject("http://localhost:8082/ratingsdata/users/" + userId, 
						UserRating.class);
		
		
		return ratings.getUserRating().stream().map(rating -> {
			//for each movie ID, call movie info service and get details
			Movie movie = restTemplate
					.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), 
							Movie.class);
			
			//Put them all together
			return new CatalogItem(movie.getName(),"Test", rating.getRating());
		}).collect(Collectors.toList());
		
		
	}
	
	//with WEB CLIENT
	/*@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		//get all rated movie IDs
		
		
		//for each movie ID, call movie info service and get details
		
		return ratings.stream().map(rating -> {
			Movie movie = webClientBuilder.build()
				.get()
				.uri("http://localhost:8081/movies/" + rating.getMovieId())
				.retrieve() //go fetch from above uri
				.bodyToMono(Movie.class) //convert to instance of Movie class //Mono is promise-asynchronous
				.block(); //gives a block of movie back
				
			return new CatalogItem(movie.getName(),"Test", rating.getRating());
		}).collect(Collectors.toList());
		//Put them all together
		
	
	}*/
			
			
			
}

/*without rest template and web client
 * 
 * return Collections.singletonList(
				(CatalogItem) new CatalogItem("3 idiots","Test",4)
		);
*/
