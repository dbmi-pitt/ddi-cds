package edu.pitt.dbmi.ohdsiv5.db;



/**
 * Location
 */

public class Location  {


    // Fields    
    Long locationId;
    String locationSourceVal;

    // Constructors
    /** default constructor */
    public Location() {
    }
   
            public Long getLocationId() {
	return locationId;
    }
    /**
     *      */
    public void setLocationId(Long locationId) {
	this.locationId = locationId;
    }

        public String getLocationSourceVal() {
	return locationSourceVal;
    }
    /**
     *      */
    public void setLocationSourceVal(String sourceVal) {
	this.locationSourceVal = sourceVal;
    }








}
