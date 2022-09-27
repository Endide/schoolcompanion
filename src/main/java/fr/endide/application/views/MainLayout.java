package fr.endide.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.theme.lumo.Lumo;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentService;
import fr.endide.application.security.SecurityService;
import fr.endide.application.views.avis.AvisView;
import fr.endide.application.views.chat.ChatView;
import fr.endide.application.views.conseildeclasse.ConseilDeClasseView;
import fr.endide.application.views.eleves.ElevesView;
import fr.endide.application.views.moncompte.MonCompteView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;
        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {

            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "gap-xs", "h-m", "items-center", "px-s", "text-body");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-m", "whitespace-nowrap");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font styling
                addClassNames("text-l", "text-secondary");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private AccessAnnotationChecker accessChecker;
    StudentService service;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    public MainLayout(AccessAnnotationChecker accessChecker, StudentService service) {
        this.service = service;
        this.accessChecker = accessChecker;

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {

        Header header = new Header();
        header.addClassNames("box-border", "flex", "flex-col", "w-full");

        Div layout = new Div();
        layout.addClassNames("flex", "items-center", "px-l");

        H1 appName = new H1("SchoolCompanion");
        appName.addClassNames("my-m", "me-auto", "text-l");
        layout.add(appName);
        Student student = service.getByEmail(currentPrincipalName);

            Avatar avatar = new Avatar(student.getFirstName() + " " + student.getLastName());
            avatar.setThemeName("xsmall");
            avatar.getElement().setAttribute("tabindex", "-1");

            MenuBar userMenu = new MenuBar();
            userMenu.setThemeName("tertiary-inline contrast");
            MenuItem userName = userMenu.addItem("");
            Div div = new Div();
            div.add(avatar);
            div.add(student.getFirstName() + " " + student.getLastName());
            div.add(new Icon("lumo", "dropdown"));
            div.getElement().getStyle().set("display", "flex");
            div.getElement().getStyle().set("align-items", "center");
            div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
            userName.add(div);
            userName.getSubMenu().addItem("Sign out", e -> {
                SecurityService.logout();
            });
        Button toggleButton = new Button(new Icon(VaadinIcon.MOON), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
            layout.add(toggleButton, userMenu);


        Nav nav = new Nav();
        nav.addClassNames("flex", "overflow-auto", "px-m", "py-xs");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("flex", "gap-s", "list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("Chat", "la la-comments", ChatView.class), //

                new MenuItemInfo("Avis", "la la-paper-plane", AvisView.class), //

                new MenuItemInfo("Conseil De Classe", "la la-list", ConseilDeClasseView.class), //

                new MenuItemInfo("Mon Compte", "la la-user", MonCompteView.class), //

                new MenuItemInfo("Eleves", "la la-universal-access", ElevesView.class), //

        };
    }

}
