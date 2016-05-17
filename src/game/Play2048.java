package game;

public class Play2048
{
    static GameFrame game;
    
    public static void main(String[] args)
    {
        newGame();
    }
    
    public static void newGame(){
        game = new GameFrame(/*this*/);
    }
}
