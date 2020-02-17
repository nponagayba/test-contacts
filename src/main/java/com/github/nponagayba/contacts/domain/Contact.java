package com.github.nponagayba.contacts.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contacts")
@Data
@EqualsAndHashCode(of = "id")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    protected Contact() {
    }

    public Contact(@NotNull String name) {
        this.name = name;
    }
}
