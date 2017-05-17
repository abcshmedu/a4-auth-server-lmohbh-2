package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by oliver on 24.04.17.
 */
public class DiscServiceImpl implements DiscService {
    static final Set<Disc> DISCS = new HashSet<>();
    private static final int CODE_LENGTH = 13;

    @Override
    public DiscServiceResult addDisc(Disc disc) {
        final DiscServiceResult discServiceResult;

        if (disc.getBarcode().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamBarcode;
        } else if (disc.getDirector().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamDirector;
        } else if (disc.getTitle().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamTitle;
        } else if (disc.getFsk() < 0) {
            discServiceResult = DiscServiceResult.NegativeFSK;
        } else if (getDisc(disc.getBarcode()) != null) {
            discServiceResult = DiscServiceResult.BarcodeAlreadyExists;
        } else if (disc.getBarcode().length() != CODE_LENGTH) {
            discServiceResult = DiscServiceResult.BarcodeInvalidLength;
        } else {
            discServiceResult = DiscServiceResult.AllRight;
            DISCS.add(disc);
        }

        return discServiceResult;
    }

    @Override
    public Disc getDisc(String barcode) {
        Optional<Disc> discList = DISCS.stream()
                .filter(disc -> disc.getBarcode().equals(barcode))
                .findFirst();

        return discList.isPresent() ? discList.get() : null;
    }

    @Override
    public Disc[] getDiscs() {
        return DISCS.toArray(new Disc[]{});
    }

    @Override
    public DiscServiceResult updateDisc(Disc disc) {
        final DiscServiceResult discServiceResult;
        Optional<Disc> toDelete = DISCS.stream().filter(other -> other.getBarcode().equals(disc.getBarcode())).findFirst();

        if (toDelete.isPresent()) {
            DISCS.remove(toDelete.get());
            DISCS.add(disc);
            discServiceResult = DiscServiceResult.AllRight;
        } else {
            discServiceResult = DiscServiceResult.NoDiscWithBarcodeFound;
        }

        return discServiceResult;
    }
}
