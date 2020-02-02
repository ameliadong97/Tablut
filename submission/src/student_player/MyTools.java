package student_player;

import java.util.*;

import coordinates.Coord;
import coordinates.Coordinates;
import coordinates.Coordinates.CoordinateDoesNotExistException;
import tablut.TablutBoardState;
import tablut.TablutMove;
import tablut.TablutBoardState.Piece;

public class MyTools {
    
    public static boolean isInitialState(TablutBoardState boardState) {
        
    // Clone the current boardState
    TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
    
    // Check if the current boardState is the initial boardState
        if (cloneBS.getPieceAt(4, 4) != Piece.KING) {
            return false;
        } else {
            List<Integer> blackSidePieces1 = Arrays.asList(3, 4, 5);
            List<Integer> blackSidePieces2 = Arrays.asList(0, 8);
            for (Integer x: blackSidePieces1 ) {
                for (Integer y : blackSidePieces2) {
                    if (cloneBS.getPieceAt(x, y) == Piece.BLACK && cloneBS.getPieceAt(y, x) == Piece.BLACK) {
                        continue;
                    } else {
                            return false;
                    }
                }
            }
            if (cloneBS.getPieceAt(4, 1) != Piece.BLACK || cloneBS.getPieceAt(1, 4) != Piece.BLACK || cloneBS.getPieceAt(4, 7) != Piece.BLACK  || cloneBS.getPieceAt(7, 4) != Piece.BLACK) {
                return false;
            }
            List<Integer> whiteSidePieces = Arrays.asList(2, 3, 5, 6);
            for (Integer x: whiteSidePieces ) {
                if (cloneBS.getPieceAt(4, x) == Piece.WHITE && cloneBS.getPieceAt(x, 4) == Piece.WHITE) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
}

    public static TablutMove moveToProtectCorner (TablutBoardState boardState, TablutMove myMove, int minDistToCorner) {
    	
            List<TablutMove> options = boardState.getAllLegalMoves();
            Coord kingPos = boardState.getKingPosition();
            if (minDistToCorner == 1) {
                for (TablutMove move: options) {
                    TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                    cloneBS.processMove(move);
                    if (cloneBS.getWinner() == 0) {
                        return move;
                    }
                }
            }
            if (minDistToCorner == 2) {
                Coord closestCorner = getClosestCorner (boardState, 2);
                if (kingPos.x == closestCorner.x && kingPos.y > closestCorner.y) {
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == kingPos.x && move.getEndPosition().y == 1) {
                            return move;
                        }
                    }
                } else if (kingPos.x == closestCorner.x && kingPos.y < closestCorner.y) {
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == kingPos.x && move.getEndPosition().y == 7) {
                            return move;
                        }
                    }
                } else if (kingPos.x > closestCorner.x && kingPos.y == closestCorner.y) {
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == 1 && move.getEndPosition().y == kingPos.y) {
                            return move;
                        }
                    }
                } else if (kingPos.x < closestCorner.x && kingPos.y == closestCorner.y){
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == 7 && move.getEndPosition().y == kingPos.y) {
                            return move;
                        }
                    }
                }
            
            }
            if (minDistToCorner == 3) {
                Coord closestCorner = getClosestCorner (boardState, 3);
                if (kingPos.x == closestCorner.x && kingPos.y > closestCorner.y) {
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == kingPos.x && (move.getEndPosition().y == 2 || move.getEndPosition().y == 1)) {
                            return move;
                        }
                    }
                } else if (kingPos.x == closestCorner.x && kingPos.y < closestCorner.y) {
                    for (TablutMove move: options) {
                        if (move.getEndPosition().x == kingPos.x && (move.getEndPosition().y == 6 || move.getEndPosition().y == 7)) {
                            return move;
                        }
                    }
                } else if (kingPos.x > closestCorner.x && kingPos.y == closestCorner.y) {
                    for (TablutMove move: options) {
                        if ((move.getEndPosition().x == 2 || move.getEndPosition().x == 1) && move.getEndPosition().y == kingPos.y) {
                            return move;
                        }
                    }
                } else if (kingPos.x < closestCorner.x && kingPos.y == closestCorner.y){
                    for (TablutMove move: options) {
                        if ((move.getEndPosition().x == 6 || move.getEndPosition().x == 7) && move.getEndPosition().y == kingPos.y) {
                            return move;
                        }
                    }
                }
                
            }
            return myMove;
    }
    
    public static Coord getClosestCorner (TablutBoardState boardState, int minDistToCorner) {
            List<Coord> corners = Coordinates.getCorners();
            Coord kingPos = boardState.getKingPosition();
        for (Coord corner : corners) {
            int distance = kingPos.distance(corner);
            if (distance == minDistToCorner) {
                return corner;
            }
        }
        return null;
    }

    public static TablutMove moveGreedy (TablutBoardState boardState, TablutMove myMove, int minNumberOfOpponentPieces) {
                
                List<TablutMove> options = boardState.getAllLegalMoves();
                Coord kingPos = boardState.getKingPosition();
                
                int opponent = boardState.getOpponent();
            int player = 0;
            if (opponent == 0) {
                    player = 1;
            }
                
                // Iterate over move options and evaluate them.
            for (TablutMove move : options) {
                // To evaluate a move, clone the boardState so that we can do modifications on it.
                TablutBoardState cloneBS = (TablutBoardState) boardState.clone();

                // Process that move, as if we actually made it happen.
                cloneBS.processMove(move);

                // Check how many opponent pieces there are now, maybe we captured some!
                int newNumberOfOpponentPieces = cloneBS.getNumberPlayerPieces(opponent);

                // If this move caused some capturing to happen, then do it! 
                if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
                		if (move.getStartPosition().x == kingPos.x && move.getStartPosition().y == kingPos.y) {
                			return move;
                		}
                            myMove = move;
                            minNumberOfOpponentPieces = newNumberOfOpponentPieces;
                }
                
                // Check if this moves caused us to win

                if (cloneBS.getWinner() == player) {
                    myMove = move;
                    break;
                }
            }
            return myMove; 
    }

    public static boolean canWeBeGreedy (TablutBoardState boardState, TablutMove myMove, int minNumberOfOpponentPieces) {
        
        List<TablutMove> options = boardState.getAllLegalMoves();
        
        int opponent = boardState.getOpponent();
        
        // Iterate over move options and evaluate them.
        for (TablutMove move : options) {
            // To evaluate a move, clone the boardState so that we can do modifications on it.
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();

            // Process that move, as if we actually made it happen.
            cloneBS.processMove(move);

            // Check how many opponent pieces there are now, maybe we captured some!
            int newNumberOfOpponentPieces = cloneBS.getNumberPlayerPieces(opponent);

            // If this move caused some capturing to happen, then do it! Greedy!
            if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
                return true;
            }
        }
            return false;
    }
    
    public static boolean isFirstMoveForSwede(TablutBoardState boardState) {
            
            // Clone the current boardState
        TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
        // Check if the current move is the first move for SWEDE
        if (cloneBS.getPieceAt(4, 4) != Piece.KING) {
                return false;
        } else {
                List<Integer> whiteSidePieces = Arrays.asList(2, 3, 5, 6);
                for (Integer x: whiteSidePieces ) {
                    if (cloneBS.getPieceAt(4, x) == Piece.WHITE && cloneBS.getPieceAt(x, 4) == Piece.WHITE) {
                        continue;
                    } else {
                        return false;
                    }
                }
        }
        
            return true;
    }

    public static boolean isWinning(TablutBoardState boardState, TablutMove myMove) {
        int opponent = boardState.getOpponent();
        int player = 0;
        if (opponent == 0) {
            player = 1;
        }
        // Clone the current boardState
        TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
        cloneBS.processMove(myMove);
        if (cloneBS.getWinner() == player) {
            return true;
        }
        return false;
    }
    
    public static boolean willKingBeCapturedNext(TablutBoardState boardState, TablutMove myMove) {
        
        int opponent = boardState.getOpponent();
        int player = 0;
        if (opponent == 0) {
            player = 1;
        }
            // Clone the current boardState
        TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
        cloneBS.processMove(myMove);
        
        Coord kingPos = cloneBS.getKingPosition();
        HashSet<Coord> oppoCoord = cloneBS.getOpponentPieceCoordinates();
        
        // If none of four neighbors is black, we consider the King to be safe
        List<Coord> neighborsKing = new ArrayList<Coord>();
        try {
                neighborsKing.add(Coordinates.get(kingPos.x+1, kingPos.y));
            neighborsKing.add(Coordinates.get(kingPos.x-1, kingPos.y));
            neighborsKing.add(Coordinates.get(kingPos.x, kingPos.y+1));
            neighborsKing.add(Coordinates.get(kingPos.x, kingPos.y-1));
        } catch (ArrayIndexOutOfBoundsException e ) {
            
        }

        for (Coord enemy: neighborsKing) {
                if (cloneBS.getPieceAt(enemy) == Piece.BLACK) {
                    return true;
                }
        }

        
            return false;
    }

    // Minimax Algorithm
    
    public static int computeUtility (TablutBoardState boardState) {
            
        int utility = 0;
            
        if (boardState.getWinner() == 1) {
            return Integer.MAX_VALUE;
        } 
        if (boardState.getWinner() == 0) {
            return Integer.MIN_VALUE;
        }
        
        Coord kingPos = boardState.getKingPosition();
        int numOfBlackPiece = boardState.getNumberPlayerPieces(0);
        int numOfWhitePiece = boardState.getNumberPlayerPieces(1);
        int kingDistToClosestCorner = Coordinates.distanceToClosestCorner(kingPos);
        utility = numOfWhitePiece - kingDistToClosestCorner - numOfBlackPiece;
        
        return utility;
    }
    
    public static boolean isLeafState (int numOfMoves) {
    
        if (numOfMoves == 3) {
            return true;
        }
            return false;
    }
    
    public static TablutMove minimax (TablutBoardState boardState) {

            int opponent = boardState.getOpponent();
            int player = 0;
            List<TablutMove> options = boardState.getAllLegalMoves();
            TablutMove myMove = options.get(1);
            
            // MUSCOVITES = min player; SWEDES = max player
            if (opponent == 0) {
                player = 1;
                int utility = Integer.MIN_VALUE;
                // return argmax (minValue)
                for (TablutMove move: options) {
                    int numOfMoves = 1;
                    TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                    cloneBS.processMove(move);
                    int newUtility = minValue(cloneBS, numOfMoves);
                    if (utility < newUtility) {
                        utility = newUtility;
                        myMove = move;
                    }
                }
            } else {
            		int utility = Integer.MAX_VALUE;
                // else return argmin (maxValue)
                for (TablutMove move: options) {
                    int numOfMoves = 1;
                    TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                    cloneBS.processMove(move);
                    int newUtility = maxValue(cloneBS, numOfMoves);
                    if (utility > newUtility) {
                        utility = newUtility;
                        myMove = move;
                    }
            }
            }
            
            
            return myMove;
    }
    
    public static int maxValue(TablutBoardState boardState, int numOfMoves) {
            if (isLeafState(numOfMoves)) {
                return computeUtility(boardState);
            }
            int utility = Integer.MIN_VALUE;
            List<TablutMove> options = boardState.getAllLegalMoves();
            numOfMoves++;
            for (TablutMove move: options) {
                TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                cloneBS.processMove(move);
                utility = Math.max(utility, minValue(cloneBS, numOfMoves));
            }
            return utility;
    }
    
    public static int minValue(TablutBoardState boardState, int numOfMoves) {
        if (isLeafState(numOfMoves)) {
            return computeUtility(boardState);
        }
        int utility = Integer.MAX_VALUE;
        List<TablutMove> options = boardState.getAllLegalMoves();
        numOfMoves++;
        for (TablutMove move: options) {
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
            cloneBS.processMove(move);
            utility = Math.min(utility, maxValue(cloneBS, numOfMoves));
        }
        return utility;
}
    
    // Minimax with alpha-beta pruning
    
    public static TablutMove alphaBetaSearch (TablutBoardState boardState) {
    	 	int opponent = boardState.getOpponent();
         int player = 0;
         List<TablutMove> options = boardState.getAllLegalMoves();
         TablutMove myMove = options.get(1);
         
         // MUSCOVITES = min player; SWEDES = max player
         if (opponent == 0) {
             player = 1;
             int utility = abMaxValue(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE,0);
             // return the move with the utility that is computed above
             for (TablutMove move: options) {
                 TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                 cloneBS.processMove(move);
                 int newUtility = abMinValue(cloneBS, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                 if (utility == newUtility) {
                     return move;
                 }
             }
         } else {
        	 	int utility = abMinValue(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE,0);
             // return the move with the utility that is computed above
        	 	for (TablutMove move: options) {
                    TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
                    cloneBS.processMove(move);
                    int newUtility = abMaxValue(cloneBS, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                    if (utility == newUtility) {
                        return move;
                    }
                }
         }
         
         
         return myMove;
    	
    }
    
    public static int abMaxValue(TablutBoardState boardState,int alpha, int beta, int numOfMoves) {
    		if (isLeafState(numOfMoves)) {
            return computeUtility(boardState);
        }
        int utility = Integer.MIN_VALUE;
        List<TablutMove> options = boardState.getAllLegalMoves();
        numOfMoves++;
        for (TablutMove move: options) {
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
            cloneBS.processMove(move);
            utility = Math.max(utility, abMinValue(cloneBS, alpha, beta, numOfMoves));
            if (utility >= beta) {
            		return utility;
            }
            alpha = Math.max(utility, alpha);
        }
        return utility;
    }
    
    public static int abMinValue(TablutBoardState boardState,int alpha, int beta, int numOfMoves) {
    		if (isLeafState(numOfMoves)) {
            return computeUtility(boardState);
        }
        int utility = Integer.MAX_VALUE;
        List<TablutMove> options = boardState.getAllLegalMoves();
        numOfMoves++;
        for (TablutMove move: options) {
            TablutBoardState cloneBS = (TablutBoardState) boardState.clone();
            cloneBS.processMove(move);
            utility = Math.min(utility, abMaxValue(cloneBS, alpha, beta, numOfMoves));
            if (utility <= alpha) {
            		return utility;
            }
            beta = Math.min(utility, beta);
        }
        return utility;
}
    
}
