/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

/**
 *
 * @author mathias
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
 
@Path("/books-by-city")
public class GetBooksByCity {
 
    @GET
    @Path("/{param}")
    public Response getMsg(@PathParam("param") String message) {
        String output = "Test " + message + "!";
        return Response.status(200).entity(output).build();
    }
}