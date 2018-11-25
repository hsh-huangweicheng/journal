package com.hwc.gm.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@IdClass(AssociatedID.class)
public class Author {

    @Id
    private long id;

    @Id
    private String name;

    @Column
    private int seq;

    public Author() {
    }

    public Author(long id, String name, int seq) {
        this.id = id;
        this.name = name;
        this.seq = seq;
    }
}
