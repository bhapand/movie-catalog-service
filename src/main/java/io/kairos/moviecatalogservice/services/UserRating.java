package io.kairos.moviecatalogservice.services;


import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.kairos.moviecatalogservice.config.LoadBalancerConfig;
import io.kairos.moviecatalogservice.dto.MovieDto;
import io.kairos.moviecatalogservice.models.Movie;
import io.kairos.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@LoadBalancerClient(name = "ratings-data", configuration = LoadBalancerConfig.class)
public class UserRating {

    public static final String RATING_SERVICE_URL = "http://ratings-data-service/ratings/api/rating/rating-info";
    @Autowired
    WebClient.Builder webclientBuilder;

    @CircuitBreaker(name = "getRatings", fallbackMethod = "getFallbackRatings")
    @RateLimiter(name = "getRatings")
    @Bulkhead(name = "getRatings", fallbackMethod = "getFallbackRatings")
    @Retry(name = "getRatings")
    @TimeLimiter(name = "getRatings")
    public Mono<Rating> getRatings(Movie movie) {
        return webclientBuilder.build().post().uri(RATING_SERVICE_URL)
                .body(BodyInserters.fromValue(new MovieDto(movie.getName())))
                .retrieve().bodyToMono(Rating.class);
    }

    public Mono<Rating> getFallbackRatings(Movie movie) {
        return Mono.just(new Rating(movie.getName(), "0"));
    }
}
