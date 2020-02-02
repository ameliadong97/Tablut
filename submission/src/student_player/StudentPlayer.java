package student_player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutBoardState.Piece;
import tablut.TablutMove;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer {
    
    
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260654160");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState boardState) {
        
        List<TablutMove> options = boardState.getAllLegalMoves();
        
        // Set an initial move as some random one.
        TablutMove myMove = options.get(1);
        
        int opponent = boardState.getOpponent();
        int player = 0;
        if (opponent == 0) {
            player = 1;
        }
        
        // If player is SWEDE 
        if (player == 1) {
            if (MyTools.isFirstMoveForSwede(boardState)) {
                if (boardState.getPieceAt(7, 5) == Piece.EMPTY) {
                    myMove = new TablutMove(4,5,7,5,1);
                } else {
                    myMove = new TablutMove(4,5,1,5,1);
                }
                return myMove;
            } else {
            			for (TablutMove move: options) {
            				if (MyTools.isWinning(boardState, move)) {
            					return move;
            				}
            			}
                		// Using Minimax with alpha-beta pruning
                		myMove = MyTools.alphaBetaSearch(boardState);
            }
        } 
        
        // If player is MUSCOVITE
        if (player == 0) {
            if (MyTools.isInitialState(boardState)) {
                myMove = new TablutMove(3,0,3,1,0);
                return myMove;
            } else {
            		for (TablutMove move: options) {
            			if (MyTools.isWinning(boardState, move)) {
            				return move;
            			}
            		}
            		// Using Minimax with alpha-beta pruning
            		myMove = MyTools.alphaBetaSearch(boardState);
            }        
        }

   
        // Return your move to be processed by the server.
        return myMove;
        }
        
        
}