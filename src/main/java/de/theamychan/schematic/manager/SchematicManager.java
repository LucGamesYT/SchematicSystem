package de.theamychan.schematic.manager;

import de.theamychan.schematic.SchematicSystem;
import de.theamychan.schematic.util.Cuboid;
import io.gomint.entity.EntityPlayer;
import io.gomint.math.Location;
import io.gomint.world.block.Block;
import io.gomint.world.block.BlockAir;
import lombok.Getter;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class SchematicManager {

    private SchematicSystem plugin;
    private Map<EntityPlayer, List<String>> schematics;
    @Getter
    private Map<EntityPlayer, Location> loc1;
    @Getter
    private Map<EntityPlayer, Location> loc2;
    @Getter
    private Map<String, LinkedList<Backup>> backup;

    public SchematicManager( SchematicSystem plugin ) {
        this.plugin = plugin;
        this.schematics = new HashMap<>();
        this.loc1 = new HashMap<>();
        this.loc2 = new HashMap<>();
        this.backup = new LinkedHashMap<>();

        File file = new File( plugin.getDataFolder().getAbsolutePath() + "/schematics" );
        if ( !file.exists() ) {
            file.mkdirs();
        }
    }


    public boolean load( String filename, EntityPlayer player ) {
        File file = new File( plugin.getDataFolder().getAbsolutePath() + "/schematics/", filename + ".schematic" );
        if ( file.exists() ) {
            List<String> list = getStringlistFromFile( filename );
            if ( list != null && list.size() > 0 ) {
                schematics.put( player, list );
                return true;
            } else {
                System.out.println( 2 );
            }
        } else {
            System.out.println( 1 );
        }
        return false;
    }

    public List<String> getStringlistFromFile( String filename ) {
        File file = new File( plugin.getDataFolder().getAbsolutePath() + "/schematics/", filename + ".schematic" );
        try {
            FileReader reader = new FileReader( file );

            BufferedReader bufferedReader = new BufferedReader( reader );
            List<String> list = new ArrayList<>();
            String line = "";

            try {
                while ((line = bufferedReader.readLine()) != null) {
                    list.add( line );
                }
                bufferedReader.close();
                return list;
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean delete( String filename ) {
        File file = new File( plugin.getDataFolder().getAbsolutePath() + "/schematics/", filename + ".schematic" );
        if ( file.exists() ) {
            file.delete();
            return true;
        }
        return false;
    }

    public boolean save( String filename, List<String> list ) {

        File file = new File( plugin.getDataFolder().getAbsolutePath() + "/schematics/", filename + ".schematic" );
        String newLine = System.getProperty( "line.separator" );
        if ( file.exists() ) {
            return false;
        }
        try {
            file.createNewFile();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        try {
            BufferedWriter writer = new BufferedWriter( new FileWriter( file ) );
            for (String s : list) {
                writer.write( s );
                writer.write( newLine );
            }
            writer.close();
            return true;
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> fromBlocklistToStringlist( List<Block> blocks, Location location ) {
        List<String> list = new ArrayList<>();
        blocks.forEach( b -> list.add( blockToString( b, location ) ) );
        return list;
    }

    public List<Block> getBlocks( Location loc1, Location loc2 ) {
        return new Cuboid( loc1, loc2 ).getBlocks();
    }

    public Map<EntityPlayer, List<String>> getSchematics() {
        return schematics;
    }

    public void paste( Location location, String filename, Consumer<Boolean> consumer ) {
        this.plugin.getScheduler().executeAsync( () -> {
            List<String> list = getStringlistFromFile( filename );
            for (String string : list) {
                String[] data = string.split( "~" );
                int x = Integer.parseInt( data[0] );
                int y = Integer.parseInt( data[1] );
                int z = Integer.parseInt( data[2] );
                Class<? extends Block> clazz = getClass( data[3] );

                plugin.getScheduler().execute( () -> {
                    Block block = location.add( x, y, z ).getBlock().setType( clazz );
                } );
            }
            consumer.accept( true );
        } );
    }

    public void paste( EntityPlayer player, Consumer<Boolean> consumer ) {
        this.plugin.getScheduler().executeAsync( () -> {
            if ( schematics.containsKey( player ) ) {
                List<String> list = schematics.get( player );
                Location location = player.getLocation();
                for (String string : list) {
                    String[] data = string.split( "~" );
                    int x = Integer.parseInt( data[0] );
                    int y = Integer.parseInt( data[1] );
                    int z = Integer.parseInt( data[2] );
                    Class<? extends Block> clazz = getClass( data[3] );

                    plugin.getScheduler().execute( () -> location.add( x, y, z ).getBlock().setType( clazz ) );
                }
                consumer.accept( true );
            }
            consumer.accept( false );
        } );
    }

    public void destroy( Location location, String filename, Consumer<Boolean> consumer ) {

        this.plugin.getScheduler().executeAsync( () -> {
            List<String> list = getStringlistFromFile( filename );
            for (String string : list) {
                String[] data = string.split( "~" );
                int x = Integer.parseInt( data[0] );
                int y = Integer.parseInt( data[1] );
                int z = Integer.parseInt( data[2] );

                plugin.getScheduler().execute( () -> {
                    Block block = location.add( x, y, z ).getBlock().setType( BlockAir.class );
                } );
            }
            consumer.accept( true );
        } );

    }

    public String blockToString( Block block, Location location ) {
        int diffx = (int) (block.getLocation().getX() - location.getBlock().getLocation().getX());
        int diffy = (int) (block.getLocation().getY() - location.getBlock().getLocation().getY());
        int diffz = (int) (block.getLocation().getZ() - location.getBlock().getLocation().getZ());
        return diffx + "~" + diffy + "~" + diffz + "~" + block.getClass().getSimpleName();
    }

    public Class<? extends Block> getClass( String blockClassName ) {
        Class<? extends Block> blockClass = null;
        try {
            blockClass = (Class<? extends Block>) Class.forName( "io.gomint.world.block.Block" + blockClassName );
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        return blockClass;
    }
}
