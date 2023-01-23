package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.StringUtil;

@Component
public class UserDto {

    @Autowired
    UserService service;

    @Value("${supervisor.email}")
    private String supEmail;

    public void add(UserForm form) throws ApiException {
        StringUtil.normalise(form, UserForm.class);
        
        UserPojo p = ConvertUtil.objectMapper(form, UserPojo.class);
        service.add(p);
    }

    public void add(LoginForm form) throws ApiException {
        StringUtil.normalise(form, LoginForm.class);
        UserPojo p = ConvertUtil.objectMapper(form, UserPojo.class);
        if(p.getEmail().equals(supEmail)){
            p.setRole("supervisor");
        }
        else{
            p.setRole("operator");
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
