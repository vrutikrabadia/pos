package com.increff.pos.pojo;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.increff.pos.util.DateTimeProvider;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractPojo {


    @Column(nullable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime updated;
    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        updated = created = DateTimeProvider.INSTANCE.timeNow();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = DateTimeProvider.INSTANCE.timeNow();
    }

}
