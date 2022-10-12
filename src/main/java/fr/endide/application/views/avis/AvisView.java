package fr.endide.application.views.avis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.RolesAllowed;

@PageTitle("Avis")
@Route(value = "avis", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
public class AvisView extends Div {
    private TextField question = new TextField("Votre Question ?");
    private TextField topic = new TextField("Nom du topic :");
    private Button send = new Button("Envoyer");

    private Component createTitle() {
        return new H3("Envoyer un avis");
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    StudentRepository repository;
    @Autowired
    public AvisView(StudentRepository repository) {
        this.repository = repository;
        addClassName("avis-view");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addClickListener(click -> {
        });
        buttonLayout.add(send);
        add(createTitle(), createFormLayout(), buttonLayout);
    }
    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(question, topic);
        return formLayout;
    }
}
