package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name cannot be blank")
    private String name;
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String phoneNumber;
    @NotBlank(message = "Address cannot be blank")

    private String address;

    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;


    private MultipartFile profileImage;
    private String picture;

}
