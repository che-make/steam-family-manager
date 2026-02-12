package com.metamedicsvr.springtemplate.dto.chat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversationMessage {

    @NotNull(message = "Role is mandatory")
    private ConversationRole role;

    @NotBlank(message = "Content is mandatory")
    private String content;

    // Used to convert the ConversationMessage to a Spring AI Message
    public Message toSpringAiMessage() {
        return switch (role) {
            case USER -> new UserMessage(content);
            case SYSTEM -> new SystemMessage(content);
            case ASSISTANT -> new AssistantMessage(content);
        };
    }
}