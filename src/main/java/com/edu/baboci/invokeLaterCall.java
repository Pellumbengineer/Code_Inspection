package com.edu.baboci;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class invokeLaterCall extends AbstractBaseJavaLocalInspectionTool {

    // This string holds a list of classes relevant to this inspection.
    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_METHOD = "SwingUtilities.invokeLater";

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new JavaElementVisitor() {

            @NonNls
            private final String DESCRIPTION_TEMPLATE = "SDK In IntelliJ code base it’s prohibited " +
                    "to use SwingUtilities#invokeLater , mainly because it could be too late to perform the runnable";

            //+ InspectionsBundle.message("");

            @Override
            public void visitReferenceExpression(PsiReferenceExpression psiReferenceExpression) {
            }

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                super.visitMethodCallExpression(expression);

                PsiExpression methodExpressionType = expression.getMethodExpression();
                String theClassName = methodExpressionType.getText();

                if (isCheckedType(theClassName)) {
                    // Identified an expression with potential problems, add to list and display.
                    holder.registerProblem(expression, DESCRIPTION_TEMPLATE);
                }
            }

            private boolean isCheckedType(String className) {
                if (className.equals(CHECKED_METHOD)) {
                    return true;
                }
                return false;
            }
        };
    }

}

