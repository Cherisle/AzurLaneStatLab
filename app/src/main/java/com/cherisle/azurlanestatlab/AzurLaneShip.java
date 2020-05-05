package com.cherisle.azurlanestatlab;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AzurLaneShip
{
    private String name;
    private String chibi_src;
    private String display_association;
    private String display_class;
    private String display_health;
    private String display_armor;
    private String display_reload;
    private String display_luck;
    private String display_firepower;
    private String display_torpedo;
    private String display_evasion;
    private String display_speed;
    private String display_antiair;
    private String display_aviation;
    private String display_oilconsumption;
    private String display_accuracy;
    private String display_antisubmarine;
    private String current_stat_variant;
    private ArrayList<String> stat_variants;
    private AzurLaneDbHelper db_helper;

    public AzurLaneShip(String n, AzurLaneDbHelper aldbh)
    {
        name = n;
        chibi_src = aldbh.getShipChibiSrc(n);
        db_helper = aldbh;
        display_association = "";
        display_class = "";
        display_health = "";
        display_armor = "";
        display_reload = "";
        display_luck = "";
        display_firepower = "";
        display_torpedo = "";
        display_evasion = "";
        display_speed = "";
        display_antiair = "";
        display_aviation = "";
        display_oilconsumption = "";
        display_accuracy = "";
        display_antisubmarine = "";
        current_stat_variant = "";
    }

    public void setCurrentStatVariant(String variant)
    {
        if(variant.equals("default"))
        {
            current_stat_variant = "100";
        }
        else
        {
            current_stat_variant = variant;
        }
    }

    public void setShipStatVariants(ArrayList<String> variants)
    {
        for(int ii=0;ii<variants.size();ii++)
        {
            Log.i("ALSL","variants: " + variants.get(ii));
        }
        stat_variants = variants;
    }
    
    public void setDisplayValues(String association, String ship_class, int health, int armor, int reload, int luck, int firepower,
                                 int torpedo, int evasion, int speed, int antiair, int aviation,
                                 int oilconsumption, int accuracy, int antisubmarine)
    {
        display_association = association;
        display_health = "" + health;
        display_armor = "" + armor;
        display_reload = "" + reload;
        display_luck = "" + luck;
        display_firepower = "" + firepower;
        display_torpedo = "" + torpedo;
        display_evasion = "" + evasion;
        display_speed = "" + speed;
        display_antiair = "" + antiair;
        display_aviation = "" + aviation;
        display_oilconsumption = "" + oilconsumption;
        display_accuracy = "" + accuracy;
        display_antisubmarine = "" + antisubmarine;

        switch(ship_class)
        {
            case "Aircraft Carrier":
                display_class = "CV";
                break;
            case "Battleship":
                display_class = "BB";
                break;
            case "Battlecruiser":
                display_class = "BC";
                break;
            case "Destroyer":
                display_class = "DD";
                break;
            case "Heavy Cruiser":
                display_class = "CA";
                break;
            case "Light Aircraft Carrier":
                display_class = "CVL";
                break;
            case "Light Cruiser":
                display_class = "CL";
                break;
            case "Monitor":
                display_class = "BM";
                break;
            default:
                display_class = "??";
                break;
        }
    }

    public String getChibiSrc()
    {
        return chibi_src;
    }

    public String getName()
    {
        return name;
    }

    public String getAssociation() { return display_association; }

    public String getClassification() { return display_class; }

    public String getCurrentStatVariant() { return current_stat_variant; }

    public AzurLaneDbHelper getDbHelper() { return db_helper; }

    public String getHealth()
    {
        return display_health;
    }

    public String getArmor()
    {
        return display_armor;
    }

    public String getReload()
    {
        return display_reload;
    }

    public String getLuck()
    {
        return display_luck;
    }

    public String getFirepower()
    {
        return display_firepower;
    }

    public String getTorpedo()
    {
        return display_torpedo;
    }

    public String getEvasion()
    {
        return display_evasion;
    }

    public String getSpeed()
    {
        return display_speed;
    }

    public String getAntiair()
    {
        return display_antiair;
    }

    public String getAviation()
    {
        return display_aviation;
    }

    public String getOilConsumption()
    {
        return display_oilconsumption;
    }

    public String getAccuracy()
    {
        return display_accuracy;
    }

    public String getAntisubmarine()
    {
        return display_antisubmarine;
    }

    public ArrayList<String> getStatVariants()
    {
        return stat_variants;
    }
}
