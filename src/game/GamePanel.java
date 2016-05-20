package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * The panel that holds the game boards
 * 
 * @author Cody Moose
 * @version 0.4.3
 * @since 4/4/2016
 */
public class GamePanel extends JPanel implements KeyListener
{
    /**
     * 
     */
    private static final long       serialVersionUID = -5452925014639836146L;
    /**
     * The values of the blocks, used to determine how to paint the blocks, and
     * for game 'motion'
     */
    private static int[][]          blockVals        = new int[4][4];
    /**
     * All the possible colors of the blocks with numbers in them
     */
    private static Color[]          colors           = new Color[] { new Color(239, 231, 222), new Color(239, 223, 198),
                    new Color(239, 174, 115), new Color(247, 150, 99), new Color(246, 124, 95), new Color(246, 94, 59),
                    new Color(237, 207, 114), new Color(237, 204, 97), new Color(237, 200, 80), new Color(237, 197, 63),
                    new Color(237, 194, 46) };
    /**
     * All 16 possible spots of 2048
     */
    private static Rectangle[][]    spots            = new Rectangle[4][4];
    /**
     * The background rectangle for the game board. Used for board proportions
     * and sizes
     */
    private Rectangle               gameBoard        = new Rectangle(0, 0, 0, 0);
    /**
     * The backgroud of the window
     */
    private Rectangle               background       = new Rectangle(0, 0, 0, 0);
    private static Random           r                = new Random();
    /**
     * The frame in which the game resides
     */
    GameFrame                       frame;
    /**
     * The point size of the font in the blocks. Used for scaling font
     */
    private static double           blockFontSize    = 20;
    private static ArrayList<Block> blocks           = new ArrayList<Block>();

    /**
     * The JPanel that holds the game
     * 
     * @param inFrame
     *            the frame in which the game is held
     */
    public GamePanel(GameFrame inFrame)
    {
        frame = inFrame;
        updateSize();
        setVisible(true);
        updateSize();
        setBounds(0, 0, (int) frame.getContentPane().getSize().getWidth(),
                        (int) frame.getContentPane().getSize().getHeight());
        background = this.getBounds();
        frame.addKeyListener(this);
        addKeyListener(this);
        placeNewBlock();
        /*-
        // * Used for testing colors - do not remove for now.
        blockVals[0][0] = 2;
        blockVals[1][0] = 4;
        blockVals[2][0] = 8;
        blockVals[3][0] = 16;
        blockVals[0][1] = 32;
        blockVals[1][1] = 64;
        blockVals[2][1] = 128;
        blockVals[3][1] = 256;
        blockVals[0][2] = 512;
        blockVals[1][2] = 1024;
        blockVals[2][2] = 2048;
        // */
        repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        updateSize();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(Color.WHITE);
        g2.fill(background);
        g2.fill(gameBoard);
        Font f = new Font("Comic Sans MS", Font.PLAIN, (int) blockFontSize);
        g2.setFont(f);
        for (int i = 0; i < spots.length; i++)
        {
            for (int j = 0; j < spots[i].length; j++)
            {
                if (blockVals[i][j] != 0)
                {
                    g2.setPaint(colors[(int) (log(blockVals[i][j], 2) - 1)]);
                    g2.fill(spots[i][j]);
                    g2.setPaint(Color.BLACK);
                    drawCenteredString(g2, Integer.toString(blockVals[i][j]), spots[i][j], g2.getFont());
//                    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
//                    int x = (spots[i][j].width - metrics.stringWidth(Integer.toString(blockVals[i][j]))) / 2;
//                    int y = ((spots[i][j].height - metrics.getHeight()) / 2) + metrics.getAscent();
//                    g2.drawString(Integer.toString(blockVals[i][j]), (int) (x + spots[i][j].getX()),
//                                    (int) (y + spots[i][j].getY()));
                }
                g2.setPaint(Color.BLACK);
                g2.draw(spots[i][j]);
            }
        }
        for (int k = 0; k < blocks.size(); k++)
        {
            g2.setPaint(colors[(int) (log(blocks.get(k).getValue(), 2) - 1)]);
            g2.fill(blocks.get(k).getRect());
            g2.setPaint(Color.BLACK);
            drawCenteredString(g2, Integer.toString(blocks.get(k).getValue()), blocks.get(k).getRect(), g2.getFont());
//            FontMetrics metrics = g2.getFontMetrics(g2.getFont());
//            int x = (blocks.get(k).getRect().width - metrics.stringWidth(Integer.toString(blocks.get(k).getValue())))
//                            / 2;
//            int y = ((blocks.get(k).getRect().height - metrics.getHeight()) / 2) + metrics.getAscent();
//            g2.drawString(Integer.toString(blocks.get(k).getValue()), (int) (x + blocks.get(k).getRect().getX()),
//                            (int) (y + blocks.get(k).getRect().getY()));
            g2.draw(blocks.get(k).getRect());
        }
        g2.setPaint(Color.BLACK);
        g2.draw(new Rectangle((int) gameBoard.getX(), (int) gameBoard.getY(), (int) gameBoard.getWidth() - 1,
                        (int) gameBoard.getHeight() - 1));
        g2.setPaint(Color.BLACK);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_UP:
                int[][] tempUp = setTemp();
                doUp();
                if (isChanged(tempUp)) placeNewBlock();
                break;
            case KeyEvent.VK_RIGHT:
                int[][] tempRight = setTemp();
                doRight();
                if (isChanged(tempRight)) placeNewBlock();
                break;
            case KeyEvent.VK_DOWN:
                int[][] tempDown = setTemp();
                doDown();
                if (isChanged(tempDown)) placeNewBlock();
                break;
            case KeyEvent.VK_LEFT:
                int[][] tempLeft = setTemp();
                doLeft();
                if (isChanged(tempLeft)) placeNewBlock();
                break;
            case KeyEvent.VK_R:
                // playMain.newGame();
                frame.newGame();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    /**
     * The logarithm base b of a
     * 
     * @param a
     *            the number into the logarithm
     * @param b
     *            the base of the logarithm
     * @return the logarithm base b of a
     */
    public static double log(double a, double b)
    {
        return Math.log(a) / Math.log(b);
    }

    /**
     * Sends blocks upward, and merges identical blocks that collide
     */
    private static void doUp()
    {
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = 1; j < spots[i].length; j++)
                {
                    if (blockVals[i][j - 1] < 2)
                    {
                        blockVals[i][j - 1] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < spots.length; i++)
        {
            for (int j = 1; j < spots[i].length; j++)
            {
                if (blockVals[i][j - 1] == blockVals[i][j])
                {
                    blockVals[i][j - 1] *= 2;
                    blockVals[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = 1; j < spots[i].length; j++)
                {
                    if (blockVals[i][j - 1] < 2)
                    {
                        blockVals[i][j - 1] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
    }

    /**
     * Sends blocks to downward, and merges identical blocks that collide
     */
    private static void doDown()
    {
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = spots[i].length - 2; j >= 0; j--)
                {
                    if (blockVals[i][j + 1] < 2)
                    {
                        blockVals[i][j + 1] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
        for (int i = 0; i < spots.length; i++)
        {
            for (int j = spots[i].length - 2; j >= 0; j--)
            {
                if (blockVals[i][j] == blockVals[i][j + 1])
                {
                    blockVals[i][j + 1] *= 2;
                    blockVals[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = spots[i].length - 2; j >= 0; j--)
                {
                    if (blockVals[i][j + 1] < 2)
                    {
                        blockVals[i][j + 1] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
    }

    /**
     * Sends blocks to the right, and merges identical blocks that collide
     */
    private static void doRight()
    {
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length - 1; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    if (blockVals[i + 1][j] < 2)
                    {
                        blockVals[i + 1][j] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
        for (int i = spots.length - 2; i >= 0; i--)
        {
            for (int j = 0; j < spots[i].length; j++)
            {
                if (blockVals[i][j] == blockVals[i + 1][j])
                {
                    blockVals[i + 1][j] *= 2;
                    blockVals[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < 4; k++)
        {
            for (int i = 0; i < spots.length - 1; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    if (blockVals[i + 1][j] < 2)
                    {
                        blockVals[i + 1][j] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
    }

    /**
     * Sends blocks to the left, and merges identical blocks that collide
     */
    private static void doLeft()
    {
        for (int k = 0; k < 4; k++)
        {
            for (int i = 1; i < spots.length; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    if (blockVals[i - 1][j] < 2)
                    {
                        blockVals[i - 1][j] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
        for (int i = 1; i < spots.length; i++)
        {
            for (int j = 0; j < spots[i].length; j++)
            {
                if (blockVals[i - 1][j] == blockVals[i][j])
                {
                    blockVals[i - 1][j] *= 2;
                    blockVals[i][j] = 0;
                }
            }
        }
        for (int k = 0; k < 4; k++)
        {
            for (int i = 1; i < spots.length; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    if (blockVals[i - 1][j] < 2)
                    {
                        blockVals[i - 1][j] = blockVals[i][j];
                        blockVals[i][j] = 0;
                    }
                }
            }
        }
    }

    /**
     * Places a new block in a random empty spot
     */
    private static void placeNewBlock()
    {
        ArrayList<Dimension> spot = new ArrayList<Dimension>();
        for (int i = 0; i < spots.length; i++)
        {
            for (int j = 0; j < spots[i].length; j++)
            {
                if (blockVals[i][j] == 0) spot.add(new Dimension(i, j));
            }
        }
        if (spot.size() > 0)
        {
            int rand = r.nextInt(spot.size());
            boolean bool = r.nextBoolean();
            int x = (int) spot.get(rand).getWidth();
            int y = (int) spot.get(rand).getHeight();
            blockVals[x][y] = (bool) ? 2 : 4;
            blocks.add(new Block(new Rectangle(spots[x][y]), blockVals[x][y]));
        }
    }

    /**
     * Sets a temporary array to be used for checking if there was a change to
     * determine if a new block may be placed
     * 
     * @return a copy of blockVals before being moved
     */
    private static int[][] setTemp()
    {
        int[][] temp = new int[4][4];
        for (int i = 0; i < blockVals.length; i++)
        {
            for (int j = 0; j < blockVals[i].length; j++)
            {
                temp[i][j] = blockVals[i][j];
            }
        }
        return temp;
    }

    /**
     * Checks if blockVals has changed since it was moved to determine if a new
     * block should be placed
     * 
     * @param temp
     *            a copy of blockVals from before it was moved
     * @return whether or not blockVals changed when a key was pressed
     */
    private static boolean isChanged(int[][] temp)
    {
        boolean diff = false;
        for (int i = 0; i < blockVals.length; i++)
        {
            for (int j = 0; j < blockVals[i].length; j++)
            {
                if (blockVals[i][j] != temp[i][j])
                {
                    diff = true;
                    break;
                }
            }
            if (diff) break;
        }
        return diff;
    }

    /**
     * Updates the size of the game board to scale to the smallest dimension of
     * the frame
     */
    private void updateSize()
    {
        Dimension size = frame.getContentPane().getSize();
        if (size.getWidth() > size.getHeight())
        {
            gameBoard.setSize((int) (7. * size.getHeight() / 8.), (int) (7. * size.getHeight() / 8.));
            gameBoard.setLocation((int) (size.getWidth() / 2 - gameBoard.getWidth() / 2),
                            (int) (size.getHeight() / 2 - gameBoard.getHeight() / 2));
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    spots[i][j] = new Rectangle((int) (gameBoard.getX() + i * gameBoard.getWidth() / 4.),
                                    (int) (gameBoard.getY() + j * gameBoard.getHeight() / 4.),
                                    (int) (gameBoard.getWidth() / 4.), (int) (gameBoard.getHeight() / 4.));
                }
            }
            blockFontSize = size.getHeight() / 25;
        }
        else if (size.getHeight() > size.getWidth())
        {
            gameBoard.setSize((int) (7. * size.getWidth() / 8.), (int) (7. * size.getWidth() / 8.));
            gameBoard.setLocation((int) (size.getWidth() / 2 - gameBoard.getWidth() / 2),
                            (int) (size.getHeight() / 2 - gameBoard.getHeight() / 2));
            for (int i = 0; i < spots.length; i++)
            {
                for (int j = 0; j < spots[i].length; j++)
                {
                    spots[i][j] = new Rectangle((int) (gameBoard.getX() + i * gameBoard.getWidth() / 4.),
                                    (int) (gameBoard.getY() + j * gameBoard.getHeight() / 4.),
                                    (int) (gameBoard.getWidth() / 4.), (int) (gameBoard.getHeight() / 4.));
                }
            }
            blockFontSize = size.getWidth() / 25;
        }
    }

    /**
     * Removes all block values
     */
    public void reset()
    {
        for (int i = 0; i < blockVals.length; i++)
        {
            for (int j = 0; j < blockVals[i].length; j++)
            {
                blockVals[i][j] = 0;
            }
        }
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g
     *            The Graphics instance.
     * @param text
     *            The String to draw.
     * @param rect
     *            The Rectangle to center the text in.
     */
    public void drawCenteredString(Graphics2D g, String text, Rectangle rect, Font font)
    {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, (int) (x + rect.getX()), (int) (y + rect.getY()));
    }
}
