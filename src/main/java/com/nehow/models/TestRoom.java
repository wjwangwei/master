package com.nehow.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by andrew on 4/26/2017.
 */
@Setter
@Getter
public class TestRoom {
    private String roomIndex;
    private int rooms;
    private String roomType;
    private String roomRateCode;
    private int adults;
    private int children;
    private List<Integer> ages;
    private int infants;

    public TestRoom() {
        this.roomIndex = "";
        this.rooms = 1;
        this.roomType = "SINGLE";
        this.adults = 1;
        this.children = 0;
        this.ages = null;
        this.infants = 0;
    }

    public TestRoom(String roomIndex, String roomType, int adults, int children, List<Integer> ages) {
        this.roomIndex = roomIndex;
        this.rooms = 1;
        this.roomType = roomType;
        this.adults = adults;
        this.children = children;
        this.ages = ages;
        this.infants = 0;
    }
}
