package com.github.nponagayba.contacts.repository;

import com.github.nponagayba.contacts.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("select c from Contact c")
    Stream<Contact> streamAll();
}
