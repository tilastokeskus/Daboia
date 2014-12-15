
package com.github.tilastokeskus.daboia.ui;

import com.github.tilastokeskus.daboia.core.Piece;
import com.github.tilastokeskus.daboia.core.Player;
import com.github.tilastokeskus.daboia.core.Snake;
import com.github.tilastokeskus.daboia.core.game.DaboiaGame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.border.EmptyBorder;

public class GamePanel extends AbstractGamePanel {
    
    private final static int SQUARE_SIZE = 14;
    private final static int PADDING = 6;
    private final static Color COLOR_APPLE = new Color(255, 100, 100);
    
    public GamePanel(DaboiaGame game) {
        super(game, SQUARE_SIZE, PADDING);
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    @Override
    protected void drawPlayers(Iterable<Player> players, Graphics g) {
        for (Player player : players) {
            if (!player.getShouldBeDrawn())
                continue;
            
            drawPlayer(player, g);
        }
    }

    @Override
    protected void drawApple(Piece apple, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        if (apple != null) {
            int x = translateToBoard(apple.x);
            int y = translateToBoard(apple.y);
            g2.setColor(COLOR_APPLE);
            g2.drawLine(x, y, x, y);
        }
    }

    protected void drawPlayer(Player player, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Path2D path = getSnakePath(player.getSnake());
        
        g2.setColor(player.getSnakeColor());
        g2.draw(path);
        
        Piece head = player.getSnake().getHead();
        int x = translateToBoard(head.x);
        int y = translateToBoard(head.y);
        
        g2.setColor(player.getSnakeColor().brighter());
        g2.drawLine(x, y, x, y);
    }
    
    private Path2D getSnakePath(Snake snake) {
        Path2D path = new Path2D.Double();

        Piece tail = snake.getTail();
        path.moveTo(translateToBoard(tail.x), translateToBoard(tail.y));            

        for (Piece piece : snake.getPieces()) {           
            path.lineTo(translateToBoard(piece.x), translateToBoard(piece.y));
        }
        
        return path;
    }
    
}
