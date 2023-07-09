package fr.endide.application.views.avis;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.endide.application.data.entity.Message;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.entity.Topics;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.data.service.TopicsRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

@PageTitle("Avis")
@Route(value = "avis", layout = MainLayout.class)
@RolesAllowed({ "ADMIN", "USER" })
public class AvisView extends Div {
    private TextField question = new TextField("Votre Question ?");
    private TextField topic = new TextField("Nom du topic :");
    private Button send = new Button("Envoyer");
    private H3 title = new H3("Poser une question a vos délégués");

    private Component createTitle() {
        return title;
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    StudentRepository repository;
    TopicsRepository topicsRepository;

    @Autowired
    public AvisView(StudentRepository repository, TopicsRepository topicsRepository) {
        this.repository = repository;
        this.topicsRepository = topicsRepository;
        addClassName("avis-view");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addClickListener(click -> {
            if (!question.isEmpty() && !topic.isEmpty()) {
                Topics newTopic = new Topics();
                List<Message> messages = new ArrayList<>();
                Message firstMess = new Message();
                firstMess.setDate(new Date());
                firstMess.setText(question.getValue());
                firstMess.setAuthor(currentPrincipalName);
                firstMess.setTopics(newTopic);
                messages.add(firstMess);
                newTopic.setName(topic.getValue());
                newTopic.setMessages(messages);
                topicsRepository.save(newTopic);
                Student currentStudent = repository.findByEmail(currentPrincipalName);
                List<String> topicsjoined = currentStudent.getTopicsJoined();
                topicsjoined.add(newTopic.getId().toString());
                currentStudent.setTopicsJoined(topicsjoined);
                repository.save(currentStudent);
                Student adminUser = repository.findByEmail("admin@schoolcompanion.com");
                List<String> adminTopicsJoined = adminUser.getTopicsJoined();
                adminTopicsJoined.add(newTopic.getId().toString());
                adminUser.setTopicsJoined(adminTopicsJoined);
                repository.save(adminUser);
                Notification.show("Conversation disponible dans la section Chat");

            } else {
                Notification.show("Vous n'avez pas remplis tout les champs");
            }
        });
        buttonLayout.add(send);
        add(createTitle(), createFormLayout(), buttonLayout);
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        if (currentPrincipalName.equals("admin@schoolcompanion.com")) {
            send.setVisible(false);
            question.setEnabled(false);
            topic.setEnabled(false);
            title.setText(
                    "Poser une question a vos délégués (vous etes déjà un délégués du coup cette page a été desactiver pour vous)");
        }
        formLayout.add(question, topic);
        return formLayout;
    }
}
