package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.ApiException;

public class UserDtoTest extends AbstractUnitTest {
    
    @Autowired
    private UserDto userDto;

    @Test
    public void testAdd() throws ApiException {
        String email = "email";
        String password = "password";
        String role = "role";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);
        UserData data = userDto.get(email);
        assertEquals(data.getEmail(), email);
        assertEquals(data.getRole(), role);
    }

    @Test
    public void testGetAll() throws ApiException {
        String email = "email";
        String password = "password";
        String role = "role";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);
        assertEquals(userDto.getAll().size(), 1);
    }

    @Test
    public void testDelete() throws ApiException {
        String email = "email";
        String password = "password";
        String role = "role";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);

        

        userDto.delete(userDto.getAll().get(0).getId());
        assertEquals(userDto.getAll().size(), 0);
    }

    @Test
    public void testAddByLoginFormSupervisor() throws ApiException {
        String email = "supervisor@pos.com";
        String password = "password";
        LoginForm form = new LoginForm();
        form.setEmail(email);
        form.setPassword(password);
        userDto.add(form);
        UserData data = userDto.get(email);
        assertEquals(data.getEmail(), email);
        assertEquals(data.getRole(), "supervisor");
    }

    @Test
    public void testAddByLoginFormOperator() throws ApiException {
        String email = "operator@pos.com";
        String password = "password";
        LoginForm form = new LoginForm();
        form.setEmail(email);
        form.setPassword(password);
        userDto.add(form);
        UserData data = userDto.get(email);
        assertEquals(data.getEmail(), email);
        assertEquals(data.getRole(), "operator");
    }

}
