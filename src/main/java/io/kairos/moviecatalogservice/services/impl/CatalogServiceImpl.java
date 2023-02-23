package io.kairos.moviecatalogservice.services.impl;

import io.kairos.moviecatalogservice.models.CatalogItem;
import io.kairos.moviecatalogservice.models.Movie;
import io.kairos.moviecatalogservice.repository.CatalogRepository;
import io.kairos.moviecatalogservice.services.CatalogService;
import io.kairos.moviecatalogservice.services.MovieInfo;
import io.kairos.moviecatalogservice.services.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    MovieInfo movieInfo;

    @Autowired
    UserRating userRating;

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Flux<CatalogItem> getCatalogItems(String userId) {
        return getCatalogItemForAllMovies().flatMap(this::getCatalogItem);
    }

    private Mono<CatalogItem> getCatalogItem(CatalogItem entity) {

        return catalogRepository.findByName(entity.getName()).switchIfEmpty(Mono.just(entity)).flatMap(catalogRepository::save);
    }

    private Flux<CatalogItem> getCatalogItemForAllMovies() {
        Flux<Movie> movieFlux = movieInfo.getMoviesInfoFromMovieService();
        return movieFlux.flatMap(movie -> userRating.getRatings(movie)
                .flatMap(movieRating -> catalogRepository.findByName(movie.getName())
                        .switchIfEmpty(Mono.just(new CatalogItem(UUID.randomUUID().toString(), movie.getName(), movie.getDescription(), movieRating.getRating())))));
    }

}