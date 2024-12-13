package com.frezza.autorizador.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
@Embeddable
public class Audit implements Serializable {

	@Serial
    private static final long serialVersionUID = -547380384097363859L;

	@Column(name = "dh_created_at")
    private LocalDateTime dtCreatedAt;

    @Column(name = "dh_updated_at")
    private LocalDateTime dtUpdatedAt;

    @PrePersist
    public void prePersist() {
        this.dtCreatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));;
    }

	@PreUpdate
	public void preUpdate() {
		this.dtUpdatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));;
	}
}
