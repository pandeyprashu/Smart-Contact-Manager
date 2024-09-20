package com.scm.services.impl;

import com.scm.entities.User;
import com.scm.exception.ResourceNotFoundException;
import com.scm.helper.AppConstants;
import com.scm.repository.UserRepository;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //User ID have to generate
        String userId= UUID.randomUUID().toString();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //Set the user role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        // Need to encode password

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> updateUser(User user) {
       User user1=userRepository.findById(user.getUserId()).orElseThrow(()->new ResourceNotFoundException("User not Found"));
       user1.setName(user.getName());
       user1.setEmail(user.getEmail());
       user1.setAbout(user.getAbout());
       user1.setPhoneNumber(user.getPhoneNumber());
       user1.setProfilePic(user.getProfilePic());
       user1.setEnabled(user.isEnabled());
       user1.setEmailVerified(user.isEmailVerified());
       user1.setPhoneVerified(user.isPhoneVerified());
       user1.setProvider(user.getProvider());
       user1.setProviderUserId(user.getProviderUserId());
       return Optional.of(userRepository.save(user1));

    }

    @Override
    public void deleteUser(String userId) {
        userRepository.delete(userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not Found")));

    }

    @Override
    public boolean isUserExists(String email) {
      User user=userRepository.findByEmail(email).orElseThrow(null);
      return user != null;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not Found"));
    }
}
