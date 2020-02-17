package com.github.nponagayba.contacts.service;

import com.github.nponagayba.contacts.domain.Contact;
import com.github.nponagayba.contacts.repository.ContactRepository;
import com.github.nponagayba.contacts.service.dto.ContactFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactRepository contactRepository;

    @Transactional(readOnly = true)
    public Slice<Contact> searchContacts(ContactFilter filter, Pageable pageable) {
        Pattern pattern = Pattern.compile(filter.getNameFilter());
        List<Contact> result;
        int limit = 0;
        if (pageable.isPaged()) {
            limit = (pageable.getPageNumber() + 1) * pageable.getPageSize() + 1;
        }

        result = searchContactsLimited(pattern, limit);

        boolean hasNext = false;
        if (result.size() == limit) {
            result = result.subList(0, result.size() - 1);
            hasNext = true;
        }
        log.info("Found {} contacts by filter: {}, limited by pageable: {}", result.size(), filter, pageable);
        return toSlice(result, pageable, hasNext);
    }

    private List<Contact> searchContactsLimited(Pattern pattern, int limit) {
        try (Stream<Contact> contactStream = contactRepository.streamAll()) {
            Stream<Contact> filteredStream = contactStream
                    .filter(not(it -> pattern.matcher(it.getName()).matches()));
            if (limit > 0) {
                filteredStream = filteredStream
                        .limit(limit);
            }
            return filteredStream.collect(Collectors.toList());
        }
    }

    private Slice<Contact> toSlice(List<Contact> result, Pageable pageable, boolean hasNext) {
        if (pageable.isPaged()) {
            int start = (int) pageable.getOffset();
            int end = (start + pageable.getPageSize()) > result.size() ? result.size() : (start + pageable.getPageSize());
            if (start < end) {
                return new SliceImpl<>(result.subList(start, end), pageable, hasNext);
            }
        }
        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Transactional
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    @Transactional
    public List<Contact> saveAll(List<Contact> contacts) {
        return contactRepository.saveAll(contacts);
    }
}
