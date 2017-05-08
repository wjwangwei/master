package com.nehow.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by andrew on 4/24/2017.
 */
@Getter
@Setter
public class Room {
    private String roomIndex;
    private String roomName;
    private String extraBedDesc;
    private List<Passenger> paxList;
}
