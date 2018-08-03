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

@Name("schem delete")
@Description("Lösche ein Schematic")
@Permission( "schematic.delete" )
@Overload({
        @Parameter( name = "name", validator = StringValidator.class, arguments = {".*"}, optional = true )
})
public class CommandDelete extends Command {

    @Override
    public CommandOutput execute( CommandSender commandSender, String alias, Map<String, Object> arguments ) {
        CommandOutput output = new CommandOutput();

        if(commandSender instanceof PlayerCommandSender ){
            EntityPlayer player = (EntityPlayer) commandSender;
            String name = (String) arguments.get( "name" );

            if(SchematicSystem.getInstance().getSchematicManager().delete( name ) ){
                output.success( "Du hast das Schematic erfolgreich gelöscht" );
            }else{
                output.fail( "Das Schematic konnte nicht geladen werden!" );
            }
        }

        return output;
    }
}
