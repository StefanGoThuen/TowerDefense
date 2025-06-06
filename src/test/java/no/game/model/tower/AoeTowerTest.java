package no.game.model.tower;

import no.game.model.Projectile;
import no.game.model.enemy.BasicEnemy;
import no.game.model.enemy.Enemy;
import no.game.model.enemy.IEnemy;
import no.grid.CellPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AoeTowerTest {

    @Test
    public void testShootAtEnemiesHitsOnlyEnemiesInRange() {
        AoeTower tower = new AoeTower(new CellPosition(5, 5));

        Enemy enemyInRange1 = new BasicEnemy(List.of(new CellPosition(6, 5)), 50, 1.0);
        Enemy enemyInRange2 = new BasicEnemy(List.of(new CellPosition(4, 4)), 50, 1.0);
        Enemy enemyOutOfRange = new BasicEnemy(List.of(new CellPosition(10, 10)), 50, 1.0);

        List<IEnemy> enemies = new ArrayList<>();
        enemies.add(enemyInRange1);
        enemies.add(enemyInRange2);
        enemies.add(enemyOutOfRange);

        List<Projectile> projectiles = tower.shootAtEnemies(enemies);

        assertEquals(2, projectiles.size());
        List<IEnemy> targets = projectiles.stream().map(Projectile::getTarget).toList();

        assertTrue(targets.contains(enemyInRange1));
        assertTrue(targets.contains(enemyInRange2));
        assertFalse(targets.contains(enemyOutOfRange));
    }

    @Test
    public void testShootCooldownPreventsSecondShot() {
        AoeTower tower = new AoeTower(new CellPosition(5, 5));
        Enemy enemy = new BasicEnemy(List.of(new CellPosition(5, 6)), 50, 1.0);

        List<IEnemy> enemies = List.of(enemy);

        List<Projectile> firstShot = tower.shootAtEnemies(enemies);
        assertEquals(1, firstShot.size());

        List<Projectile> secondShot = tower.shootAtEnemies(enemies);
        assertEquals(0, secondShot.size());

        for (int i = 0; i < 2; i++) {
            tower.tickCooldown();
        }

        List<Projectile> thirdShot = tower.shootAtEnemies(enemies);
        assertEquals(1, thirdShot.size());
    }

}
