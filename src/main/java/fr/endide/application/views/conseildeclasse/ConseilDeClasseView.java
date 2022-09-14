package fr.endide.application.views.conseildeclasse;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.endide.application.views.MainLayout;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;

@PageTitle("Conseil De Classe")
@Route(value = "conseil-de-classe", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
public class ConseilDeClasseView extends Div  {

   public ConseilDeClasseView() {
       addClassName("conseil-de-classe-view");

   }

}
