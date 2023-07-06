package fr.endide.application.views.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import fr.endide.application.data.entity.Student;
import fr.endide.application.data.entity.Topics;
import fr.endide.application.data.service.StudentService;
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
        Tabs topicsTabs = new Tabs();
        MessageList messageList = new MessageList();
        MessageInput messageInput = new MessageInput();
        add(topicsTabs, messageList, messageInput);
    }

    public void LoadData(Tabs topicsTabs, MessageList messageList, MessageInput messageInput){
        Student currentStudent = studentService.getByEmail(currentPrincipalName);
        List<Topics> topicsJoined = new ArrayList<>(); 
        for(String topicsId : currentStudent.getTopicsJoined()){
            topicsJoined.add(topicsService.get(UUID.fromString(topicsId)));
        }
        for(Topics topic : topicsJoined){
            
            Tab currentTab = new Tab(topic.getName());
            topicsTabs.add(currentTab);
        }
    }
}
