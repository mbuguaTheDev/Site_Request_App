package com.ocube.siterequest;

/**
 * Created by Opencube on 28/09/2020.
 */
public class ImagesModel {
    public String imageString;
    public String itemName;
    public String itemQty;
    public String itemUnits;


    public ImagesModel(String imageString, String itemName, String itemQty, String itemUnits) {
        this.imageString = imageString;
        this.itemName = itemName;
        this.itemQty = itemQty;
        this.itemUnits = itemUnits;

    }

    public String getImageString() {
        return imageString;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemQty() {
        return itemQty;
    }

    public String getItemUnits() {
        return itemUnits;
    }


}
