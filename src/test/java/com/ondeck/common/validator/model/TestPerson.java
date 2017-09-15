package com.ondeck.common.validator.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * A POJO to test validation framework
 */
public class TestPerson {
    private String firstName, lastName;
    private boolean superHero = false;
    private Address address;
    private List<Car> cars = new ArrayList<>();
    private BigDecimal loanBalance;
    private Integer capacityToPay;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public boolean isSuperHero() {
        return superHero;
    }

    public void setSuperHero(boolean superHero) {
        this.superHero = superHero;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Integer getCapacityToPay() {
        return capacityToPay;
    }

    public void setCapacityToPay(Integer capacityToPay) {
        this.capacityToPay = capacityToPay;
    }
}
