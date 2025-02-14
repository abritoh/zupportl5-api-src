package org.apache.clusterbr.zupportl5.dto;

import java.util.Objects;
import java.io.Serializable;

 /**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/Tuple_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
*/
public class Tuple<Value1, Value2> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Value1 value1;
    private Value2 value2;

    public Tuple(Value1 value1, Value2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public Value1 getValue1() {
        return value1;
    }

    public void setValue1(Value1 value1) {
        this.value1 = value1;
    }

    public Value2 getValue2() {
        return value2;
    }

    public void setValue2(Value2 value2) {
        this.value2 = value2;
    }


    @Override
    public String toString() {
        return "Tuple{" +
                "value1=" + value1 +
                ", value2=" + value2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(value1, tuple.value1) &&
               Objects.equals(value2, tuple.value2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value1, value2);
    }
}
