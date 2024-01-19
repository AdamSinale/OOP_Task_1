public class ConcretePlayer implements Player{
    private int wins = 0;
    final boolean playerOne;

    public ConcretePlayer(boolean isPLayerOne){
        this.playerOne = isPLayerOne;
    }

    @Override
    public boolean isPlayerOne(){
        return this.playerOne;
    }
    @Override
    public int getWins(){
        return this.wins;
    }

    public void playerWon(){
        this.wins++;
    }
}
