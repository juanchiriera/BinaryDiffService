package com.binarydiff.binaryDiff.model;

import com.binarydiff.binaryDiff.converter.JSONObjectConverter;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Item {
    @Id
    private Integer id;
    @Column
    @Convert(converter = JSONObjectConverter.class)
    private JSONObject value;

    public Item(){}

    public Item (Integer id, JSONObject value){
        this.id = id;
        this.value = value;
    }

    public JSONObject getValue() {
        return value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setValue(JSONObject value) {
        this.value = value;
    }
}

