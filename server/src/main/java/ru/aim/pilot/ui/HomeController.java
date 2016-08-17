package ru.aim.pilot.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.aim.pilot.repository.TerritoryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    private final TerritoryRepository territoryRepository;

    @Autowired
    public HomeController(TerritoryRepository territoryRepository) {
        this.territoryRepository = territoryRepository;
    }

    @RequestMapping("/")
    String index(Model model) {
        model.addAttribute("territories", territoryRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }
}
