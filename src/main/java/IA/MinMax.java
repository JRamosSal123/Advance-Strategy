package IA;

import com.raylib.Raylib.Vector2;
import map.Board;
import player.Player;
import units.Structure;
import units.Troop;
import units.Unit;

import java.util.ArrayList;
import java.util.List;

public class MinMax {

    private final Player ai;
    private final Player player;
    private final int maxDepth;

    public MinMax(Player player, Player ai, int difficulty, Board board) {
        this.ai = ai.clonePlayer();
        this.player = player.clonePlayer();
        this.maxDepth = difficulty;

        List<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(player.getUnits());
        allUnits.addAll(ai.getUnits());

        for(Unit u : this.ai.getUnits()){
            if(u instanceof Troop){
                ((Troop) u).checkArea(allUnits, board);
            }
        }
        for(Unit u : this.player.getUnits()){
            if(u instanceof Troop){
                ((Troop) u).checkArea(allUnits, board);
            }
        }
    }

    public String minmax() {
        if(ai.getUnits().size() == 1){
            return "END_TURN";
        }

        for (Unit unit : ai.getUnits()) {
            if (unit instanceof Troop troop) {
                if ((int) troop.getPosition().x() == unit.getPosition().x() &&
                        (int) troop.getPosition().y() == unit.getPosition().y()) {

                    troop.adjacent();
                    for (Unit enemy : player.getUnits()) {
                        for (Vector2 p : troop.getPosibleAttack()) {
                            if(p.x() == enemy.getPosition().x() && p.y() == enemy.getPosition().y()){
                                int targetX = (int) enemy.getPosition().x();
                                int targetY = (int) enemy.getPosition().y();
                                return "ATTACK:" + targetX + ":" + targetY + ":" + (int)unit.getPosition().x() + ":" + (int)unit.getPosition().y();
                            }
                        }
                    }
                }
            }
        }

        Move bestMove = minimax(ai, player, maxDepth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (bestMove != null) {
            return "MOVE:" + bestMove.fromX + ":" + bestMove.fromY + ":" + bestMove.toX + ":" + bestMove.toY;
        }

        return "END_TURN";
    }

private Move minimax(Player currentAI, Player currentOpponent, int depth, boolean maximizingPlayer, int alpha, int beta) {
    if (depth == 0 || currentAI.isLoser() || currentOpponent.isLoser()) {
        return new Move(evaluate(currentAI, currentOpponent));
    }

    List<Troop> units = maximizingPlayer ? getAvailableTroops(currentAI) : getAvailableTroops(currentOpponent);

    Move bestMove = null;

    for (Troop unit : units) {
        List<Vector2> possibleMoves = unit.getPosibleMovement();

        for (Vector2 move : possibleMoves) {
            int fromX = (int) unit.getPosition().x();
            int fromY = (int) unit.getPosition().y();
            int toX = (int) move.x();
            int toY = (int) move.y();

            int originalPosX = (int) unit.getPosition().x();
            int originalPosY = (int) unit.getPosition().y();
            unit.setPosition(toX, toY);

            int score = minimax(currentAI, currentOpponent, depth - 1, !maximizingPlayer, alpha, beta).score;

            unit.setPosition(originalPosX, originalPosY);

            if (maximizingPlayer) {
                if (bestMove == null || score > bestMove.score) {
                    bestMove = new Move(fromX, fromY, toX, toY, score);
                }
                alpha = Math.max(alpha, score);
            } else {
                if (bestMove == null || score < bestMove.score) {
                    bestMove = new Move(fromX, fromY, toX, toY, score);
                }
                beta = Math.min(beta, score);
            }

            if (beta <= alpha) {
                break;
            }
        }
    }

    if (bestMove == null) {
        return new Move(evaluate(currentAI, currentOpponent));
    }

    return bestMove;
}


    private List<Troop> getAvailableTroops(Player player) {
        List<Troop> troops = new ArrayList<>();
        for (Unit unit : player.getUnits()) {
            if (unit instanceof Troop) {
                troops.add((Troop) unit);
            }
        }
        return troops;
    }

private int evaluate(Player ai, Player opponent) {
    int aiScore = 0;
    int opponentScore = 0;

    Unit opponentStructure = null;
    for (Unit unit : opponent.getUnits()) {
        if (unit instanceof Structure) {
            opponentStructure = unit;
            break;
        }
    }

    for (Unit aiUnit : ai.getUnits()) {
        aiScore += aiUnit.getLife();

        if (aiUnit instanceof Troop) {
            if (opponentStructure != null) {
                double dx = aiUnit.getPosition().x() - opponentStructure.getPosition().x();
                double dy = aiUnit.getPosition().y() - opponentStructure.getPosition().y();
                double distance = Math.sqrt(dx * dx + dy * dy);

                int proximityBonus = (int)(1000 - distance);
                aiScore += Math.max(proximityBonus, 0);
            }

            for (Unit enemyUnit : opponent.getUnits()) {
                double dx = aiUnit.getPosition().x() - enemyUnit.getPosition().x();
                double dy = aiUnit.getPosition().y() - enemyUnit.getPosition().y();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= 160) {
                    aiScore += 20000;
                }
            }
        }
    }

    for (Unit unit : opponent.getUnits()) {
        opponentScore += unit.getLife();
    }

    return aiScore - opponentScore;
}

    static class Move {
        int fromX, fromY, toX, toY;
        int score;

        Move(int score) {
            this.score = score;
        }

        Move(int fromX, int fromY, int toX, int toY, int score) {
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
            this.score = score;
        }
    }
}
