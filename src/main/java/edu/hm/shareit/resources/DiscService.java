package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;

/**
 * Created by oliver on 12.04.17.
 */
public interface DiscService {
    /**
     * Adds a disc to the Service.
     * @param disc to added
     * @return DiscServiceResult with information about the success
     */
    DiscServiceResult addDisc(Disc disc);

    /**
     * Try to get a disc from the service with the given barcode.
     * @param barcode to search for
     * @return disk if found, or null
     */
    Disc getDisc(String barcode);

    /**
     * Returns all the currently saved discs in an array.
     * @return all the discs
     */
    Disc[] getDiscs();

    /**
     * Update the instance of a saved disc by barcode.
     * @param disc to be updated
     * @return DiscServiecResult with information about the success
     */
    DiscServiceResult updateDisc(Disc disc);
}
