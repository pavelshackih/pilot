package ru.aim.pilot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.aim.pilot.model.Territory;
import ru.aim.pilot.service.RevisionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserSessionInterceptor extends HandlerInterceptorAdapter {

    public static final String TERRITORY_ID = "territoryId";

    private final RevisionService revisionService;

    @Autowired
    public UserSessionInterceptor(RevisionService revisionService) {
        this.revisionService = revisionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = false;
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals("ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                Long territoryId = (Long) request.getSession().getAttribute(TERRITORY_ID);
                if (territoryId == null) {
                    Territory territory = revisionService.findTerritoryByUserName(authentication.getName());
                    if (territory != null) {
                        territoryId = territory.getId();
                        request.getSession().setAttribute(TERRITORY_ID, territoryId);
                    } else {
                        throw new IllegalStateException(String.format("Territory should be provided for user %s", authentication.getName()));
                    }
                }
            }
        }
        return super.preHandle(request, response, handler);
    }
}
