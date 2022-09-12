package org.kurodev.calculator.maths;

public interface Operation {

    Calculation conclude(Calculation a, Calculation b);

    /**
     * default: 0
     *
     * @return the precedence of this operation. Higher precedence means higher priority.
     */
    default int getPrecedence() {
        return 0;
    }

    char getOperator();
}
