package io.kairos.moviecatalogservice.services;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.kairos.moviecatalogservice.config.LoadBalancerConfig;
import io.kairos.moviecatalogservice.models.CatalogItem;
import io.kairos.moviecatalogservice.models.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

@Service
@LoadBalancerClient(name = "movie-info", configuration = LoadBalancerConfig.class)
public class MovieInfo {

    @Autowired
    WebClient.Builder webclientBuilder;
    public static final String MOVIES_INFO_SERVICE_URL = "http://movie-info-service/movies/api/info";

    @CircuitBreaker(name = "getMoviesInfoFromMovieService", fallbackMethod = "getFallbackCatalog")
    @RateLimiter(name = "getMoviesInfoFromMovieService")
    @Bulkhead(name = "getMoviesInfoFromMovieService", fallbackMethod = "getFallbackCatalog")
    @Retry(name = "getMoviesInfoFromMovieService")
    @TimeLimiter(name = "getMoviesInfoFromMovieService")
    public Flux<Movie> getMoviesInfoFromMovieService() {
        return webclientBuilder.build().get().uri(URI.create(MOVIES_INFO_SERVICE_URL))
                .retrieve()
                .bodyToFlux(Movie.class);
    }

    public Flux<Movie> getFallbackCatalog() {
        return Flux.just(new Movie(0, "House of the Dragon", "", ""));
    }
}
