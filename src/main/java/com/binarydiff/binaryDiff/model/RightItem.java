package com.binarydiff.binaryDiff.model;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="right_item")
public class RightItem extends Item{
    public RightItem(Integer id, JSONObject value) {
        super(id, value);
    }
    public RightItem(){ super(); }
}
