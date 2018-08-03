package de.theamychan.schematic.command;

import de.theamychan.schematic.SchematicSystem;
import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.PlayerCommandSender;
import io.gomint.command.annotation.Description;
import io.gomint.command.annotation.Name;
import io.gomint.command.annotation.Permission;
import io.gomint.entity.EntityPlayer;

import java.util.Map;
import java.util.function.Consumer;

@Name("schem paste")
@Description("Setzte ein Schematic")
@Permission( "schematic.paste" )
public class CommandPaste extends Command {

    @Override
    public CommandOutput execute( CommandSender commandSender, String alias, Map<String, Object> arguments ) {
        CommandOutput output = new CommandOutput();

        if(commandSender instanceof PlayerCommandSender ){
            EntityPlayer player = (EntityPlayer) commandSender;

            SchematicSystem.getInstance().getSchematicManager().paste( player, success -> {
                if(success){
                    commandSender.sendMessage( "Das Schematic wurde erfolgreich gesetzt!" );
                }else{
                    commandSender.sendMessage( "Das Schematic konnte nicht gesetzt werden!" );
                }
            } );
        }

        return output;
    }
}
