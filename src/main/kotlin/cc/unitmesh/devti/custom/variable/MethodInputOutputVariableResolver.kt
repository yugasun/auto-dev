package cc.unitmesh.devti.custom.variable

import cc.unitmesh.devti.context.ClassContextProvider
import cc.unitmesh.devti.context.MethodContext
import cc.unitmesh.devti.context.MethodContextProvider
import com.intellij.lang.LanguageCommenters
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class MethodInputOutputVariableResolver(val element: PsiElement) : VariableResolver {
    override val type: CustomIntentionVariableType = CustomIntentionVariableType.METHOD_INPUT_OUTPUT

    override fun resolve(): String {
        val methodContext = MethodContextProvider(false, false).from(element)
        if (methodContext.name == null) {
            return ""
        }

        return methodContext.inputOutputString()
    }
}