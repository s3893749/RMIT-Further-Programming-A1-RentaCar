package com.jackgharris.cosc2288.mycar.models;

import com.jackgharris.cosc2288.mycar.core.Ascii;
import com.jackgharris.cosc2288.mycar.core.Model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Reservation extends Model {

    private String dateRangeError;
    private int duration;
    private boolean hasDiscount;

    private Car car;

    public Reservation(HashMap<String, String> data) {
        super(data);
        this.hasDiscount = false;
    }

    public Reservation(){
        super(new HashMap<String, String>());
        this.hasDiscount = false;
    }

    public void setPickUpDate(String date){
        this.data.put("pickup_date",date);
    }

    public void setDropOffDate(String date){
        this.data.put("dropoff_date",date);
    }

    public String getPickUpDate(){
        return this.data.get("pickup_date");
    }

    public void setCar(Car car){
        this.car = car;
    }

    public Car getCar(){
        return this.car;
    }

    public int getRentalCost(){
        return this.car.getRentalPricePerDay() * this.duration;
    }

    public int getDiscountPrice(){

        float discount = this.car.getDiscount();

        if(discount != 0){
            this.hasDiscount = true;
        }

        return (int) (this.getRentalCost()-(this.getRentalCost()*(discount/100)));
    }

    public int getInsuranceCost(){
        return this.car.getInsurancePerDay() * this.duration;
    }

    public int getServiceFee(){
        return this.car.getServiceFee();
    }

    public int getTotalCost(){

        int rentalCost;
        if(this.hasDiscount){
            rentalCost = this.getDiscountPrice();
        }else{
            rentalCost = this.getRentalCost();
        }

        return rentalCost+this.getInsuranceCost()+this.getServiceFee();
    }
    public String getDropOffDate(){
        return this.data.get("dropoff_date");
    }

    public boolean validateDateRange(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        boolean outcome = true;

        try{
            Date pickUp = simpleDateFormat.parse(this.data.get("pickup_date"));
            Date dropOff = simpleDateFormat.parse(this.data.get("dropoff_date"));

            long timeDiff = Math.abs(dropOff.getTime() -  pickUp.getTime());

            this.duration = (int) TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

            if(dropOff.compareTo(pickUp) < 0){
                this.dateRangeError = "drop off date cannot be before pickup date";
                outcome = false;
            }

            if(dropOff.compareTo(pickUp) == 0){
                outcome = false;
                this.dateRangeError = "drop off and pickup date cannot be the same date";
            }

        } catch (ParseException e) {
            System.out.println(Ascii.red+"RESERVATION MODEL ERROR: Invalid Date Parsed"+Ascii.black);
        }

        return outcome;

    }

    public String getDateRangeError(){
        return this.dateRangeError;
    }

    public int getDuration(){
        return this.duration;
    }


}