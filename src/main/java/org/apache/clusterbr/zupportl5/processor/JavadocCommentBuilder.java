package org.apache.clusterbr.zupportl5.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/JavadocCommentBuilder_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@SkipJavadocProcessing
public class JavadocCommentBuilder  {

    private static final Logger logger = LoggerFactory.getLogger(JavadocCommentBuilder.class);

    private List<String> comments = new ArrayList<String>();

    public static String
        STR_NEW_LINE = "\n",
        START_COMMENT_SYMBOL = " * ",
        LINE_START_COMMENTS  = " * <!-- comment-processor-start -->",
        LINE__END__COMMENTS  = " * <!-- comment-processor---end -->"
        ;

    public JavadocCommentBuilder() {
    }

    public boolean haveComments() {
        if (comments.isEmpty()) return false;
    
        long defaultComments = comments.stream()
            .filter(line -> line.equals(LINE_START_COMMENTS) || line.equals(LINE__END__COMMENTS))
            .count();
    
        return (defaultComments != comments.size());
    }

    public List<String> getCommentsList() {
        return Collections.unmodifiableList(this.comments);
    }

    public String insertSTARTComment () {
        return insertCommentLine(LINE_START_COMMENTS);
    }

    public String insertENDComment () {
        return insertCommentLine(LINE__END__COMMENTS);
    }

    public JavadocCommentBuilder insertJavadocCommentBuilder (JavadocCommentBuilder moreComments) {
        for(String line : moreComments.getCommentsList()) {
            this.comments.add(line);
        }
        return this;        
    }

    public JavadocCommentBuilder insertCommentLineList (List<String> listlineText) {

        String[] arraylineText = listlineText.toArray(new String[0]);

        return insertCommentLineArray(arraylineText);
    }    

    public JavadocCommentBuilder insertCommentLineArray (String[] arraylineText) {
        StringBuilder insertedComments = new StringBuilder();
        for(String line : arraylineText) {
            String newComment = insertCommentLine(line);
            insertedComments.append(newComment);            
        }
        return this;
    }

    public String insertCommentLine (String lineText) {
        String line = START_COMMENT_SYMBOL + lineText;
        comments.add(line);
        return line;
    }

    @Override
    public String toString() {
        return String.join(STR_NEW_LINE, comments);
    }

}
