# Changelog

## [1.0.1] - 2026-10-22

### Fixed

- Allow the toggle shortcut in the Reworked Terminal when Override IDE shortcuts is enabled.

### Changed

- Replace internal layout API usage with an alphabetical fallback when neither the previous bottom panel nor Terminal is available.

## [1.0.0] - 2026-10-22

### Added

- Toggle bottom tool windows with Cmd+J on macOS or Ctrl+J on Windows/Linux.
- Hide visible bottom tool windows and return focus to the editor.
- Restore and focus the last used bottom tool window when the bottom panel is hidden.
- Remember bottom tool window usage separately for each open project.
- Leave side, floating, and separate tool windows unchanged.
- Open Terminal, or the first available bottom-bar tool window, when no previous bottom tool window can be restored.
