package com.example.raopenaihelloworld.bandname;

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
public class BandController {

    private static final Logger logger = LoggerFactory.getLogger(BandController.class);

    private final ChatClient chatClient;

    @Autowired
    public BandController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/bandname")
    public BandInfo generate(@RequestParam(value = "genre", defaultValue = "Rock") String genre, @RequestParam(value = "description", defaultValue = "cool") String description) {
        var outputParser = new BeanOutputParser<>(BandInfo.class);

        String format = outputParser.getFormat();
        logger.info("format: {}", format);
        String userMessage = """
                Please generate a cool band name for the genre {genre} which is {description}
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("genre", genre, "description", description, "format", format));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        return outputParser.parse(generation.getOutput().getContent());
    }

    @GetMapping("/ai/bandname2")
    public BandInfo generate2(@RequestParam(value = "genre", defaultValue = "Rock") String genre,
                              @RequestParam(value = "description", defaultValue = "cool") String description) {
        var outputParser = new BeanOutputParser<>(BandInfo.class);

        // Simulate retrieval of relevant data (replace with actual retrieval logic)
        String retrievedData = retrieveRelevantData();

        String format = outputParser.getFormat();
        logger.info("format: {}", format);
        String userMessage = """
                Please generate a cool band name for the genre {genre} which is {description}.
                Use the following additional context to improve the results:
                {retrievedData}
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage,
                Map.of("genre", genre, "description", description, "retrievedData", retrievedData, "format", format));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();

        return outputParser.parse(generation.getOutput().getContent());
    }

    // Example retrieval method (replace with actual implementation)
    private String retrieveRelevantData() {
        // Simulate retrieval logic (e.g., query a database or vector store)
        return "Popular bands in this genre include XYZ. The genre is known for ABC characteristics.";
    }
}
