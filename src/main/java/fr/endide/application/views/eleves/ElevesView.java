package fr.endide.application.views.eleves;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.generator.passwordGenerator;
import fr.endide.application.data.service.StudentService;
import fr.endide.application.mail.mailManager;
import fr.endide.application.views.MainLayout;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;

import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@PageTitle("Eleves")
@Route(value = "eleves/:studentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class ElevesView extends Div implements BeforeEnterObserver {

    private final String STUDENT_ID = "studentID";

    private Grid<Student> grid = new Grid<>(Student.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField email;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Create");
    private Button delete = new Button("Delete");

    private BeanValidationBinder<Student> binder;

    private Student student;

    private final StudentService studentService;

    @Autowired
    public ElevesView(StudentService studentService) {
        this.studentService = studentService;
        addClassNames("eleves-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("lastName").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.setItems(query -> studentService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // Configure Form
        binder = new BeanValidationBinder<>(Student.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        delete.addClickListener(e -> {
           
                if (!studentService.exists(email.getValue())) {
                    Notification.show("Vous devez selectionner un utilisateur.");
                }else {
                    Student selectedStudent = studentService.getByEmail(email.getValue());
                    if(selectedStudent.getEmail().equals("admin@schoolcompanion.com")){
                        Notification.show("Vous ne pouvez pas supprimer un compte admin");
                        
                    }else{
                        studentService.delete(selectedStudent.getId());
                        Notification.show("Utilisateur suprimer");
                        UI.getCurrent().navigate(ElevesView.class);
                    }
                }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (studentService.exists(email.getValue())) {
                    Notification.show("L'adresse email est déjà utilisée");
                }else {
                    Student newStudent = new Student();
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String currentPrincipalName = authentication.getName();
                    Student currentStudent = studentService.getByEmail(currentPrincipalName); 
                    newStudent.setFirstName(firstName.getValue());
                    newStudent.setLastName(lastName.getValue());
                    newStudent.setEmail(email.getValue());
                    newStudent.setRoles("USER");
                    newStudent.setSchoolLevel(currentStudent.getSchoolLevel());
                    newStudent.setProfilePicture(null);
                    String key = passwordGenerator.generateRandomSpecialCharacters(10);
                    newStudent.setHashedPassword(new BCryptPasswordEncoder().encode(key));
                    try {
                        mailManager.sendMessage(key, email.getValue());
                    } catch (EmailException ex) {
                        throw new RuntimeException(ex);
                    }
                    studentService.update(newStudent);
                    binder.writeBean(newStudent);
                    clearForm();
                    refreshGrid();
                    Notification.show("Eleves créer un mail avec son mot de passe est " + key);
                    UI.getCurrent().navigate(ElevesView.class);
                }
                } catch (ValidationException validationException) {
                Notification.show("ERROR.");
            }
        });
        grid.setSelectionMode(SelectionMode.SINGLE);
        SingleSelect<Grid<Student>, Student> personSelect = grid.asSingleSelect();
        personSelect.addValueChangeListener(e -> {
            populateForm(e.getValue());
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> studentId = event.getRouteParameters().get(STUDENT_ID).map(UUID::fromString);
        if (studentId.isPresent()) {
            Optional<Student> studentFromBackend = studentService.get(studentId.get());
            if (studentFromBackend.isPresent()) {
                populateForm(studentFromBackend.get());
            } else {
                Notification.show(String.format("The requested student was not found, ID = %s", studentId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ElevesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        Component[] fields = new Component[]{firstName, lastName, email};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }
    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel, delete);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid); 
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Student value) {
        this.student = value;
        binder.readBean(this.student);

    }
}
