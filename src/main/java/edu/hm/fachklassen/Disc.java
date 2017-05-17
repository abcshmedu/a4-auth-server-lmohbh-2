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
 * Disc Medium.
 */
public class Disc extends Medium {
    private final String barcode;
    private final String director;
    private final int fsk;

    /**
     * Default ctor for discs. Uses empty fields.
     */
    public Disc() {
        this("", "", 0, "");
    }

    /**
     * Creates a new Disc object with the parametrs.
     *
     * @param titel    Titel of the disc
     * @param barcode  Unique barcode
     * @param fsk      fsk this disc is rated for
     * @param director of the disc
     */
    public Disc(String titel, String barcode, int fsk, String director) {
        super(titel);
        if (titel == null || barcode == null || director == null) {
            throw new IllegalArgumentException("Arguments cant be null");
        }
        this.barcode = barcode;
        this.director = director;
        this.fsk = fsk;
    }

    /**
     * Unique barcode of this disc.
     *
     * @return barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Director who made this film.
     *
     * @return director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Fsk this Disc is rated for.
     *
     * @return fsk
     */
    public int getFsk() {
        return fsk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Disc disc = (Disc) o;

        if (getFsk() != disc.getFsk()) {
            return false;
        }
        if (!getBarcode().equals(disc.getBarcode())) {
            return false;
        }
        return getDirector().equals(disc.getDirector());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getBarcode().hashCode();
        result = 31 * result + getDirector().hashCode();
        result = 31 * result + getFsk();
        return result;
    }
}
