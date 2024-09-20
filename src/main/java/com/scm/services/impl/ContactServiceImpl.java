package com.scm.services.impl;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.exception.ResourceNotFoundException;
import com.scm.repository.ContactRepository;
import com.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {
        String contactId= UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepository.findById(contact.getId()).orElseThrow(()->new ResourceNotFoundException("Contact not Found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhone(contact.getPhone());
        contactOld.setPicture(contact.getPicture());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setLinkedInLink(contact.getLinkedInLink());
        contactOld.setWebSiteLink(contact.getWebSiteLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());


        return contactRepository.save(contactOld);
    }

    @Override
    public List<Contact> findAll() {
      return contactRepository.findAll();
    }

    @Override
    public Contact findById(String id) {
        return contactRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not Found"));
    }

    @Override
    public void delete(String id) {
        var contact=contactRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not Found"));
        contactRepository.delete(contact);


    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page,int size,String sortBy,String direction) {
        Sort sort=direction.equals("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page,size,sort);

        return contactRepository.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String order,User user) {
        Sort sort=order.equals("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndNameContaining(user,name,pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String order,User user) {
        Sort sort=order.equals("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndEmailContaining(user,email,pageable);
    }

    @Override
    public Page<Contact> searchByPhone(String phone, int page, int size, String sortBy, String order,User user) {
        Sort sort=order.equals("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page,size,sort);
        return contactRepository.findByUserAndPhoneContaining(user,phone,pageable);
    }


}
