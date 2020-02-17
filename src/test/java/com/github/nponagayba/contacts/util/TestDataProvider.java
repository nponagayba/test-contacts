package com.github.nponagayba.contacts.util;

import com.github.nponagayba.contacts.domain.Contact;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestDataProvider {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager entityManager() {
        return entityManager;
    }

    public void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    public List<Contact> createContacts(String... names) {
        return Arrays.stream(names)
                .map(Contact::new)
                .peek(it -> entityManager.persist(it))
                .collect(Collectors.toList());
    }
}
