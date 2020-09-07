package com.ocube.siterequest;

public class RequestedItemsModel {
    public String item;
    public String qty;
    public String units;
    public String needed;
    public String rqId;


    public RequestedItemsModel(String item, String qty, String units, String needed, String rqId) {
        this.item = item;
        this.qty = qty;
        this.units = units;
        this.needed = needed;
        this.rqId = rqId;
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

    public String getRqId() {
        return rqId;
    }
}

