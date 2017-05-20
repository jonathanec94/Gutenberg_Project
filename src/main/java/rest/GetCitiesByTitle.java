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
import DbInterface.Facade;
import DtoEntity.DtoCity;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mongo.MongoFacade;
import org.json.JSONObject;
 
@Path("/cities-by-title")
public class GetCitiesByTitle {
 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksByCity(String request) {

        Gson gson = new Gson();
        MongoFacade mf = new MongoFacade();
        Facade facade = new Facade(mf);

        JSONObject body = new JSONObject(request);
        String title = body.getString("title");
        
        List<DtoCity> cities = facade.getCitiesByTitle(title);

        String output = gson.toJson(cities);
        return Response.status(200).entity(output).build();
    }
}