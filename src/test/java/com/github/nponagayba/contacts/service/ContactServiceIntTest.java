package com.github.nponagayba.contacts.service;

import com.github.nponagayba.contacts.ContactApplication;
import com.github.nponagayba.contacts.domain.Contact;
import com.github.nponagayba.contacts.service.dto.ContactFilter;
import com.github.nponagayba.contacts.util.TestDataProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ContactApplication.class)
@Transactional
class ContactServiceIntTest {

    @Autowired
    private ContactService contactService;

    @Autowired
    private TestDataProvider dataProvider;

    @Test
    void searchContacts_Unpaged() {
        dataProvider.createContacts("abc", "bcd", "cde", "ced", "def", "fgh");
        dataProvider.flushAndClear();

        String nameFilter = "^c.*$";
        Slice<Contact> resultSlice = contactService.searchContacts(new ContactFilter(nameFilter), Pageable.unpaged());

        assertThat(resultSlice).hasSize(4);
        assertThat(resultSlice.hasNext()).isFalse();
        List<String> resultContactNames = resultSlice.getContent().stream().map(Contact::getName).collect(Collectors.toList());
        assertThat(resultContactNames).doesNotContain("cde", "ced");
    }

    @Test
    void searchContacts_Paged() {
        dataProvider.createContacts("abc", "bcd", "cde", "ced", "def", "fgh");
        dataProvider.flushAndClear();

        String nameFilter = "^qqq$";
        Pageable pageable = PageRequest.of(1, 2);
        Slice<Contact> resultSlice = contactService.searchContacts(new ContactFilter(nameFilter), pageable);

        assertThat(resultSlice).hasSize(pageable.getPageSize());
        assertThat(resultSlice.hasNext()).isTrue();
        List<String> resultContactNames = resultSlice.getContent().stream().map(Contact::getName).collect(Collectors.toList());
        assertThat(resultContactNames).containsOnly("cde", "ced");
    }

    @Test
    void searchContacts_PagedButNotFound() {
        dataProvider.flushAndClear();

        String nameFilter = "^.+$";
        Pageable pageable = PageRequest.of(1, 2);
        Slice<Contact> resultSlice = contactService.searchContacts(new ContactFilter(nameFilter), pageable);

        assertThat(resultSlice).isEmpty();
        assertThat(resultSlice.hasNext()).isFalse();
    }
}