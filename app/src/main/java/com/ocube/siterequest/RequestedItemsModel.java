package com.ocube.siterequest;

public class RequestedItemsModel {
    public String item;
    public String qty;
    public String units;
    public String needed;


    public RequestedItemsModel(String item, String qty, String units, String needed) {
        this.item = item;
        this.qty = qty;
        this.units = units;
        this.needed = needed;
    }

    public String getItem() {
        return item;
    }

    public String getQty() {
        return qty;
    }

    public String getUnits() {
        return units;
    }

    public String getNeeded() {
        return needed;
    }
}

