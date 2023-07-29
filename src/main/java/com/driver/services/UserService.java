package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        User savedUser=userRepository.save(user);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){
        User user = userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        int count=findSeriesCount(user,subscription.getSubscriptionType());

        return count;
    }
    public int findSeriesCount(User user,SubscriptionType subscriptionType){
        int count=0;
        for(WebSeries webSeries: webSeriesRepository.findAll()){
            if (user.getAge() >= webSeries.getAgeLimit()) {
                if (subscriptionType.equals(SubscriptionType.ELITE)) {
                    count++;
                } else if (subscriptionType.equals(SubscriptionType.PRO)){
                    if (webSeries.getSubscriptionType().equals(SubscriptionType.BASIC) || webSeries.getSubscriptionType().equals(SubscriptionType.PRO))
                        count++;
                } else {
                    if (webSeries.getSubscriptionType().equals(SubscriptionType.BASIC))
                        count++;
                }
            }
        }

        return count;
    }

}
