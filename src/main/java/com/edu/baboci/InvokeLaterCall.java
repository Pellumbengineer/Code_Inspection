package com.edu.baboci;

import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvokeLaterCall extends AbstractBaseJavaLocalInspectionTool {

    // This string holds the class relevant to this inspection.
    @SuppressWarnings({"WeakerAccess"})
    @NonNls
    public String CHECKED_METHOD = "javax.swing.SwingUtilities.invokeLater";

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

                final PsiMethod method = expression.resolveMethod();
                assert method != null;
                String classLibraryPath = Objects.requireNonNull(method.getContainingClass()).getQualifiedName();
                String methodName = method.getName();

                String fullPathClass = classLibraryPath + "." + methodName;

                if (fullPathClass.equals(CHECKED_METHOD)) {
                    // Identified an expression with potential problems, add to list and display.
                    holder.registerProblem(expression, DESCRIPTION_TEMPLATE);
                }
            }
        };
    }
}