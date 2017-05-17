package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by oliver on 12.04.17.
 */
@Path("/media/discs")
public class DiscResource {
    private final DiscService discService;

    /**
     * Creates a new DiskResource to handle requests.
     */
    public DiscResource() {
        this(new DiscServiceImpl());
    }

    /**
     * Ctor with added parameters to change background service used.
     * @param service to be used instead of the default.
     */
    DiscResource(DiscService service) {
        discService = service;
    }

    /**
     * Rest request creating a new Disc in the service.
     * @param disc to be created
     * @return response containing information about the success
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDisc(Disc disc) {
        final DiscServiceResult discServiceResult = discService.addDisc(disc);

        return Response.status(discServiceResult.getStatus()).build();
    }

    /**
     * Rest request to get all currently saved discs.
     * @return Response containg all discs in an array.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscs() {
        return Response.status(Response.Status.OK).entity(discService.getDiscs()).build();
    }

    /**
     * Rest request to get a disc from the service.
     * @param barcode of the disc required
     * @return disc if found
     */
    @GET
    @Path("/{barcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDisc(@PathParam("barcode") String barcode) {
        final Disc disc = discService.getDisc(barcode);
        final Response.Status responseStatus = disc == null ? Response.Status.BAD_REQUEST : Response.Status.OK;

        return Response.status(responseStatus).entity(disc).build();
    }

    /**
     * Rest request to update an existing disc.
     * @param barcode of the disc to be updated
     * @param disc with the new updated values
     * @return Response with Status about success
     */
    @PUT
    @Path("/{barcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDisc(@PathParam("barcode") String barcode, Disc disc) {
        DiscServiceResult discServiceResult;

        if (disc.getBarcode().isEmpty()) {
            disc = new Disc(disc.getTitle(), barcode, disc.getFsk(), disc.getDirector());
        }

        if (disc.getBarcode() != barcode) {
            discServiceResult = DiscServiceResult.BarcodeJSONAndURIDontMatch;
        } else {
            discServiceResult = discService.updateDisc(disc);
        }

        return Response.status(discServiceResult.getStatus()).build();
    }


}
