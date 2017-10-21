package com.fss.dao.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_file_receive", catalog = "")
public class FileReceive extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private FileVersion fileVersion;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @Column(name = "is_alert")
    private boolean isAlert;

    @Column(name = "can_revise")
    private boolean canRevise;

    @Column(name = "is_received")
    private boolean isReceived;

    @Column(name = "download_time")
    private Date downloadTime;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileVersion getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(FileVersion fileVersion) {
        this.fileVersion = fileVersion;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean getIsAlert(){return isAlert;}

    public void setIsAlert(boolean isAlert){
        this.isAlert = isAlert;
    }

    public boolean getCanRevise() {
        return canRevise;
    }

    public void setCanRevise(boolean canRevise) {
        this.canRevise = canRevise;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }
}
