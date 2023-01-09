package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.UserData;
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

    public void add(UserForm f) throws ApiException {
        UserPojo p = ConvertUtil.convertUserFormToPojo(f);
        StringUtil.normalizeUser(p);
        service.add(p);
    }

    public UserData get(String email) throws ApiException {
        UserPojo p = service.get(email);

        return ConvertUtil.convertUserPojoToData(p);
    }

    public List<UserData> getAll() {
        List<UserPojo> list = service.getAll();
        List<UserData> list2 = new ArrayList<UserData>();
        for (UserPojo p : list) {
            list2.add(ConvertUtil.convertUserPojoToData(p));
        }
        return list2;
    }

    public void delete(int id) {
        service.delete(id);
    }

}
