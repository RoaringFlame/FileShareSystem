package com.fss.dao.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_operate", catalog = "")
public class Operate {
    private String id;
    private String userId;
    private String versionId;
    private Date operateTime;
    private Integer operateFlag;

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "version_id")
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Operate tOperate = (Operate) o;

        if (id != null ? !id.equals(tOperate.id) : tOperate.id != null)
            return false;
        if (userId != null ? !userId.equals(tOperate.userId) : tOperate.userId != null)
            return false;
        if (versionId != null ? !versionId.equals(tOperate.versionId) : tOperate.versionId != null)
            return false;
        if (operateTime != null ? !operateTime.equals(tOperate.operateTime) : tOperate.operateTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (versionId != null ? versionId.hashCode() : 0);
        result = 31 * result + (operateTime != null ? operateTime.hashCode() : 0);
        return result;
    }
}
