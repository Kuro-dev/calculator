package org.kurodev.calculator.maths;

import java.util.Set;

public interface Operation {

    static String formRegex(Set<Operation> operations) {
        StringBuilder out = new StringBuilder("[");
        for (Operation value : operations) {
            out.append("\\").append(value.getOperator());
        }
        out.append("]*");
        return out.toString();
    }

    Calculation conclude(Calculation a, Calculation b);

    /**
     * @return the precendence of this operation. Higher precedence means higher priority.
     */
   default int getPrecedence(){
       return 0;
   }

    char getOperator();
}
