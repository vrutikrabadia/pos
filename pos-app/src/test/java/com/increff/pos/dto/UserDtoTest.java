package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.increff.pos.config.TestUtil;
import com.increff.pos.dao.UserDao;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.config.AbstractUnitTest;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.PasswordUtil;
import org.springframework.mock.web.MockHttpServletRequest;


public class UserDtoTest extends AbstractUnitTest {
    
    @Autowired
    private UserDto userDto;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private TestUtil testUtil;


    @Test
    public void testAdd() throws ApiException {
        String email = "email@email.com";
        String password = "password";
        String role = "operator";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);
        UserData data = userDto.get(email);
        assertEquals(data.getEmail(), email);
        assertEquals(data.getRole(), role);
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicateUser() throws ApiException {
        String email = "email@email.com";
        String password = "password";
        String role = "operator";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);
        userDto.add(form);
    }

    @Test
    public void testGetAll() throws ApiException {
        String email = "email@email.com";
        String password = "password";
        String role = "operator";
        UserForm form = new UserForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setRole(role);
        userDto.add(form);
        assertEquals(userDto.getAll().size(), 1);
    }

    @Test
    public void testDelete() throws ApiException {
        String email = "email@email.com";
        String password = "password";
        String role = "operator";
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

    @Test
    public void validatePassword() throws Exception {
        String password = "password";

        String hashedPassword = PasswordUtil.generateStorngPasswordHash(password);

        assertTrue(PasswordUtil.validatePassword(password, hashedPassword));
    }



    @Test
    public void testSignup() throws ApiException{
        String email = "abc@pos.com";
        String password = "password";
        LoginForm form = testUtil.getLoginForm(email, password);
        MockHttpServletRequest request = new MockHttpServletRequest();
        userDto.signup(request, form);

        UserPojo user = userDao.selectByColumn("email", email);
        checkEquals(user, form);
    }

    @Test
    public void testSignupSupervisor() throws ApiException {
        String password = "password";
        LoginForm form = testUtil.getLoginForm(testUtil.getProperties().getSupervisorEmail(), password);
        MockHttpServletRequest request = new MockHttpServletRequest();
        userDto.signup(request, form);
        UserPojo user = userService.get(testUtil.getProperties().getSupervisorEmail());
        checkEquals(user, form);
        assertEquals("supervisor", user.getRole().toString());
    }

    private void checkEquals(UserPojo user, LoginForm form){
        assertEquals(user.getEmail(), form.getEmail());

        assertEquals(user.getPassword(), form.getPassword());
    }



}
