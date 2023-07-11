package fr.endide.application.views.compterendu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.selection.SingleSelect;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import fr.endide.application.data.entity.Cards;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.CardService;
import fr.endide.application.data.service.StudentService;
import fr.endide.application.views.MainLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import com.vaadin.flow.function.SerializableFunction;

@PageTitle("Conseil de classe Admin")
@Route(value = "compterendu/:studentID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class CompteRenduView extends Div implements BeforeEnterObserver {

    private final String STUDENT_ID = "studentID";

    private Grid<Student> grid = new Grid<>(Student.class, false);
    private Grid<Cards> cardGrid = new Grid<>(Cards.class, false);

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Create");
    private Button delete = new Button("Delete");
    private BeanValidationBinder<Student> binder;

    private Student student;

    private Student currentStudent;

    private TextField email;
    private TextField title;
    private TextField text;

    HorizontalLayout selectedCards;

    private final StudentService studentService;
    private Cards currentCards;
    private final CardService cardService;

    @Autowired
    public CompteRenduView(StudentService studentService, CardService cardService) {
        this.studentService = studentService;
        this.cardService = cardService;
        addClassNames("eleves-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        add(splitLayout);

        cardGrid.addColumn(Cards::getName).setHeader("Titre").setAutoWidth(true);
        cardGrid.addColumn(Cards::getDescription).setHeader("Avis du conseil de classe").setAutoWidth(true);

        cardGrid.setHeight("85%");
        cardGrid.setSelectionMode(SelectionMode.SINGLE);
        SingleSelect<Grid<Cards>, Cards> cardSelect = cardGrid.asSingleSelect();
        cardSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                currentCards = e.getValue();
            } else {
                currentCards = null;
            }

        });
        delete.addClickListener(e -> {
            if (cardService.exists(currentCards.getId()) || currentCards != null) {
                cardService.delete(currentCards.getId());
                UI.getCurrent().navigate(CompteRenduView.class);
                populateCardsPreview();
            } else {
                Notification.show("Vous n'avez pas selectionner de compte-rendu");
            }
        });
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
        email.setEnabled(false);
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                Cards card = new Cards();
                card.setName(title.getValue());
                card.setEmail(email.getValue());
                card.setDescription(text.getValue());
                cardService.update(card);
                Notification.show("Le compte-rendu a bien été ajouté");
                populateCardsPreview();
                UI.getCurrent().navigate(CompteRenduView.class);

            } finally {

            }

        });
        grid.setSelectionMode(SelectionMode.SINGLE);
        SingleSelect<Grid<Student>, Student> personSelect = grid.asSingleSelect();
        personSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                populateForm(e.getValue());
                currentStudent = e.getValue();
                populateCardsPreview();
            } else {
                clearForm();
                List<Cards> nulllist = new ArrayList<>();
                cardGrid.setItems(nulllist);
                currentStudent = null;
            }
        });

    }

    private void populateCardsPreview() {
        if (currentStudent != null) {
            List<Cards> userCards = cardService.getAllByEmail(currentStudent.getEmail());
            cardGrid.setItems(userCards);
        }
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
                // refresh grid099d35a0-a2aa-4360-a920-5aed278cd664
                refreshGrid();
                event.forwardTo(CompteRenduView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        selectedCards = new HorizontalLayout();

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        email = new TextField("Email");
        title = new TextField("Titre");
        text = new TextField("Compte Rendu");
        Component[] fields = new Component[] { email, title, text };

        formLayout.add(fields);
        editorDiv.add(formLayout, cardGrid);
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
