package org.iscas.controller;

import org.iscas.entity.User;
import org.iscas.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by andyren on 2016/6/28.
 * REST方式的实现
 */
@RestController
@RequestMapping(value = "/users")
public class UserControllerRest {

    @Autowired
    private UserDAO _userDAO;

    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long userId){
        User user = null;
        try{
            user = _userDAO.getById(userId);
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return null;
        }
        return user;
    }
}
