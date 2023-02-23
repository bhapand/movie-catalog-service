package io.kairos.moviecatalogservice.repository;

import io.kairos.moviecatalogservice.models.CatalogItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CatalogRepository extends ReactiveMongoRepository<CatalogItem, String> {

    Mono<CatalogItem> findByName(String name);
    Mono<Boolean> existsByName(String name);
}
