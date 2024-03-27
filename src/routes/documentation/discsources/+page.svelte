<script lang="ts">
	import { CodeBlock, Tab, TabGroup } from '@skeletonlabs/skeleton';

	import { base } from '$app/paths';

	let buildSystem = 0;
	let language = 0;
</script>

<h1 class="h1">Disc integrations</h1>

<p>A disc integration allows any plugin to create a disc object for any soundtrack</p>
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
		href="{base}/javadocs/stable/-j-e-x-t--reborn/me.spartacus04.jext.discs.sources/-disc-source/index.html"
		>DiscSource</a
	> interface:
</p>

<!--
package me.spartacus04.jext.discs.sources

import me.spartacus04.jext.discs.Disc

interface DiscSource {
    suspend fun getDiscs(): List<Disc>
}
-->

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
import me.spartacus04.jext.discs.DiscSource;

public class MyDiscSource implements DiscSource {
    @Override
    public List<Disc> getDiscs() {
        // Your custom logic here
        return new ArrayList<>();
    }
}`}
			/>
		{:else if language === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="kotlin"
				rounded="true"
				code={`
import me.spartacus04.jext.discs.DiscSource

class MyDiscSource : DiscSource {
    override suspend fun getDiscs(): List<Disc> {
        // Your custom logic here
        return emptyList()
    }
}`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>

<p>The function should return a list of disc objects</p>
<br />
<p>
	Finally, you can register the disc source by using the <a
		href="{base}/javadocs/stable/-j-e-x-t--reborn/me.spartacus04.jext.discs/-disc-manager/index.html"
		>DiscManager</a
	>:
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
import me.spartacus04.jext.State.DISCS;

public class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        DISCS.registerDiscSource(new MyDiscSource());
    }
}`}
			/>
		{:else if language === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="kotlin"
				rounded="true"
				code={`
import me.spartacus04.jext.State.DISCS

class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        DISCS.registerDiscSource(MyDiscSource())
    }
}`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>

<h2 class="h2 mb-2">Custom playing method</h2>

<p>
	If you want to play the disc with a custom method, you can create a class that implements use the <a
		href="{base}/javadocs/stable/-j-e-x-t--reborn/me.spartacus04.jext.discs/-disc/index.html"
		>Disc</a
	> object:
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
import me.spartacus04.jext.discs.Disc;

public class MyDisc extends Disc {
    ...

    @Override
    public void play(Location location, float volume, float pitch) {
        // Your custom logic here
    }

    @Override
    public void play(Player player, float volume, float pitch) {
        // Your custom logic here
    }
}`}
			/>
		{:else if language === 1}
			<CodeBlock
				class="border border-mc-light-gray rounded-lg m-4"
				language="kotlin"
				rounded="true"
				code={`
import me.spartacus04.jext.discs.Disc

class MyDisc : Disc() {
    ...

    override fun play(location: Location, volume: Float, pitch: Float) {
        // Your custom logic here
    }

    override fun play(player: Player, volume: Float, pitch: Float) {
        // Your custom logic here
    }
}`}
			/>
		{/if}
	</svelte:fragment>
</TabGroup>
