package fr.endide.application.views.moncompte;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import java.util.List;

@PageTitle("Mon Compte")
@Route(value = "mon-compte", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
@Uses(Icon.class)
public class MonCompteView extends Div {

    private TextField firstName = new TextField("Prénom");
    private TextField lastName = new TextField("Nom de famille");
    private PasswordField password = new PasswordField("Mot de Passe");
    private PasswordField repassword = new PasswordField("Retaper le Mot de Passe");
    private Button save = new Button("Save");

    StudentRepository repository;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    private Component createTitle() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName().toString();
        return new H3("Mon compte : " + currentPrincipalName);
    }
    @Autowired
    public MonCompteView(StudentRepository repository){
        this.repository = repository;
        Student student = repository.findByEmail(currentPrincipalName);
        firstName.setValue(student.getFirstName());
        lastName.setValue(student.getLastName());
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        student.setFirstName(firstName.getValue());
        student.setLastName(lastName.getValue());
        save.addClickListener(event -> {
            System.out.println(firstName.getValue());
            repository.save(student);
            Notification.show("Saved");
        });
        buttonLayout.add(save);
        add(createTitle(), createFormLayout(), buttonLayout);
    }


    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, password, repassword);
        return formLayout;
    }

}
