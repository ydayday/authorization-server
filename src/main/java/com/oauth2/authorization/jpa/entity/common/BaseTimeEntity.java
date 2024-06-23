package com.oauth2.authorization.jpa.entity.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "insert_dtm", updatable = false)
    private LocalDateTime insertDtm;

    @LastModifiedDate
    @Column(name = "update_dtm")
    private LocalDateTime updateDtm;

    private String insertId;

    private String updateId;

    @PrePersist
    public void onPrePersist() {
        this.insertId = "SYSTEM";
        this.updateId = "SYSTEM";
    }

}
