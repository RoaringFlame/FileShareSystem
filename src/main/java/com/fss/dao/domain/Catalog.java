package com.fss.dao.domain;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "t_catalog", catalog = "")
@SQLDelete(sql = "UPDATE t_catalog SET usable = 0 WHERE id = ? and version = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class Catalog extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Catalog parentCatalog;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(targetEntity = Catalog.class, cascade = { CascadeType.ALL }, mappedBy = "parentCatalog")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("createTime desc")
    private List<Catalog> childCatalog;

    public Catalog getParentCatalog() {
        return parentCatalog;
    }

    public void setParentCatalog(Catalog parentCatalog) {
        this.parentCatalog = parentCatalog;
    }

    public List<Catalog> getChildCatalog() {
        return childCatalog;
    }

    public void setChildCatalog(List<Catalog> childCatalog) {
        this.childCatalog = childCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
