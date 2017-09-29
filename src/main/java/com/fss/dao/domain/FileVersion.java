package com.fss.dao.domain;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_file_version", catalog = "")
@SQLDelete(sql = "UPDATE t_file_version SET state = 0 WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class FileVersion extends BaseEntity implements Serializable {

    private User author;
    private File file;
    private Double number;
    private String realName;
    private Integer count;
    private boolean canCover;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @ManyToOne
    @JoinColumn(name = "file_id")
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Column(name = "number")
    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Column(name = "can_cover")
    public boolean getCanCover() {
        return canCover;
    }

    public void setCanCover(boolean canCover) {
        this.canCover = canCover;
    }

}
