package com.fss.dao.domain;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_file_version", catalog = "")
@SQLDelete(sql = "UPDATE t_file_version SET usable = 0 WHERE id = ? and version = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class FileVersion extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(name = "number")
    private Double number;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "count")
    private Integer count;

    @Column(name = "can_cover")
    private boolean canCover;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean getCanCover() {
        return canCover;
    }

    public void setCanCover(boolean canCover) {
        this.canCover = canCover;
    }

}
