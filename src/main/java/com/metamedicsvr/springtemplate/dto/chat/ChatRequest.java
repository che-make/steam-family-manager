package com.metamedicsvr.springtemplate.dto.chat;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {

    @NotNull(message = "JobId is mandatory")
    private Long jobId;

    @NotNull(message = "Message is mandatory")
    private ConversationMessage message;

    private List<ConversationMessage> conversationHistory;

}