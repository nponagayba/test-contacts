package com.github.nponagayba.contacts.web.rest;

import com.github.nponagayba.contacts.domain.Contact;
import com.github.nponagayba.contacts.service.ContactService;
import com.github.nponagayba.contacts.service.dto.ContactFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        PageableHandlerMethodArgumentResolver.class,
        SortHandlerMethodArgumentResolver.class, MappingJackson2HttpMessageConverter.class
})
class ContactResourceTest {

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private SortHandlerMethodArgumentResolver sortArgumentResolver;

    @Autowired
    private HttpMessageConverter<?>[] httpMessageConverters;

    @Mock
    private ContactService contactService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ContactResource(contactService))
                .setCustomArgumentResolvers(pageableArgumentResolver, sortArgumentResolver)
                .setMessageConverters(httpMessageConverters)
                .build();
    }

    @Test
    void searchContacts() throws Exception {
        Contact contact = new Contact("Bcd");
        contact.setId(1L);
        List<Contact> result = List.of(contact);
        String nameFilter = "^A.*$";
        ContactFilter filter = new ContactFilter(nameFilter);
        when(contactService.searchContacts(eq(filter), any()))
                .thenReturn(new SliceImpl<>(result, Pageable.unpaged(), false));

        mockMvc.perform(get("/hello/contacts").param("nameFilter", nameFilter))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("X-Has-Next-Page", "false"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(contact.getId()))
                .andExpect(jsonPath("$[0].name").value(contact.getName()));
    }

    @Test
    void searchContacts_NoNameFilter() throws Exception {
        mockMvc.perform(get("/hello/contacts"))
                .andExpect(status().isBadRequest());
    }

}