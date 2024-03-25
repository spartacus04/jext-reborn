<script lang="ts">
	import { CodeBlock, Tab, TabGroup } from '@skeletonlabs/skeleton';

	let buildSystem = 0;
	let language = 0;
</script>

<h1 class="h1">Permission integrations</h1>

<p>
	A permission integration is used to determine if a disc can play or if a jukebox gui can open in a
	certain area.
</p>
<p>
	They are used by plugins like Griefprevention and WorldGuard. A plugin can register it's own
	integration by doing the following:
</p>
<br />
<p>First add the jext repository and dependency:</p>

<TabGroup>
	<Tab bind:group={buildSystem} name="Gradle Groovy" value={0}>Gradle Groovy</Tab>
	<Tab bind:group={buildSystem} name="Gradle Kotlin" value={1}>Gradle Kotlin</Tab>
	<Tab bind:group={buildSystem} name="Maven" value={2}>Maven</Tab>
	<svelte:fragment slot="panel">
		{#if buildSystem === 0}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="gradle"
				rounded="true"
				code={`
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

// ...

dependencies {
    compileOnly 'com.github.spartacus04:jext-reborn:VERSION'
}
`}
			/>
		{:else if buildSystem === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="gradle"
				rounded="true"
				code={`
repositories {
    mavenCentral()
    maven { url = uri('https://jitpack.io') }
}

// ...

dependencies {
    compileOnly("com.github.spartacus04:jext-reborn:VERSION")
}`}
			/>
		{:else if buildSystem === 2}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="xml"
				rounded="true"
				code={`
<repositories>
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
</dependency>`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>

<p>
	You can now create a class that implements the <a
		href="../-j-e-x-t--reborn/me.spartacus04.jext.integrations/-integration/index.html"
		>Integration</a
	> interface:
</p>

<TabGroup>
	<Tab bind:group={language} name="Java" value={0}>Java</Tab>
	<Tab bind:group={language} name="Kotlin" value={1}>Kotlin</Tab>
	<svelte:fragment slot="panel">
		{#if language === 0}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="java"
				rounded="true"
				code={`
import com.spartacus.jext.Integration;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MyIntegration implements Integration {
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
}`}
			/>
		{:else if language === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="kotlin"
				rounded="true"
				code={`
import com.spartacus.jext.Integration
import org.bukkit.block.Block
import org.bukkit.entity.Player

class MyIntegration : Integration {
    override val id = "my_integration"

    override fun hasJukeboxGuiAccess(player: Player, block: Block): Boolean {
        // Your implementation here
        return true
    }

    override fun hasJukeboxAccess(player: Player, block: Block): Boolean {
        // Your implementation here
        return true
    }
}`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>

<p>
	Finally, you can register your integration by using the <a
		href="../-j-e-x-t--reborn/me.spartacus04.jext.integrations/-integrations-manager/index.html"
		>IntegrationManager</a
	> :
</p>

<TabGroup>
	<Tab bind:group={language} name="Java" value={0}>Java</Tab>
	<Tab bind:group={language} name="Kotlin" value={1}>Kotlin</Tab>
	<svelte:fragment slot="panel">
		{#if language === 0}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="java"
				rounded="true"
				code={`
import me.spartacus04.jext.State.INTEGRATIONS;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        INTEGRATIONS.registerIntegrations(new MyIntegration());
    }
}`}
			/>
		{:else if language === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="kotlin"
				rounded="true"
				code={`
import me.spartacus04.jext.State.INTEGRATIONS

class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        INTEGRATIONS.registerIntegrations(MyIntegration())
    }
}`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>
