package no.game.model.tower;

import java.util.ArrayList;
import java.util.List;

import no.game.model.Projectile;
import no.game.model.enemy.IEnemy;
import no.grid.CellPosition;

public class AoeTower extends Tower {

    public AoeTower(CellPosition position) {
        super(position, 2, 4, 2);
    }

    public List<Projectile> shootAtEnemies(List<IEnemy> enemies) {
        List<Projectile> projectiles = new ArrayList<>();

        if (!canShoot())
            return projectiles;

        for (IEnemy enemy : enemies) {
            if (isInRange(enemy.getPosition())) {
                projectiles.add(new Projectile(this.getPosition(), enemy, this.getDamage()));
            }
        }

        resetCooldown();
        return projectiles;
    }

    private boolean isInRange(CellPosition enemyPos) {
        int dx = Math.abs(enemyPos.col() - this.getPosition().col());
        int dy = Math.abs(enemyPos.row() - this.getPosition().row());
        return dx + dy <= this.getRange();
    }

}
