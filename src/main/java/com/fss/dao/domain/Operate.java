package com.fss.dao.domain;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_operate", catalog = "")
@SQLDelete(sql = "UPDATE t_operate SET state = 0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class Operate extends BaseEntity implements Serializable {

    private User operator;
    private FileVersion fileVersion ;
    private Date operateTime;
    private Integer operateFlag;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    @ManyToOne
    @JoinColumn(name = "version_id")
    public FileVersion getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(FileVersion fileVersion) {
        this.fileVersion = fileVersion;
    }

    @Column(name = "operate_time")
    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Column(name = "operate_flag")
    public Integer getOperateFlag() {
        return operateFlag;
    }

    public void setOperateFlag(Integer operateFlag) {
        this.operateFlag = operateFlag;
    }

}
