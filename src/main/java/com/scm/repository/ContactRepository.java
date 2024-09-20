package com.scm.repository;

import com.scm.entities.Contact;
import com.scm.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,String> {
    //Custom Finder Method
    Page<Contact> findByUser(User user, Pageable pageable);

    //Custom Query Method
    @Query("SELECT c FROM Contact c WHERE c.user.userId = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user, String email, Pageable pageable);
    Page<Contact> findByUserAndPhoneContaining(User user, String phone, Pageable pageable);

}
