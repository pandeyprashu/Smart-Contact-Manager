package com.scm.services;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactService {

    Contact save(Contact contact);

    Contact update(Contact contact);

    List<Contact> findAll();

    Contact findById(String id);

    void delete(String id);


    public List<Contact> getByUserId(String userId);

    public Page<Contact> getByUser(User user, int page,int size,String sortField,String sortDir);

    Page<Contact> searchByName(String name,int page,int size,String sortBy,String order,User user);

    Page<Contact> searchByEmail(String email,int page,int size,String sortBy,String order,User user);

    Page<Contact> searchByPhone(String phone,int page,int size,String sortBy,String order,User user);

}