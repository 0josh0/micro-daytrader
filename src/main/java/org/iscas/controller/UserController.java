package org.iscas.controller;
import org.iscas.entity.User;
import org.iscas.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by andyren on 2016/6/28.
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    private UserDAO _userDAO;

    @RequestMapping("/")
    @ResponseBody
    String home(){
        return "hello world! 任仲山您好！";
    }

    @RequestMapping(value = "/create")
    @ResponseBody
    public String create(@RequestParam(value = "email") String email, @RequestParam(value = "name") String name){
        try{
            User user = new User(email, name);
            _userDAO.create(user);
        }catch(Exception ex){
            return "Error creating the user: " + ex.toString();
        }

        return "User successfully created!";
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(
            @RequestParam(value = "id") long id) {
        try{
            User user = new User(id);
            _userDAO.delete(user);
        }catch (Exception ex){
            return "Error occurs during delete the user " +  ex.getMessage();
        }

        return "User successful deleted!";
    }

    @RequestMapping(value = "/get-by-email")
    @ResponseBody
    public String getByEmail(@RequestParam(value = "email") String email){
        String userId;
        try{
            User user = _userDAO.getByEmail(email);
            userId = String.valueOf(user.getId());
        }catch (Exception ex){
            return "User not found" + ex.toString();
        }
        return "The User id is : " + userId;
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public String updateName(long id, String email, String name){

        try{
            User user = _userDAO.getById(id);
            user.setEmail(email);
            user.setName(name);
            _userDAO.update(user);
        }catch (Exception ex){
            return "Error in updating the user " + ex.toString();
        }

        return "User successfully updated!";
    }

}
