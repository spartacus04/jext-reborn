<script lang="ts">
	import { undo } from '$lib/assets';
	import TabbedCodeBlock from '$lib/components/docs/TabbedCodeBlock.svelte';

	let buildSystem = 'Maven';
	let language = 'Java';
</script>

<div class="flex h-full flex-col w-full">
	<div class="flex flex-row bg-surface-background w-full py-4 px-4">
		<a href="../">
			<img src={undo} alt="back" class="h-4 w-4 mr-2" />
		</a>
		<b class="text-white text-xs uppercase">CUSTOM DISC SOURCES</b>
	</div>
	<div class="p-4 flex-col flex text-white overflow-y-auto w-full">
		<h2 class="text-4xl mb-4">Custom disc sources</h2>

		<p>
			Custom disc sources are used to add custom music discs to the plugin. A plugin can register
			it's own custom disc source by doing the following:
		</p>

		<p>First add the jext repository and dependency:</p>

		<TabbedCodeBlock
			bind:currentTab={buildSystem}
			tabs={['Maven', 'Gradle Groovy', 'Gradle Kotlin']}
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
			Now create a class that implements the DiscPlayingMethod interface, to define how you want to
			play the discs.
		</p>

		<TabbedCodeBlock
			bind:currentTab={language}
			tabs={['Java', 'Kotlin']}
			tabsContents={[
				`import com.spartacus04.jext.discs.discplaying.DiscPlayingMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MyDiscPlayingMethod implements DiscPlayingMethod {
    // Plays the disc at the specified location.
    @Override
    public void playLocation(Location location, String namespace, float volume, float pitch) {
        // Your implementation here
    }

    // Plays the disc for the specified player.
    @Override
    public void playPlayer(Player player, String namespace, float volume, float pitch) {
        // Your implementation here
    }
}`,
				`import com.spartacus04.jext.discs.discplaying.DiscPlayingMethod
import org.bukkit.Location
import org.bukkit.entity.Player

class MyDiscPlayingMethod : DiscPlayingMethod {
    // Plays the disc at the specified location.
    override fun playLocation(location: Location, namespace: String, volume: Float, pitch: Float) {
        // Your implementation here
    }

    // Plays the disc for the specified player.
    override fun playPlayer(player: Player, namespace: String, volume: Float, pitch: Float) {
        // Your implementation here
    }
}`
			]}
		/>

		<p>
			Now, create a class that implements the DiscSource interface, to define how you want to load
			the discs.
		</p>

		<TabbedCodeBlock
			bind:currentTab={language}
			tabs={['Java', 'Kotlin']}
			tabsContents={[
				`import com.spartacus04.jext.discs.sources.DiscSource;
import com.spartacus04.jext.discs.Disc;

import java.util.List;

public class MyDiscSource implements DiscSource {
    // Gets the discs.
    @Override
    public List<Disc> getDiscs() {
        // Your implementation here
        return List.of();
    }
}`,
				`import com.spartacus04.jext.discs.sources.DiscSource
import com.spartacus04.jext.discs.Disc

class MyDiscSource : DiscSource {
    // Gets the discs.
    override suspend fun getDiscs(): List<Disc> {
        // Your implementation here
        return emptyList()
    }
}`
			]}
		/>

		<p>
			Before playing the discs, you need to register a method to stop the discs playback. To do
			this, create a class that implements the DiscStoppingMethod interface.
		</p>

		<TabbedCodeBlock
			bind:currentTab={language}
			tabs={['Java', 'Kotlin']}
			tabsContents={[
				`import com.spartacus04.jext.discs.discstopping.DiscStoppingMethod;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class MyDiscStoppingMethod implements DiscStoppingMethod {
    // Defines the list of required plugins for the disc stopping method.
    @Override
    public List<String> requires() {
        return List.of();
    }

    // Stops the disc for the specified player.
    @Override
    public void stop(Player player) {
        // Your implementation here
    }

    // Stops the disc for the specified player with the specified namespace.
    @Override
    public void stop(Player player, String namespace) {
        // Your implementation here
    }

    // Stops the disc for the specified location.
    @Override
    public void stop(Location location) {
        // Your implementation here
    }

    // Stops the disc for the specified location with the specified namespace.
    @Override
    public void stop(Location location, String namespace) {
        // Your implementation here
    }
}`,
				`import com.spartacus04.jext.discs.discstopping.DiscStoppingMethod
import org.bukkit.Location
import org.bukkit.entity.Player

class MyDiscStoppingMethod : DiscStoppingMethod {
    // Defines the list of required plugins for the disc stopping method.
    override val requires = emptyList<String>()

    // Stops the disc for the specified player.
    override fun stop(player: Player) {
        // Your implementation here
    }

    // Stops the disc for the specified player with the specified namespace.
    override fun stop(player: Player, namespace: String) {
        // Your implementation here
    }

    // Stops the disc for the specified location.
    override fun stop(location: Location) {
        // Your implementation here
    }

    // Stops the disc for the specified location with the specified namespace.
    override fun stop(location: Location, namespace: String) {
        // Your implementation here
    }
}`
			]}
		/>

		<p>
			Finally, you can register the custom disc source and disc stopping method by using the
			DiscManager:
		</p>

		<TabbedCodeBlock
			bind:currentTab={language}
			tabs={['Java', 'Kotlin']}
			tabsContents={[
				`import com.spartacus04.jext.JextState;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        JextState.DISCS.registerDiscSource(new MyDiscSource(), () -> println("Discs reloaded"));
        JextState.DISCS.registerDiscStoppingMethod(new MyDiscStoppingMethod());
    }
}`,
				`import com.spartacus04.jext.JextState

class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        JextState.DISCS.registerDiscSource(MyDiscSource()) { println("Discs reloaded") }
        JextState.DISCS.registerDiscStoppingMethod(MyDiscStoppingMethod())
    }
}`
			]}
		/>

		<p>You can now use the discs in game!</p>
	</div>
</div>
