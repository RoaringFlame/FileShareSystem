package com.fss.dao.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "t_file_receive", catalog = "")
public class FileReceive {
    private String id;
    private String versionId;
    private String FileId;
    private String userId;
    private boolean isAlert;
    private boolean canRevise;
    private boolean isReceived;
    private Date downloadTime;

    @Id
    @Column(name = "id")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "version_id")
    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Column(name = "file_id")
    public String getFileId() {
        return FileId;
    }

    public void setFileId(String fileId) {
        FileId = fileId;
    }

    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "is_alert")
    public boolean getIsAlert(){return isAlert;}

    public void setIsAlert(boolean isAlert){
        this.isAlert = isAlert;
    }

    @Column(name = "can_revise")
    public boolean getCanRevise() {
        return canRevise;
    }

    public void setCanRevise(boolean canRevise) {
        this.canRevise = canRevise;
    }

    @Column(name = "is_received")
    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    @Column(name = "download_time")
    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileReceive that = (FileReceive) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (versionId != null ? !versionId.equals(that.versionId) : that.versionId != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (downloadTime != null ? !downloadTime.equals(that.downloadTime) : that.downloadTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (versionId != null ? versionId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (downloadTime != null ? downloadTime.hashCode() : 0);
        return result;
    }
}
