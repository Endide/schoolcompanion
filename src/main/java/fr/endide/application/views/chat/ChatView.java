package fr.endide.application.views.chat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.hibernate.mapping.Array;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Horizontal;

import fr.endide.application.data.entity.Message;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.entity.Topics;
import fr.endide.application.data.service.StudentService;
import fr.endide.application.data.service.TopicsRepository;
import fr.endide.application.data.service.TopicsService;
import fr.endide.application.views.MainLayout;

@PageTitle("Chat")
@Route(value = "", layout = MainLayout.class)
@RolesAllowed({ "USER", "ADMIN" })
public class ChatView extends Div {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();
    TopicsService topicsService;
    StudentService studentService;

    public ChatView(TopicsService topicsService, StudentService studentService) {
        this.topicsService = topicsService;
        this.studentService = studentService;
        addClassName("chat-view");
        TabSheet topicsTabs = new TabSheet();
        add(topicsTabs);
        LoadData(topicsTabs);
    }

    public void LoadData(TabSheet topicsTabs) {
        Student currentStudent = studentService.getByEmail(currentPrincipalName);
        List<Topics> topicsJoined = new ArrayList<>();
        for (String topicsId : currentStudent.getTopicsJoined()) {
            topicsJoined.add(topicsService.get(UUID.fromString(topicsId)));
        }
        for (Topics topic : topicsJoined) {

            MessageList messageList = new MessageList();
            VerticalLayout messageLayout = new VerticalLayout();
            HorizontalLayout inputLayout = new HorizontalLayout();
            List<MessageListItem> preItems = new ArrayList<MessageListItem>();
            for (Message message : topic.getMessages()) {
                Student author = studentService.getByEmail(message.getAuthor());
                MessageListItem preMessages = new MessageListItem(message.getText(), message.getDate().toInstant(),
                        author.getFirstName() + " " + author.getLastName());
                preItems.add(preMessages);
            }
            messageList.setItems(preItems);
            MessageInput messageInput = new MessageInput();
            messageInput.addSubmitListener(submitEvent -> {
                MessageListItem newMessage = new MessageListItem(
                        submitEvent.getValue(), Instant.now(),
                        currentStudent.getFirstName() + " " + currentStudent.getLastName());
                newMessage.setUserColorIndex(3);
                List<MessageListItem> items = new ArrayList<>(messageList.getItems());
                items.add(newMessage);
                messageList.setItems(items);
                Message userNewMessage = new Message();
                userNewMessage.setDate(new Date());
                userNewMessage.setText(submitEvent.getValue());
                userNewMessage.setAuthor(currentStudent.getEmail());
                userNewMessage.setTopics(topic);
                topic.getMessages().add(userNewMessage);
                topicsService.update(topic);
            });
            Button delButton = new Button(new Icon(VaadinIcon.TRASH));
            delButton.addClickListener(e ->{
              topicsService.remove(topic);
              Notification.show("Conversation supprimer avec succés");
              currentStudent.getTopicsJoined().remove(topic.getId().toString());
              UI.getCurrent().navigate(ChatView.class);
            });
            inputLayout.add(messageInput, delButton);
            messageLayout.add(messageList, inputLayout);
            messageLayout.setSizeFull();
            messageList.setSizeFull();
            messageInput.setWidthFull();

            topicsTabs.add(topic.getName(), messageLayout);
        }

    }
}
