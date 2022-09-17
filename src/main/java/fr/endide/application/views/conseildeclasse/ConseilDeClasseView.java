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
import fr.endide.application.data.entity.Cards;
import fr.endide.application.data.service.CardRepository;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.security.RolesAllowed;

@PageTitle("Conseil De Classe")
@Route(value = "conseil-de-classe", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
public class ConseilDeClasseView extends Div implements AfterNavigationObserver {

    Grid<Cards> grid = new Grid<>();
    CardRepository repository;
    @Autowired
    public ConseilDeClasseView(CardRepository repository) {
        this.repository = repository;
        addClassName("conseil-de-classe-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(cards -> createCard(cards));
        add(grid);
    }

    private HorizontalLayout createCard(Cards cards) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(cards.getName());
        name.addClassName("name");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Icon likeIcon = VaadinIcon.DOWNLOAD.create();
        likeIcon.addClassName("icon");
        return null;
    }
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Cards cards = repository.findByEmail("admin@schoolcompanion.com");
        // Set some data when this view is displayed.
        List<Cards> cardsList = new ArrayList<>();
        cardsList.add(cards);
        grid.setItems(cardsList);
    }

}
