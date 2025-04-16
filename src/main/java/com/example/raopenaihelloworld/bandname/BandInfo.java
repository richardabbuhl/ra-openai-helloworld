package com.example.raopenaihelloworld.bandname;

import lombok.Data;

@Data
public class BandInfo {

    private String bandName;
    private String genre;
    private String yearFormed;
    private String country;
    private String members;
    private String albums;
    private String awards;

    @Override
    public String toString() {
        return "BandInfo{" +
                "bandName='" + bandName + '\'' +
                ", genre='" + genre + '\'' +
                ", yearFormed='" + yearFormed + '\'' +
                ", country='" + country + '\'' +
                ", members='" + members + '\'' +
                ", albums='" + albums + '\'' +
                ", awards='" + awards + '\'' +
                '}';
    }

}
