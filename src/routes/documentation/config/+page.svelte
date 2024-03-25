<div class="table-container -m-5 w-[calc(100%+2.5rem)] max-h-[100]">
	<table
		class="table table-hover border-collapse [&>*]:[&>*]:[&>*]:border font-minecraft h4 border-mc-light-gray"
	>
		<thead class="">
			<tr>
				<th>Key</th>
				<th>Description</th>
				<th>Default value</th>
				<th>Value type</th>
				<th>Enum values</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>lang</td>
				<td
					>If set to auto, the plugin will use the player's locale; if set to silent, the plugin
					won't output any messages; if set to custom the plugin will use the language specified in
					the custom language file; If set to a locale, the plugin will use that locale.</td
				>
				<td>auto</td>
				<td>enum</td>
				<td
					>auto, silent, custom, af_za, ar_sa, ca_es, cs_cz, da_dk, de_de, el_gr, en_us, es_es,
					fi_fi, fr_fr, he_il, hu_hu, it_it, ja_jp, ko_kr, nl_nl, no_no, pl_pl, pt_br, pt_pt, ro_ro,
					ru_ru, sr_sp, sv_se, tr_tr, uk_ua, vi_vn, zh_cn, zh_tw</td
				>
			</tr>
			<tr>
				<td>jukebox-behaviour</td>
				<td
					>If set to vanilla, the plugin will use the vanilla jukebox behaviour; if set to gui, the
					plugin will use the custom GUI.</td
				>
				<td>vanilla</td>
				<td>enum</td>
				<td>vanilla, gui</td>
			</tr>
			<tr>
				<td>jukebox-gui-style</td>
				<td>Determines the style of the jukebox GUI.</td>
				<td>scroll-vertical</td>
				<td>enum</td>
				<td>scroll-vertical, scroll-horizontal, page-vertical, page-horizontal</td>
			</tr>
			<tr>
				<td>jukebox-gui-size</td>
				<td>Sets the maximum amount of items that can be added to a jukebox GUI.</td>
				<td>96</td>
				<td>integer</td>
				<td></td>
			</tr>
			<tr>
				<td>disable-music-overlap</td>
				<td>If set to true, the plugin will not play music if there is already music playing.</td>
				<td>true</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>disc-loottables-limit</td>
				<td
					>Sets the maximum amount of discs that can be found in chests, the default amount is 2.</td
				>
				<td>{'{"chests/*": 2}'}</td>
				<td>object</td>
				<td>chests/* (loot table name)</td>
			</tr>
			<tr>
				<td>fragment-loottables-limit</td>
				<td
					>Sets the maximum amount of disc fragments that can be found in chests, the default amount
					is 3.</td
				>
				<td>{'{"chests/*": 3}'}</td>
				<td>object</td>
				<td>chests/* (loot table name)</td>
			</tr>
			<tr>
				<td>check-for-updates</td>
				<td>If set to true, the plugin will check for updates on startup.</td>
				<td>true</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>allow-metrics</td>
				<td
					>If set to true, the plugin will send metrics to bStats. Please consider enabling this, as
					it helps me improve the plugin.</td
				>
				<td>true</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>force-resource-pack</td>
				<td
					>If set to true, the plugin will force players to use the resource pack. If the player
					declines, they will be kicked.</td
				>
				<td>false</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>enable-resource-pack-host</td>
				<td
					>If set to true, the plugin will host the resource pack and automatically send it to
					players.</td
				>
				<td>true</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>web-interface-port</td>
				<td>The port the web interface api & resource pack will be hosted on.</td>
				<td>9871</td>
				<td>integer</td>
				<td></td>
			</tr>
			<tr>
				<td>web-interface-api-enabled</td>
				<td
					>If set to true, the plugin will have a REST api running to edit the discs settings and
					the config.</td
				>
				<td>true</td>
				<td>boolean</td>
				<td></td>
			</tr>
			<tr>
				<td>web-interface-password</td>
				<td>The password required to access the web interface api.</td>
				<td></td>
				<td>string</td>
				<td></td>
			</tr>
		</tbody>
	</table>
</div>
