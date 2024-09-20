package com.scm.controller;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import com.scm.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import static com.scm.helper.AppConstants.PAGE_SIZE;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    Logger logger= LoggerFactory.getLogger(ContactController.class);


    private final ModelMapper modelMapper;

    public ContactController() {
        this.modelMapper = new ModelMapper();
    }


    //add contact page

    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession session) {
        String fileName = UUID.randomUUID().toString();
        String fileURl = imageService.uploadImage(contactForm.getProfileImage(), fileName);
        if (bindingResult.hasErrors()) {
            session.setAttribute("Message", Message.builder().content("All fields are mandatory").type(MessageType.RED).build());

            return "user/add_contact";
        }

        Contact contact = modelMapper.map(contactForm, Contact.class);
        String userName = Helper.getEmailFromPrincipal(authentication);
        User user = userService.getUserByEmail(userName);
        contact.setUser(user);
        contact.setPicture(fileURl);

        contactService.save(contact);


        return "redirect:/user/contacts/add";
    }

    //View Contacts
    @RequestMapping
    public String viewContact(Authentication authentication,
                              @RequestParam(value = "page",defaultValue = "0")int page,
                              @RequestParam(value="size",defaultValue = PAGE_SIZE+"")int size,
                              @RequestParam(value="sortBy",defaultValue = "name")String sortBy,
                              @RequestParam(value="direction",defaultValue = "asc")String direction,
                              Model model) {
        String username = Helper.getEmailFromPrincipal(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContacts = contactService.getByUser(user,page,size,sortBy,direction);


        model.addAttribute("pageContacts", pageContacts);
        model.addAttribute("pageSize",PAGE_SIZE);
        model.addAttribute("contactSearchForm",new ContactSearchForm());
        return "user/contacts";
    }

    //Search Handler
    @GetMapping("/search")
    public String searchHandler(/*@RequestParam("keyword") String keyword,
                                @RequestParam("field")String field,*/
                                @ModelAttribute ContactSearchForm contactSearchForm,
                                @RequestParam(value = "page",defaultValue = "0")int page,
                                @RequestParam(value="size",defaultValue = PAGE_SIZE+"")int size,
                                @RequestParam(value="sortBy",defaultValue = "name")String sortBy,
                                @RequestParam(value="direction",defaultValue = "asc")String direction,
                                Model model, Authentication authentication) {

        var user=userService.getUserByEmail(Helper.getEmailFromPrincipal(authentication));
        //logger.info("Keyword: {} Field: {}", keyword, field);
        Page<Contact> contacts = switch (contactSearchForm.getField()) {
            case "name" -> contactService.searchByName(contactSearchForm.getValue(), page, size, sortBy, direction,user);
            case "email" -> contactService.searchByEmail(contactSearchForm.getValue(), page, size, sortBy, direction,user);
            case "phone" -> contactService.searchByPhone(contactSearchForm.getValue(), page, size, sortBy, direction,user);
            default -> null;
        };
        model.addAttribute("pageContacts", contacts);
        model.addAttribute("pageSize",PAGE_SIZE);
        model.addAttribute("contactSearchForm", contactSearchForm);

        return "user/search";
    }


    @RequestMapping("/delete/{id}")
    public String deleteContact(@PathVariable String id,
                                HttpSession session) {
        logger.info("Contact Deleted {}",id);
        contactService.delete(id);
        session.setAttribute("Message", Message.builder().content("Contact Deleted Successfully").type(MessageType.GREEN).build());
        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{id}")
    public String updateContactViewForm(@PathVariable String id, Model model) {
        Contact contact = contactService.findById(id);
        ContactForm contactForm = modelMapper.map(contact, ContactForm.class);
        contactForm.setPicture(contact.getPicture());
        contactForm.setPhoneNumber(contact.getPhone());
        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId",id);
        return "user/update_contact";
    }

    @RequestMapping(value="/update/{id}",method=RequestMethod.POST)
    public String updateContact(@PathVariable String id,ContactForm contactForm,Model model){
        var con=new Contact();
        con.setId(id);
        con.setName(contactForm.getName());
        con.setEmail(contactForm.getEmail());
        con.setPhone(contactForm.getPhoneNumber());
        con.setFavorite(String.valueOf(contactForm.isFavorite()));
        con.setAddress(contactForm.getAddress());
        con.setDescription(contactForm.getDescription());
        con.setWebSiteLink(contactForm.getWebsiteLink());
        con.setLinkedInLink(contactForm.getLinkedInLink());
        con.setPicture(contactForm.getPicture());
        //Process Image
        if(contactForm.getProfileImage()!=null){
            String fileName = UUID.randomUUID().toString();
            String fileURl = imageService.uploadImage(contactForm.getProfileImage(), fileName);
            con.setCloudinaryImagePublicId(fileName);
            con.setPicture(fileURl);
        }
        contactService.update(con);
        return "redirect:/user/contacts/view/"+id;


    }
}

