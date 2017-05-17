package edu.hm.fachklassen;

/*
*ShareIt
* Date: 12.04.2017
* java version 1.8.0_73
* Windows 10 (1607)
* Intel(R) Core(TM) i5-4200U CPU @ 1.60GHz 2.30 GHz
* @Author Sebastian Heunke, heunke@hm.edu
*/

/**
 * Representation of a copy of a medium.
 */
public class Copy {
    private final Medium medium;
    private final String owner;

    /**
     * Creates a copy of given medium with given owner.
     *
     * @param owner  Owner of the new copy
     * @param medium Medium to be copied
     */
    Copy(String owner, Medium medium) {
        if (owner == null || medium == null) {
            throw new IllegalArgumentException("Arguments cant be null!");
        }
        this.owner = owner;
        this.medium = medium;
    }

    /**
     * Returns the medium the copy represents.
     *
     * @return Medium.
     */
    public Medium getMedium() {
        return medium;
    }

    /**
     * Owner of this copy.
     *
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
}
