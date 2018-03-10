package com.pluralsight.bookstore.rest;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.repository.BookRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/books")
@Api("Books")
public class BookEndpoint {


    @GET
    @Path("/{id: \\d+}")
    @Produces(APPLICATION_JSON)
    @ApiOperation(value = "Returns a book given an identifier", response = Book.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Book found"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book = bookRepository.find(id);
        if (book == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(book).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createURI = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();

        return Response.created(createURI).build();
    }

    @DELETE
    @Path("/{id: \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {

        bookRepository.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();
        if(books.size() == 0){
            return Response.noContent().build();
        }
        return Response.ok(books).build();
    }

    @GET
    @Path("/count")
    @Produces(APPLICATION_JSON)
    public Response countBooks() {

        Long count = bookRepository.countAll();
        if(count == 0){
            return Response.noContent().build();
        }
        return Response.ok(count).build();
    }

    @Inject
    private BookRepository bookRepository;


}
