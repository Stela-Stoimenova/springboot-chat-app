package ro.sisc.ttj.inttjence.dtos;

import java.util.List;

public class MessageRequestDto {
    private String model;
    private List<MessageDto> messages;

    // Getters and setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }
}
