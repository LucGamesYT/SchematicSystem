package de.theamychan.schematic.command;

import de.theamychan.schematic.SchematicSystem;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.annotation.*;
import io.gomint.command.validator.StringValidator;
import io.gomint.entity.EntityPlayer;

import java.util.Map;

@Name("pos")
@Description("Setzte Positionen")
@Permission( "schematic.position" )
@Overload({
        @Parameter( name = "position", validator = StringValidator.class, arguments = {"1|2"})
})
public class CommandPosition extends Command {

    @Override
    public CommandOutput execute( CommandSender commandSender, String alias, Map<String, Object> arguments ) {
        CommandOutput output = new CommandOutput();

        if(commandSender instanceof EntityPlayer ){
            EntityPlayer player = (EntityPlayer) commandSender;
            String position = (String) arguments.getOrDefault( "position", "1" );

            if(position.equalsIgnoreCase( "1" )){
                SchematicSystem.getInstance().getSchematicManager().getLoc1().put( player, player.getLocation() );
                output.success( "Du hast die erste Position gesetzt!" );
            }else if(position.equalsIgnoreCase( "2" )){
                SchematicSystem.getInstance().getSchematicManager().getLoc2().put( player, player.getLocation() );
                output.success( "Du hast die zweite Position gesetzt!" );
            }else{
                output.fail( "Bitte benutze nur pos1 oder pos2" );
            }

        }

        return output;
    }
}
