package de.theamychan.schematic.command;

import de.theamychan.schematic.SchematicSystem;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.PlayerCommandSender;
import io.gomint.command.annotation.*;
import io.gomint.command.validator.StringValidator;
import io.gomint.entity.EntityPlayer;

import java.util.Map;

@Name("schem load")
@Description("Lade ein Schematic")
@Permission( "schematic.load" )
@Overload({
        @Parameter( name = "name", validator = StringValidator.class, arguments = {".*"}, optional = true )
})
public class CommandLoad extends Command {

    @Override
    public CommandOutput execute( CommandSender commandSender, String alias, Map<String, Object> arguments ) {
        CommandOutput output = new CommandOutput();

        if(commandSender instanceof PlayerCommandSender ){
            EntityPlayer player = (EntityPlayer) commandSender;
            String name = (String) arguments.get( "name" );

            if(SchematicSystem.getInstance().getSchematicManager().load( name, player ) ){
                output.success( "Das Schematic wurde erfolgreich geladen!" );
            }else{
                output.fail( "Das Schematic konnte nicht geladen werden!" );
            }
        }

        return output;
    }
}
