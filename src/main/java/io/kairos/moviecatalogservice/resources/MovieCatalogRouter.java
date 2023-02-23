package io.kairos.moviecatalogservice.resources;

import io.kairos.moviecatalogservice.models.CatalogItem;
import io.kairos.moviecatalogservice.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class MovieCatalogRouter {

    @Autowired
    CatalogService catalogService;

    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(RequestPredicates.GET("/catalog_router/{userId}"),
                request -> ServerResponse.ok().body(catalogService.getCatalogItems(request.pathVariable("userId")), CatalogItem.class));
    }
}
