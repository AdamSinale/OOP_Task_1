public class Move {
    final Position from,to;
    final ConcretePiece pieceLeft,pieceRight,pieceTop,pieceBottom, mainPiece;
    public Move(Position from,Position to,ConcretePiece pieceL,ConcretePiece pieceR,ConcretePiece pieceT,ConcretePiece pieceB,ConcretePiece pieceM){
        this.from=from;
        this.to=to;
        this.pieceLeft=pieceL;
        this.pieceRight=pieceR;
        this.pieceTop=pieceT;
        this.pieceBottom=pieceB;
        this.mainPiece=pieceM;
    }
    public Position getFrom(){ return this.from; }
    public Position getTo(){ return this.to; }
    public ConcretePiece getPieceLeft(){ return this.pieceLeft; }
    public ConcretePiece getPieceRight(){ return this.pieceRight; }
    public ConcretePiece getPieceTop(){ return this.pieceTop; }
    public ConcretePiece getPieceBottom(){ return this.pieceBottom; }
    public ConcretePiece getMainPiece(){ return this.mainPiece; }
}
