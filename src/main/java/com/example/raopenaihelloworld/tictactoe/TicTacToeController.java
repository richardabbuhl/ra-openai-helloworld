package com.example.raopenaihelloworld.tictactoe;

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
public class TicTacToeController {

    private static final Logger logger = LoggerFactory.getLogger(TicTacToeController.class);

    private final ChatClient chatClient;

    @Autowired
    public TicTacToeController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/tictactoe/formattted")
    public Board getNextMoveFormatted(@RequestParam(value = "board", defaultValue = "X - - X - - - - -") String board, @RequestParam(value = "player", defaultValue = "X") String player) {
        var outputParser = new BeanOutputParser<>(Board.class);
        logger.info("board: {}", board);

        String format = outputParser.getFormat();
        logger.info("format: " + format);
        String userMessage = """
                Given this tic-tac-toe board {board} please generate the next move for player {player}. Mark the next move with an 'A'. The board is formatted as follows:
                {format}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(userMessage, Map.of("board", board, "player", player, "format", format));
        Prompt prompt = promptTemplate.create();
        Generation generation = chatClient.call(prompt).getResult();
        return outputParser.parse(generation.getOutput().getContent());
    }

    @GetMapping("/ai/tictactoe/simple")
    public String getNextMoveSimple(@RequestParam(value = "board", defaultValue = "X - -, X - -, - - -") String board, @RequestParam(value = "player", defaultValue = "X") String player) {
        String format = "Your response should be in JSON format.\n" +
                "Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.\n" +
                "Do not include markdown code blocks in your response.\n" +
                "Here is the JSON Schema instance your output must adhere to:\n" +
                "```{\n" +
                "  \"$schema\" : \"https://json-schema.org/draft/2020-12/schema\",\n" +
                "  \"type\" : \"object\",\n" +
                "  \"properties\" : {\n" +
                "      \"board\" : \"string\"\n" +
                "      \"indexNextMove\" : \"integer\"\n" +
                "    }\n" +
                "  }\n" +
                "}```\n";
        return chatClient.call("Given this tic-tac-toe board " + board + " please generate the next move and mark it with an 'A' for player " + player + " format " + format);
    }

}
