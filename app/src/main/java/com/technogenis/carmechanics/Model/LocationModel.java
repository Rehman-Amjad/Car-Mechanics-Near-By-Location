package com.technogenis.carmechanics.Model;

public class LocationModel
{
    String ownerUserUID;
    String marqueeAddKey;
    String marqueeLatitude;
    String marqueeLongitude;

    public LocationModel(String ownerUserUID, String marqueeAddKey, String marqueeLatitude, String marqueeLongitude) {
        this.ownerUserUID = ownerUserUID;
        this.marqueeAddKey = marqueeAddKey;
        this.marqueeLatitude = marqueeLatitude;
        this.marqueeLongitude = marqueeLongitude;
    }

    public LocationModel() {
    }

    public String getOwnerUserUID() {
        return ownerUserUID;
    }

    public void setOwnerUserUID(String ownerUserUID) {
        this.ownerUserUID = ownerUserUID;
    }

    public String getMarqueeAddKey() {
        return marqueeAddKey;
    }

    public void setMarqueeAddKey(String marqueeAddKey) {
        this.marqueeAddKey = marqueeAddKey;
    }

    public String getMarqueeLatitude() {
        return marqueeLatitude;
    }

    public void setMarqueeLatitude(String marqueeLatitude) {
        this.marqueeLatitude = marqueeLatitude;
    }

    public String getMarqueeLongitude() {
        return marqueeLongitude;
    }

    public void setMarqueeLongitude(String marqueeLongitude) {
        this.marqueeLongitude = marqueeLongitude;
    }
}
