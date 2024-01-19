import java.util.Comparator;

public class Pawn extends ConcretePiece{
    private int kills = 0;
    private static int defendersKills = 0;
    public Pawn(Player player){
        super(player);
        this.type = "♟";
    }
    public Pawn(Pawn pawn) {
        super(pawn);
        this.kills = pawn.getKills();
    }
    public void copy(ConcretePiece pawn) {
        super.copy(pawn);
        if(pawn instanceof Pawn) {
            this.kills = ((Pawn) pawn).getKills();
        }
    }
    public int getKills(){
        return this.kills;
    }
    public void killed(){
        if(getOwner().isPlayerOne()){ Pawn.defendersKills++; }
        this.kills++;
    }
    public static int getDefendersKills(){
        return Pawn.defendersKills;
    }
    public static void removeTotalkills(int kills){
        Pawn.defendersKills -= kills;
    }
    public String toString(){
        return (getOwner().isPlayerOne() ? "D" : "A") + this.id;
    }
}
class killsComparator implements Comparator<Pawn> {
    private boolean playerOneWon;
    public killsComparator(boolean playerOneWon){
        this.playerOneWon = playerOneWon;
    }

    public int compare(Pawn piece1, Pawn piece2){
        if(piece2.getKills() - piece1.getKills() == 0){
            if (piece1.getId() == piece2.getId()) {
                if(this.playerOneWon == piece1.getOwner().isPlayerOne()){return -1;}
                return 1;
            }
            return piece1.getId() - piece2.getId();
        }
        return piece2.getKills() - piece1.getKills();
    }
}
