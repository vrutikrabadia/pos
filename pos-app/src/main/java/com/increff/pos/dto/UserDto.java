package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.Roles;
import com.increff.pos.model.data.InfoData;
import com.increff.pos.model.data.UserData;
import com.increff.pos.model.data.UserPrincipal;
import com.increff.pos.model.form.LoginForm;
import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.util.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.spring.Properties;
import com.increff.pos.util.ConvertUtil;
import com.increff.pos.util.PasswordUtil;
import com.increff.pos.util.SecurityUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class UserDto {

    @Autowired
    UserService service;

    @Autowired
	private Properties properties;

    @Autowired
	private InfoData info;

    public ModelAndView login(HttpServletRequest req, LoginForm loginForm) throws ApiException {
		info.setMessage("No message");
		UserPojo userPojo = service.get(loginForm.getEmail());

		boolean passwordValidate = false;
		try{
			passwordValidate = PasswordUtil.validatePassword(loginForm.getPassword(), userPojo.getPassword());
		}
		catch(Exception exception){
			System.out.println("Error validating password");
			info.setMessage("Error validating password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}
		
		boolean authenticated = (Objects.nonNull(userPojo) && passwordValidate);
		if (!authenticated) {
			info.setMessage("Invalid username or password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}

		// Create authentication object
		Authentication authentication = convert(userPojo);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);

 		return new ModelAndView("redirect:/ui/home");

	}

    public ModelAndView signup(HttpServletRequest req, LoginForm loginForm) throws ApiException{
		info.setMessage("No message");
		try{
			add(loginForm);
		}
		catch(ApiException apiException){
			info.setMessage(apiException.getMessage());
			info.setShown(false);
			return new ModelAndView("redirect:/site/signup");
		}
		
		UserPojo userPojo = service.get(loginForm.getEmail());
		boolean authenticated = (Objects.nonNull(userPojo) && Objects.equals(userPojo.getPassword(), loginForm.getPassword()));
		if (!authenticated) {
			info.setMessage("Invalid username or password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}

		Authentication authentication = convert(userPojo);
		HttpSession session = req.getSession(true);
		SecurityUtil.createContext(session);
		SecurityUtil.setAuthentication(authentication);

		return new ModelAndView("redirect:/ui/home");
	}

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}

    public void add(UserForm userForm) throws ApiException {
        ValidateUtil.validateForms(userForm);

        ValidateUtil.validateEmail(userForm.getEmail());
        ValidateUtil.validatePassword(userForm.getPassword());

        try{
            userForm.setPassword(PasswordUtil.generateStorngPasswordHash(userForm.getPassword()));
        }
        catch(Exception e){
            throw new ApiException("Password hashing failed");
        }

        UserPojo userPojo = ConvertUtil.objectMapper(userForm, UserPojo.class);
        service.add(userPojo);
    }

    public void add(LoginForm loginForm) throws ApiException {
        ValidateUtil.validateForms(loginForm);

        ValidateUtil.validateEmail(loginForm.getEmail());
        ValidateUtil.validatePassword(loginForm.getPassword());

        try{
            loginForm.setPassword(PasswordUtil.generateStorngPasswordHash(loginForm.getPassword()));
        }
        catch(Exception exception){
            throw new ApiException("Password hashing failed");
        }
        
        UserPojo userPojo = ConvertUtil.objectMapper(loginForm, UserPojo.class);
        if(userPojo.getEmail().equals(properties.getSupervisorEmail())){
            userPojo.setRole(Roles.SUPERVISOR);
        }
        else{
            userPojo.setRole(Roles.OPERATOR);
        }
        service.add(userPojo);
    }

    public UserData get(String email) throws ApiException {
        UserPojo userPojo = service.get(email);

        return ConvertUtil.objectMapper(userPojo, UserData.class);
    }

    public List<UserData> getAll() {
        List<UserPojo> userPojoList = service.getAll();
        List<UserData> userDataList = new ArrayList<UserData>();
        for (UserPojo userPojo : userPojoList) {
            userDataList.add(ConvertUtil.objectMapper(userPojo, UserData.class));
        }
        return userDataList;
    }

    public void delete(Integer id) {
        service.delete(id);
    }

    private static Authentication convert(UserPojo userPojo) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(userPojo.getEmail());
		principal.setRole(String.valueOf(userPojo.getRole()));
		principal.setId(userPojo.getId());

		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(String.valueOf(userPojo.getRole())));
		// you can add more roles if required

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}

}
