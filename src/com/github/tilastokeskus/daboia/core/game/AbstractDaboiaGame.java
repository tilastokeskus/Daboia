
package com.github.tilastokeskus.daboia.core.game;

import com.github.tilastokeskus.daboia.core.Direction;
import com.github.tilastokeskus.daboia.core.Piece;
import com.github.tilastokeskus.daboia.core.Player;
import com.github.tilastokeskus.daboia.core.Snake;
import java.util.List;

public abstract class AbstractDaboiaGame extends DaboiaGame {
    
    protected int numPlayersAlive;
    protected int numMovesNotEaten;
    protected Board board;    
    private boolean placeApples;
    
    public AbstractDaboiaGame(List<Player> players, int width, int height) {
        super(players, width, height);
        this.numPlayersAlive = players.size();
        this.numMovesNotEaten = 0;
        this.board = new Board(width, height);
        this.placeApples = true;
    }
    
    @Override
    public void reset() {
        this.numPlayersAlive = players.size();
        this.numMovesNotEaten = 0;
        this.board = new Board(width, height);
        
        for (Player player : players) {
            player.reset();
        }
    }
    
    @Override
    public void enableApples(boolean enable) {
        this.placeApples = enable;
    }
    
    @Override
    public void setApple(Piece newApple) {
        this.board.setApple(newApple);
    }
    
    @Override
    public Piece getApple() {
        return this.board.getApple();
    }
    
    @Override
    public int[][] getBoard() {
        return this.board.getCore();
    }
    
    @Override
    public void makeMove(Player player, Direction direction) {
        moveSnake(player.getSnake(), direction);
        checkIfAppleEaten(player.getSnake());
        
        if (playerShouldDie(player)) {
            handlePlayerDeath(player);
        }
        
        this.board.refresh(players);
    }
    
    @Override
    public boolean isGameOver() {
        return isInfiniteGame()
               || board.numUnoccupied() == 0
               || numPlayersAlive == 0
               || (numPlayers() > 1 && numPlayersAlive < 2);
    }
    
    @Override
    public int numUnoccupied() {
        return this.board.numUnoccupied();
    }
    
    @Override
    public int numPlayersAlive() {
        return this.numPlayersAlive;
    }
    
    /**
     * Check if the game has been running for too long without an apple being eaten.
     * @return   true or false.
     */
    private boolean isInfiniteGame() {
        int area = this.board.getWidth() * this.board.getHeight();
        return this.numMovesNotEaten > this.numPlayersAlive * area * 3;
    }
    
    private boolean playerShouldDie(Player player) {        
        return playerIsOutOfBounds(player) || playerCollidesWithSomeone(player);
    }
    
    private boolean playerIsOutOfBounds(Player player) {
        int x = player.getSnake().getHead().x;
        int y = player.getSnake().getHead().y;
        return x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight();
    }
    
    private boolean playerCollidesWithSomeone(Player player) {
        /* also checks for collision with itself */
        for (Player otherPlayer : this.players) {
            if (!otherPlayer.isAlive()) continue;
            
            if (player.getSnake().collidesWith(otherPlayer.getSnake())) {
                return true;
            }
        }    
        
        return false;
    }
    
    protected void moveSnake(Snake snake, Direction direction) {
        this.board.set(snake.getHead().x, snake.getHead().y, BoardConstants.SNAKE_BODY);
        this.board.set(snake.getTail().x, snake.getTail().y, BoardConstants.FLOOR);
        
        snake.move(direction);
        this.numMovesNotEaten++;
        
        this.board.set(snake.getHead().x, snake.getHead().y, BoardConstants.SNAKE_HEAD);
        this.board.set(snake.getTail().x, snake.getTail().y, BoardConstants.SNAKE_BODY);
    }
    
    protected void checkIfAppleEaten(Snake snake) {
        if (snake.collidesWith(this.getApple())) {
            snake.grow();
            this.placeApple();
            this.numMovesNotEaten = 0;
        }
    }
    
    private void placeApple() {
        if (this.placeApples) {
            this.board.placeApple();
        }
    }
    
    protected void handlePlayerDeath(Player player) {
        player.setIsAlive(false);
        numPlayersAlive--;
        
        for (Piece piece : player.getSnake().getPieces()) {
            this.board.set(piece.x, piece.y, BoardConstants.FLOOR);
        }
        
        if (this.numPlayers() > 1 && numPlayersAlive > 1) {
            player.setShouldBeDrawn(false);
        }
    }

}