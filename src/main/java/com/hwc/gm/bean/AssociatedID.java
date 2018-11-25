package com.hwc.gm.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssociatedID implements Serializable {

    private long id;

    private String name;

    @Override
    public String toString() {
        return "AssociatedID{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
