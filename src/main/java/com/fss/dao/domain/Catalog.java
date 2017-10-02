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
@SQLDelete(sql = "UPDATE t_catalog SET state = 0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class Catalog extends BaseEntity implements Serializable {

    private Catalog parentCatalog;
    private String name;
    private String description;

    @OneToMany(targetEntity = Catalog.class, cascade = { CascadeType.ALL }, mappedBy = "parentCatalog")
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("createTime desc")
    private List<Catalog> childCatalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
