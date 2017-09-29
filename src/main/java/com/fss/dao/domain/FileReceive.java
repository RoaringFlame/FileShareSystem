package com.fss.dao.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.jws.HandlerChain;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_file_receive", catalog = "")
@SQLDelete(sql = "UPDATE t_file_receive SET state = 0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class FileReceive extends BaseEntity implements Serializable {

    private File file;
    private User receiver;
    private boolean isAlert;
    private boolean canRevise;
    private boolean isReceived;
    private Date downloadTime;

    @ManyToOne
    @JoinColumn(name = "file_id")
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
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
}
