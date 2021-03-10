package com.binarydiff.binaryDiff.model;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "left_item")
public class LeftItem extends Item{
    public LeftItem(Integer id, JSONObject value) {
        super(id, value);
    }
    public LeftItem(){
        super();
    }
}
