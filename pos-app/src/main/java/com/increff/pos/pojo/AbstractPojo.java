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

    //TODO: Implement physical naming startergy

    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @Version
    @Column(name = "version")
    private long version;

    @PrePersist
    protected void onCreate() {
        updated = created = DateTimeProvider.INSTANCE.timeNow();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = DateTimeProvider.INSTANCE.timeNow();
    }

}
