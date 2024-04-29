package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.User;

@Controller
public class ProfileController {
	private static final Logger log = LogManager.getLogger(ProfileController.class);
    
    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;
    
    @GetMapping("/perfil")
    public String perfil(Model model) {
        long userId = ((User)httpSession.getAttribute("u")).getId();
        TypedQuery<User> u_query = entityManager.createQuery("select u from User u where u.id = :userId", User.class);
        u_query.setParameter("userId", userId);
        u_query.setMaxResults(1);
        User u;

        try {
            u = u_query.getSingleResult();
        }
        catch (NoResultException e) {
            log.error("Error: " +e);
            return "error";
        }
        catch (Exception e) {
            log.error("Error: " +e);
            return "error";
        }
        
        model.addAttribute("username", u.getUsername());

        return "perfil";
    }
}
