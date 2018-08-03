package de.theamychan.schematic.manager;

import io.gomint.math.Location;
import lombok.Getter;

import java.util.List;

public class Backup {

    @Getter
    private Location location;
    @Getter
    private List<String> list;

    public Backup( Location location, List<String> list ) {
        this.location = location;
        this.list = list;
    }
}
