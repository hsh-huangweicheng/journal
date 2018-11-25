package com.hwc.gm.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@IdClass(AssociatedID.class)
public class Address {

    @Id
    private long id;

    @Id
    @Column(length = 512)
    private String name;

    @Column
    private int seq;

    public Address() {
    }

    public Address(long id, String name, int seq) {
        this.id = id;
        this.name = name;
        this.seq = seq;
    }
}
