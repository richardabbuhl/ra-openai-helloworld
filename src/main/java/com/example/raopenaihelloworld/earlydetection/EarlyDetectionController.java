package com.example.raopenaihelloworld.earlydetection;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class EarlyDetectionController {

    private static final Logger logger = LoggerFactory.getLogger(EarlyDetectionController.class);

    private final ChatClient chatClient;

    @Autowired
    public EarlyDetectionController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/detect")
    public EarlyDetectionInfo generate(@RequestParam(value = "deployements", defaultValue = "0.0") String deployements) {
        var outputParser = new BeanOutputParser<>(EarlyDetectionInfo.class);

        String format = outputParser.getFormat();
        logger.info("format: {}", format);
        String userMessage = """
                What is the probabilty the system will go down in the next 1 hour?
                Number of deployments in the last 24 hours: {deployments}
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("deployments", deployements, "format", format));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        return outputParser.parse(generation.getOutput().getContent());
    }


}
