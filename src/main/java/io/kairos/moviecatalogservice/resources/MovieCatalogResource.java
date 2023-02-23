package io.kairos.moviecatalogservice.resources;

import io.kairos.moviecatalogservice.models.CatalogItem;
import io.kairos.moviecatalogservice.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/catalog")
public class MovieCatalogResource {

    @Autowired
    CatalogService catalogService;

    @GetMapping(value = "/{userId}")
    public Flux<CatalogItem> getCatalogs(@PathVariable(value = "userId") String userId) {
        return catalogService.getCatalogItems(userId);
    }
}
