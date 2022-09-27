package fr.endide.application.data.message;

import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.spring.annotation.SpringComponent;
import fr.endide.application.data.entity.Message;
import fr.endide.application.data.entity.Student;
import fr.endide.application.data.service.MessageService;
import fr.endide.application.data.service.StudentService;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.stream.Stream;

@SpringComponent
@EnableTransactionManagement
public class MessagePersister implements CollaborationMessagePersister {

    private final MessageService messageService;
    private final StudentService userService;

    public MessagePersister(MessageService messageService, StudentService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
        Stream<Message> messages = messageService.findAllByTopicSince(query.getTopicId(), query.getSince()).stream();
        return messages.map(messageEntity -> {
                    Student author = userService.getByEmail(messageEntity.getStudentMail());
                    UserInfo userInfo = new UserInfo(author.getEmail(), author.getFirstName() + " " + author.getLastName());
                    return new CollaborationMessage(userInfo, messageEntity.getText(), messageEntity.getDate());
                });
    }
    @Override
    public void persistMessage(PersistRequest request) {
        CollaborationMessage message = request.getMessage();
        Message messageEntity = new Message();
        messageEntity.setTopic(request.getTopicId());
        messageEntity.setText(message.getText());
        messageEntity.setStudentMail(userService.getByEmail(message.getUser().getId()).getEmail());
        messageEntity.setDate(message.getTime());
        messageService.update(messageEntity);
    }
}
