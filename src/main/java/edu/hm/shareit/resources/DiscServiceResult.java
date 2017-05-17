package edu.hm.shareit.resources;

import javax.ws.rs.core.Response;

/**
 * Enum describing a DiscService Result.
 * Created by oliver on 12.04.17.
 */
public enum DiscServiceResult {
    NoDiscWithBarcodeFound("No Disc with given Barcode found", Response.Status.BAD_REQUEST),
    MissingParamDirector("Missing Director", Response.Status.BAD_REQUEST),
    MissingParamTitle("Missing Title", Response.Status.BAD_REQUEST),
    MissingParamBarcode("Missing Barcode", Response.Status.BAD_REQUEST),
    NegativeFSK("FSK must not be negative", Response.Status.BAD_REQUEST),
    BarcodeAlreadyExists("Barcode is already existing", Response.Status.BAD_REQUEST),
    BarcodeInvalidLength("A barcode must contain 13 digits", Response.Status.BAD_REQUEST),
    BarcodeJSONAndURIDontMatch("Barcode in JSON and URI don't match", Response.Status.BAD_REQUEST),
    AllRight("Accepted", Response.Status.OK);

    private final String reason;
    private final Response.Status status;

    /**
     * Creates a new DiscServiceResult.
     * @param reason Reason this result happened.
     * @param status Return Status this response should return
     */
    DiscServiceResult(String reason, Response.Status status) {
        this.reason = reason;
        this.status = status;
    }

    /**
     * Reason this Result has returned.
     * @return String explanation
     */
    public String getReason() {
        return reason;
    }

    /**
     * Getter for the current Response status.
     * @return Status of the result
     */
    public Response.Status getStatus() {
        return status;
    }
}
