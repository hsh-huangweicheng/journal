package com.hwc.gm.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@IdClass(AssociatedID.class)
public class Institution {

    @Id
    private long id;

    @Id
    @Column(length = 512)
    private String name;

    @Column
    private int seq;

    public Institution() {
    }

    public Institution(long id, String name, int seq) {
        this.id = id;
        this.name = name;
        this.seq = seq;
    }
}
