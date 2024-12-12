<script lang="ts">
	import { undo } from "$lib/assets";
	import TabbedCodeBlock from "$lib/components/docs/TabbedCodeBlock.svelte";

    let buildSystem = 'Maven';
    let language = 'Java';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">PERMISSION INTEGRATIONS</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
        <h2 class="text-4xl mb-4">Permission integrations</h2>

        <p>
            A permission integration is used to determine if a disc can play or if a jukebox gui can open in a
            certain area. They are used by plugins like Griefprevention and WorldGuard. A plugin can register it's own integration by doing the following:
        </p>

        <p>
            First add the jext repository and dependency:
        </p>

        <TabbedCodeBlock
            bind:currentTab={buildSystem}
            tabs={["Maven", "Gradle Groovy", "Gradle Kotlin"]}
            tabsContents={[
                `<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<!-- ... -->

<dependency>
    <groupId>com.github.spartacus04</groupId>
    <artifactId>jext-reborn</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
    <exclusions>
        <exclusion>
            <groupId>*</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>`,
                `dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// ...

dependencies {
    compileOnly 'com.github.spartacus04:jext-reborn:VERSION'
}`,
                `repositories {
    mavenCentral()
    maven { url = uri('https://jitpack.io') }
}

// ...

dependencies {
    compileOnly("com.github.spartacus04:jext-reborn:VERSION")
}`
            ]}
        />

        <p>
            You can now create a class that implements the PermissionIntegration interface:
        </p>

        <TabbedCodeBlock
            bind:currentTab={language}
            tabs={["Java", "Kotlin"]}
            tabsContents={[
                `import com.spartacus04.jext.integrations.PermissionIntegration;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MyIntegration implements PermissionIntegration {
    @Override
    public String getId() {
        return "my_integration";
    }

    @Override
    public boolean hasJukeboxGuiAccess(Player player, Block block) {
        // Your implementation here
        return true;
    }

    @Override
    public boolean hasJukeboxAccess(Player player, Block block) {
        // Your implementation here
        return true;
    }
}`,
                `import com.spartacus04.jext.integrations.PermissionIntegration
import org.bukkit.block.Block
import org.bukkit.entity.Player

class MyIntegration : PermissionIntegration {
    override val id = "my_integration"

    override fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean {
        // Your implementation here
        return true
    }

    override fun hasJukeboxAccess(player: Player, block: Block): Boolean {
        // Your implementation here
        return true
    }
}`]} />

        <p>
            Finally, you can register your integration by using the PermissionsIntegrationManager:
        </p>

        <TabbedCodeBlock
            bind:currentTab={language}
            tabs={["Java", "Kotlin"]}
            tabsContents={[
                `import me.spartacus04.jext.JextState;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        JextState.INTEGRATIONS.registerIntegrations(new MyIntegration());
    }
}`,
                `import me.spartacus04.jext.JextState

class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        JextState.INTEGRATIONS.registerIntegrations(MyIntegration())
    }
}`]} />

        <p>
            Your integration is now registered and will be used by JukeboxExtendedReborn.
        </p>
    </div>
</div>
