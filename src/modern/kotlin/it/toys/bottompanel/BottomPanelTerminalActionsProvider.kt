package it.toys.bottompanel

import com.intellij.terminal.frontend.view.TerminalAllowedActionsProvider

class BottomPanelTerminalActionsProvider : TerminalAllowedActionsProvider {
    override fun getActionIds(): List<String> =
        listOf("it.toys.bottompanel.ToggleBottomPanelAction")
}
