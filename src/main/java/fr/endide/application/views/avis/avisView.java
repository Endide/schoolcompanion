package fr.endide.application.views.avis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;

public class avisView extends Div {
    private TextField question = new TextField("Votre Question ?");
    private TextField topic = new TextField("Nom du topic :");
    Checkbox checkbox = new Checkbox("Anonyme");
    private Button send = new Button("Envoyer");

    private Component createTitle() {
        return new H3("Personal information");
    }

    public avisView(){

        add(createTitle(), createFormLayout(), createButtonLayout());
    }
    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(question, topic, checkbox);
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
