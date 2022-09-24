package fr.endide.application.views.chat;

import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.message.MessagePersister;
import fr.endide.application.data.service.MessageRepository;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.security.RolesAllowed;

@PageTitle("Chat")
@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})

public class ChatView extends VerticalLayout {
    StudentRepository repository;
    MessageRepository messageRepository;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    @Autowired
    public ChatView(StudentRepository repository, MessagePersister persister, MessageRepository messageRepository){
        this.repository = repository;
        addClassName("chat-view");
        setSpacing(false);
        Tabs tabs = new Tabs(new Tab("#general"));
        tabs.setWidthFull();
        Student student = repository.findByEmail(currentPrincipalName);
        UserInfo userInfo = new UserInfo(student.getEmail(), student.getFirstName() + " " + student.getLastName());
        CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#general", persister);
        list.setWidthFull();
        list.addClassNames("chat-view-message-list");
        CollaborationMessageInput input = new CollaborationMessageInput(list);
        input.addClassNames("chat-view-message-input");
        input.setWidthFull();
        add(tabs,list, input);
        setSizeFull();
        expand(list);
        tabs.addSelectedChangeListener(event -> {
            String channelName = event.getSelectedTab().getLabel();
            list.setTopic("chat/" + channelName);
        });
    }

}
