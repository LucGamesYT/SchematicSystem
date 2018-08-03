package de.theamychan.schematic.command;

import de.theamychan.schematic.SchematicSystem;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.PlayerCommandSender;
import io.gomint.command.annotation.*;
import io.gomint.command.validator.StringValidator;
import io.gomint.entity.EntityPlayer;
import io.gomint.world.block.Block;

import java.util.List;
import java.util.Map;

@Name("schem save")
@Description("Speicher ein Schematic")
@Permission( "schematic.save" )
@Overload({
        @Parameter( name = "name", validator = StringValidator.class, arguments = {".*"}, optional = true )
})
public class CommandSave extends Command {

    @Override
    public CommandOutput execute( CommandSender commandSender, String alias, Map<String, Object> arguments ) {
        CommandOutput output = new CommandOutput();

        if(commandSender instanceof PlayerCommandSender ){
            EntityPlayer player = (EntityPlayer) commandSender;
            String name = (String) arguments.get( "name" );

            List<Block> blockList = SchematicSystem.getInstance().getSchematicManager().getBlocks( SchematicSystem.getInstance().getSchematicManager().getLoc1().get( player ), SchematicSystem.getInstance().getSchematicManager().getLoc2().get( player ) );
            List<String> list = SchematicSystem.getInstance().getSchematicManager().fromBlocklistToStringlist( blockList, player.getLocation() );

            if(SchematicSystem.getInstance().getSchematicManager().save( name, list ) ){
                output.success( "Du hast das Schematic gespeichert!" );
            }else{
                output.fail( "Das Schematic konnte nicht gespeichert werden!" );
            }

        }
        return output;
    }
}
