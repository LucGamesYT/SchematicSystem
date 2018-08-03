package de.theamychan.schematic.util;

import io.gomint.GoMint;
import io.gomint.math.Location;
import io.gomint.world.Chunk;
import io.gomint.world.World;
import io.gomint.world.block.Block;
import io.gomint.world.block.BlockAir;

import java.util.*;

public class Cuboid implements Iterable<Block>, Cloneable {

    public String worldName;
    public int x1, y1, z1;
    public int x2, y2, z2;

    /**
     * Construct a Cuboid given two Location objects which represent any two corners
     * of the Cuboid.
     *
     * @param l1 one of the corners
     * @param l2 the other corner
     */
    public Cuboid( Location l1, Location l2) {
        if (! l1.getWorld().equals(l2.getWorld())) {
            throw new IllegalArgumentException("locations must be on the same world");
        }
        worldName = l1.getWorld().getWorldName();
        x1 = Math.min((int)l1.getBlock().getLocation().getX(), (int) l2.getBlock().getLocation().getX());
        y1 = Math.min((int)l1.getBlock().getLocation().getY(), (int) l2.getBlock().getLocation().getY());
        z1 = Math.min((int)l1.getBlock().getLocation().getZ(), (int) l2.getBlock().getLocation().getZ());
        x2 = Math.max((int)l1.getBlock().getLocation().getX(), (int) l2.getBlock().getLocation().getX());
        y2 = Math.max((int)l1.getBlock().getLocation().getY(), (int) l2.getBlock().getLocation().getY());
        z2 = Math.max((int)l1.getBlock().getLocation().getZ(), (int) l2.getBlock().getLocation().getZ());
    }

    /**
     * Construct a one-block Cuboid at the given Location of the Cuboid.
     *
     * @param l1 location of the Cuboid
     */
    public Cuboid(Location l1) {
        this(l1, l1);
    }

    /**
     * Copy constructor.
     *
     * @param other the Cuboid to copy
     */
    public Cuboid(Cuboid other) {
        this(other.getWorld(), other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    /**
     * Construct a Cuboid in the given World and xyz co-ordinates
     *
     * @param world the Cuboid's world
     * @param x1 X co-ordinate of corner 1
     * @param y1 Y co-ordinate of corner 1
     * @param z1 Z co-ordinate of corner 1
     * @param x2 X co-ordinate of corner 2
     * @param y2 Y co-ordinate of corner 2
     * @param z2 Z co-ordinate of corner 2
     */
    public Cuboid( World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = world.getWorldName();
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    /**
     * Construct a Cuboid in the given world name and xyz co-ordinates.
     *
     * @param worldName the Cuboid's world name
     * @param x1 X co-ordinate of corner 1
     * @param y1 Y co-ordinate of corner 1
     * @param z1 Z co-ordinate of corner 1
     * @param x2 X co-ordinate of corner 2
     * @param y2 Y co-ordinate of corner 2
     * @param z2 Z co-ordinate of corner 2
     */
    private Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    public Cuboid(Map<String,Object> map) {
        worldName = (String)map.get("worldName");
        x1 = (Integer) map.get("x1");
        x2 = (Integer) map.get("x2");
        y1 = (Integer) map.get("y1");
        y2 = (Integer) map.get("y2");
        z1 = (Integer) map.get("z1");
        z2 = (Integer) map.get("z2");
    }

    public Map<String, Object> serialize() {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("worldName", worldName);
        map.put("x1", x1);
        map.put("y1", y1);
        map.put("z1", z1);
        map.put("x2", x2);
        map.put("y2", y2);
        map.put("z2", z2);
        return map;
    }

    /**
     * Get the Location of the lower northeast corner of the Cuboid (minimum XYZ
     * co-ordinates).
     *
     * @return Location of the lower northeast corner
     */
    public Location getLowerNE() {
        return new Location(getWorld(), x1, y1, z1);
    }

    /**
     * Get the Location of the upper southwest corner of the Cuboid (maximum XYZ
     * co-ordinates).
     *
     * @return Location of the upper southwest corner
     */
    public Location getUpperSW() {
        return new Location(getWorld(), x2, y2, z2);
    }

    /**
     * Get the blocks in the Cuboid.
     *
     * @return The blocks in the Cuboid
     */
    public List<Block> getBlocks() {
        Iterator<Block> blockI = this.iterator();
        List<Block> copy = new ArrayList<Block>();
        while (blockI.hasNext())
            copy.add(blockI.next());
        return copy;
    }

    /**
     * Get the the centre of the Cuboid
     *
     * @return Location at the centre of the Cuboid
     */
    public Location getCenter() {
        float x1 = getUpperX() + 1;
        float y1 = getUpperY() + 1;
        float z1 = getUpperZ() + 1;

        float x = (float) (getLowerX() + (x1 - getLowerX()) / 2.0);
        float y = (float) (getLowerY() + (y1 - getLowerY()) / 2.0);
        float z = (float) (getLowerZ() + (z1 - getLowerZ()) / 2.0);

        return new Location(getWorld(), x , y, z );
    }

    /**
     * Get the Cuboid's world.
     *
     * @return the World object representing this Cuboid's world
     * @throws IllegalStateException if the world is not loaded
     */
    public World getWorld() {
        World world = GoMint.instance().getWorld(worldName);
        if (world == null) {
            throw new IllegalStateException("world '" + worldName + "' is not loaded");
        }
        return world;
    }

    /**
     * Get the size of this Cuboid along the X axis
     *
     * @return	Size of Cuboid along the X axis
     */
    public float getSizeX() {
        return (x2 - x1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Y axis
     *
     * @return	Size of Cuboid along the Y axis
     */
    public float getSizeY() {
        return (y2 - y1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Z axis
     *
     * @return	Size of Cuboid along the Z axis
     */
    public float getSizeZ() {
        return (z2 - z1) + 1;
    }

    /**
     * Get the minimum X co-ordinate of this Cuboid
     *
     * @return	the minimum X co-ordinate
     */
    public float getLowerX() {
        return x1;
    }

    /**
     * Get the minimum Y co-ordinate of this Cuboid
     *
     * @return	the minimum Y co-ordinate
     */
    public float getLowerY() {
        return y1;
    }

    /**
     * Get the minimum Z co-ordinate of this Cuboid
     *
     * @return	the minimum Z co-ordinate
     */
    public float getLowerZ() {
        return z1;
    }

    /**
     * Get the maximum X co-ordinate of this Cuboid
     *
     * @return	the maximum X co-ordinate
     */
    public float getUpperX() {
        return x2;
    }

    /**
     * Get the maximum Y co-ordinate of this Cuboid
     *
     * @return	the maximum Y co-ordinate
     */
    public float getUpperY() {
        return y2;
    }

    /**
     * Get the maximum Z co-ordinate of this Cuboid
     *
     * @return	the maximum Z co-ordinate
     */
    public float getUpperZ() {
        return z2;
    }

    /**
     * Get the Blocks at the eight corners of the Cuboid.
     *
     * @return array of Block objects representing the Cuboid corners
     */
    public Block[] corners() {
        Block[] res = new Block[8];
        World w = getWorld();
        res[0] = w.getBlockAt(x1, y1, z1);
        res[1] = w.getBlockAt(x1, y1, z2);
        res[2] = w.getBlockAt(x1, y2, z1);
        res[3] = w.getBlockAt(x1, y2, z2);
        res[4] = w.getBlockAt(x2, y1, z1);
        res[5] = w.getBlockAt(x2, y1, z2);
        res[6] = w.getBlockAt(x2, y2, z1);
        res[7] = w.getBlockAt(x2, y2, z2);
        return res;
    }

    /**
     * Expand the Cuboid in the given direction by the given amount.  Negative amounts will
     * shrink the Cuboid in the given direction.  Shrinking a cuboid's face past the opposite face
     * is not an error and will return a valid Cuboid.
     *
     * @param dir	the direction in which to expand
     * @param amount	the number of blocks by which to expand
     * @return	a new Cuboid expanded by the given direction and amount
     */
    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case North:
                return new Cuboid(worldName, x1 - amount, y1, z1, x2, y2, z2);
            case South:
                return new Cuboid(worldName, x1, y1, z1, x2 + amount, y2, z2);
            case East:
                return new Cuboid(worldName, x1, y1, z1 - amount, x2, y2, z2);
            case West:
                return new Cuboid(worldName, x1, y1, z1, x2, y2, z2 + amount);
            case Down:
                return new Cuboid(worldName, x1, y1 - amount, z1, x2, y2, z2);
            case Up:
                return new Cuboid(worldName, x1, y1, z1, x2, y2 + amount, z2);
            default:
                throw new IllegalArgumentException("invalid direction " + dir);
        }
    }

    /**
     * Shift the Cuboid in the given direction by the given amount.
     *
     * @param dir	the direction in which to shift
     * @param amount	the number of blocks by which to shift
     * @return	a new Cuboid shifted by the given direction and amount
     */
    public Cuboid shift(CuboidDirection dir, int amount) {
        return expand(dir, amount).expand(dir.opposite(), -amount);
    }

    /**
     * Outset (grow) the Cuboid in the given direction by the given amount.
     *
     * @param dir	the direction in which to outset (must be Horizontal, Vertical, or Both)
     * @param amount	the number of blocks by which to outset
     * @return	a new Cuboid outset by the given direction and amount
     */
    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid c;
        switch (dir) {
            case Horizontal:
                c = expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            case Vertical:
                c = expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            case Both:
                c = outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            default:
                throw new IllegalArgumentException("invalid direction " + dir);
        }
        return c;
    }

    /**
     * Inset (shrink) the Cuboid in the given direction by the given amount.  Equivalent
     * to calling outset() with a negative amount.
     *
     * @param dir the direction in which to inset (must be Horizontal, Vertical, or Both)
     * @param amount	the number of blocks by which to inset
     * @return	a new Cuboid inset by the given direction and amount
     */
    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    /**
     * Return true if the point at (x,y,z) is contained within this Cuboid.
     *
     * @param x	the X co-ordinate
     * @param y	the Y co-ordinate
     * @param z	the Z co-ordinate
     * @return	true if the given point is within this Cuboid, false otherwise
     */
    public boolean contains(int x, int y, int z) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    /**
     * Check if the given Block is contained within this Cuboid.
     *
     * @param b	the Block to check for
     * @return	true if the Block is within this Cuboid, false otherwise
     */
    public boolean contains(Block b) {
        return contains(b.getLocation());
    }

    /**
     * Check if the given Location is contained within this Cuboid.
     *
     * @param l	the Location to check for
     * @return	true if the Location is within this Cuboid, false otherwise
     */
    public boolean contains(Location l) {
        return worldName.equals(l.getWorld().getWorldName()) && contains((int)l.getBlock().getLocation().getX(), (int)l.getBlock().getLocation().getY(),(int) l.getBlock().getLocation().getZ());
    }

    /**
     * Get the volume of this Cuboid.
     *
     * @return	the Cuboid volume, in blocks
     */
    public float volume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    /**
     * Contract the Cuboid, returning a Cuboid with any air around the edges removed, just
     * large enough to include all non-air blocks.
     *
     * @return a new Cuboid with no external air blocks
     */
    public Cuboid contract() {
        return this.
                contract(CuboidDirection.Down).
                contract(CuboidDirection.South).
                contract(CuboidDirection.East).
                contract(CuboidDirection.Up).
                contract(CuboidDirection.North).
                contract(CuboidDirection.West);
    }

    /**
     * Contract the Cuboid in the given direction, returning a new Cuboid which has no exterior empty space.
     * E.g. a direction of Down will push the top face downwards as much as possible.
     *
     * @param dir	the direction in which to contract
     * @return	a new Cuboid contracted in the given direction
     */
    public Cuboid contract(CuboidDirection dir) {
        Cuboid face = getFace(dir.opposite());
        switch (dir) {
            case Down:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getLowerY() > this.getLowerY()) {
                    face = face.shift(CuboidDirection.Down, 1);
                }
                return new Cuboid(worldName, x1, y1, z1, x2, (int) face.getUpperY(), z2);
            case Up:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getUpperY() < this.getUpperY()) {
                    face = face.shift(CuboidDirection.Up, 1);
                }
                return new Cuboid(worldName, x1, (int) face.getLowerY(), z1, x2, y2, z2);
            case North:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getLowerX() > this.getLowerX()) {
                    face = face.shift(CuboidDirection.North, 1);
                }
                return new Cuboid(worldName, x1, y1, z1, (int) face.getUpperX(), y2, z2);
            case South:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getUpperX() < this.getUpperX()) {
                    face = face.shift(CuboidDirection.South, 1);
                }
                return new Cuboid(worldName, (int) face.getLowerX(), y1, z1, x2, y2, z2);
            case East:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getLowerZ() > this.getLowerZ()) {
                    face = face.shift(CuboidDirection.East, 1);
                }
                return new Cuboid(worldName, x1, y1, z1, x2, y2,(int) face.getUpperZ());
            case West:
                while (face.containsOnly(GoMint.instance().createBlock( BlockAir.class )) && face.getUpperZ() < this.getUpperZ()) {
                    face = face.shift(CuboidDirection.West, 1);
                }
                return new Cuboid(worldName, x1, y1, (int) face.getLowerZ(), x2, y2, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    /**
     * Get the Cuboid representing the face of this Cuboid.  The resulting Cuboid will be
     * one block thick in the axis perpendicular to the requested face.
     *
     * @param dir	which face of the Cuboid to get
     * @return	the Cuboid representing this Cuboid's requested face
     */
    public Cuboid getFace(CuboidDirection dir	) {
        switch (dir) {
            case Down:
                return new Cuboid(worldName, x1, y1, z1, x2, y1, z2);
            case Up:
                return new Cuboid(worldName, x1, y2, z1, x2, y2, z2);
            case North:
                return new Cuboid(worldName, x1, y1, z1, x1, y2, z2);
            case South:
                return new Cuboid(worldName, x2, y1, z1, x2, y2, z2);
            case East:
                return new Cuboid(worldName, x1, y1, z1, x2, y2, z1);
            case West:
                return new Cuboid(worldName, x1, y1, z2, x2, y2, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    /**
     * Check if the Cuboid contains only blocks of the given type
     *
     * @param block the block to check for
     * @return true if this Cuboid contains only blocks of the given type
     */
    public boolean containsOnly(Block block) {
        for (Block b : this) {
            if (b != block) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the Cuboid big enough to hold both this Cuboid and the given one.
     *
     * @param other the other Cuboid to include
     * @return	a new Cuboid large enough to hold this Cuboid and the given Cuboid
     */
    public Cuboid getBoundingCuboid(Cuboid other) {
        if (other == null) {
            return this;
        }

        int xMin = Math.min((int)getLowerX(),(int) other.getLowerX());
        int yMin = Math.min((int)getLowerY(),(int) other.getLowerY());
        int zMin = Math.min((int)getLowerZ(),(int) other.getLowerZ());
        int xMax = Math.max((int)getUpperX(),(int) other.getUpperX());
        int yMax = Math.max((int)getUpperY(),(int) other.getUpperY());
        int zMax = Math.max((int)getUpperZ(),(int) other.getUpperZ());

        return new Cuboid(worldName, xMin, yMin, zMin, xMax, yMax, zMax);
    }

    /**
     * Get a block relative to the lower NE point of the Cuboid.
     *
     * @param x	the X co-ordinate
     * @param y	the Y co-ordinate
     * @param z	the Z co-ordinate
     * @return	the block at the given position
     */
    public Block getRelativeBlock(int x, int y, int z) {
        return getWorld().getBlockAt(x1 + x, y1 + y, z1 + z);
    }

    /**
     * Get a block relative to the lower NE point of the Cuboid in the given World.  This
     * version of getRelativeBlock() should be used if being called many times, to avoid
     * excessive calls to getWorld().
     *
     * @param w	the World
     * @param x	the X co-ordinate
     * @param y	the Y co-ordinate
     * @param z	the Z co-ordinate
     * @return	the block at the given position
     */
    public Block getRelativeBlock(World w, int x, int y, int z) {
        return w.getBlockAt(x1 + x, y1 + y, z1 + z);
    }

    /**
     * Get a list of the chunks which are fully or partially contained in this cuboid.
     *
     * @return a list of Chunk objects
     */
    public List<Chunk> getChunks() {
        List<Chunk> res = new ArrayList<>();

        World w = getWorld();
        int x1 = (int) getLowerX() & ~0xf; int x2 = (int) getUpperX() & ~0xf;
        int z1 = (int) getLowerZ() & ~0xf; int z2 = (int) getUpperZ() & ~0xf;
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                res.add(w.getChunk(x >> 4, z >> 4));
            }
        }
        return res;
    }


    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Block> iterator() {
        return new CuboidIterator(getWorld(), x1, y1, z1, x2, y2, z2);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Cuboid clone() throws CloneNotSupportedException {
        return new Cuboid(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Cuboid: " + worldName + "," + x1 + "," + y1 + "," + z1 + "=>" + x2 + "," + y2 + "," + z2;
    }

    public class CuboidIterator implements Iterator<Block> {
        private World w;
        private int baseX, baseY, baseZ;
        private int x, y, z;
        private int sizeX, sizeY, sizeZ;

        public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
            this.w = w;
            baseX = x1;
            baseY = y1;
            baseZ = z1;
            sizeX = Math.abs(x2 - x1) + 1;
            sizeY = Math.abs(y2 - y1) + 1;
            sizeZ = Math.abs(z2 - z1) + 1;
            x = y = z = 0;
        }

        public boolean hasNext() {
            return x < sizeX && y < sizeY && z < sizeZ;
        }

        public Block next() {
            Block b = w.getBlockAt(baseX + x, baseY + y, baseZ + z);
            if (++x >= sizeX) {
                x = 0;
                if (++y >= sizeY) {
                    y = 0;
                    ++z;
                }
            }
            return b;
        }

        public void remove() {
            // nop
        }
    }

    public enum CuboidDirection {

        North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown;

        public CuboidDirection opposite() {
            switch(this) {
                case North:
                    return South;
                case East:
                    return West;
                case South:
                    return North;
                case West:
                    return East;
                case Horizontal:
                    return Vertical;
                case Vertical:
                    return Horizontal;
                case Up:
                    return Down;
                case Down:
                    return Up;
                case Both:
                    return Both;
                default:
                    return Unknown;
            }
        }
    }

}
