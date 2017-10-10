package com.fss.dao.domain;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public class BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "usable")
    private Boolean usable;

    @Column(name = "create_time",columnDefinition = "timestamp default now()")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean isUsable() {
        return usable;
    }

    public void setUsable(Boolean usable) {
        this.usable = usable;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
