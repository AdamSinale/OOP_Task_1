public class King extends ConcretePiece{


    public King(Player firstPlayer) {
        super(firstPlayer);
        this.type = "♔";
    }
    public King(King king) {
        super(king);
    }
    public String toString(){
        return "K" + getId();
    }
}
