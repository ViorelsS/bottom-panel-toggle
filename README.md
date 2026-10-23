# Bottom Panel Toggle

![Bottom Panel Toggle](src/main/resources/META-INF/pluginIcon.svg)

[![Install from JetBrains Marketplace](https://img.shields.io/badge/JetBrains%20Marketplace-Install-087CFA?style=for-the-badge)](https://plugins.jetbrains.com/plugin/35398-bottom-panel-toggle)

One shortcut to hide and restore the bottom panel in IntelliJ IDEA.

**macOS:** `Cmd+J` · **Windows / Linux:** `Ctrl+J`

## Why this plugin exists

Working in IntelliJ often means switching between the editor, Terminal, Run,
Debug, and Services. Each tool window has its own shortcut. Hiding the active
tool window depends on which window is active, while hiding all tool windows
also hides side panels such as Project.

I wanted one consistent command for the bottom panel:

- Hide the bottom tool windows that are currently visible and return to the editor.
- Press the same shortcut again to restore the last used bottom tool window.
- Keep side panels open.
- Make the decision from the visible layout, regardless of whether the editor,
  a bottom tool window, or Project has focus.

Bottom Panel Toggle adds that command using the IntelliJ Platform APIs.

## Behavior

| Current layout | Result |
| --- | --- |
| Terminal is visible at the bottom | Hide it and focus the editor. |
| Run, Debug, or Services is visible at the bottom | Hide it and focus the editor. |
| Multiple bottom tool windows are visible | Hide them all; the next toggle restores the last used one. |
| No bottom tool window is visible | Restore and focus the last used bottom tool window. |
| Project is open on the left | Toggle the bottom panel and keep Project open. |
| A tool window has been moved to a side | Leave it there; it no longer counts as a bottom panel. |
| A tool window is floating or in a separate window | Leave it alone. |
| No previous bottom tool window can be restored | Open Terminal if available at the bottom; otherwise open the bottom tool window whose visible button label comes first alphabetically. |

Hiding a tool window does not close its tabs or stop running processes.
The last used bottom tool window is remembered separately for each open project
and is not persisted when the project is closed.

## Installation

Install the plugin from
[JetBrains Marketplace](https://plugins.jetbrains.com/plugin/35398-bottom-panel-toggle).
The IDE selects the newest available plugin version compatible with its build.

You can also download a ZIP from
[GitHub releases](https://github.com/ViorelsS/bottom-panel-toggle/releases).
The same source project produces two packages:

| IntelliJ IDEA version | Plugin ZIP | Terminal integration |
| --- | --- | --- |
| 2024.1–2025.3.x | `bottom-panel-toggle-1.0.2.zip` (legacy) | Disable **Override IDE shortcuts** if the terminal captures the shortcut. |
| 2025.3.6.1–2026.2.x | `bottom-panel-toggle-1.0.3.zip` (modern) | Includes automatic shortcut support for the Reworked Terminal. |

Where both packages are compatible, choose **1.0.3**. The overlapping ranges
also cover early 2025.3 builds with the legacy package.

To install a downloaded ZIP:

1. Open **Settings → Plugins** in IntelliJ IDEA.
2. Open the gear menu and select **Install Plugin from Disk…**.
3. Select the ZIP for your IDE version.
4. Restart the IDE if prompted.
5. Configure the shortcut as described below.

### Shortcut conflicts

The standard IntelliJ keymaps already use `Cmd+J` / `Ctrl+J` for
**Insert Live Template**. Assign the combination to **Toggle Bottom Panel**
and remove that same combination from conflicting actions in **Settings → Keymap**.
The plugin does not remove shortcuts from other actions automatically.

Default bindings are supplied for the standard Windows/Linux and macOS keymaps.
If you use another keymap, assign the shortcut to **Toggle Bottom Panel** manually.

The command is also available from **Tools → Toggle Bottom Panel** and
**Find Action**.

### Terminal key handling

The modern package allows the shortcut in the **Reworked Terminal** while
**Override IDE shortcuts** is enabled.

With the legacy package or other terminal engines, if the terminal sends the
shortcut to the shell instead of the IDE, disable
**Settings → Tools → Terminal → Override IDE shortcuts**.
The shortcut must also be assigned to **Toggle Bottom Panel** in your active keymap.

The Terminal plugin is optional. If it is absent or disabled, the toggle
uses another available bottom tool window.

## Compatibility

| Package | Minimum IDE build | Maximum IDE build | Compilation target |
| --- | --- | --- | --- |
| Legacy 1.0.2 | `241.14494.240` | `253.*` | Java 17 |
| Modern 1.0.3 | `253.33813.55` | `262.*` | Java 21 |

Plugin Verifier reported **Compatible** for every package listed below:

| IntelliJ IDEA | Edition | Build | Plugin package |
| --- | --- | --- | --- |
| 2024.1 | Community | `241.14494.240` | Legacy 1.0.2 |
| 2024.2.6 | Community | `242.26775.15` | Legacy 1.0.2 |
| 2024.3.7.1 | Community | `243.28141.41` | Legacy 1.0.2 |
| 2025.1.7.2 | Community | `251.29188.72` | Legacy 1.0.2 |
| 2025.2.6.3 | Community | `252.28539.97` | Legacy 1.0.2 |
| 2025.3 | Unified | `253.28294.334` | Legacy 1.0.2 |
| 2025.3.6.1 | Unified | `253.33813.55` | Legacy 1.0.2 and Modern 1.0.3 |
| 2026.1.5 | Unified | `261.27258.48` | Modern 1.0.3 |
| 2026.2.3 | Unified | `262.10968.63` | Modern 1.0.3 |

The legacy reports contain no API usage warnings. Each modern report contains
two experimental API usage findings for `TerminalAllowedActionsProvider`,
which provides the Reworked Terminal integration. Neither package has deprecated
or internal API usage findings in these reports.

[Plugin Verifier checks binary compatibility](https://plugins.jetbrains.com/docs/intellij/verifying-plugin-compatibility.html).
It does not replace manual behavior checks or guarantee compatibility with future IDE releases.

Manual smoke tests passed on **macOS** in the 2024.1 legacy sandbox and the
2025.3.6.1 modern sandbox: hide and restore Terminal and Run, return focus to the
editor, and keep Project open. In the modern sandbox, the terminal shortcut was
also checked with **Override IDE shortcuts enabled**.
These two packages have not yet been manually tested on Windows or Linux.

## Development

Use **JDK 21** to run the included Gradle Wrapper. Compilation uses a **JDK 17
toolchain** for legacy and a **JDK 21 toolchain** for modern. The configured
toolchain resolver can download the required JDK if it is not installed locally.

The toggle implementation, action registration, icons, and license are shared
in `src/main/`. The modern build also includes the terminal provider and its
optional dependency descriptors from `src/modern/`.

Select the package using `-PideLine=legacy` or `-PideLine=modern`.
Without this option, Gradle builds **modern**.

From the repository directory:

```bash
# Start an isolated IDE with the selected package loaded.
./gradlew -PideLine=legacy runIde
./gradlew -PideLine=modern runIde

# Build each ZIP and check its structure and binary compatibility.
./gradlew -PideLine=legacy buildPlugin verifyPluginStructure verifyPlugin
./gradlew -PideLine=modern buildPlugin verifyPluginStructure verifyPlugin
```

On Windows, use `gradlew.bat` instead of `./gradlew`.
The first run downloads Gradle, the required IDE distributions, and dependencies.

| Package | ZIP location | Verification reports |
| --- | --- | --- |
| Legacy | `build/legacy/distributions/bottom-panel-toggle-1.0.2.zip` | `build/legacy/reports/pluginVerifier/` |
| Modern | `build/modern/distributions/bottom-panel-toggle-1.0.3.zip` | `build/modern/reports/pluginVerifier/` |

The packages use separate build directories and sandbox directories under
`.intellijPlatform/sandbox/legacy/` and `.intellijPlatform/sandbox/modern/`.
Configure shortcut conflicts separately in each sandbox.

### Versioning

Both packages use plugin ID `it.toys.bottom-panel-toggle` and one Marketplace
listing. Set the modern version with `version` and the legacy version with
`legacyVersion` in `gradle.properties`.

Each uploaded package needs a unique version because
[Marketplace does not accept multiple artifacts with the same version](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html#publishing-a-plugin).
Keep the modern version higher when the IDE compatibility ranges overlap.
Both packages can be attached to one GitHub release from the same source commit.

### Manual verification

In each sandbox, open a project and an editor file. Resolve shortcut conflicts.
For modern, use the Reworked Terminal with **Override IDE shortcuts enabled**.
For legacy or other terminal engines, disable that setting if it captures the shortcut.
Then check:

1. With no bottom tool window visible and no previously used panel, toggle.
   Terminal should open if available at the bottom.
2. With the cursor in Terminal, toggle again. Terminal should hide and the
   editor should receive focus. Toggle once more to restore and focus Terminal.
3. Open another bottom tool window and toggle twice.
   It should hide, then reopen with focus.
4. Keep Project open on the left and repeat the toggle.
   Project should remain open.
5. Open a bottom tool window, hide it with the mouse, focus Project, and toggle.
   The most recently used bottom tool window should reopen.
6. Move a tool window to a side or make it floating.
   The bottom-panel command should leave it alone.
7. Disable the Terminal plugin, restart the sandbox, and open a fresh project
   with at least two available bottom-bar buttons. With no previous bottom panel,
   the toggle should open the tool window whose button label comes first
   alphabetically, regardless of button order on the bar.

## Contributing

When reporting a problem, include your plugin version, IntelliJ version,
operating system, terminal engine, active keymap, tool window positions,
and the steps needed to reproduce it.

For a change, explain the behavior it improves and how you verified it.

## License

[MIT](LICENCE.md) © 2026 Viorelss.
