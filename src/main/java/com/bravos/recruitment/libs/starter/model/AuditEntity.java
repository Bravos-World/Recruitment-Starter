package com.bravos.recruitment.libs.starter.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldDefaults(level = PROTECTED)
public abstract class AuditEntity {

  Long createdAt;

  Long updatedAt;

  @CreatedBy
  Long createdBy;

  @LastModifiedBy
  Long updatedBy;

  @PrePersist
  void onCreated() {
    long now = System.currentTimeMillis();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  void onUpdated() {
    this.updatedAt = System.currentTimeMillis();
  }

}
