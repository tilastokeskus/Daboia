
package daboia.domain;

import daboia.DaboiaLogic;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DaboiaGame {
    
    private final List<Player> players;
    
    private int[][] board;
    private Piece apple;
    private boolean gameOver;    
    private int numPlayersAlive;
    private int numMovesNotEaten;
    
    public DaboiaGame(Collection<Player> players, int width, int height) {
        this.players = new ArrayList<>(players);
        this.apple = new Piece(width/2, height/2);
        this.gameOver = false;
        this.board = new int[height][width];
        this.numPlayersAlive = players.size();
        this.numMovesNotEaten = 0;
        
        this.board[height - 2][1] = -2;
        this.board[apple.y][apple.x] = 8;
    }
    
    public int numPlayers() {
        return this.players.size();
    }
    
    public Player getPlayer(int playerID) {
        if (playerID >= players.size()) {
            return null;
        }
        
        return players.get(playerID);
    }
    
    public DaboiaLogic getLogicHandler(int playerID) {
        return this.players.get(playerID).getLogicHandler();
    }
    
    public void sendKeyInput(char c) {
        for (Player player : this.players) {
            player.getLogicHandler().sendKey(c);
        }
    }
    
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public int width() {
        return this.board[0].length;
    }
    
    public int height() {
        return this.board.length;
    }
    
    public Piece apple() {
        return this.apple;
    }
    
    public int[][] board() {
        int[][] boardCopy = new int[height()][width()];
        for (int i = 0; i < boardCopy.length; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, boardCopy[0].length);
        }
        
        return boardCopy;
    }
    
    public boolean gameOver() {
        return this.gameOver;
    }
    
    public void makeMove(Player player, Direction direction) {
        Snake snake = player.getSnake();
        
        snake.move(direction);
        this.numMovesNotEaten++;
        
        if (snake.doesCollideWith(apple)) {
            snake.grow();
            this.placeApple();
            this.numMovesNotEaten = 0;
        }
        
        if (playerIsDead(player)) {
            handlePlayerDeath(player);
        }
        
        if (this.isInfiniteGame() || this.freeSpots() == 0 || this.numPlayersAlive == 0
                || (this.numPlayers() > 1 && this.numPlayersAlive < 2)) {
            this.gameOver = true;
        }
        
        if (!this.gameOver) {
            refreshBoard();
        }
    }
    
    /**
     * Check if the game has been running for too long
     * without an apple being eaten.
     * 
     * @return   True or false.
     */
    public boolean isInfiniteGame() {
        int area = this.width() * this.height();
        return this.numMovesNotEaten > this.numPlayersAlive * area * 3;
    }
    
    private void refreshBoard() {
        this.board = new int[height()][width()];
        
        for (Player player : this.players) {
            if (player.isDead()) {
                continue;
            }
            
            Snake snake = player.getSnake();
            
            for (Piece piece : snake.getPieces()) {
                board[piece.y][piece.x] = -1;
            }
            
            board[snake.getHead().y][snake.getHead().x] = -2;
        }
        
        if (apple != null) {
            board[apple.y][apple.x] = 8;
        }
    }
    
    private void handlePlayerDeath(Player player) {
        System.out.println("Player " + (player.getID() + 1) + " died with snake length " + player.getSnake().getLength());

        player.kill();
        numPlayersAlive--;
        
        if (this.numPlayers() > 1 && numPlayersAlive > 1) {
            player.doNotDraw();
        }
    }
    
    private boolean playerIsDead(Player player) {        
        Snake snake = player.getSnake();
        int x = snake.getHead().x;
        int y = snake.getHead().y;
        if (x < 0 || x >= this.width() || y < 0 || y >= this.height()) {
            return true;
        }
        
        /* also checks for collision with itself */
        for (Player otherPlayer : this.players) {
            if (snake.doesCollideWith(otherPlayer.getSnake())) {
                return true;
            }
        }    
        
        return false;
    }
    
    private void placeApple() {
        if (freeSpots() == 0) {
            this.apple = null;
            return;
        }
        
        List<Piece> allowedPoints = new ArrayList<>();
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                allowedPoints.add(new Piece(x, y));
            }
        }
        
        for (Player player : this.players) {
            allowedPoints.removeAll(player.getSnake().getPieces());
        }
        
        Collections.shuffle(allowedPoints);
        this.apple = allowedPoints.get(0);
    }
    
    private int freeSpots() {
        int freeSpots = this.width() * this.height();
        for (Player player : this.players) {
            freeSpots -= player.getSnake().getTrueLength();
        }
        
        return freeSpots;
    }
    
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }

}
