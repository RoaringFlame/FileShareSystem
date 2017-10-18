package com.fss.dao.domain;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "t_file", catalog = "")
@SQLDelete(sql = "UPDATE t_file SET usable = 0 WHERE id = ? and version = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "usable <> 0")
public class File extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @ManyToOne
    @JoinColumn(name = "new_version_id")
    private FileVersion newFileVersion;

    @Column(name = "file_name")
    private String fileName;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public FileVersion getNewFileVersion() {
        return newFileVersion;
    }

    public void setNewFileVersion(FileVersion newFileVersion) {
        this.newFileVersion = newFileVersion;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
