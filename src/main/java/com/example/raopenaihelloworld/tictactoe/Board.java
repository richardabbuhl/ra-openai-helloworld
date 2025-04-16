package com.example.raopenaihelloworld.tictactoe;

import lombok.Data;

import java.util.List;

@Data
public class Board {
    private List<String> board;
    private String nextPlayer;
    private Integer indexNextMove;

}
