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
import fr.endide.application.views.MainLayout;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;

@PageTitle("Chat")
@Route(value = "chat", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})

public class ChatView extends VerticalLayout {

    public ChatView() {
        addClassName("chat-view");
        setSpacing(false);
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Tabs allow us to change chat rooms.
        Tabs tabs = new Tabs(new Tab("#general"), new Tab("#support"), new Tab("#casual"));
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
