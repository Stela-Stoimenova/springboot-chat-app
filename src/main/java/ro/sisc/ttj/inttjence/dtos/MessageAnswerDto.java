package ro.sisc.ttj.inttjence.dtos;

import java.util.List;

public class MessageAnswerDto {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public static class Choice {
        private MessageDto message;

        public MessageDto getMessage() {
            return message;
        }

        public void setMessage(MessageDto message) {
            this.message = message;
        }
    }
}
