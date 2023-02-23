package io.kairos.moviecatalogservice.services;

import io.kairos.moviecatalogservice.models.CatalogItem;
import reactor.core.publisher.Flux;

public interface CatalogService {

    Flux<CatalogItem> getCatalogItems(String userId);
}
