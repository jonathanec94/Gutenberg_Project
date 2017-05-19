/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DtoEntity;

/**
 *
 * @author nikolai
 */
public class DtoCity {
    private Integer id;
    private String name;
    private double latitude;
    private double longitude;

    public DtoCity(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }



    public double getLatitude() {
        return latitude;
    }



    public double getLongitude() {
        return longitude;
    }


    public Integer getId() {
        return id;
    }

  
    
}
