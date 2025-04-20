package ro.sisc.ttj.inttjence.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;
import ro.sisc.ttj.inttjence.dtos.MessageAnswerDto;
import ro.sisc.ttj.inttjence.dtos.MessageDto;
import ro.sisc.ttj.inttjence.dtos.MessageRequestDto;
import ro.sisc.ttj.inttjence.models.Message;
import ro.sisc.ttj.inttjence.models.Model;
import ro.sisc.ttj.inttjence.repository.MessageRepository;
import ro.sisc.ttj.inttjence.repository.ModelRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private ModelRepository modelRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

//    public MessageService(MessageRepository messageRepository, ModelRepository modelRepository) {
//        this.messageRepository = messageRepository;
//        this.modelRepository = modelRepository;
//    }

    public List<MessageDto> getAllMessages() {
        List<Message> messagesFromDb = messageRepository.findAllByOrderByInsertTimestampAsc();

        return messagesFromDb.stream()
                .map(m -> new MessageDto(m.getRole(), m.getContent(), m.getModel()))
                .collect(Collectors.toList());
    }

    public MessageDto createMessage(MessageDto dto) {
        // Create message and store it
        Message savedMessage = createMessageFromDto(dto);

        // Create a context of previous messages for the chat
        List<MessageDto> contextMessages = messageRepository.findAllByOrderByInsertTimestampAsc().stream()
                .map(m -> new MessageDto(m.getRole(), m.getContent(), m.getModel()))
                .collect(Collectors.toList());

        // Fetch model details for API call
        Model targetModel = modelRepository.findFirstByName(dto.getModel());
        if (targetModel == null) {
            throw new RuntimeException("Model '" + dto.getModel() + "' not found in the database.");
        }

        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setModel(dto.getModel());
        messageRequestDto.setMessages(contextMessages);

        String serializedRequest;
        try {
            serializedRequest = objectMapper.writeValueAsString(messageRequestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed.", e);
        }

        // Prepare and execute API request
        Request request = new Request.Builder()
                .url(targetModel.getUrl())
                .post(RequestBody.create(serializedRequest, MediaType.parse("application/json")))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + targetModel.getApiKey())
                .build();

        try (Response httpResponse = httpClient.newCall(request).execute()) {
            if (!httpResponse.isSuccessful() || httpResponse.body() == null) {
                throw new RuntimeException("Connection to LLM failed.");
            }

            // Deserialize response and save the answer
            MessageAnswerDto responseDto = objectMapper.readValue(httpResponse.body().byteStream(), MessageAnswerDto.class);
            MessageDto finalResponse = responseDto.getChoices().get(0).getMessage();

            // Save final response in DB
            createMessageFromDto(finalResponse);

            return finalResponse;
        } catch (IOException e) {
            throw new RuntimeException("Error during HTTP request execution.", e);
        }
    }

    private Message createMessageFromDto(MessageDto dto) {
        Message message = new Message();
        message.setContent(dto.getContent());
        message.setRole(dto.getRole());
        message.setModel(dto.getModel());
        message.setInsertTimestamp(LocalDateTime.now(ZoneId.of("UTC")));
        return messageRepository.save(message);
    }
}
