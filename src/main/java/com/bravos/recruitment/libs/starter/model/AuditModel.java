package com.bravos.recruitment.libs.starter.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public abstract class AuditModel {

  Long createdAt;

  Long updatedAt;

  Long createdBy;

  Long updatedBy;

}
