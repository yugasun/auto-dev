package cc.unitmesh.database.actions

import cc.unitmesh.database.actions.base.SqlMigrationContext
import cc.unitmesh.devti.AutoDevBundle
import cc.unitmesh.devti.gui.sendToChatWindow
import cc.unitmesh.devti.intentions.action.base.ChatBaseIntention
import cc.unitmesh.devti.provider.TextContextPrompter
import cc.unitmesh.devti.template.GENIUS_MIGRATION
import cc.unitmesh.devti.template.TemplateRender
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.sql.dialects.oracle.OraDialect

class GenerateFunctionAction : ChatBaseIntention() {
    override fun priority() = 901

    override fun getFamilyName(): String = AutoDevBundle.message("migration.database.plsql")

    override fun getText(): String = AutoDevBundle.message("migration.database.plsql.generate.function")

    private val logger = logger<GenerateFunctionAction>()

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor == null || file == null) return false
        val isOracle = file.language is OraDialect
        return isOracle
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) return
        val selectedText = editor.selectionModel.selectedText ?: return

        val templateRender = TemplateRender(GENIUS_MIGRATION)
        val template = templateRender.getTemplate("gen-function.vm")
        templateRender.context = SqlMigrationContext(
            lang = file.language.displayName,
            sql = selectedText,
        )

        val prompt = templateRender.renderTemplate(template)

        logger.info("Prompt: $prompt")

        sendToChatWindow(project, getActionType()) { panel, service ->
            service.handlePromptAndResponse(panel, TextContextPrompter(prompt), null, true)
        }
    }

}


