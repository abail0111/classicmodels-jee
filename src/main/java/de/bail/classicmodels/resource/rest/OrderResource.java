package de.bail.classicmodels.resource.rest;

import de.bail.classicmodels.model.dto.OrderDto;
import de.bail.classicmodels.model.enities.Order;
import de.bail.classicmodels.model.mapper.OrderMapper;
import de.bail.classicmodels.service.OrderService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource extends CrudResource<Order, OrderDto, Integer, OrderService, OrderMapper> {

    public OrderResource() {
        super("/order/");
    }

    @Override
    public void linkDTO(OrderDto dto) {
        if (dto != null && dto.getCustomer() != null && dto.getCustomer().getId() != 0) {
            Link link = getLinkService().BuildLinkRelated("/customer/" + dto.getCustomer().getId(), MediaType.APPLICATION_JSON);
            dto.getCustomer().setLink(link);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "create new Order")
    @Override
    public Response create(OrderDto entity) {
        return super.create(entity);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read Order")
    @Override
    public Response read(@PathParam("id") Integer id) {
        return super.read(id);
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "read all Orders")
    public Response readAll(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("100") int limit,
            @QueryParam("status") String status) {

        List<Order> orders;
        int count;
        if (status != null && !status.isEmpty()) {
            orders = getService().filterByStatus(status, offset, limit);
            count = getService().countByFilter(status);
        } else {
            orders = getService().getAllEntitiesPagination(offset, limit);
            count = getService().count();
        }
        List<OrderDto> dto = getMapper().toResourceList(orders);
        dto.forEach(this::linkDTO);
        return Response.ok(dto).header("x-total-count", count).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "update Order")
    @Override
    public Response update(@PathParam("id") Integer id, OrderDto entity) {
        return super.update(id, entity);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "delete Order")
    @Override
    public Response delete(@PathParam("id") Integer id) {
        return super.delete(id);
    }

}
