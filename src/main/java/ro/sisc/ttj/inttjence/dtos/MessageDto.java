package ro.sisc.ttj.inttjence.dtos;

public class MessageDto {
    private String role;
    private String content;
    private String model;

    // Constructor, getters, and setters
    public MessageDto(String role, String content, String model) {
        this.role = role;
        this.content = content;
        this.model = model;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
