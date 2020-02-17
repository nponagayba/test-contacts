package com.github.nponagayba.contacts.web.rest;

import com.github.nponagayba.contacts.domain.Contact;
import com.github.nponagayba.contacts.service.ContactService;
import com.github.nponagayba.contacts.service.dto.ContactFilter;
import com.github.nponagayba.contacts.util.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hello/contacts")
@RequiredArgsConstructor
public class ContactResource {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getContacts(@Valid ContactFilter filter, Pageable pageable) {
        Slice<Contact> slice = contactService.searchContacts(filter, pageable);
        HttpHeaders headers = PaginationUtils.generateSliceHttpHeaders(slice);
        return ResponseEntity.ok().headers(headers).body(slice.getContent());
    }

    @PutMapping("/bulk")
    public List<Contact> saveBulkContacts(@RequestBody @Valid List<Contact> contacts) {
        return contactService.saveAll(contacts);
    }

    @PutMapping
    public ResponseEntity<Contact> saveContact(@RequestBody @Valid Contact contact) {
        boolean updating = contact.getId() != null;
        Contact result = contactService.save(contact);
        if (updating) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
    }
}
