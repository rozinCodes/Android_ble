package com.waltonbd.smartscale.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tbl_units")
public class UnitsTable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String UnitType;
    private String Name;

    public UnitsTable() {
    }

    public UnitsTable(String unitType, String name) {
        UnitType = unitType;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String unitType) {
        UnitType = unitType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
