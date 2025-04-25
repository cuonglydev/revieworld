package com.example.Security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.Entity.DefaultRank;
import com.example.Entity.User;
import com.example.Service.DefaultRankService;
import com.example.Service.UserService;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DefaultRankService defaultRankService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String picture = user.getAttribute("picture");
        String providerId = user.getAttribute("sub"); // Google ID

        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {
           
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setPicture(picture);
            newUser.setProvider("GOOGLE");
            newUser.setProviderId(providerId);
            newUser.setAmount(0.0);
            newUser.setBonusAmount(0.0);
            
            Date currentDate = new Date();
            newUser.setCreatedAt(currentDate);
            
            DefaultRank defaultRank = defaultRankService.findById(1);
			if(defaultRank.isStatus()) {
				if(defaultRank.getRank() != null) {
					newUser.setRank(defaultRank.getRank());
				}
			}
			
			newUser.setStatus("ACTIVE");
            userService.save(newUser);
        }

        return user;
    }
}