package es.ucm.fdi.iw.controller;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.Report;

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

	// private static final Logger log = LogManager.getLogger(AdminController.class);


	@GetMapping("/")
    @Transactional
    public String index(Model model) {
        TypedQuery<Report> reportQuery = entityManager.createQuery("select g from Report", Report.class);
        var reports = reportQuery.getResultList();
        model.addAttribute("reports", reports);

        return "admin";
    }
}
