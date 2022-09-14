package fr.endide.application.views.avis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.endide.application.views.MainLayout;

import javax.annotation.security.RolesAllowed;

@PageTitle("Avis")
@Route(value = "avis", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
public class AvisView extends Div {
    private TextField question = new TextField("Votre Question ?");
    private TextField topic = new TextField("Nom du topic :");
    private Button send = new Button("Envoyer");

    private Component createTitle() {
        return new H3("Personal information");
    }
    public AvisView(){

        add(createTitle(), createFormLayout(), createButtonLayout());
    }
    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(question, topic);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(send);
        return buttonLayout;
    }
}
