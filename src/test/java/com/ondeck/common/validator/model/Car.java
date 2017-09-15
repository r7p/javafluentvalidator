package com.ondeck.common.validator.model;

/**
 * Created by rpatel on 11/17/16.
 */
public class Car {
    private String make, model;

    public Car(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


}
