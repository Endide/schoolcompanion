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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.StudentRepository;
import fr.endide.application.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;
import javax.annotation.security.RolesAllowed;

@PageTitle("Chat")
@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})

public class ChatView extends VerticalLayout {
    StudentRepository repository;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentPrincipalName = authentication.getName();

    @Autowired
    public ChatView() {
        addClassName("chat-view");
        setSpacing(false);
        Student student = repository.findByEmail(currentPrincipalName);
        UserInfo userInfo = new UserInfo(student.getId().toString(), student.getFirstName() + " " + student.getLastName(), "https://icon2.cleanpng.com/20180920/att/kisspng-user-logo-information-service-design-5ba34f886b6700.1362345615374293844399.jpg");
        // Tabs allow us to change chat rooms.
        Tabs tabs = new Tabs(new Tab("#classe"));
        tabs.setWidthFull();

        CollaborationMessageList list = new CollaborationMessageList(userInfo, "chat/#general");
        list.setWidthFull();
        list.addClassNames("chat-view-message-list");
        CollaborationMessageInput input = new CollaborationMessageInput(list);
        input.addClassNames("chat-view-message-input");
        input.setWidthFull();

        // Layouting
        add(tabs, list, input);
        setSizeFull();
        expand(list);

        // Change the topic id of the chat when a new tab is selected
        tabs.addSelectedChangeListener(event -> {
            String channelName = event.getSelectedTab().getLabel();
            list.setTopic("chat/" + channelName);
        });
    }

}
