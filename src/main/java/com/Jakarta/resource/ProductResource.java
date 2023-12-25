package com.Jakarta.resource;

import com.Jakarta.Warehouse;
import com.Jakarta.model.Product;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/products")
public class ProductResource {

    @Inject
    private Warehouse warehouse;

    private static final Logger logger = LoggerFactory.getLogger(ProductResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(@Valid Product product) {
        warehouse.addProduct(product);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@QueryParam("page") int page, @QueryParam("pageSize") int pageSize) {
        logger.info("Request received to get all products with pagination. Page: {}, PageSize: {}", page, pageSize);

        int totalProducts = warehouse.getAllProducts().size();
        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalProducts);

        if (start < totalProducts) {
            List<Product> paginatedProducts = warehouse.getAllProducts().subList(start, end);
            return Response.ok(paginatedProducts).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No products found for the requested page.").build();
        }
    }

    @GET
    @Path("/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("productId") String productId) {
        Product product = warehouse.getProductById(productId);
        if (product != null) {
            return Response.ok(product).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/category/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCategory(@PathParam("category") String category) {
        List<Product> products = warehouse.getProductsByCategory(category);
        if (products != null && !products.isEmpty()) {
            return Response.ok(products).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
