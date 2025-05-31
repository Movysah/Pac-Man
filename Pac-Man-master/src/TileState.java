/**
 * Represents the possible states of a tile in the Pac-Man game.
 * - Empty: No object on the tile.
 * - WALL: An impassable wall.
 * - POWER_UP: A power-up item that can be collected.
 * - DOT: A dot that can be collected.
 */
public enum TileState {
    Empty,
    WALL,
    POWER_UP,
    DOT
}