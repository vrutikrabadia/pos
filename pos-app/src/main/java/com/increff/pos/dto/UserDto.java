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
import com.increff.pos.service.ApiException;
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

    public ModelAndView login(HttpServletRequest req, LoginForm f) throws ApiException {
		info.setMessage("No message");
		UserPojo p = service.get(f.getEmail());

		boolean passwordValidate = false;
		try{
			passwordValidate = PasswordUtil.validatePassword(f.getPassword(), p.getPassword());
		}
		catch(Exception e){
			info.setMessage("Error validating password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}
		
		boolean authenticated = (p != null && passwordValidate);
		if (!authenticated) {
			info.setMessage("Invalid username or password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}

		// Create authentication object
		Authentication authentication = convert(p);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);

		return new ModelAndView("redirect:/ui/home");

	}

    public ModelAndView signup(HttpServletRequest req, LoginForm f) throws ApiException{
		info.setMessage("No message");
		try{
			add(f);
		}
		catch(ApiException e){
			info.setMessage(e.getMessage());
			info.setShown(false);
			return new ModelAndView("redirect:/site/signup");
		}
		
		UserPojo p = service.get(f.getEmail());
		boolean authenticated = (p != null && Objects.equals(p.getPassword(), f.getPassword()));
		if (!authenticated) {
			info.setMessage("Invalid username or password");
			info.setShown(false);
			return new ModelAndView("redirect:/site/login");
		}

		Authentication authentication = convert(p);
		HttpSession session = req.getSession(true);
		SecurityUtil.createContext(session);
		SecurityUtil.setAuthentication(authentication);

		return new ModelAndView("redirect:/ui/home");
	}

    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}

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
        if(p.getEmail().equals(properties.getSupEmail())){
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

    private static Authentication convert(UserPojo p) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(p.getEmail());
		principal.setRole(String.valueOf(p.getRole()));
		principal.setId(p.getId());

		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(String.valueOf(p.getRole())));
		// you can add more roles if required

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}

}
