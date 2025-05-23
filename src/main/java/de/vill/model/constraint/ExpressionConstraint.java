package de.vill.model.constraint;

import de.vill.model.Feature;
import de.vill.model.building.VariableReference;
import de.vill.model.expression.AggregateFunctionExpression;
import de.vill.model.expression.Expression;
import de.vill.model.expression.LiteralExpression;

import java.util.*;

public abstract class ExpressionConstraint extends Constraint {
    private Expression left;
    private Expression right;
    private final String expressionSymbol;

    public ExpressionConstraint(Expression left, Expression right, String expressionSymbol) {
        this.left = left;
        this.right = right;
        this.expressionSymbol = expressionSymbol;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public String getExpressionSymbol() {
        return expressionSymbol;
    }

    @Override
    public String toString(boolean withSubmodels, String currentAlias) {
        return left.toString(withSubmodels, currentAlias) +
            " " +
            expressionSymbol +
            " " +
            right.toString(withSubmodels, currentAlias);
    }

    @Override
    public List<Constraint> getConstraintSubParts() {
        return Collections.emptyList();
    }

    public List<Expression> getExpressionSubParts() {
        return Arrays.asList(left, right);
    }

    public void replaceExpressionSubPart(Expression oldSubExpression, Expression newSubExpression) {
        if (left == oldSubExpression) {
            left = newSubExpression;
        } else if (right == oldSubExpression) {
            right = newSubExpression;
        }
    }

    @Override
    public void replaceConstraintSubPart(Constraint oldSubConstraint, Constraint newSubConstraint) {
        // no sub constraints
    }

    public boolean evaluate(Set<Feature> selectedFeatures) {
        double leftResult = left.evaluate(selectedFeatures);
        double rightResult = right.evaluate(selectedFeatures);

        if ("==".equals(expressionSymbol)) {
            return leftResult == rightResult;
        } else if ("<".equals(expressionSymbol)) {
            return leftResult < rightResult;
        } else if (">".equals(expressionSymbol)) {
            return leftResult > rightResult;
        } else if (">=".equals(expressionSymbol)) {
            return leftResult >= rightResult;
        } else if ("<=".equals(expressionSymbol)) {
            return leftResult <= rightResult;
        } else if ("!=".equals(expressionSymbol)) {
            return leftResult != rightResult;
        }
        return false;
    }

    @Override
    public int hashCode(int level) {
        final int prime = 31;
        int result = prime * level + (left == null ? 0 : left.hashCode());
        result = prime * result + (right == null ? 0 : right.hashCode());
        result = prime * result + (expressionSymbol == null ? 0 : expressionSymbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ExpressionConstraint other = (ExpressionConstraint) obj;
        return Objects.equals(expressionSymbol, other.expressionSymbol) && Objects.equals(left, other.left)
            && Objects.equals(right, other.right);
    }

    @Override
    public List<VariableReference> getReferences() {
        List<VariableReference> references = new ArrayList<>();
        references.addAll(left.getReferences());
        references.addAll(right.getReferences());
        return references;
    }
}
