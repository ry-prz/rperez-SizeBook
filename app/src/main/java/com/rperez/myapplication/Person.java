package com.rperez.myapplication;

import java.util.Date;

/**
 * Created by ryanp on 2017-02-05.
 */
public class Person {
    public String name;
    public String date;
    public float neck;
    public float bust;
    public float chest;
    public float waist;
    public float hip;
    public float inseam;
    public String comment;

    public Person(String name) {
        this.name = name;
        date = "";
        neck =  0f;
        bust = 0f;
        chest = 0f;
        waist= 0f;
        inseam = 0f;
        comment = "";

    }

    @Override
    public String toString() {

        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getNeck() {
        return neck;
    }

    public void setNeck(float neck) {
        this.neck = neck;
    }

    public float getBust() {
        return bust;
    }

    public void setBust(float bust) {
        this.bust = bust;
    }

    public float getChest() {
        return chest;
    }

    public void setChest(float chest) {
        this.chest = chest;
    }

    public float getWaist() {
        return waist;
    }

    public void setWaist(float waist) {
        this.waist = waist;
    }

    public float getHip() {
        return hip;
    }

    public void setHip(float hip) {
        this.hip = hip;
    }

    public float getInseam() {
        return inseam;
    }

    public void setInseam(float inseam) {
        this.inseam = inseam;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
