package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Report;
import es.ucm.fdi.iw.model.User;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/")
    @Transactional
    public String index(Model model) {
        // Query to get all open reports
        TypedQuery<Report> openReportQuery = entityManager.createQuery("select r from Report r where r.isOpen = true", Report.class);
        var openReports = openReportQuery.getResultList();
        model.addAttribute("openReports", openReports);
        
        // Query to get all closed reports
        TypedQuery<Report> closedReportQuery = entityManager.createQuery("select r from Report r where r.isOpen = false", Report.class);
        var closedReports = closedReportQuery.getResultList();
        model.addAttribute("closedReports", closedReports);
        
        return "admin";
    }

    @PostMapping("/markReport")
    @Transactional
    public String unbanUser(@RequestParam("reportId") Long reportId) {
        // Find the report entity by ID
        Report r = entityManager.find(Report.class, reportId);

        User u = (User)httpSession.getAttribute("u");

        // Only admins
        if (!u.hasRole(User.Role.ADMIN)) return "/error";

        // Check if the report exists
        if (r != null) {
            // Toggle the report open/closed status
            r.setIsOpen(!r.getIsOpen());
        }

        // Redirect back to the current page
        return "redirect:/admin/";
    }    
}
