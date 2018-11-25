package com.hwc.gm.bean;

import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Article {

    @Id
    private long id;

    @Column
    private String source;

    @Column
    private String researchField;

    @Column(length = 1024)
    private String articleName;

    @Column
    private int cited;

    @Column
    private int year;

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                "\n, source='" + source + '\'' +
                "\n, researchField='" + researchField + '\'' +
                "\n, articleName='" + articleName + '\'' +
                "\n, cited=" + cited +
                "\n, year=" + year +
                "\n, authorList=" + authorList +
                '}';
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Author> authorList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Address> addressList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Country> countryList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<Institution> institutionList;
}
