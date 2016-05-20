package game;

import java.awt.Rectangle;

public class Block extends Rectangle
{
    /**
     * 
     */
    private static final long serialVersionUID = 6730592158688682868L;
    private static Rectangle rect = new Rectangle(0, 0, 0, 0);
    private static int value;
    
    public Block(Rectangle r, int val)
    {
        rect = r;
        value = val;
    }
    public Rectangle getRect()
    {
        return rect;
    }
    public void setRect(Rectangle rect)
    {
        Block.rect = rect;
    }
    public int getValue()
    {
        return value;
    }
    public void setValue(int value)
    {
        Block.value = value;
    }
}
