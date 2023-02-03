package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.increff.pos.model.Roles;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.PasswordUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class UserDto {

    @Autowired
    UserService service;

    @Value("${supervisor.email}")
    private String supEmail;

    public void add(UserForm form) throws ApiException {
        ValidateUtil.validateForms(form);

        ValidateUtil.validateEmail(form.getEmail());
        ValidateUtil.validatePassword(form.getPassword());

        try{
            form.setPassword(PasswordUtil.generateStorngPasswordHash(form.getPassword()));
        }
        catch(Exception e){
            throw new ApiException("Password hashing failed");
        }

        UserPojo p = ConvertUtil.objectMapper(form, UserPojo.class);
        service.add(p);
    }

    public void add(LoginForm form) throws ApiException {
        ValidateUtil.validateForms(form);

        ValidateUtil.validateEmail(form.getEmail());
        ValidateUtil.validatePassword(form.getPassword());

        try{
            form.setPassword(PasswordUtil.generateStorngPasswordHash(form.getPassword()));
        }
        catch(Exception e){
            throw new ApiException("Password hashing failed");
        }
        
        UserPojo p = ConvertUtil.objectMapper(form, UserPojo.class);
        if(p.getEmail().equals(supEmail)){
            p.setRole(Roles.supervisor);
        }
        else{
            p.setRole(Roles.operator);
        }
        service.add(p);
    }

    public UserData get(String email) throws ApiException {
        UserPojo p = service.get(email);

        return ConvertUtil.objectMapper(p, UserData.class);
    }

    public List<UserData> getAll() {
        List<UserPojo> list = service.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(ConvertUtil.objectMapper(p, UserData.class));
        }
        return list2;
    }

    public void delete(Integer id) {
        service.delete(id);
    }

}
