package de.theamychan.schematic;

import de.theamychan.schematic.manager.SchematicManager;
import io.gomint.plugin.Plugin;
import io.gomint.plugin.PluginName;
import io.gomint.plugin.Version;
import lombok.Getter;

@PluginName( "SchematicSystem" )
@Version( minor = 1, major = 0 )
public class SchematicSystem extends Plugin {

    @Getter
    private static SchematicSystem instance;
    @Getter
    private SchematicManager schematicManager;

    @Override
    public void onInstall() {
        instance = this;
        schematicManager = new SchematicManager( this );

    }

    @Override
    public void onUninstall() {

    }

}
